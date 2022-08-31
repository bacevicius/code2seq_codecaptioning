#!/bin/bash

# This file downloads and prepares the FunCom dataset specifically.

cd datasets/funcom/dataset

# ------------ 
# Download and unzip tokenized and filtered datasets
# wget http://leclair.tech/data/funcom/index_v5.html#procdata
# wget http://leclair.tech/data/funcom/index_v5.html#tokdata

# Or: copy an existing dataset
cp ../../../data/funcom_filtered.tar.gz funcom_filtered.tar.gz
cp ../../../data/funcom_tokenized.tar.gz funcom_tokenized.tar.gz
# ------------ 

# Unzip the downloaded archives
tar xvf funcom_filtered.tar.gz
tar xvf funcom_tokenized.tar.gz

# Copy required files from both datasets
cp ./funcom_processed/functions.json functions.json
cp ./funcom_tokenized/comments comments
cp ./funcom_tokenized/fid_pid fid_pid

# Create training directories
mkdir test
mkdir train
mkdir valid

# Run the shuffle file which creates training data distributions
python shuffle.py

# Remove the now redundant files
rm *.tar.gz
rm -rf funcom*
rm comments
rm fid_pid
rm functions.json

echo "Dataset downloaded and prepared."
exit