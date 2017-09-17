#!/usr/bin/env python
import numpy as np 
import pickle
import sys
from time import gmtime, strftime
from sklearn.externals import joblib
#one must change 'cuguilke' to his own username
caffe_root = '/home/cuguilke/caffe/'
svm_root = caffe_root + 'SVM'
sys.path.insert(0, caffe_root + 'python')
import caffe
import os
#CPU mode	
#caffe.set_mode_cpu()
#GPU mode
caffe.set_device(0)
caffe.set_mode_gpu()

class Treelogy_Identifier:
	def __init__(self, ID):
		global caffe_root
		self.idle = True
		self.ID = ID
		self.caffe_root = caffe_root
		if not os.path.isfile(self.caffe_root + 'models/finetune_flickr_style/tree_identification_v7.caffemodel'):
			print "[" + self.get_time() + "] " + "WARNING: Treelogy_Identifier" + str(self.ID) + " is NOT initialized"
			print "[" + self.get_time() + "] " + "You need to download pre-trained tree_identification_v7 CaffeNet model..."
		else:
			print "[" + self.get_time() + "] " + "Treelogy_Identifier " + str(self.ID) + " is initialized"
			self.guesses = {} #result dictionary
			self.image_filename	= ""
			self.net = caffe.Net(self.caffe_root + 'models/finetune_flickr_style/deploy.prototxt', self.caffe_root + 'models/finetune_flickr_style/tree_identification_v7.caffemodel', caffe.TEST)
			self.transformer = caffe.io.Transformer({'data': self.net.blobs['data'].data.shape})
			self.svm_identifier = SVM_Tree_Identifier(self.ID)

	def run(self, imagename, mode=1):
		# mode: 0 -> CNN Result | 1 -> Merged SVM & CNN Result
		print "[" + self.get_time() + "] " + "Treelogy_Identifier " + str(self.ID) + " is running..."
		self.image_filename = imagename
		self.guesses = {}
		self.transformer.set_transpose('data', (2,0,1))
		self.transformer.set_mean('data', np.load(self.caffe_root + 'python/caffe/imagenet/ilsvrc_2012_mean.npy').mean(1).mean(1)) # mean pixel
		self.transformer.set_raw_scale('data', 255)  # the reference model operates on images in [0,255] range instead of [0,1]
		self.transformer.set_channel_swap('data', (2,1,0))  # the reference model has channels in BGR order instead of RGB
		# larger (max. 50) batch size may be used for faster computation of multiple images
		# set net to batch size of 1
		self.net.blobs['data'].reshape(1,3,227,227)
		#Feed in the image (with some preprocessing) and classify with a forward pass.
		#In order to feed your own images, put them under ~/caffe/examples/images/ 
		self.net.blobs['data'].data[...] = self.transformer.preprocess('data', caffe.io.load_image(self.caffe_root + 'examples/images/' + self.image_filename))
		out = self.net.forward()
		fc6_feature_vector = self.net.blobs['fc6'].data[0]
		predicted_class = "Predicted class is #{}.".format(out['prob'][0].argmax())
		# load labels
		imagenet_labels_filename = self.caffe_root + 'data/flickr_style/synset_words.txt'
		try:
			labels = np.loadtxt(imagenet_labels_filename, str, delimiter='\t')
		except:
			print "You should run Ilke Cugu's create_train_val_text_v4.py"
		# sort top k predictions from softmax output
		top_k = self.net.blobs['prob'].data[0].flatten().argsort()[-1:-6:-1]
		label = labels[top_k]
		self.net.forward()
		# run SVM_Tree_Identifier with fc6_feature_vector
		svm_prediction = self.svm_identifier.predict(fc6_feature_vector)
		# add Caffe's predictions
		for i in range(0,len(label)):
			guess = str(label[i]).lower().split(" ")
			guess_string = guess[1]
			#guess_string = guess_string.replace("_", " ")
			guess_list = guess_string.split("|") #separate common name & latin name
			guess = {"name": guess_list[0], "latin_name": guess_list[1], "percentage": str("{0:.2f}".format(100 * self.net.blobs['prob'].data[0][top_k[i]]))}
			self.guesses.update({(i+1): guess})
		if mode == 1: # Merge results of Caffe & SVM
			in_top5 = True
			if self.guesses[1]["name"] != svm_prediction:
				in_top5 = False
				for i in range(2, 6):
					if self.guesses[i]["name"] == svm_prediction:
						in_top5 = True
						temp = self.guesses[i]["latin_name"]
						names = ["dummy","","","","",""]
						latin_names = ["dummy","","","","",""]
						for j in range(1,6):
							names[j] = self.guesses[j]["name"]
							latin_names[j] = self.guesses[j]["latin_name"]
						# shift names
						for j in range(2,6):
							if names[j-1] != svm_prediction:
								self.guesses[j]["name"] = names[j-1]
								self.guesses[j]["latin_name"] = latin_names[j-1]
							else:
								break
						# set top prediction as SVM's prediction
						self.guesses[1]["name"] = svm_prediction
						self.guesses[1]["latin_name"] = temp
						break
			if not in_top5:
				self.guesses[5]["name"] = svm_prediction
				self.guesses[5]["latin_name"] = ""
		print "[" + self.get_time() + "] " + "Treelogy_Identifier " + str(self.ID) + " completed the identification"

	def get_results(self):
		return self.guesses

	def get_time(self):
		return strftime("%a, %d %b %Y %X", gmtime())	 

# Directory schema must be like this:
# ./-
#	|-- SVM_Tree_Identifier.py
#   |-- SVM/-   
#			|-- svmClassifier_Pickle_caffe_finetune_fc6_v9.p
#			|-- synset_words_Pickle_caffe_finetune_fc6_v9.p
#			|-- svmClassifier_Pickle_caffe_finetune_fc6_v9.p_01.npy.z
class SVM_Tree_Identifier:
	def __init__(self, ID): 
		global svm_root
		self.ID = ID  
		self.predicted = None
		self.clf = joblib.load(svm_root + "/svmClassifier_Pickle_caffe_finetune_fc6_v9.p")
		self.synset_words_dict = joblib.load(svm_root + "/synset_words_Pickle_caffe_finetune_fc6_v9.p") 
		self.terminate = False 

	def predict(self, featureVector):      
		instance = np.array(featureVector) 
		self.predicted = self.clf.predict([instance]) 
		for k,l in self.synset_words_dict.items():
			if l[1] == self.predicted[0]:
				break
		return (l[0].split('|')[0]).lower()