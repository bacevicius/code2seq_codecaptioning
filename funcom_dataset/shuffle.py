import argparse
import collections
import json
import random
import math

def load_pid():
	f = 'fid_pid'
	pidtofid = collections.defaultdict(list)
	for line in open(f, 'r').readlines()[1:]:
		t = line.split('\t')
		fid = int(t[0])
		pid = int(t[1])
		pidtofid[pid].append(fid)

	return pidtofid

def load_data(fname):
	data = {}
	for line in open(fname):
		tmp = line.split('\t')
		fid = int(tmp[0])
		value = tmp[1]
		data[fid] = value
	return data

def load_json(fname):
	f = open(fname)
	function_json = json.load(f)
	
	return function_json

def write(data, fname):
    fo = open(fname, 'w')
    for fid, string in data.items():
        fo.write("{}\t{}\n".format(fid, string))
    fo.close()

def write_function(functiondata, commentdata, fname):
    fo = open(fname, 'w')
    for fid, functioncode in functiondata.items():
		
        fo.write(str({"\n/**\n{}\n*/\n\t{}\n".format(commentdata[fid], functioncode)})+ "\n")

    fo.close()

if __name__ == '__main__':
	print("Making new train/valid/test split")
	parser = argparse.ArgumentParser(description='')
	parser.add_argument('--seed', type=int, default=None)
	parser.add_argument('--valid-size', type=float, default=0.05)
	parser.add_argument('--test-size', type=float, default=0.05)
	args = parser.parse_args()

	# Get args ####
	seed = args.seed
	valid_size = args.valid_size
	test_size = args.test_size

	# set seed for random splits
	if seed is not None:
		print("Using seed {}".format(seed))
	else:
		print("Using random seed")

	random.seed(a=seed)

	f1 = 'comments'
	f2 = 'functions.json'

	pidlist = load_pid()
	coms = load_data(f1)

	src = load_json(f2)

	shuffle_list = list(pidlist.keys())
	random.shuffle(shuffle_list)

	testnum = math.ceil(len(shuffle_list)*test_size)
	validnum = math.ceil(len(shuffle_list)*valid_size)

	testset = shuffle_list[:testnum]
	validset = shuffle_list[testnum:(testnum+validnum)]
	trainset = shuffle_list[(testnum+validnum):]

	print("Project counts:")
	print("Train: {} Valid: {} Test: {}".format(len(trainset), len(validset), len(testset)))

	trainfun = {}
	validfun = {}
	testfun = {}

	traincom = {}
	validcom = {}
	testcom = {}

	for pid in trainset:
		for fid in pidlist[pid]:
			traincom[fid] = coms[fid].strip()
			trainfun[fid] = src[str(fid)].strip()
	for pid in validset:
		for fid in pidlist[pid]:
			validcom[fid] = coms[fid].strip()
			validfun[fid] = src[str(fid)].strip()
	for pid in testset:
		for fid in pidlist[pid]:
			testcom[fid] = coms[fid].strip()
			testfun[fid] = src[str(fid)].strip()


	ftrain = './train/functions.train.jsonl'
	fvalid = './valid/functions.val.jsonl'
	ftest = './test/functions.test.jsonl'

	write_function(trainfun, traincom, ftrain)
	write_function(validfun, validcom, fvalid)
	write_function(testfun, testcom, ftest)