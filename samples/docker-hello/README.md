# docker-hello

Minimal BusyBox image used by this repository's `ci.yml` to exercise
`docker-build.yml` with `push: false` and Trivy enabled.

## Local build

```bash
docker build -t local/docker-hello:dev -f samples/docker-hello/Dockerfile samples/docker-hello
docker run --rm local/docker-hello:dev
```

## Caller tip

Prefer your own Dockerfile and base image in consumer repos. This sample exists
only to keep the reusable workflow's self-test green without a registry push.
