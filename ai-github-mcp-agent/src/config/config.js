import dotenv from 'dotenv';
import { fileURLToPath } from 'url';
import { dirname, join } from 'path';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

dotenv.config({ path: join(__dirname, '../../.env') });

export const config = {
    github: {
        token: process.env.GITHUB_TOKEN,
        owner: process.env.REPOSITORY_OWNER || 'Mmb0129',
        repo: process.env.REPOSITORY_NAME || 'Quest1-Internship',
        branch: process.env.REPOSITORY_BRANCH || 'main',
        path: process.env.REPOSITORY_PATH || 'lisp-interpreter'
    },
    gemini: {
        apiKey: process.env.GEMINI_API_KEY,
        model: process.env.GEMINI_MODEL || 'gemini-1.5-flash-latest'
    },
    mcp: {
        serverCommand: 'npx',
        serverArgs: ['-y', '@modelcontextprotocol/server-github']
    }
};

export function validateConfig() {
    const errors = [];

    if (!config.github.token) {
        errors.push('GITHUB_TOKEN is required in .env file');
    }

    if (!config.gemini.apiKey) {
        errors.push('GEMINI_API_KEY is required in .env file');
        errors.push('Get your free API key at: https://makersuite.google.com/app/apikey');
    }

    if (errors.length > 0) {
        console.error('\nConfiguration Errors:');
        errors.forEach(err => console.error(`  - ${err}`));
        console.error('\nPlease create a .env file with the required variables.\n');
        process.exit(1);
    }

    console.log('Configuration validated successfully');
}