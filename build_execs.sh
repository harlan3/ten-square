#!/bin/bash

cd ./processpdfs/mergepdfs/
ant clean;ant
cd ../../

cd ./processpdfs/pdftodbase/
ant clean;ant
cd ../../

cd ./processpdfs/splitpdfs/
ant clean;ant
cd ../../

cd ./processpdfs/extractimages/
ant clean;ant
cd ../../

cd ./searchtool/
ant clean;ant
cd ../

cd ./Debug
make clean; make tensquare
cd ../
