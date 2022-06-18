import subprocess
import sys

model_dir = "../dataset/trained_models/with_comments/"

reference_file = model_dir + "ref.txt"
prediction_file = model_dir + "pred.txt"

with open(prediction_file) as predictions:
    pipe = subprocess.Popen(
        ["perl", "multi-bleu.perl", reference_file],
        stdin=predictions,
        stdout=sys.stdout,
        stderr=sys.stderr,
    )
