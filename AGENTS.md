# Agent notes

Human-first rules for changing reusable workflows in this repository.

## Compatibility

- Treat workflow `inputs` and job outputs as a public API for callers.
- Prefer additive inputs with defaults over renaming or removing existing ones.
- If a change breaks callers, bump the major version tag and document migration.

## Semver and tags

- Ship via PR → merge to `main`.
- Cut annotated tags (`v0.x.y` then `v1`) only when CI on `main` is green.
- Do not rewrite published tags.

## Secrets and logs

- Never echo tokens, PATs, or credentials.
- Do not add repository secrets this repo does not need.
- Reusable workflows should not require secrets unless the caller passes them.

## Change size

- Smallest correct change that fixes or adds one concern.
- Keep sample callers under `samples/` green after edits.

## Verification

- Update or extend `samples/python-hello` and `samples/java-hello` when workflow contracts change.
- Confirm self-test CI (`.github/workflows/ci.yml`) calls the reusable workflows against `samples/`.
- Prefer running example callers before tagging a release.
