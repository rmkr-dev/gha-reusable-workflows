# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project uses [Semantic Versioning](https://semver.org/spec/v2.0.0.html) for annotated tags that consumers pin.

## [Unreleased]

### Added

- Monorepo / matrix caller docs (`docs/references/monorepo.md`) with path-filter example

### Changed

- Pin composite actions used by reusable CI workflows to `@v0.2.0`

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

[Unreleased]: https://github.com/rmkr-dev/gha-reusable-workflows/compare/v0.2.0...HEAD
[0.2.0]: https://github.com/rmkr-dev/gha-reusable-workflows/compare/v0.1.0...v0.2.0
[0.1.0]: https://github.com/rmkr-dev/gha-reusable-workflows/releases/tag/v0.1.0
