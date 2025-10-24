# detekt - Static Code Analysis

## What is detekt?

detekt is a static code analysis tool for Kotlin. It helps maintain code quality by detecting code smells, complexity issues, and potential bugs before they reach production. It's highly configurable and can be integrated into your development workflow and CI/CD pipeline.

## Official Resources

- **GitHub Repository:** https://github.com/detekt/detekt
- **Official Documentation:** https://detekt.dev/
- **Rule Sets:** https://detekt.dev/docs/rules/

## Configuration

### Location
- **Configuration file:** `config/detekt/detekt.yml`
- **Baseline file:** `config/detekt/baseline.xml`
- **Gradle configuration:** `build.gradle.kts` (root)

### Current Setup
```kotlin
detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(file("$rootDir/config/detekt/detekt.yml"))
    baseline = file("$rootDir/config/detekt/baseline.xml")
}
```

## Available Commands

### Run detekt Analysis
```bash
./gradlew detekt
```
Analyzes all Kotlin code and reports issues based on configured rules.

### Run Combined Style Check
```bash
./gradlew verifyStyle
```
Runs both ktlint and detekt checks together.

### Run All Checks
```bash
./gradlew check
```
Runs detekt, ktlint, lint, and all tests.

## Features

- **Code Smells Detection:** Identifies design issues and antipatterns
- **Complexity Analysis:** Detects overly complex functions and classes
- **Formatting Rules:** Ensures consistent code style (complementary to ktlint)
- **Custom Rules:** Extensible with custom rule sets
- **Baseline Support:** Track existing issues without failing builds
- **HTML Reports:** Generates detailed reports in `build/reports/detekt/`

## CI/CD Integration

detekt runs automatically on every pull request via GitHub Actions:
- Location: `.github/workflows/pr-validation.yml`
- Step: "Code Quality - Detekt"
- Behavior: Fails the build if issues are found

## Common Use Cases

### Find All Issues
```bash
./gradlew detekt
```
Then check the report at: `build/reports/detekt/detekt.html`

### Update Baseline
If you want to accept current issues and only track new ones:
```bash
./gradlew detekt --baseline config/detekt/baseline.xml
```

### Format Check Only
To run detekt's formatting rules specifically:
```bash
./gradlew detekt --include "**/formatting/**"
```

## Configuration Tips

1. **Customize Rules:** Edit `config/detekt/detekt.yml` to enable/disable rules
2. **Set Thresholds:** Configure complexity thresholds (cyclomatic complexity, etc.)
3. **Exclude Patterns:** Add exclusion patterns for generated code
4. **Use Baseline:** For legacy code, create a baseline to track only new issues

## Related Documentation

- [ktlint Documentation](./ktlint.md) - Complementary code formatting tool
- [GitHub Actions](./github-actions.md) - See how detekt runs in CI

## Setup Guide

To set up detekt from scratch, see: [Initial Project Setup Prompt](../prompts/01-initial-project-setup.md)
