# Support

## How to get help

1. Read [docs/references/troubleshooting.md](docs/references/troubleshooting.md) and [docs/references/examples.md](docs/references/examples.md).
2. Compare your caller with the self-test in [`.github/workflows/ci.yml`](.github/workflows/ci.yml) and packages under [`samples/`](samples/).
3. Open a GitHub Discussion or Issue on this repository with:
   - The `uses:` pin (for example `@v0.4.0`)
   - Workflow file name and relevant `with:` inputs
   - Link to a failing Actions run (redacted of secrets)

## What we support

| Area | Supported |
| --- | --- |
| Reusable workflows listed in the README | Yes on the latest annotated tag and `main` |
| Composite actions in `.github/actions/*` | Yes when pinned to the same tag as the calling workflows |
| Third-party actions (Dependabot bumps) | Best-effort; majors reviewed via CI self-tests |
| Caller-specific org policies / runners | Out of scope (document in your repo) |

## Security issues

Do not file public issues for vulnerabilities. Use [SECURITY.md](SECURITY.md).
