import { GoogleGenerativeAI } from '@google/generative-ai';
import { config } from '../config/config.js';

export class AIAgent {
    constructor() {
        this.genAI = new GoogleGenerativeAI(config.gemini.apiKey);

        this.model = this.genAI.getGenerativeModel({
            model: config.gemini.model // pass as-is
        });

        this.conversationHistory = [];
    }



    async analyze(prompt, context = {}) {
        console.log('\nAI Agent analyzing...\n');

        const systemPrompt = `You are an expert code analyzer specializing in repository analysis. 
You have access to GitHub repository data and can provide detailed insights about code structure, architecture, and quality.

When analyzing code:
- Focus on structure and organization
- Identify key components and their relationships
- Highlight architectural patterns
- Suggest improvements
- Be specific and actionable
- Provide clear, well-formatted output`;

        const userMessage = this.buildPrompt(prompt, context);
        const fullPrompt = `${systemPrompt}\n\n${userMessage}`;

        try {
            const result = await this.model.generateContent(fullPrompt);
            const response = await result.response;
            const text = response.text();

            this.conversationHistory.push({
                prompt: userMessage,
                response: text
            });

            return text;
        } catch (error) {
            console.error('AI analysis failed:', error.message);

            // Handle rate limiting or quota errors
            if (error.message.includes('quota') || error.message.includes('rate limit')) {
                throw new Error('API quota exceeded. Please wait a moment and try again, or check your API key at https://makersuite.google.com/app/apikey');
            }

            throw error;
        }
    }

    buildPrompt(prompt, context) {
        let fullPrompt = prompt;

        if (context.files && context.files.length > 0) {
            fullPrompt += '\n\n## Repository Files:\n';
            context.files.forEach(file => {
                fullPrompt += `\n### ${file.path}\n`;
                if (file.content) {
                    // Limit content to avoid token limits
                    const content = file.content.length > 8000
                        ? file.content.slice(0, 8000) + '\n... (truncated)'
                        : file.content;
                    fullPrompt += `\`\`\`${file.language || ''}\n${content}\n\`\`\`\n`;
                }
            });
        }

        if (context.structure) {
            fullPrompt += '\n\n## Directory Structure:\n```\n' + context.structure + '\n```\n';
        }

        if (context.metadata) {
            fullPrompt += '\n\n## Repository Metadata:\n' + JSON.stringify(context.metadata, null, 2);
        }

        return fullPrompt;
    }

    async summarizeCodebase(files, structure) {
        const prompt = `Analyze this codebase and provide a comprehensive summary including:

1. **Project Overview**: What does this project do? What is its purpose?
2. **Architecture**: How is the code organized? What's the overall structure?
3. **Key Components**: What are the main files/modules and their purposes?
4. **Implementation Details**: What features are implemented?
5. **Code Quality**: Assessment of code organization and best practices
6. **Recommendations**: Suggestions for improvement or extension

Please be specific and reference actual files and code patterns you observe.`;

        return await this.analyze(prompt, { files, structure });
    }

    async explainComponent(componentName, code) {
        const prompt = `Explain the purpose and functionality of "${componentName}" in detail.`;

        return await this.analyze(prompt, {
            files: [{ path: componentName, content: code }]
        });
    }

    clearHistory() {
        this.conversationHistory = [];
    }


}