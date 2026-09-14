# SBOM reusable workflow

Workflow: [`.github/workflows/sbom.yml`](../../.github/workflows/sbom.yml)

Pin: `rmkr-dev/gha-reusable-workflows/.github/workflows/sbom.yml@v0.5.0`

## Inputs

| Input | Type | Default | Notes |
| --- | --- | --- | --- |
| `path` | string | `.` | Path to scan |
| `format` | string | `spdx-json` | Passed to anchore/sbom-action |
| `artifact-name` | string | `sbom` | Basename; `.spdx.json` appended |
| `upload-artifact` | boolean | `true` | Upload workflow artifact |
| `upload-artifact-retention` | number | `0` | Days to retain; `0` = repo default |
| `timeout-minutes` | number | `30` | Job timeout |

## Verify step

When `format` is `spdx-json`, the workflow parses the SPDX JSON and fails on empty/unparseable documents.

## Examples

```yaml
jobs:
  sbom:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/sbom.yml@v0.5.0
    with:
      path: .
      artifact-name: sbom-root
      upload-artifact: true
      upload-artifact-retention: 14
```

Self-tests: `sbom-sample` / `sbom-java-sample` in `.github/workflows/ci.yml`.

See also: [permissions.md](../security/permissions.md), [troubleshooting.md](troubleshooting.md).
