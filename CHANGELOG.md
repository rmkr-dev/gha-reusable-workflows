## [Unreleased]

### Added

- Job summaries on `codeql.yml` and `dependency-review.yml` (input echo for callers)

### Changed

- Security docs cover CodeQL/dependency-review job summaries and v0.5.x inputs
- Pin composite actions used by reusable CI workflows to `@v0.5.1`
- `samples/docker-hello` caller tips cover annotations/target/cache/trivy-exit-code
- Architecture docs map labeled post-v0.5.1
- `samples/java-hello` / `python-hello` caller tips for maven-opts/extra-args and ruff/pytest/fail-fast
- `samples/java-multi` documents `maven-extra-args` for `-pl`/`-am`

## [0.5.1] - 2026-09-14

### Fixed

- `python-ci.yml` `fail-fast` input description now matches pytest `-x` behavior (was Maven-worded)

### Changed

- Pin composite actions used by reusable CI workflows to `@v0.5.0`
- Architecture diagram and permissions examples prefer `@v0.5.0`
- Architecture docs map labeled post-v0.5.0
- Edge-cases catalog covers Docker target/cache/annotations/trivy-exit-code, Python fail-fast/`ruff-args`/`pytest-args`, dependency-review deny-licenses, CodeQL build-mode, release draft/prerelease
- Development docs prefer `@v0.5.0`; samples catalog caller tips refreshed
- Troubleshooting tables cover trivy-exit-code/target/cache, CodeQL build-mode/timeout, dependency-review deny-licenses/comment-summary, release draft/prerelease, Maven opts/extra-args
- Docker-build reference documents OCI `annotations`; README adds CodeQL / dependency-review input table
- Examples Docker snippet shows optional labels/annotations/target/trivy-exit-code; release-notes footer example uses v0.5.0
- SUPPORT.md example pin prefers `@v0.5.0`
- PR template additive-input examples mention `annotations`

## [0.5.0] - 2026-09-14

### Added

- `docker-build.yml` `annotations` input (OCI annotations; empty default)
- `python-ci.yml` `ruff-args` input (extra flags for `ruff check`; empty default)
- `codeql.yml` `timeout-minutes` input (default `360`)
- `java-maven-ci.yml` `maven-opts` input (sets `MAVEN_OPTS`; empty default)
- `dependency-review.yml` `comment-summary-in-pr` input (default `true`)
- `docker-build.yml` `trivy-exit-code` input (default `1`; set `0` for report-only scans)
- `codeql.yml` `build-mode` input (optional; empty uses codeql-action default)
- `sbom.yml` `timeout-minutes` input (default `30`)
- `dependency-review.yml` `deny-licenses` input (comma-separated SPDX ids; empty default)
- `java-maven-ci.yml` `maven-extra-args` input (extra CLI args after goals; empty default)
- `docker-build.yml` `target` input (multi-stage; empty default)
- `docker-build.yml` `enable-gha-cache` input (default `true`; toggles buildx `type=gha` cache)
- `python-ci.yml` `pytest-args` input (extra flags appended after `pytest -q`; empty default)

### Fixed

- `python-ci.yml` `fail-fast` now passes pytest `-x` when tests run (was summary-only)

### Changed

- README documents `release-tag.yml` input table (`draft` / `prerelease` / notes)
- Pin composite actions used by reusable CI workflows to `@v0.4.2`

## [0.4.2] - 2026-09-14

### Added

- `docker-build.yml` `trivy-ignore-unfixed` input (default `true`)
- `release-tag.yml` `prerelease` input (default `false`)
- Samples catalog tips for fetch-depth / Docker labels-build-args
- `docker-build.yml` `build-args` input (comma-separated `KEY=VALUE`; empty default)

### Changed

- CODEOWNERS covers `/docs/`; CONTRIBUTING mentions ownership
- Architecture docs note self-test concurrency + workflow_dispatch
- Examples/SBOM docs show `upload-artifact-retention` usage
- CONTRIBUTING / development docs prefer `@v0.4.1` consumer pins
- Pin composite actions used by reusable CI workflows to `@v0.4.1`

## [0.4.1] - 2026-09-14

### Added

- `java-distribution` input on `java-maven-ci.yml` / composite (default `temurin`)
- CODEOWNERS paths for workflows/composites; PR template additive-input checkbox
- Self-test `ci.yml` `workflow_dispatch` trigger for manual sample matrix runs
- `codeql.yml` optional `queries` input (passed to codeql-action/init)
- `dependency-review.yml` `fail-on-severity` input (default `low`)
- SBOM reference (`docs/references/sbom.md`)
- Architecture docs map for action-pins, concurrency, permissions, troubleshooting
- `fetch-depth` input on `python-ci.yml` and `java-maven-ci.yml` (default `1`; `0` = full history)
- SUPPORT.md with support boundaries and how-to-ask guidance
- `release-tag.yml` `draft` input (default `false`) for unpublished GitHub Releases
- Troubleshooting guide (`docs/references/troubleshooting.md`) for Python/Java/Docker/SBOM/CodeQL callers
- `docker-build.yml` `labels` input (OCI labels passed to build-push-action; empty default)
- Caller permissions reference (`docs/security/permissions.md`) with per-workflow least-privilege table
- Self-test `ci.yml` concurrency group with `cancel-in-progress` for superseded PR/branch runs
- Caller concurrency patterns (`docs/references/concurrency.md`) and examples snippet
- Action pin matrix reference (`docs/references/action-pins.md`)
- `sbom.yml` `upload-artifact-retention` input (days; `0` = repo default)

### Changed

- Self-test concurrency: `cancel-in-progress` only for `pull_request` (preserve main CI)
- Dependabot major bumps (CI green): `docker/build-push-action` v6→v7 (#46), `docker/setup-buildx-action` v3→v4 (#48)
- README SBOM input table (`format`, retention)
- CodeQL security/examples guidance; expand pull request template for Docker/SBOM/CI surface
- Pin composite actions used by reusable CI workflows to `@v0.4.0`
- CONTRIBUTING / development docs for v0.4.0 surface; Dependabot groups patch/minor `github-actions`

### Fixed

- Wire `setup-maven-project` `distribution` input (was missing after #65)

## [0.4.0] - 2026-09-14

### Added

- `sbom.yml` `format` input (default `spdx-json`) and post-generate verify step (non-empty / parseable SPDX JSON)
- `sbom-sample` and `sbom-java-sample` CI self-tests
- Docker build reference (`docs/references/docker-build.md`)
- Matrix caller patterns (`strategy.matrix` on reusable workflow jobs, language/version and Docker-per-service matrices) in `docs/references/monorepo.md`
- Samples catalog (`samples/README.md`), caller edge-cases (`docs/references/edge-cases.md`), and release-notes conventions (`docs/references/release-notes.md`)
- `release-tag.yml` job-summary preview of CHANGELOG excerpt (`notes-bytes` + markdown preview)

### Changed

- Expanded sample READMEs: `docker-hello` (Trivy), `java-hello` (Failsafe IT naming), `java-multi` (`-pl`/`-am`), `python-compileall-only`
- Consumer docs cross-links for Docker, matrix, edge cases, and release notes

### Fixed

- Dropped unsupported `dependency-review` self-test on this repository (reusable workflow remains for callers with Dependency graph)

## [0.3.0] - 2026-09-14

### Added

- `docker-build.yml` reusable workflow (`workflow_call`): buildx build, `push` default false, optional Trivy scan
- Sample `samples/docker-hello` and self-test job in `ci.yml`
- `release-tag.yml`: optional CHANGELOG section extraction (`changelog-path`) with soft `notes-fallback`
- Monorepo / matrix caller docs (`docs/references/monorepo.md`) with path-filter example
- `samples/python-hello` optional `ruff` so self-test exercises the existing `ruff check` path
- `samples/java-hello` Failsafe `integration-test` profile and `java-failsafe-sample` CI job

### Changed

- Self-test `ci.yml` now covers Docker build and Java Failsafe verify in addition to Python/Java samples

## [0.2.0] - 2026-09-13

### Added

- Workflow hardening inputs for Python/Java CI: `timeout-minutes`, `fail-fast`, cache toggles, `maven-goals`
- Job summaries and workflow outputs (`python-version` / `tests-ran`, `java-version` / `maven-goals`)
- Composite actions: `setup-python-project`, `setup-maven-project` (wired into reusable CI workflows)
- Samples: `python-requirements-dev` (requirements-dev install path), `java-multi` (Maven reactor)
- SBOM `upload-artifact` input and retention guidance in step summary
- Expanded security docs (CodeQL, dependency-review, SBOM retention, caller-owned Scorecard rationale)
- This changelog and semver guidance for workflow consumers

### Changed

- Action pins bumped via Dependabot: `actions/checkout` v7, `actions/setup-python` v7, `actions/setup-java` v6, `github/codeql-action` v4, `actions/dependency-review-action` v5
- Self-test CI now exercises four samples

### Security

- Clarified `SECURITY.md` contact channels and supported versions table

## [0.1.0] - 2026-09-13

### Added

- Initial reusable workflows: `python-ci`, `java-maven-ci`, `codeql`, `dependency-review`, `sbom`, `release-tag`
- Self-test `ci.yml` with `samples/python-hello` and `samples/java-hello`
- Foundation docs, MIT license, Dependabot for `github-actions`

[Unreleased]: https://github.com/rmkr-dev/gha-reusable-workflows/compare/v0.5.1...HEAD
[0.5.1]: https://github.com/rmkr-dev/gha-reusable-workflows/compare/v0.5.0...v0.5.1
[0.5.0]: https://github.com/rmkr-dev/gha-reusable-workflows/compare/v0.4.2...v0.5.0
[0.4.2]: https://github.com/rmkr-dev/gha-reusable-workflows/compare/v0.4.1...v0.4.2
[0.4.1]: https://github.com/rmkr-dev/gha-reusable-workflows/compare/v0.4.0...v0.4.1
[0.4.0]: https://github.com/rmkr-dev/gha-reusable-workflows/compare/v0.3.0...v0.4.0
[0.3.0]: https://github.com/rmkr-dev/gha-reusable-workflows/compare/v0.2.0...v0.3.0
[0.2.0]: https://github.com/rmkr-dev/gha-reusable-workflows/compare/v0.1.0...v0.2.0
[0.1.0]: https://github.com/rmkr-dev/gha-reusable-workflows/releases/tag/v0.1.0
