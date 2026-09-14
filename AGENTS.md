# Agent notes

Human-first rules for changing reusable workflows in this repository.

## Compatibility

- Treat workflow `inputs` and job outputs as a public API for callers.
- Prefer additive inputs with defaults over renaming or removing existing ones.
- If a change breaks callers, bump the major version tag and document migration.

## Semver and tags

- Ship via PR → merge to `main` (multi-commit PRs preferred).
- Cut annotated tags (`v0.x.y` then `v1`) only when CI on `main` is green.
- Do not rewrite published tags.
- After a new tag: bump composite action pins in a follow-up PR (tag must exist first).

## Secrets and logs

- Never echo tokens, PATs, or credentials.
- Do not add repository secrets this repo does not need.
- Reusable workflows should not require secrets unless the caller passes them.
- `docker-build.yml` does not log in to registries; callers own push auth.

## Change size

- Smallest correct change that fixes or adds one concern.
- Keep sample callers under `samples/` green after edits.
- Update `docs/references/*` and `CHANGELOG.md` Unreleased in the same wave when contracts change.

## Verification

- Update or extend samples when workflow contracts change (see `samples/README.md`).
- Confirm self-test CI (`.github/workflows/ci.yml`) calls the reusable workflows against `samples/`.
- Prefer running example callers before tagging a release.
- SBOM self-tests must keep the verify step green (non-empty / parseable SPDX when format is `spdx-json`).

## Action pins

- Keep the matrix in `docs/references/action-pins.md` current when Dependabot majors merge or composites are retargeted.
