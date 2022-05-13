import os
import cv2
import numpy as np
import imageio

black_img = ""
img_path = []
image_count = 0
src_folder = "images"
dest_folder = "../textures"

def load_img_paths_from_folder(folder):

    global image_count, img_path
    for filename in sorted(os.listdir(folder)):
        img_path.append(os.path.join(folder,filename))

    image_count = len(img_path)

def make_mosaic_image(index):
	
    images = []
	
    for x in range(index, index+100):
        if (x < image_count):
            img = cv2.imread(img_path[x])
            images.append(img)
        else:
            images.append(black_img)
	
    mosaic_h1 =  cv2.hconcat([ images[0],  images[1],  images[2],  images[3],  images[4],  images[5],  images[6],  images[7],  images[8],  images[9]])
    mosaic_h2 =  cv2.hconcat([images[10], images[11], images[12], images[13], images[14], images[15], images[16], images[17], images[18], images[19]])
    mosaic_h3 =  cv2.hconcat([images[20], images[21], images[22], images[23], images[24], images[25], images[26], images[27], images[28], images[29]])
    mosaic_h4 =  cv2.hconcat([images[30], images[31], images[32], images[33], images[34], images[35], images[36], images[37], images[38], images[39]])
    mosaic_h5 =  cv2.hconcat([images[40], images[41], images[42], images[43], images[44], images[45], images[46], images[47], images[48], images[49]])
    mosaic_h6 =  cv2.hconcat([images[50], images[51], images[52], images[53], images[54], images[55], images[56], images[57], images[58], images[59]])
    mosaic_h7 =  cv2.hconcat([images[60], images[61], images[62], images[63], images[64], images[65], images[66], images[67], images[68], images[69]])
    mosaic_h8 =  cv2.hconcat([images[70], images[71], images[72], images[73], images[74], images[75], images[76], images[77], images[78], images[79]])
    mosaic_h9 =  cv2.hconcat([images[80], images[81], images[82], images[83], images[84], images[85], images[86], images[87], images[88], images[89]])
    mosaic_h10 = cv2.hconcat([images[90], images[91], images[92], images[93], images[94], images[95], images[96], images[97], images[98], images[99]])

    mosaic = cv2.vconcat([mosaic_h1, mosaic_h2, mosaic_h3, mosaic_h4, mosaic_h5, mosaic_h6, mosaic_h7, mosaic_h8, mosaic_h9, mosaic_h10])
	
    del images
	
    return mosaic

black_img = np.zeros((1638,1638,3), np.uint8)

load_img_paths_from_folder(src_folder)

if (image_count % 100 == 0):
	num_faces = image_count // 100
else:
	num_faces = (image_count // 100) + 1

for x in range(num_faces):
    mosaic = make_mosaic_image(x * 100)
    cv2.imwrite(dest_folder + "//face" + str(x+1) + ".png", mosaic)
    del mosaic
    #if exception from cv2.imwriter above try the line below instead
    #imageio.imsave(dest_folder + "//face" + str(x+1) + ".png", mosaic)
