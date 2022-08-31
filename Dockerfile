FROM nvcr.io/nvidia/tensorflow:20.03-tf2-py3
ARG comment-or-not-to-comment_VERSION="1.0-SNAPSHOT"
LABEL name="ciselab/funcom"
LABEL url="https://github.com/ciselab/comment-or-not-to-comment"
LABEL vcs="https://github.com/ciselab/comment-or-not-to-comment"

# Install Java
RUN apt-get update && apt-get install ca-certificates-java -y && apt install openjdk-11-jdk -y

# Copy code2seq files
COPY JavaExtractor/ /app/code2seq/JavaExtractor/
COPY *.py /app/code2seq/
COPY *.sh /app/code2seq/
COPY requirements_docker.txt /app/code2seq/
COPY datasets/funcom /app/code2seq/datasets/funcom

COPY cppminer/ /app/code2seq/cppminer/
COPY models/ /app/code2seq/models/
COPY Input.source /app/code2seq/

# Install code2seq requirements
WORKDIR /app/code2seq
RUN pip install -r requirements_docker.txt

# Preprocess variables
ENV preprocess=true
ENV comments=true
ENV stopwords=true

# Training variables
ENV train=true
ENV trainFromScratch=true
ENV continueTrainingFromCheckpoint=false 

#Evaluation variables
# 

# Entrypoints are used to run preprocessing/training in a reproducible way, as a template what to do. Defaults will be given here, changed by docker-compose.
ENTRYPOINT ["bash","./entrypoint.sh"]