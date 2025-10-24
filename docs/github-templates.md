# GitHub Templates - Pull Request Template

## What are GitHub Templates?

GitHub templates provide a consistent structure for pull requests and issues. The PR template ensures all contributors provide necessary information when creating pull requests, making code review more efficient and thorough.

## Official Resources

- **Documentation:** https://docs.github.com/en/communities/using-templates-to-encourage-useful-issues-and-pull-requests
- **Best Practices:** https://docs.github.com/en/communities/using-templates-to-encourage-useful-issues-and-pull-requests/about-issue-and-pull-request-templates

## Configuration

### Location
- **PR Template:** `.github/pull_request_template.md`

This template automatically populates the description field when creating a new pull request.

## Pull Request Template Structure

### 1. Description 📋
Brief overview of what this PR does and why.

**Purpose:** Provides context for reviewers about the changes.

### 2. Changes Made 👨‍💻
Bulleted list of the main changes in the PR.

**Purpose:** Quick overview of what was modified, added, or removed.

### 3. Type of Change ⚙️
Checkbox list to categorize the PR:
- [ ] New feature
- [ ] Bug fix
- [ ] Architecture
- [ ] Refactoring (no functional changes)
- [ ] Documentation update
- [ ] CI/CD update
- [ ] Other

**Purpose:** Helps reviewers understand the nature and scope of changes.

### 4. Related Links 🔗
Links to related issues, documentation, or external resources.

**Purpose:** Provides additional context and traceability.

### 5. Screenshots / Recordings 📸
Visual evidence of changes (especially for UI updates).

**Purpose:** Makes UI changes easier to review without running the app.

**Includes:**
- Collapsible section for multiple screenshots
- Table for before/after comparisons
- Image size controls (`width=300px`)

### 6. Testing Performed 🛡
Checklist of testing activities:
- [ ] Unit tests added/updated
- [ ] UI tests added/updated
- [ ] Manual testing performed

**Purpose:** Ensures proper test coverage and validation.

### 7. Notes & Ideas 💡
Additional notes, ideas, or discussion points.

**Purpose:** Space for technical notes or future improvements.

## How to Use

### Creating a PR

1. **Push your branch** to GitHub
2. **Click "New Pull Request"** on GitHub
3. **Template auto-fills** the description
4. **Fill in each section:**
   - Write a clear description
   - List all changes
   - Check the type of change
   - Add relevant links
   - Include screenshots for UI changes
   - Document testing performed
   - Add any relevant notes

### Example PR Description

```markdown
## Description 📋
This PR adds a new feature to display user profile information with real-time updates using Kotlin Flow.

## Changes Made 👨‍💻
- Created `ProfileScreen` composable with Material3 design
- Implemented `ProfileViewModel` with Flow-based state management
- Added `ProfileRepository` with coroutine-based data fetching
- Created unit tests for ViewModel and Repository
- Updated navigation graph to include profile screen

## Type of Change ⚙️
- [x] New feature
- [ ] Bug fix
- [ ] Architecture
- [ ] Refactoring (no functional changes)
- [ ] Documentation update
- [ ] CI/CD update
- [ ] Other (please describe):

## Related Links 🔗
- Issue #42
- [Flow Documentation](https://kotlinlang.org/docs/flow.html)

## Screenshots / Recordings 📸
<img src="https://example.com/screenshot.png" width=300px>

## Testing Performed 🛡
- [x] Unit tests added/updated
- [ ] UI tests added/updated
- [x] Manual testing performed

## Notes & Ideas 💡
- Consider adding pull-to-refresh in future iteration
- Profile caching could be optimized with Room database
```

## Best Practices

### For Contributors

1. **Be Thorough:** Fill in all relevant sections
2. **Be Specific:** Provide detailed descriptions, not just "fix bug"
3. **Use Checkboxes:** Mark completed items with [x]
4. **Add Context:** Link to related issues or documentation
5. **Include Visuals:** Screenshots for UI, diagrams for architecture
6. **Document Testing:** Show what was tested and how

### For Reviewers

1. **Check Completeness:** Ensure all sections are filled
2. **Review Testing:** Verify adequate test coverage
3. **Request Updates:** Ask for missing information
4. **Validate Links:** Check that referenced issues/docs exist

## Template Customization

### Adding Sections

Edit `.github/pull_request_template.md`:

```markdown
## New Section 🎯
<!-- Description of new section -->

- Point 1
- Point 2
```

### Removing Sections

Simply delete unwanted sections from the template file.

### Section Order

Rearrange sections by cutting and pasting in the template file.

## Multiple Templates

You can create multiple PR templates for different purposes:

```
.github/
  PULL_REQUEST_TEMPLATE/
    feature_template.md
    bugfix_template.md
    release_template.md
```

Users can then choose which template to use when creating a PR.

## CI Integration

The PR template works alongside GitHub Actions:
- Template ensures documentation is complete
- GitHub Actions ensures code quality
- Together they provide comprehensive PR validation

## Common Patterns

### Feature PR
- Focus on new functionality
- Detailed screenshots
- Comprehensive testing section

### Bug Fix PR
- Include steps to reproduce
- Show before/after behavior
- Reference original issue

### Refactoring PR
- Explain why refactoring was needed
- Document what changed structurally
- Confirm no functional changes

### Documentation PR
- List all documentation updates
- Link to affected docs
- Note any missing documentation

## Related Documentation

- [GitHub Actions](./github-actions.md) - CI/CD automation
- [Contributing Guidelines](../CLAUDE.md) - Project contribution guidelines

## Setup Guide

To set up GitHub templates from scratch, see: [Initial Project Setup Prompt](../prompts/01-initial-project-setup.md)
