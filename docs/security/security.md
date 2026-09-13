# Security

## Threat model (brief)

Reusable workflows run on **caller** repositories with the caller's `GITHUB_TOKEN` and permissions. A malicious or overly broad workflow could exfiltrate secrets or weaken checks. This repository therefore keeps workflows **minimal**, **secret-agnostic by default**, and **free of hardcoded credentials**.

## Practices

| Practice | Detail |
| --- | --- |
| No secrets in logs | Never `echo` tokens; mask sensitive outputs |
| Least privilege | Callers should set `permissions:` narrowly on jobs that call these workflows |
| Pin by tag | Prefer `@v0.1.0` / annotated semver tags over mutable branches for production callers |
| Dependency review | Use `dependency-review.yml` on pull requests |
| CodeQL | Use `codeql.yml` with a JSON `languages` input (for example `'["python"]'`) |
| SBOM | Generate and upload an SBOM artifact for release traceability |
| Dependabot | `github-actions` updates are enabled in this repo |

## Reporting

See [SECURITY.md](../../SECURITY.md) for how to report vulnerabilities privately.

## Runner trust

Jobs use GitHub-hosted runners. Consumers who need stronger isolation should follow GitHub’s hardened runner guidance in their own orgs; this repo does not customize runner images.
