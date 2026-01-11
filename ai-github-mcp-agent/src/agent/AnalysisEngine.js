import { config } from '../config/config.js';

export class AnalysisEngine {
    constructor(mcpClient, aiAgent) {
        this.mcp = mcpClient;
        this.ai = aiAgent;
        this.repositoryData = {
            files: [],
            structure: '',
            metadata: {}
        };
    }

    async analyzeRepository() {
        console.log('\n📊 Starting repository analysis...\n');

        const { owner, repo, path, branch } = config.github;

        try {
            // Step 1: Search for files in the repository path
            console.log('📁 Discovering repository files...');
            await this.discoverFiles(owner, repo, path);

            // Step 2: Fetch key source files
            console.log('📄 Fetching source files...');
            await this.fetchDiscoveredFiles(owner, repo, branch);

            // Step 3: Run AI analysis
            console.log('🧠 Running AI analysis...\n');
            const analysis = await this.ai.summarizeCodebase(
                this.repositoryData.files,
                this.repositoryData.structure
            );

            return {
                analysis,
                filesAnalyzed: this.repositoryData.files.length,
                repository: `${owner}/${repo}/${path}`
            };
        } catch (error) {
            console.error('❌ Analysis failed:', error.message);
            throw error;
        }
    }

    async discoverFiles(owner, repo, path) {
        try {
            // Use search_code to find files in the specified path
            const query = `repo:${owner}/${repo} path:${path}`;
            console.log(`🔍 Searching for files with query: ${query}`);

            const response = await this.mcp.callTool('search_code', { q: query });

            if (!response.content || response.content.length === 0) {
                console.log('⚠️  No files found in search results');

                // Try alternative: search for common file extensions in the path
                const extensions = ['js', 'py', 'md', 'json', 'txt', 'lisp', 'cl', 'scm'];
                console.log('🔍 Trying alternative search strategies...');

                for (const ext of extensions) {
                    const extQuery = `repo:${owner}/${repo} path:${path} extension:${ext}`;
                    try {
                        const extResponse = await this.mcp.callTool('search_code', { q: extQuery });
                        if (extResponse.content && extResponse.content.length > 0) {
                            const results = JSON.parse(extResponse.content[0].text);
                            if (results.items && results.items.length > 0) {
                                results.items.forEach(item => {
                                    const filename = item.path.split('/').pop();
                                    this.repositoryData.structure += `📄 ${item.path}\n`;
                                    this.repositoryData.files.push({
                                        path: item.path,
                                        name: filename,
                                        url: item.html_url,
                                        needsFetch: true
                                    });
                                });
                            }
                        }
                    } catch (err) {
                        // Continue to next extension
                    }
                }

                if (this.repositoryData.files.length === 0) {
                    // Last resort: try to fetch some common files directly
                    const commonFiles = ['README.md', 'index.js', 'main.py', 'app.js', 'parser.js', 'evaluator.js'];
                    for (const file of commonFiles) {
                        const filePath = path ? `${path}/${file}` : file;
                        this.repositoryData.files.push({
                            path: filePath,
                            name: file,
                            needsFetch: true
                        });
                    }
                }
                return;
            }

            const searchResults = JSON.parse(response.content[0].text);

            if (searchResults.items && searchResults.items.length > 0) {
                console.log(`✓ Found ${searchResults.items.length} files`);

                searchResults.items.forEach(item => {
                    const filename = item.path.split('/').pop();
                    this.repositoryData.structure += `📄 ${item.path}\n`;

                    if (this.isSourceFile(filename)) {
                        this.repositoryData.files.push({
                            path: item.path,
                            name: filename,
                            url: item.html_url,
                            needsFetch: true
                        });
                    }
                });
            } else {
                console.log('⚠️  No files found in search results');
            }
        } catch (error) {
            console.warn(`⚠️  Could not discover files:`, error.message);

            // Fallback: try to fetch common files
            console.log('📝 Attempting to fetch common files directly...');
            const commonFiles = ['README.md', 'index.js', 'main.py', 'package.json'];
            for (const file of commonFiles) {
                const filePath = path ? `${path}/${file}` : file;
                this.repositoryData.files.push({
                    path: filePath,
                    name: file,
                    needsFetch: true
                });
            }
        }
    }

    async fetchDiscoveredFiles(owner, repo, branch) {
        const filesToFetch = this.repositoryData.files.filter(f => f.needsFetch);

        if (filesToFetch.length === 0) {
            console.log('⚠️  No files to fetch');
            return;
        }

        console.log(`📥 Fetching ${filesToFetch.length} files...`);

        for (const fileInfo of filesToFetch) {
            await this.fetchFileContent(owner, repo, fileInfo.path, branch);
        }
    }

    async fetchFileContent(owner, repo, filePath, branch) {
        try {
            const response = await this.mcp.getFileContents(owner, repo, filePath, branch);

            if (response.content && response.content.length > 0) {
                const fileData = JSON.parse(response.content[0].text);

                let content = '';
                if (fileData.content) {
                    content = Buffer.from(fileData.content, 'base64').toString('utf-8');
                } else if (fileData.download_url) {
                    // Some files might provide a download URL instead
                    content = '(Content available via download URL)';
                }

                // Find the file in our array and update it
                const fileIndex = this.repositoryData.files.findIndex(f => f.path === filePath);
                if (fileIndex !== -1) {
                    this.repositoryData.files[fileIndex] = {
                        path: filePath,
                        content: content.slice(0, 10000), // Limit content size
                        size: fileData.size || content.length,
                        language: this.getLanguage(filePath),
                        needsFetch: false
                    };
                }

                console.log(`  ✓ ${filePath}`);
            }
        } catch (error) {
            console.warn(`  ✗ Could not fetch ${filePath}: ${error.message}`);
            // Remove failed files from the list
            this.repositoryData.files = this.repositoryData.files.filter(f => f.path !== filePath);
        }
    }

    isSourceFile(filename) {
        const sourceExtensions = [
            '.js', '.ts', '.py', '.java', '.cpp', '.c', '.h',
            '.rb', '.go', '.rs', '.php', '.swift', '.kt',
            '.lisp', '.cl', '.scm', '.rkt', '.el',
            '.md', '.txt', '.json', '.yml', '.yaml'
        ];

        return sourceExtensions.some(ext => filename.toLowerCase().endsWith(ext));
    }

    getLanguage(filename) {
        const ext = filename.toLowerCase().split('.').pop();
        const languageMap = {
            'js': 'javascript',
            'ts': 'typescript',
            'py': 'python',
            'java': 'java',
            'cpp': 'cpp',
            'c': 'c',
            'rb': 'ruby',
            'go': 'go',
            'rs': 'rust',
            'php': 'php',
            'lisp': 'lisp',
            'cl': 'lisp',
            'scm': 'scheme',
            'md': 'markdown',
            'json': 'json',
            'yml': 'yaml',
            'yaml': 'yaml'
        };

        return languageMap[ext] || ext;
    }

    getRepositoryData() {
        return this.repositoryData;
    }
}