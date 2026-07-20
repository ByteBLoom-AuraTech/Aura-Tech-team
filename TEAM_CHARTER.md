# Team Charter — Aura Tech Team

## 1. Workflow (Fatima Abu Azab)

**Team Roles**
- Team Lead: Final decision on architecture & conflicts
- Maintainer: Merges approved PRs, manages branch protection rules
- Reviewer (x2 min): Reviews and approves every PR before merge
- Contributor: Implements features on dedicated feature branches

**Branching Model**
We adopt **GitHub Flow** (simplified trunk-based workflow):
- `main` is always deployable/stable.
- All work happens on `feature/*` branches created from `main`.
- No direct commits to `main` — everything goes through a Pull Request.
- Branches are deleted after merge to keep the repo clean.

**Commit Prefix Rules**
- `feat:` — New feature
- `fix:` — Bug fix
- `docs:` — Documentation only changes
- `chore:` — Maintenance, tooling, config changes
- `refactor:` — Code change with no behavior change
- `test:` — Adding or updating tests
- `style:` — Formatting, whitespace, no logic change

**Rules:**
- No commit is accepted without a valid prefix.
- Description must be in imperative mood (e.g. "add", not "added").
- Commits must be scoped (one logical change per commit).

**Pull Request Rules**
- Submitted only via structured PR (no direct pushes to `main`).
- Requires **minimum 2 approvals** before merge.
- Merge strategy: **Squash and Merge** to keep history clean.
- All conflicts resolved collaboratively before merge.

---

## 2. Clean Code Standards (Noor Aljouidi)

**How to Name Things**
- Variables & Functions: Use `camelCase` (e.g., `userId`, `calculateTotal`). Make names clear so anyone can understand them.
- Classes: Use `PascalCase` (e.g., `UserService`, `MainScreen`).
- Constants: Use all capital letters with underscores `UPPER_CASE` (e.g., `MAX_RETRY`).

**Writing Functions**
- One Job: Every function should do only one thing.
- Keep it short: Try to make functions short (not more than 20 lines).
- Parameters: Do not use more than 3 parameters for one function.

**Code Style**
- Spaces: Use 4 spaces for indentation to keep the code neat.
- Clean Up: Delete any unused imports or commented-out code before pushing.
- Comments: Write comments only to explain "why" you did something, not "what" the code does.

---

## 3. Communication & SLAs (Rawan Rezq)

We are a team that believes in helping each other and growing together. Here is how we work:
- Chat: We stay in touch daily on WhatsApp.
- Meet: We sync up every day at 04:00 AM on Google Meet.
- Collaborate: We share what we learn and help each other out constantly.
- Decisions: We only move forward when we all agree.
- Review SLA: Any Pull Request must be reviewed within 24 hours of being opened.
- Escalation: If no review happens within 48 hours, the author should notify the team on WhatsApp.

---

## 4. Architecture & Ignores (Baraa Abo Sharar)

**Project Folders**
We keep our project simple and organized like this:
- packages/ — Each feature of the app gets its own folder here (like `login/`, `profile/`, `settings/`). Everything related to that feature stays together.
- usecases/ — This is where we put the "actions" our app can do (like `LoginUser`, `SaveProfile`). Think of it as one file = one job the app does.
- Example:
 src/
 packages/
 login/
 profile/
 usecases/
 LoginUser
 SaveProfile
If you're adding new code, ask yourself: "Is this a feature?" → put it in `packages/`. "Is this an action?" → put it in `usecases/`.

### What We Don't Upload (.gitignore)
Some files should **never** be pushed to GitHub because they are personal, temporary, or too big:

* `.idea/` — IntelliJ's personal settings (different for everyone's computer).
* `.env` — secret passwords and API keys.
* `*.log` — log files created automatically while running the app.
* `build/` — files generated automatically when the project builds.

**Rule of thumb:** if the file gets created automatically or has secrets in it, it goes in `.gitignore`.
