import cv2
import numpy as np
from operations import removeStemPre, imfill, findRectangle, resizeImage, centerImage, optimizeWhitePixels

def eliminateBackground(img, imagename):

    row     = img.shape[0]
    column  = img.shape[1]
    channel = img.shape[2]
    
    procSize = 256;
    height  = float(row)
    width   = float(column)
    
    if width > height:
        image = cv2.resize(img, (procSize, int(height/(width/procSize))), interpolation = cv2.INTER_CUBIC)
    else:
        image = cv2.resize(img, (int(width/(height/procSize)), procSize), interpolation = cv2.INTER_CUBIC)
    
    im      = np.copy(image)
    lab_im  = cv2.cvtColor( im, cv2.COLOR_BGR2LAB)
    
    if 3*np.sum(lab_im[:,:,2])/lab_im.size<131: # another possible condition if 3*np.sum(lab_im[:,:,0])/lab_im.size>155:
        ab  = lab_im[:,:,1:3]
    else:
        ab  = lab_im[:,:,0:2]

    # 2-means clustering with 3 iteration using Euclidean distance
    nrows   = ab.shape[0]
    ncols   = ab.shape[1]
    ab_list = ab.reshape(nrows*ncols,2)
    ab_list = np.float32(ab_list)
    
    nColors = 2
    criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 3, 1.0)
    flags = cv2.KMEANS_RANDOM_CENTERS
    compactness,labels, cluster_centers = cv2.kmeans(ab_list,nColors,None,criteria,10,flags)
    
    pixel_labels = labels.reshape(nrows, ncols)


    distances = np.zeros( (nColors,1) )
    for k in range(nColors):            # find the true cluster
        a = cluster_centers[k,0]
        b = cluster_centers[k,1]
        distances[k] = ((a-ab[10,10,0])**2 + (b-ab[10,10,1])**2)**(5e-1)
    
    ind = np.argmax(distances)
    
    # segment the image pixels in true cluster
    color = np.copy(im)
    color[pixel_labels != ind] = 255;
    segmented_image = color;
    
    # convert image into grayscale and binary for further processing
    I       = cv2.cvtColor(segmented_image,cv2.COLOR_BGR2GRAY)  # convertion to grayscale image
    blur    = cv2.GaussianBlur(I,(5,5),0)
    ret, BW = cv2.threshold(blur,0,255,cv2.THRESH_BINARY_INV+cv2.THRESH_OTSU)
    
    # additional operations
    if BW[10,10]!=0: BW = 255-BW
    BW = imfill(BW)
    BW = removeStemPre(BW)         # remove stem from leaf 
    labeledImage = 255 - BW
    
    segmented_image = np.copy(im)
    segmented_image[BW != 255] = 255
    
    # find bounding box of the leaf
    #[x, y, width, height] = findRectangle(labeledImage)
    
    # resize cropped image
    finalImage = resizeImage(segmented_image, desiredSize=256)
       
    #make all whitish pixels perfect white (rgb - 255 255 255)
    #finalImage = optimizeWhitePixels(finalImage, imgsize=256, threshold=30)
    #move the leaf to the center of the image
    #finalImage = centerImage(finalImage)

    cv2.imwrite(imagename, finalImage)   


