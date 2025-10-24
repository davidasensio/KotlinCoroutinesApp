# ktlint - Kotlin Linter and Formatter

## What is ktlint?

ktlint is an anti-bikeshedding Kotlin linter with built-in formatting capabilities. It enforces the official Kotlin coding conventions and style guide, ensuring consistent code formatting across your team without endless debates about code style.

## Official Resources

- **GitHub Repository:** https://github.com/pinterest/ktlint
- **Official Documentation:** https://pinterest.github.io/ktlint/
- **Gradle Plugin:** https://github.com/JLLeitschuh/ktlint-gradle

## Configuration

### Location
- **Gradle configuration:** `build.gradle.kts` (root)
- **Plugin:** Applied to all subprojects

### Current Setup
```kotlin
subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set("1.3.1")
        android.set(true)
        outputColorName.set("RED")
        ignoreFailures.set(false)

        filter {
            exclude("**/build/**")
        }
    }
}
```

## Available Commands

### Check Code Style
```bash
./gradlew ktlintCheck
```
Checks all Kotlin files against ktlint rules without modifying them.

### Format Code
```bash
./gradlew formatKotlin
```
Automatically formats all Kotlin files to conform to ktlint rules. This is a custom task defined in the root `build.gradle.kts` that calls `ktlintFormat`.

### Format Individual Module
```bash
./gradlew :app:ktlintFormat
```
Formats only the `app` module.

### Run Combined Style Check
```bash
./gradlew verifyStyle
```
Runs both ktlint and detekt checks together.

## Features

- **Automatic Formatting:** Fixes most style issues automatically
- **Android Support:** Special rules for Android projects
- **Editor Integration:** Works with IntelliJ IDEA, Android Studio, VS Code
- **Git Hooks:** Can be integrated with Git pre-commit hooks
- **Gradle Integration:** Seamless integration with build process
- **Fast Execution:** Quickly checks and formats files

## CI/CD Integration

ktlint runs automatically on every pull request via GitHub Actions:
- Location: `.github/workflows/pr-validation.yml`
- Step: "Code Style - ktlint Check"
- Behavior: Fails the build if formatting issues are found

## Common Use Cases

### Before Committing
Always format your code before committing:
```bash
./gradlew formatKotlin
```

### Check Without Fixing
To see what would be changed without modifying files:
```bash
./gradlew ktlintCheck
```

### Format Specific File
```bash
./gradlew ktlintFormat --include="**/MainActivity.kt"
```

## Editor Integration

### Android Studio / IntelliJ IDEA

1. Install the ktlint plugin from the marketplace
2. Configure the plugin to use the project's ktlint version
3. Enable "Format on Save" for automatic formatting

### VS Code

1. Install the "Kotlin" extension
2. Configure ktlint in workspace settings
3. Enable format on save

## Configuration Tips

1. **Android Mode:** Already enabled via `android.set(true)`
2. **Color Output:** Errors shown in red for better visibility
3. **Strict Mode:** `ignoreFailures = false` ensures builds fail on violations
4. **Exclude Patterns:** Build directories are automatically excluded

## Disable Rules

If you need to disable specific rules, add to `build.gradle.kts`:
```kotlin
ktlint {
    filter {
        exclude("**/generated/**")
    }
    disabledRules.set(setOf("no-wildcard-imports"))
}
```

## Related Documentation

- [detekt Documentation](./detekt.md) - Complementary static analysis tool
- [GitHub Actions](./github-actions.md) - See how ktlint runs in CI

## Setup Guide

To set up ktlint from scratch, see: [Initial Project Setup Prompt](../prompts/01-initial-project-setup.md)
