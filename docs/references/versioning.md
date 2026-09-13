# Versioning reusable workflows

## Tags consumers should pin

| Ref | Meaning |
| --- | --- |
| `@v0.2.0` | Exact annotated release (preferred for production) |
| `@v0.1.0` | Previous minor |
| `@main` | Latest merged work (dogfooding only) |
| `@v1` | Reserved for a future stable major line |

Create annotated tags from green `main` only.

## Semver rules for this repo

- **MAJOR** (`v1.0.0`): removing or renaming a `workflow_call` input/output, or changing defaults in a breaking way.
- **MINOR** (`v0.2.0`): additive inputs/outputs, new workflows or composites, documentation that matches new behavior.
- **PATCH** (`v0.2.1`): fixes that preserve inputs/outputs and documented contracts (including safe action pin bumps when behavior is compatible).

Composite actions under `.github/actions/` are pinned by workflows to a ref (currently `@main` during `0.x`; prefer matching the workflow tag after each release).

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
