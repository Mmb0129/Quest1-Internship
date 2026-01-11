import { MCPClient } from './mcp/MCPClient.js';
import { AIAgent } from './agent/AIAgent.js';
import { AnalysisEngine } from './agent/AnalysisEngine.js';
import { config, validateConfig } from './config/config.js';

async function main() {
    console.log('🚀 GitHub Repository Analyzer via MCP\n');
    console.log('═'.repeat(50));

    // Validate configuration
    validateConfig();

    const mcpClient = new MCPClient();
    const aiAgent = new AIAgent();
    let analysisEngine;

    try {
        // Connect to MCP server
        await mcpClient.connect();

        // Initialize analysis engine
        analysisEngine = new AnalysisEngine(mcpClient, aiAgent);


        // Display target repository
        console.log('\n Target Repository:');
        console.log(`   ${config.github.owner}/${config.github.repo}`);
        console.log(`   Path: ${config.github.path}`);
        console.log(`   Branch: ${config.github.branch}`);
        console.log('═'.repeat(50));

        // Run analysis
        const result = await analysisEngine.analyzeRepository();

        // Display results
        console.log('\n' + '═'.repeat(50));
        console.log('ANALYSIS REPORT');
        console.log('═'.repeat(50));
        console.log(`\nFiles Analyzed: ${result.filesAnalyzed}`);
        console.log(`Repository: ${result.repository}\n`);
        console.log('─'.repeat(50));
        console.log(result.analysis);
        console.log('─'.repeat(50));


        // Display repository structure
        const repoData = analysisEngine.getRepositoryData();
        if (repoData.structure) {
            console.log('\nRepository Structure:\n');
            console.log(repoData.structure);
        }

        console.log('\nAnalysis completed successfully!\n');

    } catch (error) {
        console.error('\nError:', error.message);
        if (error.stack) {
            console.error('\nStack trace:', error.stack);
        }
        process.exit(1);
    } finally {
        // Cleanup
        if (mcpClient) {
            await mcpClient.close();
        }
    }
}

// Handle graceful shutdown
process.on('SIGINT', async () => {
    console.log('\n\nShutting down gracefully...');
    process.exit(0);
});

// Run main function
main().catch(error => {
    console.error('Fatal error:', error);
    process.exit(1);
});