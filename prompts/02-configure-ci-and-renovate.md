# Prompt: Configure CI/CD and Renovate Bot

## Objective
Set up GitHub Actions for continuous integration and Renovate bot for automated dependency management.

## Instructions

Please configure CI/CD and automated dependency management for this Android project:

### 1. GitHub Actions - PR Validation Workflow

Create `.github/workflows/pr-validation.yml` with:

**Job 1: Code Quality & Build Validation**
- Trigger: on pull_request and push to main/develop branches
- Runner: ubuntu-latest
- Timeout: 30 minutes
- Required permissions:
  - contents: read
  - checks: write
  - pull-requests: write
  - issues: write

**Steps:**
1. Checkout code with fetch-depth: 0
2. Setup JDK 21 (Temurin distribution)
3. Setup Android SDK
4. Cache Gradle packages
5. Run code style check: `./gradlew ktlintCheck`
6. Run code quality check: `./gradlew detekt`
7. Run lint check: `./gradlew lint`
8. Compile debug sources: `./gradlew app:compileDebugSources`
9. Compile test sources: `./gradlew app:compileDebugUnitTestSources`
10. Run unit tests: `./gradlew app:testDebugUnitTest`
11. Build Debug APK (conditional on 'apk' label)
12. Upload artifacts (test results, lint reports, detekt reports)
13. Publish test results using `EnricoMi/publish-unit-test-result-action@v2`
14. Comment on PR with build results

**Job 2: Instrumented Tests (E2E)**
- Depends on validation job
- Only runs when PR has 'end2endTests' label
- Same permissions as Job 1
- Uses Android emulator (API 31, Pixel 6 profile)
- Caches AVD for faster runs
- Runs: `./gradlew app:connectedDebugAndroidTest`
- Uploads instrumented test results
- Comments on PR with E2E test results

### 2. Renovate Bot Configuration

Create `renovate.json` at repository root with:

**Base Configuration:**
- Extend: config:recommended, :dependencyDashboard, :semanticCommits
- Schedule: Weekly on Monday mornings (before 9 AM UTC)
- Labels: ["dependencies"]
- Enable dependency dashboard
- Semantic commits with prefix: "chore(deps):"

**Security:**
- Vulnerability alerts with "security" label
- Immediate PRs for security issues (no schedule)

**Grouping Rules:**
1. **Jetpack Compose** - All androidx.compose packages and compose-bom
2. **AndroidX** - All androidx packages (excluding compose)
3. **Kotlin** - All org.jetbrains.kotlin and kotlinx libraries
4. **Testing** - JUnit, Espresso, and test-related dependencies
5. **Build Tools** - AGP and Gradle
6. **Code Quality** - ktlint, detekt, lint tools
7. **Development Tools** - Timber, compose-lint-checks, etc.
8. **GitHub Actions** - All action updates

**Auto-merge Policy:**
- Disabled (all PRs require manual review)
- Major updates require Dependency Dashboard approval

**Rate Limiting:**
- Max 5 concurrent PRs
- Max 2 PRs per hour

**Other Settings:**
- Gradle support enabled
- Rebase only when conflicted
- Platform automerge: false
- Status check verification: true

### 3. GitHub Repository Configuration

**Required Actions:**
1. Install Renovate GitHub App on the repository
2. Grant necessary permissions to the app
3. Wait for onboarding PR from Renovate
4. Review and merge the onboarding PR

### 4. Documentation Updates

Update `CLAUDE.md` with:
- Renovate configuration section under "Development Tools"
- Document update schedule and behavior
- Document auto-merge policy
- Document monitoring via Dependency Dashboard

## Expected Outcome

After running this prompt, the project should have:
- ✅ GitHub Actions workflow for PR validation
- ✅ Automated code quality checks on every PR
- ✅ Optional APK build and E2E tests via labels
- ✅ Renovate bot configuration ready
- ✅ Weekly automated dependency update PRs
- ✅ Security vulnerability alerts
- ✅ Grouped dependency updates by ecosystem

## Verification Steps

### For GitHub Actions:
1. Create a test branch and push changes
2. Open a pull request
3. Verify workflow runs automatically
4. Check that all quality checks pass
5. Add 'apk' label to test APK build
6. Add 'end2endTests' label to test E2E workflow

### For Renovate:
1. Install Renovate GitHub App
2. Wait for onboarding PR (within minutes)
3. Review the onboarding PR
4. Merge to activate Renovate
5. Check Dependency Dashboard issue
6. Wait for first scheduled run (next Monday)

## Common Issues

### GitHub Actions 403 Error
If you get "Resource not accessible by integration" error:
- Ensure jobs have `pull-requests: write` and `issues: write` permissions
- This is required for posting test results and comments

### Renovate Not Creating PRs
- Verify GitHub App is installed and authorized
- Check Dependency Dashboard for rate limiting or errors
- Ensure `renovate.json` is valid JSON

## Related Documentation
- [GitHub Actions Documentation](../docs/github-actions.md)
- [Renovate Documentation](../docs/renovate.md)
