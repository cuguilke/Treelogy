# Treelogy
A Leaf Based Tree Identificaton System

This repository contains the original model (CNN for Treelogy leaf dataset) and codes described in the paper "Treelogy: A Novel Tree Classifier Utilizing Deep and Hand-crafted Representations" (https://arxiv.org/abs/1701.08291). This model is the one used in the Treelogy smart-phone application.

Project website:

https://senior.ceng.metu.edu.tr/2016/payinekereg/

Latest caffemodel (v7) for tree classification:

https://www.dropbox.com/s/11web90s506073t/tree_identification_v7.caffemodel?dl=0

Treelogy dataset:

~~http://treelogy.info/dataset~~ (The link is dead due to [insufficient funds](https://www.youtube.com/watch?v=fEzHttCKR8s))

https://senior.ceng.metu.edu.tr/2016/payinekereg/dataset.html

Other necessary files:
  - synsetwords.txt -> Label file for 57 tree species.
  - deploy.prototxt -> Architecture of the ConvNet
  - ilsvrc_2012_mean.npy -> Imagemean of BVLC reference model (since our imagemean did not improve the accuracy)
 
## Citation

If you use this model or dataset in your research, please cite:

```
@article{ccuugu2017treelogy,
  title={Treelogy: A Novel Tree Classifier Utilizing Deep and Hand-crafted Representations},
  author={{\c{C}}u{\u{g}}u, {\.I}lke and {\c{S}}ener, Eren and Erciyes, {\c{C}}a{\u{g}}r{\i} and Balc{\i}, Burak and Ak{\i}n, Emre and {\"O}nal, It{\i}r and Aky{\"u}z, Ahmet O{\u{g}}uz},
  journal={arXiv preprint arXiv:1701.08291},
  year={2017}
}
```
