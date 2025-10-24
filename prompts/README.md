# AI Prompts for Project Setup

This folder contains reusable prompts for setting up and configuring the Kotlin Coroutines Showcase project. These prompts are designed to be used with AI assistants like Claude Code to automate and standardize project setup tasks.

## Available Prompts

### [01 - Initial Project Setup](./01-initial-project-setup.md)
Configure the foundational development tools and project structure.

**Includes:**
- detekt configuration for code quality
- ktlint setup for code formatting
- .gitignore for Android/Kotlin projects
- GitHub PR template
- Gradle Version Catalogs
- EditorConfig

### [02 - Configure CI and Renovate](./02-configure-ci-and-renovate.md)
Set up continuous integration and automated dependency management.

**Includes:**
- GitHub Actions workflow for PR validation
- Code quality checks (ktlint, detekt, lint)
- Unit and instrumented tests
- Renovate bot configuration

## How to Use These Prompts

1. **Copy the prompt content** from the desired `.md` file
2. **Paste it to your AI assistant** (e.g., Claude Code)
3. **Review the changes** suggested by the AI
4. **Approve and commit** the changes

## Adding New Prompts

When adding new setup tasks to the project:

1. Create a new numbered prompt file (e.g., `03-new-feature.md`)
2. Document the setup steps clearly
3. Update this README with the new prompt
4. Add corresponding documentation in `docs/` folder

## Related Documentation

See the [Technical Documentation](../docs/) folder for detailed information about each tool and configuration.
