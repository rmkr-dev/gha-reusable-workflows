# Security

## Threat model (brief)

Reusable workflows run on **caller** repositories with the caller's `GITHUB_TOKEN` and permissions. A malicious or overly broad workflow could exfiltrate secrets or weaken checks. This repository therefore keeps workflows **minimal**, **secret-agnostic by default**, and **free of hardcoded credentials**.

## Practices

| Practice | Detail |
| --- | --- |
| No secrets in logs | Never `echo` tokens; mask sensitive outputs |
| Least privilege | Callers should set `permissions:` narrowly on jobs that call these workflows |
| Pin by tag | Prefer annotated semver tags (`@v0.2.0`, `@v0.1.0`) over mutable branches for production callers |
| Dependency review | Use `dependency-review.yml` on pull requests |
| CodeQL | Use `codeql.yml` with a JSON `languages` input (for example `'["python"]'` or `'["java-kotlin"]'`) |
| SBOM | Generate and upload an SBOM artifact for release traceability |
| Dependabot | `github-actions` updates are enabled in this repo |

## CodeQL (`codeql.yml`)

- Caller must pass `languages` as a JSON array string.
- Optional `working-directory` sets CodeQL `source-root` for monorepos.
- Required permissions on the caller job: `security-events: write`, `contents: read`, `actions: read`.
- Matrix uses `fail-fast: false` so one language failure does not cancel others.

## Dependency review (`dependency-review.yml`)

- Intended for `pull_request` events only (GitHub requirement for the action).
- Caller should grant `pull-requests: write` when commenting is desired; `contents: read` is always required.
- Blocks merge only when the caller configures branch protection / required checks — this workflow surfaces findings.

## SBOM (`sbom.yml`)

- Uses `anchore/sbom-action` to produce **SPDX JSON**.
- Uploads an Actions artifact named `${{ inputs.artifact-name }}.spdx.json` (default `sbom.spdx.json`).
- **Retention**: artifact lifetime follows the repository or organization Actions artifact retention setting (GitHub default is **90 days** unless customized). Callers who need longer retention should download the artifact in a follow-up job or attach it to a GitHub Release.
- `upload-release-assets` is left `false` so callers control release attachment explicitly.

## OpenSSF Scorecard

This repository **does not** ship a reusable Scorecard workflow yet. Reasons:

1. Scorecard is most valuable on the **consumer** repository (branch protection, token permissions, pinned actions in *that* repo).
2. Running Scorecard inside a reusable workflow still needs the caller's `security-events` / PAT setup and often a publish step to the public Scorecard API — that is org-specific.
3. Prefer documenting that callers add [`ossf/scorecard-action`](https://github.com/ossf/scorecard-action) in their own `.github/workflows/` when they want a public badge.

If demand grows, a thin `scorecard.yml` `workflow_call` wrapper can be added in a later minor release without breaking existing inputs.

## Reporting

See [SECURITY.md](../../SECURITY.md) for how to report vulnerabilities privately.

## Runner trust

Jobs use GitHub-hosted runners. Consumers who need stronger isolation should follow GitHub’s hardened runner guidance in their own orgs; this repo does not customize runner images.

### Dependency review self-test

`ci.yml` runs `dependency-review-sample` when `github.event_name == 'pull_request'`, calling the reusable `dependency-review.yml` workflow.
