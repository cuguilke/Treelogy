#!/usr/bin/env python
import numpy as np 
import sys
from time import gmtime, strftime
#one must change 'cuguilke' to his own username
caffe_root = '/home/cuguilke/caffe/'
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
            #print "[" + self.get_time() + "] " + "Treelogy_Identifier " + str(self.ID) + " is initialized"
            self.guesses = {} #result dictionary
            self.image_filename = ""
            self.net = caffe.Net(self.caffe_root + 'models/finetune_flickr_style/deploy.prototxt', self.caffe_root + 'models/finetune_flickr_style/tree_identification_v7.caffemodel', caffe.TEST)
            self.transformer = caffe.io.Transformer({'data': self.net.blobs['data'].data.shape})

    def run(self, imagename):
        #print "[" + self.get_time() + "] " + "Treelogy_Identifier " + str(self.ID) + " is running..."
        self.image_filename = imagename
        self.guesses = {} 
        self.transformer.set_mean('data', np.load(self.caffe_root + 'python/caffe/imagenet/ilsvrc_2012_mean.npy').mean(1).mean(1)) # mean pixel
        self.transformer.set_transpose('data', (2,0,1))
        self.transformer.set_raw_scale('data', 255)  # the reference model operates on images in [0,255] range instead of [0,1]
        self.transformer.set_channel_swap('data', (2,1,0))  # the reference model has channels in BGR order instead of RGB
        # larger (max. 50) batch size may be used for faster computation of multiple images
        # set net to batch size of 1
        self.net.blobs['data'].reshape(1,3,227,227)
        #Feed in the image (with some preprocessing) and classify with a forward pass.
        #In order to feed your own images, put them under ~/caffe/examples/images/ 
        #self.net.blobs['data'].data[...] = self.transformer.preprocess('data', caffe.io.load_image(self.caffe_root + 'examples/images/' + self.image_filename))
        self.net.blobs['data'].data[...] = self.transformer.preprocess('data', caffe.io.load_image(self.caffe_root + 'data/flickr_style/test_images/' + self.image_filename))
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
        self.net.forward()
        #print imagename + "-> Result identifier's " + str(top_k[0]) + ": " + labels[top_k[0]]
        # Caffe's predictions
        for i in range(0,len(top_k)):
            guess = top_k[i]
            guess = {"id": guess}
            self.guesses.update({(i+1): guess})
        #print "[" + self.get_time() + "] " + "Treelogy_Identifier " + str(self.ID) + " completed the identification"

    def get_results(self):
        return self.guesses

    def get_time(self):
        return strftime("%a, %d %b %Y %X", gmtime())

target_dir = '/home/cuguilke/caffe/data/flickr_style/'
test_dir = target_dir + 'test_images/'
test_image_count = 0
correct_1st_guess = 0
top5_accuracy = 0
labels = {}

if os.path.exists(test_dir):
    identifier = Treelogy_Identifier(1)
    print "[ %1 ] " + "Testing phase is initialized."
    dirs = [x[0] for x in os.walk(test_dir)] 
    print "[ %2 ] " + "Gathering labels..."
    with open (target_dir + "test.txt", "r") as labelfile:
        for line in labelfile:
            info = (line.split("/")[-1][:-1]).split(" ")
            labels.update({info[0]: int(info[1])})
    dir_count = len(dirs)
    for i in range(1,dir_count):
        dirname = dirs[i].split("/")[-1]
        for f in os.listdir(dirs[i]):
            identifier.run(dirname + "/" + f)
            test_image_count += 1
            prediction = identifier.get_results()
            if prediction[1]["id"] == labels[f]:
                correct_1st_guess += 1
            if prediction[1]["id"] == labels[f] or prediction[2]["id"] == labels[f] or prediction[3]["id"] == labels[f]  or prediction[4]["id"] == labels[f]  or prediction[5]["id"] == labels[f]:
                top5_accuracy += 1
        progress = int((float(i)/dir_count)*100)
        log = "[ %" + str(progress) + " ] Testing..."
        if progress > 2:
            print log
    accuracy = (correct_1st_guess / float(test_image_count)) * 100 
    accuracy5 = (top5_accuracy / float(test_image_count)) * 100 
    print "[ %100 ] Testing is completed. Top-1 Accuracy: %" + "%.2f" % accuracy
    print "[ %100 ] Testing is completed. Top-5 Accuracy: %" + "%.2f" % accuracy5