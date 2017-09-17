import cv2
import numpy as np
from PIL import Image


def optimizeWhitePixels(img, imgsize, threshold):
    optimized_img = img.__copy__()
    for i in range(0, imgsize):
        for j in range(0, imgsize):
            if float(optimized_img[i][j][0]) >= (255 - threshold) and float(optimized_img[i][j][1]) >= (255 - threshold) and float(optimized_img[i][j][2]) >= (255 - threshold):
                optimized_img[i][j][0] = 255
                optimized_img[i][j][1] = 255
                optimized_img[i][j][2] = 255
    return optimized_img

#######################################################

def centerImage(image):
    copy = image.__copy__()
    copy.fill(255)

    [left, right, top, down] = findRectangle(image)

    columnnum = image.shape[1]
    rownum = image.shape[0]

    a = (columnnum - (right - left))/2
    b = (rownum - (down - top))/2
   
    for i in range(0, (down - top)):
        for j in range(0, (right - left)):
            copy[i+b][j+a] = image[i+top][j+left]

    return copy

#######################################################

def findRectangle(image):

    columnnum = image.shape[1]
    rownum = image.shape[0]

    for i in range(columnnum):  # column search
        temp = 0;
        for j in range(rownum): # row search
            if image[j][i] == 0 :
                temp = 1
                break
        if temp == 1 :
            break

    left = i

    for i in range(columnnum):  # column search
        temp = 0;
        for j in range(rownum): # row search
            if image[j][columnnum - 1 - i] == 0 :
                temp = 1
                break
        if temp == 1 :
            break

    right = columnnum - 1 - i

    for i in range(rownum):  # column search
        temp = 0;
        for j in range(columnnum): # row search
            if image[i][j] == 0 :
                temp = 1
                break
        if temp == 1 :
            break

    top = i

    for i in range(rownum):  # column search
        temp = 0;
        for j in range(columnnum): # row search
            if image[rownum - 1 - i][j] == 0 :
                temp = 1
                break
        if temp == 1 :
            break

    bottom = rownum - 1 - i

    width = right - left
    height = bottom - top

    list = [left, top , width , height]
    return list

#######################################################

def removeStemPre(img):

    se = cv2.getStructuringElement(cv2.MORPH_ELLIPSE,(9,9))
    
    #img = cv2.morphologyEx(img, cv2.MORPH_CLOSE, se)
    img = cv2.morphologyEx(img, cv2.MORPH_OPEN, se)
    
    # Connected Component Analysis
    ret, labels, stats, centroids = cv2.connectedComponentsWithStats(img)
    
    maxLabel = 0
    maxSize  = 0
    for i in range( ret ):
        sizeOfComp = np.sum(labels==i)
        if sizeOfComp > maxSize and not (stats[i,0]==0 and stats[i,1]==0):
            maxSize  = sizeOfComp
            maxLabel = i
    
    for i in range( ret ):
        if i != maxLabel:
            img[ labels==i ] = 0
    
    return img

##############################################

def removeStemPost(img):

    se = cv2.getStructuringElement(cv2.MORPH_ELLIPSE,(2,2))
    
    img = cv2.morphologyEx(img, cv2.MORPH_OPEN, se)
    
    return img

###################################################

def imfill(im_bw):

    im_floodfill = im_bw.copy()

    h, w = im_bw.shape[:2]
    mask = np.zeros((h+2, w+2), np.uint8)

    cv2.floodFill(im_floodfill, mask, (0,0), 255);

    im_floodfill_inv = cv2.bitwise_not(im_floodfill)
     
    im_out = im_bw | im_floodfill_inv

    return im_out
    
##################################################

def resizeImage(im, desiredSize):
    im1 = Image.fromarray(np.uint8(im))
    sizex, sizey = im1.size 

    if sizex > sizey:
        n2 = int(round((float(sizey)/sizex)*desiredSize)) 
        resizedImage = im1.resize((desiredSize, int(n2))) 
        new_im = Image.new('RGB', (desiredSize,desiredSize), color=(255,255,255))
        blankImage = Image.new('RGB', ((desiredSize, int(float(desiredSize)-n2)/2)), color=(255,255,255)) 
        new_im.paste(blankImage, (0,0))
        new_im.paste(resizedImage, (0,blankImage.size[1]))
        new_im.paste(blankImage, (0, resizedImage.size[1] + blankImage.size[1]))
        #new_im.save("Out" + imageFile) 
    elif sizex < sizey:   
        n2 = int(round((float(sizex)/sizey)*desiredSize))
        resizedImage = im1.resize((int(n2), desiredSize))
        new_im = Image.new('RGB', (desiredSize,desiredSize), color=(255,255,255))
        blankImage = Image.new('RGB', ((int(float(desiredSize)-n2)/2), desiredSize), color=(255,255,255))
        new_im.paste(blankImage, (0,0))
        new_im.paste(resizedImage, (blankImage.size[0],0))
        new_im.paste(blankImage, (resizedImage.size[0] + blankImage.size[0],0))
        #new_im.save("Out" + imageFile) 
    else:
        new_im = im1.resize((desiredSize, desiredSize)) 

    new_im = new_im.resize((desiredSize, desiredSize)) 
    return np.asarray(new_im)