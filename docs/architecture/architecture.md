# Architecture

## Purpose

This repository hosts **reusable** GitHub Actions workflows (`on: workflow_call`) consumed by other personal repositories. Callers pin a ref (`@v1` or `@main`) and pass inputs such as `working-directory` and language version.

## Components

| Component | Role |
| --- | --- |
| Caller repos | Own application code; declare thin wrapper workflows that `uses:` this repo |
| Reusable workflows | `.github/workflows/*.yml` with `workflow_call` — Python CI, Java/Maven CI, CodeQL, dependency review, SBOM, release helpers |
| Self-test CI | `.github/workflows/ci.yml` in this repo calls the reusable workflows against `samples/` |
| GitHub-hosted runners | Execute jobs; no self-hosted runners in this design |
| Security / supply-chain jobs | CodeQL analysis, dependency review on PRs, SBOM artifact upload |
| Samples | `samples/python-hello`, `samples/java-hello` — minimal packages that keep contracts honest |

## Data flow (summary)

1. A push or pull request in a **caller** repo triggers a thin workflow.
2. That workflow invokes a reusable workflow here with `uses: ...@ref`.
3. Jobs run on **GitHub-hosted** runners (checkout, language setup, test/lint).
4. Optional jobs run CodeQL, dependency-review, or SBOM generation and upload artifacts.
5. Release helpers may create or react to tags/releases when called.

## Design choices

- **Free-first**: GitHub-hosted runners and free actions only.
- **Minimal inputs**: sensible defaults (`python-version: 3.12`, `java-version: 21`).
- **Resilient Python path**: if no project metadata exists, still compile sample sources.
- **Documented Java contract**: caller must supply `pom.xml`.
- **No secrets by default**: reusable workflows stay secret-agnostic.

## Related diagrams

- [architecture-diagram.md](architecture-diagram.md) — Mermaid overview
- [network-diagram.md](network-diagram.md) — runner egress expectations
