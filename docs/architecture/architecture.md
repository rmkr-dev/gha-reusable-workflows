# Architecture

## Purpose

This repository hosts **reusable** GitHub Actions workflows (`on: workflow_call`) consumed by other personal repositories. Callers pin a ref (`@v0.1.0`, later `@v1`, or `@main`) and pass inputs such as `working-directory` and language version.

## Components

| Component | Role |
| --- | --- |
| Caller repos | Own application code; declare thin wrapper workflows that `uses:` this repo |
| Reusable workflows | `.github/workflows/{python-ci,java-maven-ci,codeql,dependency-review,sbom,release-tag}.yml` |
| Composite actions | `.github/actions/setup-python-project`, `.github/actions/setup-maven-project` — shared setup helpers |
| Self-test CI | `.github/workflows/ci.yml` calls `python-ci` and `java-maven-ci` against `samples/` |
| GitHub-hosted runners | Execute jobs (`ubuntu-latest`); no self-hosted runners |
| Security / supply-chain jobs | CodeQL analysis, dependency review on PRs, SBOM artifact upload |
| Samples | `samples/python-hello`, `samples/java-hello` — keep contracts honest |

## Data flow (summary)

1. A push or pull request in a **caller** repo triggers a thin workflow.
2. That workflow invokes a reusable workflow here with `uses: ...@ref`.
3. Jobs run on **GitHub-hosted** runners (checkout, language setup, test/lint).
4. Optional jobs run CodeQL, dependency-review, or SBOM generation and upload artifacts.
5. `release-tag.yml` can create an annotated `v*` tag and GitHub Release when called with a `tag` input.

## Design choices

- **Free-first**: GitHub-hosted runners and free actions only.
- **Minimal inputs**: defaults `python-version: 3.12`, `java-version: 21`.
- **Composite setup helpers**: language install + cache + install/verify live under `.github/actions/` for reuse by workflows.
- **Resilient Python path**: if no project metadata exists, still run `compileall`.
- **Documented Java contract**: caller must supply `pom.xml`.
- **No secrets by default**: reusable workflows stay secret-agnostic.
- **Scorecard is caller-owned**: see security docs for why there is no reusable Scorecard wrapper yet.

## Related diagrams

- [architecture-diagram.md](architecture-diagram.md) — Mermaid overview
- [network-diagram.md](network-diagram.md) — runner egress expectations
- [examples.md](../references/examples.md) — consumer snippets
