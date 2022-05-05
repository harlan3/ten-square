/*
 * Ten Square (OpenGL Decagonal Prism Surface Viewer)
 * Copyright (c) 2022 Harlan Murphy - Orbisoftware
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

#include <boost/thread.hpp>
#include <GL/glut.h>
#include <stdio.h>
#include <stdlib.h>
#include <iostream>
#include <cmath>
#include <png.h>

#include "JSONObject.hpp"
#include "server/ServerMain.hpp"
#include "server/ReceiveDatagramThread.hpp"

void mouseCB(int button, int stat, int x, int y);

GLuint texture[1];
GLuint width = 800;
GLuint height = 600;
GLfloat xRot, zRot;
GLfloat zDist;
bool fullScreen;
bool mouseLeftDown;
bool mouseRightDown;
int zoomFactor = 1.0;

// Search Tool Controls
int curPageNum = 1;
int newPageNum = 1;
int curTextureIndex = 1;
int newTextureIndex = 1;
int newZoomFactor = 1;
int curZoomFactor = 1;

bool appRunning = true;
ServerMain serverMain;
ReceiveDatagramThread receivedDatagramThread;

bool loadPngImage(char *name, int &outWidth, int &outHeight, bool &outHasAlpha,
		unsigned char *&outData) {

	png_structp png_ptr;
	png_infop info_ptr;
	unsigned int sig_read = 0;
	int color_type, interlace_type;
	FILE *fp;

	if ((fp = fopen(name, "rb")) == NULL)
		return false;

	png_ptr = png_create_read_struct(PNG_LIBPNG_VER_STRING, NULL, NULL, NULL);
	if (png_ptr == NULL) {
		fclose(fp);
		return false;
	}

	info_ptr = png_create_info_struct(png_ptr);
	if (info_ptr == NULL) {
		fclose(fp);
		png_destroy_read_struct(&png_ptr, NULL, NULL);
		return false;
	}

	if (setjmp(png_jmpbuf(png_ptr))) {

		png_destroy_read_struct(&png_ptr, &info_ptr, NULL);
		fclose(fp);

		return false;
	}

	png_init_io(png_ptr, fp);
	png_set_sig_bytes(png_ptr, sig_read);
	png_read_png(png_ptr, info_ptr,
	PNG_TRANSFORM_STRIP_16 | PNG_TRANSFORM_PACKING | PNG_TRANSFORM_EXPAND,
			NULL);

	png_uint_32 width, height;
	int bit_depth;
	png_get_IHDR(png_ptr, info_ptr, &width, &height, &bit_depth, &color_type,
			&interlace_type, NULL, NULL);
	outWidth = width;
	outHeight = height;

	unsigned int row_bytes = png_get_rowbytes(png_ptr, info_ptr);
	outData = (unsigned char*) malloc(row_bytes * outHeight);

	png_bytepp row_pointers = png_get_rows(png_ptr, info_ptr);

	for (int i = 0; i < outHeight; i++)
		memcpy(outData + (row_bytes * i), row_pointers[i], row_bytes);

	png_destroy_read_struct(&png_ptr, &info_ptr, NULL);

	fclose(fp);

	return true;
}

void loadGLTexture(int faceIndex) {

	string pngFileStr = "textures/face" + std::to_string(faceIndex) + ".png";
	const char *pngFile = pngFileStr.c_str();

	for (int i = 0; i < 1; ++i) {

		unsigned char *textureImage;
		int imageWidth, imageHeight;
		bool hasAlpha;
		loadPngImage((char*) pngFile, imageWidth, imageHeight, hasAlpha,
				textureImage);

		// copy the texture to OpenGL
		glGenTextures(1, &texture[i]);

		// set active texture and configure it
		glBindTexture(GL_TEXTURE_2D, texture[i]);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		// copy bitmap data to texture object
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		glTexImage2D(GL_TEXTURE_2D, 0, hasAlpha ? 4 : 3, imageWidth,
				imageHeight, 0, hasAlpha ? GL_RGBA : GL_RGB, GL_UNSIGNED_BYTE,
				textureImage);

		free(textureImage);

		// unbind
		glBindTexture(GL_TEXTURE_2D, 0);

	}
}

int init() {

	glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
	glPolygonMode(GL_FRONT, GL_FILL);
	glCullFace(GL_FRONT);
	glEnable(GL_CULL_FACE);
	glEnable(GL_TEXTURE_2D);
	loadGLTexture(1);

	xRot = -90;
	zRot = 0;
	zDist = 0;
	zoomFactor = 1.0;

	return 1;
}

void setProjectionMatrix() {

	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(atan(tan(50.0 * M_PI / 360.0) / zoomFactor) * 360.0 / M_PI,
			(float) width / (float) height, 0.0001, 100);
}

void display() {

	glClear(GL_COLOR_BUFFER_BIT);
	glLoadIdentity();

	setProjectionMatrix();

	glTranslatef(0.0f, 0.0f, -10.0f);
	glRotatef(xRot, 1.0f, 0.0f, 0.0f);
	glRotatef(zRot, 0.0f, 0.0f, 1.0f);
	glTranslatef(0.0f, 0.0f, -zDist);

	glBindTexture(GL_TEXTURE_2D, texture[0]);
	glBegin(GL_QUADS);

	// Quad 1
	glTexCoord2d(1.0, 1.0);
	glVertex3f(-1.0, 0.0, -3.09);
	glTexCoord2d(0.9, 1.0);
	glVertex3f(-0.809017, 0.587785, -3.09);
	glTexCoord2d(0.9, 0.0);
	glVertex3f(-0.809017, 0.587785, 3.09);
	glTexCoord2d(1.0, 0.0);
	glVertex3f(-1, 0.0, 3.09);

	// Quad 2
	glTexCoord2d(0.9, 1.0);
	glVertex3f(-0.809017, 0.587785, -3.09);
	glTexCoord2d(0.8, 1.0);
	glVertex3f(-0.309017, 0.951057, -3.09);
	glTexCoord2d(0.8, 0.0);
	glVertex3f(-0.309017, 0.951057, 3.09);
	glTexCoord2d(0.9, 0.0);
	glVertex3f(-0.809017, 0.587785, 3.09);

	// Quad 3
	glTexCoord2d(0.8, 1.0);
	glVertex3f(-0.309017, 0.951057, -3.09);
	glTexCoord2d(0.7, 1.0);
	glVertex3f(0.309017, 0.951057, -3.09);
	glTexCoord2d(0.7, 0.0);
	glVertex3f(0.309017, 0.951057, 3.09);
	glTexCoord2d(0.8, 0.0);
	glVertex3f(-0.309017, 0.951057, 3.09);

	// Quad 4
	glTexCoord2d(0.7, 1.0);
	glVertex3f(0.309017, 0.951057, -3.09);
	glTexCoord2d(0.6, 1.0);
	glVertex3f(0.809017, 0.587785, -3.09);
	glTexCoord2d(0.6, 0.0);
	glVertex3f(0.809017, 0.587785, 3.09);
	glTexCoord2d(0.7, 0.0);
	glVertex3f(0.309017, 0.951057, 3.09);

	// Quad 5
	glTexCoord2d(0.6, 1.0);
	glVertex3f(0.809017, 0.587785, -3.09);
	glTexCoord2d(0.5, 1.0);
	glVertex3f(1.0, 0.0, -3.09);
	glTexCoord2d(0.5, 0.0);
	glVertex3f(1.0, 0.0, 3.09);
	glTexCoord2d(0.6, 0.0);
	glVertex3f(0.809017, 0.587785, 3.09);

	// Quad 6
	glTexCoord2d(0.5, 1.0);
	glVertex3f(1.0, 0.0, -3.09);
	glTexCoord2d(0.4, 1.0);
	glVertex3f(0.809017, -0.587785, -3.09);
	glTexCoord2d(0.4, 0.0);
	glVertex3f(0.809017, -0.587785, 3.09);
	glTexCoord2d(0.5, 0.0);
	glVertex3f(1.0, 0.0, 3.09);

	// Quad 7
	glTexCoord2d(0.4, 1.0);
	glVertex3f(0.809017, -0.587785, -3.09);
	glTexCoord2d(0.3, 1.0);
	glVertex3f(0.309017, -0.951057, -3.09);
	glTexCoord2d(0.3, 0.0);
	glVertex3f(0.309017, -0.951057, 3.09);
	glTexCoord2d(0.4, 0.0);
	glVertex3f(0.809017, -0.587785, 3.09);

	// Quad 8
	glTexCoord2d(0.3, 1.0);
	glVertex3f(0.309017, -0.951057, -3.09);
	glTexCoord2d(0.2, 1.0);
	glVertex3f(-0.309017, -0.951057, -3.09);
	glTexCoord2d(0.2, 0.0);
	glVertex3f(-0.309017, -0.951057, 3.09);
	glTexCoord2d(0.3, 0.0);
	glVertex3f(0.309017, -0.951057, 3.09);

	// Quad 9
	glTexCoord2d(0.2, 1.0);
	glVertex3f(-0.309017, -0.951057, -3.09);
	glTexCoord2d(0.1, 1.0);
	glVertex3f(-0.809017, -0.587785, -3.09);
	glTexCoord2d(0.1, 0.0);
	glVertex3f(-0.809017, -0.587785, 3.09);
	glTexCoord2d(0.2, 0.0);
	glVertex3f(-0.309017, -0.951057, 3.09);

	// Quad 10
	glTexCoord2d(0.1, 1.0);
	glVertex3f(-0.809017, -0.587785, -3.09);
	glTexCoord2d(0.0, 1.0);
	glVertex3f(-1.0, 0.0, -3.09);
	glTexCoord2d(0.0, 0.0);
	glVertex3f(-1.0, 0.0, 3.09);
	glTexCoord2d(0.1, 0.0);
	glVertex3f(-0.809017, -0.587785, 3.09);

	glEnd();

	glutSwapBuffers();
}

double getZRotInc() {
	return pow(1.2203 * zoomFactor, -0.415);
}

double getZScrollInc() {
	return pow(0.5881 * zoomFactor, -0.656);
}

void lookAtPage(int newPageNum) {

	int row;

	if (newPageNum % 10 == 0)
		row = (newPageNum / 10);
	else
		row = (newPageNum / 10) + 1;

	int column = newPageNum % 10;

	zRot = -36.083 * column + 108.45;
	zDist = -0.6184 * row + 3.3992;
}

void idleFunction() {

	if (mouseLeftDown) {
		zRot -= getZRotInc();
		glutPostRedisplay();
	} else if (mouseRightDown) {
		zRot += getZRotInc();
		glutPostRedisplay();
	}

	if (curTextureIndex != newTextureIndex) {
		loadGLTexture(newTextureIndex);
		curTextureIndex = newTextureIndex;
		glutPostRedisplay();
	}

	if (curPageNum != newPageNum) {
		lookAtPage(newPageNum);
		curPageNum = newPageNum;
		glutPostRedisplay();
	}

	if (curZoomFactor != newZoomFactor) {
		zoomFactor = newZoomFactor;
		curZoomFactor = newZoomFactor;
		glutPostRedisplay();
	}

	if (!appRunning)
		exit(0);
}

void reshape(int w, int h) {

	width = w;
	height = h;

	glViewport(0, 0, (GLsizei) width, (GLsizei) height);
	setProjectionMatrix();
}

void keyboard(unsigned char key, int x, int y) {
	switch (key) {

	case 'f':
		if (!fullScreen) {
			glutFullScreen();
			fullScreen = true;
		} else if (fullScreen) {
			glutReshapeWindow(800, 600);
			glutPositionWindow(0, 0);
			fullScreen = false;
		}
		break;
	case 'Z':
		if (zoomFactor > 1.99) {
			zoomFactor -= 1.0f;
			glutPostRedisplay();
		}
		break;
	case 'z':
		if (zoomFactor < 99.01) {
			zoomFactor += 1.0f;
			glutPostRedisplay();
		}
		break;
	case 'p':
		cout << endl;
		cout << "xRot = " << xRot << endl;
		cout << "zRot = " << zRot << endl;
		cout << "zDist = " << zDist << endl;
		cout << "zoomFactor = " << zoomFactor << endl;
		break;
	case 'q':
		appRunning = false;
		break;

	default:
		break;
	}
}

void mouseCB(int button, int state, int x, int y) {

	if (button == GLUT_LEFT_BUTTON) {
		if (state == GLUT_DOWN)
			mouseLeftDown = true;
		else if (state == GLUT_UP)
			mouseLeftDown = false;
	} else if (button == GLUT_RIGHT_BUTTON) {
		if (state == GLUT_DOWN)
			mouseRightDown = true;
		else if (state == GLUT_UP)
			mouseRightDown = false;
	} else if (button == 3) { // Scroll wheel up
		if (state == GLUT_UP)
			return;
		zDist += getZScrollInc() * 0.2;
	} else if (button == 4) { // Scroll wheel down
		if (state == GLUT_UP)
			return;
		zDist -= getZScrollInc() * 0.2;
	}

	glutPostRedisplay();
}

void processMessages() {

	while (appRunning) {

		vector<JSONPacket> packetList;

		receivedDatagramThread.drainTo(packetList);

		for (JSONPacket packet : packetList) {

			json::JSONObject jsonObject(packet.data);
			int msgId = jsonObject.getInt("msgId");

			switch (msgId) {

			case 1:
				newTextureIndex = jsonObject.getInt("newTextureIndex");
				break;

			case 2:
				newPageNum = jsonObject.getInt("newPageNum");
				break;

			case 3:
				newZoomFactor = jsonObject.getInt("newZoomFactor");
				break;
			}

		}

		boost::this_thread::sleep(boost::posix_time::milliseconds(100));
	}
}

int main(int argc, char **argv) {

	boost::thread t1(&processMessages);

	receivedDatagramThread.start();

	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB);
	glutInitWindowSize(width, height);
	glutCreateWindow("Ten-Square Surface Viewer");
	init();
	glutDisplayFunc(display);
	glutIdleFunc(idleFunction);
	glutReshapeFunc(reshape);
	glutKeyboardFunc(keyboard);
	glutMouseFunc(mouseCB);
	glutMainLoop();
	return 0;
}
