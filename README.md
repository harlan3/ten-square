# ten-square
OpenGL Decagonal Prism Surface Viewer

Ten-Square provides the capability to display pages from one or more PDFs onto the surface
of a Decagonal Prism, which is formed by ten square side faces and two regular decagon caps.
The text content of the PDFs is indexed into a database and can be searched by keyword.
Upon a page selection, the camera in OpenGL will then move to display the page of interest
on the surface of a Decagonal Prism.

Prerequisites:
* Linux PC with Bash terminal (Windows or Mac are not supported)
* Hardware accelerated Open GL
* GL_MAX_TEXTURE_SIZE of 16384 or greater
* Modern g++ compiler
* Dev libraries for: boost_regex, boost_thread, pthread, png, glut, GLU, GL
* Python 3 
* Python modules: cv2, numpy and imageio
* OpenJDK 11 or newer
* ant java build tool

Setup:
After installing the prerequisites, run the following scripts:
* ./build_execs.sh
* ./wipe_assets.sh
* ./gen_assets.sh

All of these scripts must complete without any errors.

Execution:
Run ten-square with following command:
* Debug/tensquare

This should bring up the Decagonal Prism textured with the PDF pages from PDF files located
in the source_pdfs folder.

Next run the Search Tool:
* cd searchtool
* java -jar dist/searchtool.jar

Mouse Controls
* left click - rotate left
* right click - rotate right
* scroll wheel - pan up and down

Keyboard Controls
* ⬆ ⬇ ⬅ ➡ pan and rotation controls
* f toggle full screen mode
* + and - zoom controls (The search tool also has a slider bar for zooming)
* p print out camera information for current camera position
* q quit

Search Tool

* Select texture index to display in combo box.
* Type in keyword and select search to query database for keyword match from pdf text content.
* Select one of the search results to view that page on the Decagonal Prism.
* Use slider bar to zoom in and out and mouse controls as defined above.
