FROM ubuntu:latest
LABEL authors="luu"

ENTRYPOINT ["top", "-b"]