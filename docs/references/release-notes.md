# Release notes conventions

## CHANGELOG format

This repository follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) with
Semantic Versioning tags (`vX.Y.Z`).

```markdown
## [Unreleased]

### Added
- …

## [0.4.0] - 2026-09-14

### Added
- …
```

Footer links:

```markdown
[Unreleased]: https://github.com/rmkr-dev/gha-reusable-workflows/compare/v0.4.0...HEAD
[0.4.0]: https://github.com/rmkr-dev/gha-reusable-workflows/compare/v0.3.0...v0.4.0
```

## How `release-tag.yml` uses the CHANGELOG

1. Strip the leading `v` from the tag (`v0.3.1` → `0.3.1`).
2. Extract the `## [0.3.1]` section until the next `## ` heading.
3. If found, `gh release create … --notes-file` with that section.
4. If not found, honor `notes-fallback`:
   - `generate` (default) → `--generate-notes` when `generate-notes: true`
   - `notes` / `tag` → plain title/notes equal to the tag string

Existing tags and releases are left alone (safe skip).

## Writing notes consumers care about

- Call out **new** `workflow_call` inputs/outputs and whether they are additive.
- Call out **breaking** renames/removals (those require a major bump).
- Mention sample/self-test coverage when it documents a new contract.
- Prefer short bullets over commit dumps; `generate-notes` is the fallback, not the ideal.

## Publishing checklist

1. CI green on `main`.
2. Move `[Unreleased]` bullets into `## [X.Y.Z] - YYYY-MM-DD`.
3. Update compare footer links.
4. Create annotated tag + release (`release-tag.yml` or `gh release create`).
5. Bump consumer pin docs from `@v0.4.2` → `@v0.5.0` (then composites after the tag exists).

Semver rules: [versioning.md](versioning.md). History: [CHANGELOG.md](../../CHANGELOG.md).

## Draft releases

Pass the `draft` input (`draft: true`) to `release-tag.yml` so `gh release create` adds `--draft`.
Annotated tags are still created; the GitHub Release stays unpublished until promoted.

```yaml
jobs:
  release:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/release-tag.yml@v0.5.0
    with:
      tag: v0.5.0
      draft: true
      changelog-path: CHANGELOG.md
```

## Prereleases

Pass `prerelease: true` (optionally with `draft: true`) to mark the GitHub Release as a prerelease.
