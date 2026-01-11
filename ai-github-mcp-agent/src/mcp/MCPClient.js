import { Client } from '@modelcontextprotocol/sdk/client/index.js';
import { StdioClientTransport } from '@modelcontextprotocol/sdk/client/stdio.js';
import { config } from '../config/config.js';

export class MCPClient {
    constructor() {
        this.client = null;
        this.transport = null;
    }

    async connect() {
        console.log('🔌 Connecting to GitHub MCP Server...');

        try {
            this.transport = new StdioClientTransport({
                command: config.mcp.serverCommand,
                args: config.mcp.serverArgs,
                env: {
                    ...process.env,
                    GITHUB_PERSONAL_ACCESS_TOKEN: config.github.token
                }
            });

            this.client = new Client({
                name: 'github-analyzer-client',
                version: '1.0.0'
            }, {
                capabilities: {}
            });

            await this.client.connect(this.transport);
            console.log('✅ Connected to GitHub MCP Server');

            const tools = await this.listTools();
            console.log(`📦 Available tools: ${tools.map(t => t.name).join(', ')}`);

            return true;
        } catch (error) {
            console.error('❌ Failed to connect to MCP server:', error.message);
            throw error;
        }
    }

    async listTools() {
        if (!this.client) {
            throw new Error('MCP client not connected');
        }
        const response = await this.client.listTools();
        return response.tools;
    }

    async callTool(toolName, args) {
        if (!this.client) {
            throw new Error('MCP client not connected');
        }

        console.log(`🔧 Calling tool: ${toolName}`);
        const response = await this.client.callTool({
            name: toolName,
            arguments: args
        });

        return response;
    }

    async getFileContents(owner, repo, path, branch = 'main') {
        return await this.callTool('get_file_contents', {
            owner,
            repo,
            path,
            ref: branch
        });
    }

    async searchRepositories(query) {
        return await this.callTool('search_repositories', {
            query
        });
    }

    async createIssue(owner, repo, title, body) {
        return await this.callTool('create_issue', {
            owner,
            repo,
            title,
            body
        });
    }

    async listRepositoryContents(owner, repo, path = '', branch = 'main') {
        // Use search_code to list files in a directory
        const query = `repo:${owner}/${repo} path:${path}`;
        return await this.callTool('search_code', {
            q: query
        });
    }

    async close() {
        if (this.client) {
            await this.client.close();
            console.log('🔌 Disconnected from MCP server');
        }
    }
}