# ai_dockerfile
FROM pytorch/pytorch:1.9.1-cuda11.1-cudnn8-devel
ARG DEBIAN_FRONTEND=noninteractive
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
ENV PATH /opt/conda/bin:$PATH
ENV PATH "$PATH:/root/anaconda/bin"
ENV PYTHONDONTWRITEBYTECODE 1
#플랫폼 사용 필요
RUN apt-get update
RUN apt-get update && apt-get install -y wget\
    python3-pip \
    python3-dev \
        postgresql-10 \
        libpq-dev \
        bzip2 \
        git \
        sox \
        ffmpeg \
        libcairo2 \
        libcairo2-dev \
        texlive-full \
        libxrender1

RUN conda create -n chemprop python=3.8

RUN /bin/bash -c "source activate chemprop"
RUN conda install -c conda-forge rdkit
RUN conda install -y torchaudio cudatoolkit=11.3 -c pytorch
RUN exit

RUN pip3 install git+https://github.com/bp-kelley/descriptastorus
RUN pip3 install chemprop
RUN pip install seaborn

#플랫폼 사용 필요
ADD ./requirements.txt /work/requirements.txt
RUN pip3 install --upgrade pip
RUN pip3 install -r /work/requirements.txt
WORKDIR /work

# 참고 : https://medium.com/dot-debug/running-chrome-in-a-docker-container-a55e7f4da4a8
# chrome driver와 browser 설치파일 같이 알고리즘에 같이 업로드, 설치는 이 안에서 하기 0420
RUN useradd apps
RUN mkdir -p /home/apps && chown apps:apps /home/apps

# Install x11vnc.
RUN apt-get install -y \
    x11vnc \
    xvfb \
    fluxbox \
    wget \
    wmctrl \


# Set the Chrome repo.
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list

# Install Chrome.
RUN apt-get update && apt-get -y install google-chrome-stable

COPY ../bootstrap.sh /

CMD '/bootstrap.sh'