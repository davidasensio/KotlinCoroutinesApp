# GitHub Actions - CI/CD Workflows

## What are GitHub Actions?

GitHub Actions is a CI/CD platform that allows you to automate your build, test, and deployment pipeline. In this project, it automatically validates every pull request to ensure code quality and functionality before merging.

## Official Resources

- **Documentation:** https://docs.github.com/en/actions
- **Marketplace:** https://github.com/marketplace?type=actions
- **Workflow Syntax:** https://docs.github.com/en/actions/reference/workflow-syntax-for-github-actions

## Configuration

### Location
- **Workflows directory:** `.github/workflows/`
- **Main workflow:** `.github/workflows/pr-validation.yml`

## PR Validation Workflow

### Trigger Events
- Pull requests to `main` or `develop` branches
- Pushes to `main` or `develop` branches

### Job 1: Code Quality & Build Validation

**Purpose:** Validate code quality, run tests, and ensure the project builds successfully.

**Steps:**
1. **Checkout Code** - Fetches the repository code
2. **Setup JDK 21** - Installs Java Development Kit (Temurin distribution)
3. **Setup Android SDK** - Installs Android SDK and tools
4. **Cache Gradle** - Caches Gradle dependencies for faster builds
5. **Code Style Check** - Runs `./gradlew ktlintCheck`
6. **Code Quality Check** - Runs `./gradlew detekt`
7. **Lint Check** - Runs `./gradlew lint`
8. **Compile Debug Sources** - Compiles app code
9. **Compile Test Sources** - Compiles test code
10. **Run Unit Tests** - Executes unit tests
11. **Build Debug APK** (conditional) - Builds APK if PR has `apk` label
12. **Upload Artifacts** - Uploads test results and reports
13. **Publish Test Results** - Posts test results as PR comment
14. **Comment PR** - Comments on PR with build status

**Duration:** ~5-10 minutes (without APK build)

**Required Permissions:**
```yaml
permissions:
  contents: read
  checks: write
  pull-requests: write
  issues: write
```

### Job 2: Instrumented Tests (E2E)

**Purpose:** Run instrumented tests on Android emulator (optional, label-triggered).

**Trigger:** Only runs when PR has `end2endTests` label

**Steps:**
1. **Setup Environment** - Similar to Job 1
2. **Cache AVD** - Caches Android Virtual Device for faster runs
3. **Create/Load AVD** - Sets up Pixel 6 emulator (API 31)
4. **Run Instrumented Tests** - Executes `./gradlew app:connectedDebugAndroidTest`
5. **Upload Test Results** - Uploads instrumented test reports
6. **Comment PR** - Posts E2E test results

**Duration:** ~15-20 minutes (includes emulator startup)

## Using Labels

### `apk` Label
Add this label to your PR to:
- Build a debug APK
- Upload APK as an artifact
- Download APK from the Actions artifacts tab

**Use case:** When you want to test the app on a physical device or share with QA.

### `end2endTests` Label
Add this label to your PR to:
- Run instrumented tests on Android emulator
- Validate UI functionality
- Test end-to-end user flows

**Use case:** For UI changes or when full E2E validation is needed.

## Viewing Results

### In Pull Request
1. Check the "Checks" tab in your PR
2. View detailed logs for each step
3. Read the bot comment with summary

### Test Results
- Click on the "Publish Test Results" check
- View test summary, failures, and skipped tests
- Download detailed HTML reports from artifacts

### Artifacts
1. Go to the Actions tab
2. Click on your workflow run
3. Scroll to "Artifacts" section
4. Download available artifacts:
   - `app-debug-{run_number}` - Debug APK (if built)
   - `test-results-{run_number}` - Unit test reports
   - `lint-reports-{run_number}` - Lint analysis
   - `detekt-reports-{run_number}` - detekt analysis
   - `instrumented-test-results-{run_number}` - E2E test reports

## Quality Checks

### Always Run
- ✅ Code style (ktlint)
- ✅ Code quality (detekt)
- ✅ Lint checks
- ✅ Unit tests
- ✅ Compilation

### Conditional (with labels)
- 📦 Debug APK build (`apk` label)
- 🧪 Instrumented tests (`end2endTests` label)

## Common Scenarios

### Standard PR (No Labels)
```
Validation runs:
✅ ktlint → detekt → lint → compile → tests
Duration: ~5-10 minutes
```

### PR with APK (Add `apk` label)
```
Validation runs:
✅ ktlint → detekt → lint → compile → tests → build APK
📦 APK available in artifacts
Duration: ~6-12 minutes
```

### PR with E2E (Add `end2endTests` label)
```
Job 1: Validation (same as standard)
Job 2: E2E Tests on emulator
🧪 Instrumented test results posted
Duration: ~20-30 minutes total
```

### PR with Both Labels
```
Job 1: Full validation + APK build
Job 2: E2E Tests
📦 APK + 🧪 E2E results available
Duration: ~25-35 minutes total
```

## Troubleshooting

### Build Failing on ktlint
```bash
# Fix locally before pushing
./gradlew formatKotlin
git add .
git commit -m "fix: Format code with ktlint"
git push
```

### Build Failing on detekt
```bash
# Check issues locally
./gradlew detekt

# Review report
open build/reports/detekt/detekt.html

# Fix issues and push
```

### Tests Failing
```bash
# Run tests locally
./gradlew app:testDebugUnitTest

# Check specific test
./gradlew app:testDebugUnitTest --tests "*ClassName*"

# Fix and push
```

### Permission Errors (403)
If actions fail with "Resource not accessible by integration":
- Check that workflow has proper permissions
- Verify repository Actions settings allow workflows
- Ensure `GITHUB_TOKEN` permissions are correct

## Performance Tips

1. **Use Caching:** Gradle caching is enabled, reuses dependencies
2. **Skip E2E When Not Needed:** Only add `end2endTests` label when necessary
3. **Run Checks Locally First:** Fix issues before pushing to save CI time
4. **Use Draft PRs:** Draft PRs can skip some checks if configured

## Monitoring

### Success Rate
- View Actions tab for workflow success rate
- Check failure patterns to identify flaky tests
- Monitor average run duration

### Costs
- GitHub Actions is free for public repositories
- Private repositories have monthly free minutes
- Monitor usage in repository Settings → Billing

## Workflow Maintenance

### Update Actions Versions
Renovate automatically updates GitHub Actions to latest versions:
- Creates PRs for action updates
- Groups all action updates together
- Labeled with `github-actions`

### Modify Workflow
1. Edit `.github/workflows/pr-validation.yml`
2. Test changes in a feature branch
3. Review workflow run results
4. Merge when validated

## Related Documentation

- [detekt](./detekt.md) - Code quality checks
- [ktlint](./ktlint.md) - Code formatting checks
- [Renovate](./renovate.md) - Automated dependency updates

## Setup Guide

To set up GitHub Actions from scratch, see: [Configure CI and Renovate Prompt](../prompts/02-configure-ci-and-renovate.md)
