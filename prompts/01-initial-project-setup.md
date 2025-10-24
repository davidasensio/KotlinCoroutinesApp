# Prompt: Initial Android Project Setup

## Objective
Set up a professional Android project with modern development tools including code quality checks, formatting, and project configuration.

## Instructions

Please configure this Android project with the following professional development setup:

### 1. Code Quality - detekt
- Install detekt plugin (latest stable version)
- Create configuration file at `config/detekt/detekt.yml`
- Create baseline file at `config/detekt/baseline.xml`
- Configure detekt in root `build.gradle.kts`:
  - Enable `buildUponDefaultConfig = true`
  - Set `allRules = false`
  - Point to config files
- Add Gradle task: `./gradlew detekt`

### 2. Code Formatting - ktlint
- Install ktlint Gradle plugin (latest stable version)
- Configure ktlint in root `build.gradle.kts`:
  - Apply to all subprojects
  - Enable Android mode
  - Set version to latest stable
  - Exclude build directories
  - Set `ignoreFailures = false`
- Add Gradle tasks:
  - `./gradlew ktlintCheck` - Check code style
  - `./gradlew formatKotlin` - Format code

### 3. Version Catalog
- Create `gradle/libs.versions.toml` for centralized dependency management
- Include versions for:
  - AGP (Android Gradle Plugin)
  - Kotlin
  - AndroidX libraries (core-ktx, lifecycle, activity-compose)
  - Jetpack Compose BOM
  - Testing libraries (JUnit, Espresso)
  - Development tools (Timber, detekt, ktlint)

### 4. .gitignore
- Create comprehensive `.gitignore` for Android/Kotlin projects
- Include standard Android build directories
- Exclude IDE files (.idea/, *.iml)
- Exclude Gradle wrapper jar if needed
- Exclude local.properties and keystore files

### 5. EditorConfig
- Create `.editorconfig` file for consistent coding style
- Configure for Kotlin files:
  - indent_size = 4
  - indent_style = space
  - end_of_line = lf
  - charset = utf-8
  - trim_trailing_whitespace = true
  - insert_final_newline = true

### 6. GitHub PR Template
- Create `.github/pull_request_template.md`
- Include sections:
  - Description
  - Changes Made
  - Type of Change (checkboxes)
  - Related Links
  - Screenshots/Recordings
  - Testing Performed
  - Notes & Ideas

### 7. Gradle Tasks
Create convenience Gradle tasks in root `build.gradle.kts`:
- `formatKotlin` - Format all Kotlin code
- `verifyStyle` - Run ktlint + detekt

## Expected Outcome

After running this prompt, the project should have:
- ✅ detekt configured and ready to run
- ✅ ktlint configured with formatting capabilities
- ✅ Version catalog for dependency management
- ✅ Proper .gitignore for Android projects
- ✅ EditorConfig for consistent code style
- ✅ Professional GitHub PR template
- ✅ Convenience Gradle tasks for code quality

## Verification Commands

```bash
# Verify detekt works
./gradlew detekt

# Verify ktlint works
./gradlew ktlintCheck

# Format code
./gradlew formatKotlin

# Run all style checks
./gradlew verifyStyle
```

## Related Documentation
- [detekt Documentation](../docs/detekt.md)
- [ktlint Documentation](../docs/ktlint.md)
- [GitHub Templates Documentation](../docs/github-templates.md)
