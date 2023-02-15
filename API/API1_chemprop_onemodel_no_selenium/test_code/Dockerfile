#Python 도커파일 템플릿
FROM ubuntu:18.04

ARG DEBIAN_FRONTEND=noninteractive

#플랫폼 사용 필요
RUN apt-get update && apt-get install -y wget\
		python3-pip \
		python3-dev \
        postgresql-10 \
        libpq-dev

#플랫폼 사용 필요
# ADD ./requirements.txt /work/requirements.txt
# RUN pip3 install --upgrade pip
# RUN pip install -r /work/requirements.txt
# WORKDIR /work