# Technical Documentation

This folder contains technical documentation for all development tools, CI/CD processes, and configurations used in the Kotlin Coroutines Showcase project.

## Development Tools

### Architecture
- **[Hilt](./hilt.md)** - Dependency injection for Android

### Code Quality & Formatting
- **[detekt](./detekt.md)** - Static code analysis for Kotlin
- **[ktlint](./ktlint.md)** - Kotlin code formatter and linter

### Dependency Management
- **[Renovate Bot](./renovate.md)** - Automated dependency updates

### CI/CD & Automation
- **[GitHub Actions](./github-actions.md)** - Continuous integration workflows
- **[GitHub Templates](./github-templates.md)** - Pull request templates

## Quick Reference

### Common Commands

```bash
# Code Quality
./gradlew detekt                 # Run static code analysis
./gradlew ktlintCheck           # Check code formatting
./gradlew formatKotlin          # Format all Kotlin code
./gradlew verifyStyle           # Run ktlint + detekt

# Build
./gradlew app:assembleDebug     # Build debug APK

# Testing
./gradlew app:testDebugUnitTest                    # Run unit tests
./gradlew app:connectedDebugAndroidTest           # Run instrumented tests

# Comprehensive Check
./gradlew check                  # Run all checks (style + tests + lint)
```

### Configuration Files

| Tool | Configuration Location |
|------|----------------------|
| Hilt | `di/` package, `app/build.gradle.kts` |
| detekt | `config/detekt/detekt.yml` |
| ktlint | `build.gradle.kts` (root) |
| Renovate | `renovate.json` |
| GitHub Actions | `.github/workflows/` |
| PR Template | `.github/pull_request_template.md` |

## Setup Guides

Need to set up these tools from scratch? Check out our [AI prompts](../prompts/) for automated setup instructions.

## Contributing

When adding new tools or technologies to the project:

1. Create documentation in this folder (e.g., `new-tool.md`)
2. Update this README with links
3. Add setup instructions to `prompts/` folder
4. Update `CLAUDE.md` if relevant for AI assistants

## Additional Resources

- [CLAUDE.md](../CLAUDE.md) - Instructions for AI assistants working with this project
- [README.md](../README.md) - Project overview and quick start guide
