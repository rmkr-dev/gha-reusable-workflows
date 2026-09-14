# Versioning reusable workflows

## Tags consumers should pin

| Ref | Meaning |
| --- | --- |
| `@v0.5.1` | Exact annotated release (preferred for production) |
| `@v0.5.0` | Previous patch |
| `@v0.4.2` | Previous patch |
| `@v0.4.0` | Previous minor |
| `@main` | Latest merged work (dogfooding only) |
| `@v1` | Reserved for a future stable major line |

Create annotated tags from green `main` only.

## Semver rules for this repo

- **MAJOR** (`v1.0.0`): removing or renaming a `workflow_call` input/output, or changing defaults in a breaking way.
- **MINOR** (`v0.4.0`): additive inputs/outputs, new workflows or composites, documentation that matches new behavior.
- **PATCH** (`v0.2.1`): fixes that preserve inputs/outputs and documented contracts (including safe action pin bumps when behavior is compatible).

Composite actions under `.github/actions/` are pinned by workflows to a ref (follow-up PR after each annotated tag; see `AGENTS.md`).

## Preparing a release

1. Ensure CI is green on `main`.
2. Update `CHANGELOG.md` (move Unreleased notes into the new version section).
3. Create an annotated tag and GitHub Release:

```bash
git tag -a v0.2.0 -m "v0.2.0"
git push origin v0.2.0
gh release create v0.2.0 --generate-notes --target main
```

Or call `release-tag.yml` from a trusted workflow with `tag: v0.2.0`.

## v0.2.0 notes (summary)

- Hardening inputs + summaries on Python/Java CI
- Composite setup actions
- Extra samples and security/SBOM docs
- Dependabot action major bumps merged on `main`

See [CHANGELOG.md](../../CHANGELOG.md).

## Release notes source

`release-tag.yml` prefers a matching `## [X.Y.Z]` section from `changelog-path` (default `CHANGELOG.md`). If none is found, it uses `gh release create --generate-notes` when `generate-notes` is true. Empty `tag` input remains a safe no-op.

Detailed CHANGELOG / soft-notes conventions: [release-notes.md](release-notes.md).

## Latest patch

`v0.5.1` ships post-`v0.5.0` docs hygiene (architecture/permissions/edge-cases/troubleshooting/examples/SUPPORT pins and catalogs) plus a python-ci `fail-fast` description clarification. Prefer `@v0.5.1` for production pins.

## Previous minor

`v0.5.0` ships a hardening-input wave on top of `v0.4.2`: Docker `annotations` / `target` / `enable-gha-cache` / `trivy-exit-code`, Python `ruff-args` / `pytest-args` (and fail-fast → pytest `-x`), Java `maven-extra-args` / `maven-opts`, CodeQL `timeout-minutes` / `build-mode`, dependency-review `deny-licenses` / `comment-summary-in-pr`, and SBOM `timeout-minutes`.
