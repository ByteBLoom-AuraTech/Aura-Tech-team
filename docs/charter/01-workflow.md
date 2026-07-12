# Workflow Charter — Member 1

## 1. Team Roles

| Role              | Responsibility                                       |
|-------------------|------------------------------------------------------|
| Team Lead         | Final decision on architecture & conflicts           |
| Maintainer        | Merges approved PRs, manages branch protection rules |
| Reviewer (x2 min) | Reviews and approves every PR before merge           |
| Contributor       | Implements features on dedicated feature branches    |

## 2. Branching Model

We adopt **GitHub Flow** (simplified trunk-based workflow):

- `main` is always deployable/stable.
- All work happens on `feature/*` branches created from `main`.
- No direct commits to `main` — everything goes through a Pull Request.
- Branches are deleted after merge to keep the repo clean.

Naming convention:

| Prefix    | Meaning                                 |
|-----------|-----------------------------------------|
| feat:     | New feature                             |
| fix:      | Bug fix                                 |
| docs:     | Documentation only changes              |
| chore:    | Maintenance, tooling, config changes    |
| refactor: | Code change with no behavior change     |
| test:     | Adding or updating tests                |
| style:    | Formatting, whitespace, no logic change |

**Rules:**
- No commit is accepted without a valid prefix.
- Description must be in imperative mood (e.g. "add", not "added").
- Commits must be scoped (one logical change per commit).

## 4. Pull Request Rules

- Submitted only via structured PR (no direct pushes to `main`).
- Requires **minimum 2 approvals** before merge.
- Merge strategy: **Squash and Merge** to keep history clean.
- All conflicts resolved collaboratively before merge.