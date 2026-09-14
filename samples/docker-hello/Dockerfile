# Minimal static sample for docker-build reusable workflow self-test.
# BusyBox keeps the image small; callers should choose their own base images.
FROM busybox:1.36.1-uclibc

COPY hello.txt /hello.txt
CMD ["cat", "/hello.txt"]
