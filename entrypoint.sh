#!/bin/bash

if [ "$preprocess" = true ]; 
then bash preprocess.sh 
else echo "Not preprocessing."
fi

if [ "$train" = true -a "$trainFromScratch" = true ]; 
then bash train.sh
else echo "Not training a new model."
fi