# Action pin matrix

Current major/minor pins used by workflows and composites in this repository.
Prefer Dependabot PRs for bumps; major bumps stay ungrouped for manual review.

## First-party / GitHub

| Action | Pin | Used by |
| --- | --- | --- |
| `actions/checkout` | `v7` | All reusable workflows + composites path |
| `actions/setup-python` | `v7` | `setup-python-project` |
| `actions/setup-java` | `v6` | `setup-maven-project` |
| `github/codeql-action/*` | `v4` | `codeql.yml` |
| `actions/dependency-review-action` | `v5` | `dependency-review.yml` |

## Docker

| Action | Pin | Used by |
| --- | --- | --- |
| `docker/setup-buildx-action` | `v4` | `docker-build.yml` (Dependabot #48) |
| `docker/build-push-action` | `v7` | `docker-build.yml` (Dependabot #46) |

## Security / SBOM / scan

| Action | Pin | Used by |
| --- | --- | --- |
| `anchore/sbom-action` | `v0` | `sbom.yml` |
| `aquasecurity/trivy-action` | `v0.36.0` | `docker-build.yml` |

## This repository's composites

Reusable CI workflows pin composites to the latest annotated tag:

| Composite | Pin |
| --- | --- |
| `setup-python-project` | `@v0.5.0` |
| `setup-maven-project` | `@v0.5.0` |

After cutting a new annotated tag, bump these pins in a follow-up PR (see `AGENTS.md`).

## Consumer guidance

- Pin **this** repository's workflows with an annotated tag (`@v0.5.0`), not `@main`, for production.
- Upstream action majors may change defaults (for example Buildx / build-push). Rely on self-test `ci.yml` (`docker-sample`) before merging Dependabot majors.
- Patch/minor `github-actions` updates are grouped by Dependabot; majors open as single PRs.

See also: [docker-build.md](docker-build.md), [versioning.md](versioning.md), [../../CHANGELOG.md](../../CHANGELOG.md).

Composite `setup-maven-project` accepts optional `distribution` (default `temurin`) for `actions/setup-java`.
