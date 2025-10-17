# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an Android application demonstrating Kotlin Coroutines best practices, built with Jetpack Compose and Material3. The project uses Clean Architecture + MVVM pattern and is designed to showcase coroutine patterns including structured concurrency, flow, channels, and advanced coroutine patterns.

**Package:** `com.handysparksoft.kotlincoroutines`
**Min SDK:** 31 | **Target SDK:** 36
**Build System:** Gradle with Kotlin DSL and Version Catalogs

## Build Commands

### Essential Commands
```bash
# Build the app
./gradlew app:assembleDebug

# Build with Compose compiler metrics (useful for optimization analysis)
./gradlew app:assembleDebug -PenableComposeCompilerReports=true
# Metrics will be in: app/build/compose_metrics/

# Format code with ktlint
./gradlew formatKotlin

# Verify code style (runs ktlint + detekt)
./gradlew verifyStyle

# Run all checks (style + tests + lint)
./gradlew check
```

### Testing Commands
```bash
# Compile tests only (faster for verification)
./gradlew app:compileDebugUnitTestSources

# Run unit tests
./gradlew app:testDebugUnitTest
./gradlew app:testDebugUnitTest --tests "*ClassName*"        # Specific class
./gradlew app:testDebugUnitTest --tests "*ClassName.methodName*"  # Specific test

# Run instrumented tests
./gradlew app:connectedDebugAndroidTest

# Run all tests
./gradlew test
```

### Style and Quality Tools
```bash
# Individual style checks
./gradlew ktlintCheck
./gradlew detekt
./gradlew lint

# Fix lint issues automatically
./gradlew lintFix
```

## Architecture

### Application Structure
- **Entry Point:** `CoroutinesApp` extends `Application`
    - Initializes Timber logging (debug builds only)
- **Main Activity:** `MainActivity`
    - Uses `enableEdgeToEdge()` for modern edge-to-edge display
    - Sets up Compose UI with Material3 theming

### Code Organization (Planned Structure)
Based on Clean Architecture + MVVM:
- **Data Layer:** Repositories, data sources (remote/local)
- **Domain Layer:** Use cases, business logic
- **Presentation Layer:** Compose screens, ViewModels, UI state

### Naming Conventions

**Data Layer:**
- Repositories: `{Feature}Repository`
- Remote data sources: `{Feature}RemoteDataSource`
- Local data sources: `{Feature}LocalDataSource`

**Domain Layer:**
- Use cases: `{Action}UseCase` (e.g., `GetProfileUseCase`)

**Presentation Layer (Compose):**
- Screens: `{Feature}Screen` - main composable first, previews after
- Screen containers: `{Feature}ScreenContainer` - wraps screen for ViewModel integration
- State-specific composables: `{Feature}Content`, `{Feature}Loading`, `{Feature}Error`
- User interactions: `{Feature}UserInteractions` - interface for presenter callbacks
- Test tags: `{Feature}ScreenTestTag` - grouped in objects for easy access

## Logging Guidelines

**Use Timber for all logging:**
- `Timber.d()` - Debug information (debug builds only)
- `Timber.e()` - Errors that should be reported
- `Timber.wtf()` - Fatal errors/crashes (debug/QA builds)

**Important:** Never use crash reporting as a general logging mechanism.

## Development Tools

### Version Catalog
Dependencies are managed in `gradle/libs.versions.toml`. Key dependencies:
- Compose BOM (Bill of Materials) for Compose versions
- Kotlin 2.0.21
- AGP 8.13.0-rc01
- Timber for logging
- Compose Lint Checks for Compose-specific linting

### Code Quality Configuration
- **ktlint:** Configured in root `build.gradle.kts` (version 1.3.1, Android mode enabled)
- **detekt:** Configuration at `config/detekt/detekt.yml` with baseline at `config/detekt/baseline.xml`
- **Compose Lint Checks:** Automatically applied to catch Compose-specific issues

## CI/CD Behavior

The project includes environment-aware development guidelines in `app/CLAUDE.md`:

**Local Machine:**
- Do not commit or push changes automatically
- Make local changes only
- Assume development environment is configured

**CI Environment (GitHub Actions/Bitrise):**
- Can create PRs, commit and push changes when required
- Must setup required environment (Java 21, Android SDK)
- Must verify build success before creating PRs
- Use module-specific commands to minimize build time

## Important Files

- `CoroutinesApp.kt` - Application entry point, initializes Timber
- `MainActivity.kt` - Main activity with Compose setup
- `build.gradle.kts` (root) - Build configuration with ktlint/detekt setup
- `app/build.gradle.kts` - App module configuration
- `gradle/libs.versions.toml` - Centralized dependency versions
- `app/CLAUDE.md` - Additional Android development guidelines for AI agents
