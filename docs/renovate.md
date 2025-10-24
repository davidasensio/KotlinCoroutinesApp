# Renovate Bot - Automated Dependency Updates

## What is Renovate?

Renovate is an automated dependency update tool that keeps your project dependencies up-to-date. It automatically creates pull requests when new versions of your dependencies are available, making dependency management effortless and keeping your project secure.

## Official Resources

- **GitHub App:** https://github.com/apps/renovate
- **Official Documentation:** https://docs.renovatebot.com/
- **Configuration Reference:** https://docs.renovatebot.com/configuration-options/

## Configuration

### Location
- **Configuration file:** `renovate.json` (root)

### Update Schedule
- **Regular updates:** Weekly on Monday mornings (before 9 AM UTC)
- **Security updates:** Immediate (not scheduled)

## Current Configuration

### Dependency Grouping

Renovate groups related dependencies into single PRs for easier review:

1. **Jetpack Compose** - All androidx.compose packages and Compose BOM
2. **AndroidX** - All AndroidX libraries (excluding Compose)
3. **Kotlin** - Kotlin compiler and kotlinx libraries
4. **Testing** - JUnit, Espresso, and other test libraries
5. **Build Tools** - Android Gradle Plugin (AGP) and Gradle
6. **Code Quality** - ktlint, detekt, and lint tools
7. **Development Tools** - Timber, Compose Lint Checks, etc.
8. **GitHub Actions** - All GitHub Action updates

### Auto-merge Policy

**Current setting:** All auto-merge is **disabled**

All dependency updates require manual review and approval before merging. This ensures:
- Full control over when dependencies are updated
- Opportunity to review breaking changes
- Time to test changes before merging

### Rate Limiting
- Maximum 5 concurrent pull requests
- Maximum 2 pull requests per hour
- Prevents overwhelming the repository with PRs

## Monitoring Updates

### Dependency Dashboard

Renovate automatically creates a GitHub issue called "Dependency Dashboard" that shows:
- All pending updates
- Rate-limited PRs waiting to be created
- Ignored dependencies
- Configuration errors or warnings

**Access:** Check your repository's Issues tab for "Dependency Dashboard"

### PR Labels

Each Renovate PR is labeled for easy identification:
- `dependencies` - All dependency updates
- `compose` - Jetpack Compose updates
- `androidx` - AndroidX library updates
- `kotlin` - Kotlin ecosystem updates
- `testing` - Testing library updates
- `build-tools` - Build tool updates
- `code-quality` - Code quality tool updates
- `dev-tools` - Development tool updates
- `github-actions` - GitHub Action updates
- `security` - Security vulnerability fixes

## Common Tasks

### Review Pending Updates
1. Check the "Dependency Dashboard" issue
2. Review each pending update
3. Click "Approve" for updates you want

### Merge an Update
1. Review the Renovate PR
2. Check that CI passes (ktlint, detekt, tests)
3. Review the changelog/release notes
4. Test locally if needed
5. Merge when ready

### Ignore a Dependency
Add to the `ignoreDeps` array in `renovate.json`:
```json
{
  "ignoreDeps": ["com.example:unwanted-library"]
}
```

### Pause Updates
Add to `renovate.json`:
```json
{
  "enabled": false
}
```

## Update Behavior

### What Happens Weekly (Monday Mornings)

1. Renovate checks for new dependency versions
2. Creates PRs for updates (respecting rate limits)
3. Groups related dependencies together
4. Adds appropriate labels
5. Uses semantic commit messages: `chore(deps): update X to Y`

### Security Vulnerabilities

- **Immediate PRs:** Not subject to schedule
- **Special label:** `security`
- **Priority:** High priority for review

### Major Version Updates

Major version updates require approval via the Dependency Dashboard before PRs are created. This prevents automatic creation of potentially breaking changes.

## CI Integration

All Renovate PRs automatically trigger GitHub Actions:
- Code style checks (ktlint)
- Code quality checks (detekt)
- Lint checks
- Unit tests
- Compilation verification

PRs only merge if all checks pass (when manual merge is performed).

## Installation

### First Time Setup

1. **Install Renovate GitHub App:**
   - Visit: https://github.com/apps/renovate
   - Click "Install" or "Configure"
   - Select this repository

2. **Review Onboarding PR:**
   - Renovate creates an onboarding PR
   - Review detected dependencies
   - Check configuration
   - Merge to activate

3. **Monitor First Run:**
   - Check Dependency Dashboard issue
   - Review created PRs
   - Wait for next Monday for scheduled updates

## Troubleshooting

### No PRs Being Created
- Check Dependency Dashboard for errors
- Verify GitHub App is installed
- Check rate limiting status
- Review `renovate.json` for syntax errors

### PRs Not Running CI
- Ensure GitHub Actions workflow is enabled
- Check workflow permissions in repository settings
- Verify `GITHUB_TOKEN` has necessary permissions

### Too Many PRs
- Adjust `prConcurrentLimit` in `renovate.json`
- Reduce `prHourlyLimit`
- Consider grouping more dependencies together

## Best Practices

1. **Review Regularly:** Check Dependency Dashboard weekly
2. **Read Changelogs:** Review what changed before merging
3. **Test Critical Updates:** Test major updates locally before merging
4. **Keep Config Updated:** Adjust grouping as project grows
5. **Monitor Security:** Merge security updates promptly

## Configuration Customization

### Change Schedule
```json
{
  "schedule": ["before 9am on the first day of the month"]
}
```

### Add New Group
```json
{
  "packageRules": [
    {
      "groupName": "My Custom Group",
      "matchPackagePatterns": ["^com\\.example\\."],
      "labels": ["custom-group"]
    }
  ]
}
```

## Related Documentation

- [GitHub Actions](./github-actions.md) - CI integration with Renovate
- [CLAUDE.md](../CLAUDE.md) - AI assistant guidelines including Renovate

## Setup Guide

To set up Renovate from scratch, see: [Configure CI and Renovate Prompt](../prompts/02-configure-ci-and-renovate.md)
