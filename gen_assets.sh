#!/bin/bash

cd ./processpdfs/mergepdfs
java -jar dist/mergepdfs.jar
cd ../../

cd ./processpdfs/splitpdfs
mkdir split_pdfs
java -jar dist/splitpdfs.jar
cd ../../

cd ./processpdfs/extractimages/
mkdir ../images
java -Xmx8G -jar dist/extractimages.jar
cd ../../

mkdir ./textures
cd ./processpdfs
python3 make_mosaic.py
cd ../

cd processpdfs/pdftodbase
rm -rf dbase
mkdir dbase
java -jar dist/pdftodbase.jar
cd ../../

cd searchtool
rm -rf dbase
cp -r ../processpdfs/pdftodbase/dbase .
cd ../

