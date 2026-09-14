# Security Policy

## Supported versions

| Version | Supported |
| --- | --- |
| Latest annotated tag (for example `v0.4.0`) | Yes |
| `main` | Yes (rolling) |
| Older tags | Best-effort only |

Security fixes land on `main` first and are included in the next annotated release.

## Reporting a vulnerability

Please use one of these private channels:

1. **Preferred:** open a [private GitHub security advisory](https://github.com/rmkr-dev/gha-reusable-workflows/security/advisories/new) on this repository.
2. Or contact the maintainer via the GitHub profile for [`rmkr-dev`](https://github.com/rmkr-dev) (profile contact / email listed there).

Do **not** file public issues for undisclosed vulnerabilities.

Include: affected workflow file(s), ref (`@tag` / commit), and a minimal reproduction when possible.

## Scope

In scope:

- Secret leakage or unsafe defaults in reusable workflow YAML
- Supply-chain weaknesses in pinned actions used here
- Incorrect permission recommendations that would broaden caller tokens

Out of scope:

- Vulnerabilities solely in upstream actions (report upstream; we will bump pins)
- Misconfiguration entirely in a caller repository’s own workflows

## Scorecard / badges

Callers who want OpenSSF Scorecard should add it in **their** repository. See [docs/security/security.md](docs/security/security.md#openssf-scorecard) for why this repo does not yet wrap Scorecard as a reusable workflow.
