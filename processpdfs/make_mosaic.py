import os
import cv2
import numpy as np
import imageio

global black_img
src_folder = "images"
dest_folder = "../textures"

def load_images_from_folder(folder):
	
    images = []
    
    for filename in sorted(os.listdir(folder)):
        img = cv2.imread(os.path.join(folder,filename))
        if img is not None:
            images.append(img)
    return images

def make_mosaic_image(images, index):
	
    mosaic_h1 =  cv2.hconcat([images[index +  0], images[index +  1], images[index +  2], images[index +  3], images[index +  4], images[index +  5], images[index +  6], images[index +  7], images[index +  8], images[index +  9]])
    mosaic_h2 =  cv2.hconcat([images[index + 10], images[index + 11], images[index + 12], images[index + 13], images[index + 14], images[index + 15], images[index + 16], images[index + 17], images[index + 18], images[index + 19]])
    mosaic_h3 =  cv2.hconcat([images[index + 20], images[index + 21], images[index + 22], images[index + 23], images[index + 24], images[index + 25], images[index + 26], images[index + 27], images[index + 28], images[index + 29]])
    mosaic_h4 =  cv2.hconcat([images[index + 30], images[index + 31], images[index + 32], images[index + 33], images[index + 34], images[index + 35], images[index + 36], images[index + 37], images[index + 38], images[index + 39]])
    mosaic_h5 =  cv2.hconcat([images[index + 40], images[index + 41], images[index + 42], images[index + 43], images[index + 44], images[index + 45], images[index + 46], images[index + 47], images[index + 48], images[index + 49]])
    mosaic_h6 =  cv2.hconcat([images[index + 50], images[index + 51], images[index + 52], images[index + 53], images[index + 54], images[index + 55], images[index + 56], images[index + 57], images[index + 58], images[index + 59]])
    mosaic_h7 =  cv2.hconcat([images[index + 60], images[index + 61], images[index + 62], images[index + 63], images[index + 64], images[index + 65], images[index + 66], images[index + 67], images[index + 68], images[index + 69]])
    mosaic_h8 =  cv2.hconcat([images[index + 70], images[index + 71], images[index + 72], images[index + 73], images[index + 74], images[index + 75], images[index + 76], images[index + 77], images[index + 78], images[index + 79]])
    mosaic_h9 =  cv2.hconcat([images[index + 80], images[index + 81], images[index + 82], images[index + 83], images[index + 84], images[index + 85], images[index + 86], images[index + 87], images[index + 88], images[index + 89]])
    mosaic_h10 = cv2.hconcat([images[index + 90], images[index + 91], images[index + 92], images[index + 93], images[index + 94], images[index + 95], images[index + 96], images[index + 97], images[index + 98], images[index + 99]])

    mosaic = cv2.vconcat([mosaic_h1, mosaic_h2, mosaic_h3, mosaic_h4, mosaic_h5, mosaic_h6, mosaic_h7, mosaic_h8, mosaic_h9, mosaic_h10])
	
    return mosaic

global black_img
black_img = np.zeros((1638,1638,3), np.uint8)

images = load_images_from_folder(src_folder)

image_count = len(images)
if (image_count % 100 == 0):
	num_faces = image_count // 100
	num_black = 0
else:
	num_faces = (image_count // 100) + 1
	num_black = 100 - (image_count % 100)

for x in range(num_black):
	images.append(black_img)

for x in range(num_faces):
    mosaic = make_mosaic_image(images, x * 100)
    cv2.imwrite(dest_folder + "//face" + str(x+1) + ".png", mosaic)
    del mosaic
    #if exception from cv2.imwriter above try the line below instead
    #imageio.imsave(dest_folder + "//face" + str(x+1) + ".png", mosaic)
