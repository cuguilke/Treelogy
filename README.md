# Treelogy
A Leaf Based Tree Identificaton System

This repository contains the original model (CNN for Treelogy leaf dataset) and codes described in the paper "Treelogy: A Novel Tree Classifier Utilizing Deep and Hand-crafted Representations" (https://arxiv.org/abs/1701.08291). This model is the one used in the Treelogy smart-phone application.

Latest caffemodel (v7) for tree classification:

https://www.dropbox.com/s/11web90s506073t/tree_identification_v7.caffemodel?dl=0

Treelogy dataset:

http://treelogy.info/dataset

Other necessary files:
  - synsetwords.txt -> Label file for 57 tree species.
  - deploy.prototxt -> Architecture of the ConvNet
  - ilsvrc_2012_mean.npy -> Imagemean of BVLC reference model (since our imagemean did not improve the accuracy)
 

