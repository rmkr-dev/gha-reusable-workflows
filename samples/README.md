# Samples catalog

Each sample is a **contract fixture** for this repository's `ci.yml` self-tests.
Consumer apps should not copy these layouts wholesale — use them to understand which
install / lint / test path a reusable workflow will take.

| Sample | Stack | What it proves |
| --- | --- | --- |
| [`python-hello`](python-hello) | Python + pyproject | Editable install, pytest, optional `ruff check` |
| [`python-requirements-dev`](python-requirements-dev) | requirements-dev.txt | Dev requirements install path + pytest |
| [`python-requirements-only`](python-requirements-only) | requirements.txt | Runtime requirements only (no pyproject) |
| [`python-compileall-only`](python-compileall-only) | bare sources | No metadata → `compileall` fallback (no pytest) |
| [`java-hello`](java-hello) | Maven single module | Surefire `test` + Failsafe `verify -Pintegration-test` |
| [`java-multi`](java-multi) | Maven reactor | Multi-module `test` from reactor root |
| [`docker-hello`](docker-hello) | Dockerfile | `docker-build` with `push: false` + Trivy |

## Edge coverage map

| Edge case | Covered by |
| --- | --- |
| No `pyproject.toml` / no requirements | `python-compileall-only` |
| Requirements without packaging metadata | `python-requirements-only` |
| Separate `requirements-dev.txt` | `python-requirements-dev` |
| Ruff installed via extras | `python-hello` (`[project.optional-dependencies].dev`) |
| Custom `maven-goals` / Failsafe profile | `java-hello` + `java-failsafe-sample` job |
| Reactor `-pl`-style layout (full reactor test) | `java-multi` |
| Docker build without registry push | `docker-hello` |
| SBOM path scan (Python / Java) | `sbom-sample` / `sbom-java-sample` jobs |

Caller pitfalls and unsupported edges: [edge-cases.md](../docs/references/edge-cases.md).
