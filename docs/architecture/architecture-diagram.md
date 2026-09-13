# Architecture diagram

```mermaid
flowchart LR
  subgraph Callers["Caller repositories"]
    JP["Java/Maven repo"]
    PP["Python repo"]
  end

  subgraph Reusable["rmkr-dev/gha-reusable-workflows"]
    PY["python-ci.yml"]
    JV["java-maven-ci.yml"]
    CQ["codeql.yml"]
    DR["dependency-review.yml"]
    SB["sbom.yml"]
    RT["release-tag.yml"]
  end

  subgraph Runners["GitHub-hosted runners"]
    R["ubuntu-latest jobs"]
  end

  subgraph Supply["Security / release outputs"]
    CQout["CodeQL results"]
    DRout["Dependency review"]
    SBout["SBOM artifact"]
    RELout["Release / tag"]
  end

  JP -->|uses @v1 or @main| JV
  JP -->|uses| CQ
  JP -->|uses| DR
  JP -->|uses| SB
  JP -->|uses| RT
  PP -->|uses @v1 or @main| PY
  PP -->|uses| CQ
  PP -->|uses| DR
  PP -->|uses| SB
  PP -->|uses| RT

  PY --> R
  JV --> R
  CQ --> R
  DR --> R
  SB --> R
  RT --> R

  CQ --> CQout
  DR --> DRout
  SB --> SBout
  RT --> RELout
```

Self-test path (this repository): `ci.yml` → `python-ci` / `java-maven-ci` against `samples/`.

Callers pin `@v0.1.0` (or `@main` while dogfooding).
