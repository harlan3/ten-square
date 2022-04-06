/*
 * Ten Square (OpenGL Decagon Prism Surface Viewer)
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

#include <GL/glut.h>
#include <stdio.h>
#include <stdlib.h>
#include <iostream>
#include <cmath>

#include "BMP_File_Loader.h"

void mouseCB(int button, int stat, int x, int y);

GLuint texture[1];
GLfloat xRot, yRot, zRot;
GLfloat xDist, yDist, zDist;
bool fullScreen;
int scaleFactor = 1.0;

void loadGLTextures() {

	const char *bmpFile[1] = { "ref_images/grid512.bmp" };

	for (int i = 0; i < 1; ++i) {

		BMP_File_Loader bmpFileLoader;
		unsigned char *bytes;
		unsigned int imageWidth, imageHeight;
		bmpFileLoader.LoadImage(bmpFile[i], bytes, imageWidth, imageHeight);

		// texture format based on # of bits per pixel
		GLint format = GL_RGB;
		GLint internalFormat = GL_RGB8;

		// copy the texture to OpenGL
		glGenTextures(1, &texture[i]);

		// set active texture and configure it
		glBindTexture(GL_TEXTURE_2D, texture[i]);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		// copy bitmap data to texture object
		glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, imageWidth, imageHeight,
				0, format, GL_UNSIGNED_BYTE, bytes);

		free(bytes);

		// unbind
		glBindTexture(GL_TEXTURE_2D, 0);

	}
}

GLfloat getZDistInc() {

	return 0.0987*scaleFactor + 0.1711;
}

GLfloat getZRotInc() {

	return -scaleFactor + 10.667;
}

int init() {

	glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
	glPolygonMode(GL_FRONT, GL_FILL);
	glPolygonMode(GL_BACK, GL_LINE);
	glCullFace(GL_BACK);
	glEnable(GL_CULL_FACE);
	glEnable(GL_TEXTURE_2D);
	loadGLTextures();

	xRot = -90;
	yRot = 0;
	zRot = 0;
	xDist = 0;
	yDist = 0;
	zDist = 0;
	scaleFactor = 1.0;

	return 1;
}

void display() {

	glClear(GL_COLOR_BUFFER_BIT);
	glLoadIdentity();

	glTranslatef(0.0f, 0.0f, -5.0f);
	glRotatef(xRot, 1.0f, 0.0f, 0.0f);
	glRotatef(yRot, 0.0f, 1.0f, 0.0f);
	glRotatef(zRot, 0.0f, 0.0f, 1.0f);
	glTranslatef(xDist, 0.0f, 0.0f);
	glTranslatef(0.0f, yDist, -0.0f);
	glTranslatef(0.0f, 0.0f, -zDist);

	glScalef(scaleFactor, scaleFactor, scaleFactor);

	glBindTexture(GL_TEXTURE_2D, texture[0]);
	glBegin(GL_QUADS);

	// Quad 1
	glTexCoord2d(0.0, 1.0);
	glVertex3f(-1.0, 0.0, -3.09);
	glTexCoord2d(0.1, 1.0);
	glVertex3f(-0.809017, 0.587785, -3.09);
	glTexCoord2d(0.1, 0.0);
	glVertex3f(-0.809017, 0.587785, 3.09);
	glTexCoord2d(0.0, 0.0);
	glVertex3f(-1, 0.0, 3.09);

	// Quad 2
	glTexCoord2d(0.1, 1.0);
	glVertex3f(-0.809017, 0.587785, -3.09);
	glTexCoord2d(0.2, 1.0);
	glVertex3f(-0.309017, 0.951057, -3.09);
	glTexCoord2d(0.2, 0.0);
	glVertex3f(-0.309017, 0.951057, 3.09);
	glTexCoord2d(0.1, 0.0);
	glVertex3f(-0.809017, 0.587785, 3.09);

	// Quad 3
	glTexCoord2d(0.2, 1.0);
	glVertex3f(-0.309017, 0.951057, -3.09);
	glTexCoord2d(0.3, 1.0);
	glVertex3f(0.309017, 0.951057, -3.09);
	glTexCoord2d(0.3, 0.0);
	glVertex3f(0.309017, 0.951057, 3.09);
	glTexCoord2d(0.2, 0.0);
	glVertex3f(-0.309017, 0.951057, 3.09);

	// Quad 4
	glTexCoord2d(0.3, 1.0);
	glVertex3f(0.309017, 0.951057, -3.09);
	glTexCoord2d(0.4, 1.0);
	glVertex3f(0.809017, 0.587785, -3.09);
	glTexCoord2d(0.4, 0.0);
	glVertex3f(0.809017, 0.587785, 3.09);
	glTexCoord2d(0.3, 0.0);
	glVertex3f(0.309017, 0.951057, 3.09);

	// Quad 5
	glTexCoord2d(0.4, 1.0);
	glVertex3f(0.809017, 0.587785, -3.09);
	glTexCoord2d(0.5, 1.0);
	glVertex3f(1.0, 0.0, -3.09);
	glTexCoord2d(0.5, 0.0);
	glVertex3f(1.0, 0.0, 3.09);
	glTexCoord2d(0.4, 0.0);
	glVertex3f(0.809017, 0.587785, 3.09);

	// Quad 6
	glTexCoord2d(0.5, 1.0);
	glVertex3f(1.0, 0.0, -3.09);
	glTexCoord2d(0.6, 1.0);
	glVertex3f(0.809017, -0.587785, -3.09);
	glTexCoord2d(0.6, 0.0);
	glVertex3f(0.809017, -0.587785, 3.09);
	glTexCoord2d(0.5, 0.0);
	glVertex3f(1.0, 0.0, 3.09);

	// Quad 7
	glTexCoord2d(0.6, 1.0);
	glVertex3f(0.809017, -0.587785, -3.09);
	glTexCoord2d(0.7, 1.0);
	glVertex3f(0.309017, -0.951057, -3.09);
	glTexCoord2d(0.7, 0.0);
	glVertex3f(0.309017, -0.951057, 3.09);
	glTexCoord2d(0.6, 0.0);
	glVertex3f(0.809017, -0.587785, 3.09);

	// Quad 8
	glTexCoord2d(0.7, 1.0);
	glVertex3f(0.309017, -0.951057, -3.09);
	glTexCoord2d(0.8, 1.0);
	glVertex3f(-0.309017, -0.951057, -3.09);
	glTexCoord2d(0.8, 0.0);
	glVertex3f(-0.309017, -0.951057, 3.09);
	glTexCoord2d(0.7, 0.0);
	glVertex3f(0.309017, -0.951057, 3.09);

	// Quad 9
	glTexCoord2d(0.8, 1.0);
	glVertex3f(-0.309017, -0.951057, -3.09);
	glTexCoord2d(0.9, 1.0);
	glVertex3f(-0.809017, -0.587785, -3.09);
	glTexCoord2d(0.9, 0.0);
	glVertex3f(-0.809017, -0.587785, 3.09);
	glTexCoord2d(0.8, 0.0);
	glVertex3f(-0.309017, -0.951057, 3.09);

	// Quad 10
	glTexCoord2d(0.9, 1.0);
	glVertex3f(-0.809017, -0.587785, -3.09);
	glTexCoord2d(1.0, 1.0);
	glVertex3f(-1.0, 0.0, -3.09);
	glTexCoord2d(1.0, 0.0);
	glVertex3f(-1.0, 0.0, 3.09);
	glTexCoord2d(0.9, 0.0);
	glVertex3f(-0.809017, -0.587785, 3.09);

	glEnd();

	glutSwapBuffers();
}

void reshape(int w, int h) {

	glViewport(0, 0, (GLsizei) w, (GLsizei) h);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(10.0f, (GLfloat) w / (GLfloat) h, .01, 100);
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
}

void keyboard(unsigned char key, int x, int y) {
	switch (key) {
	case '1':
		xRot = -90;
		yRot = 0;
		zRot = 0;
		xDist = 0;
		yDist = 0;
		zDist = 0;
		scaleFactor = 1.0;
		glutPostRedisplay();
		break;
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
	case 'S':
		scaleFactor -= 1.0f;
		glutPostRedisplay();
		break;
	case 's':
		scaleFactor += 1.0f;
		glutPostRedisplay();
		break;
	case 'p':
		cout << endl;
		cout << "xRot = " << xRot << endl;
		cout << "yRot = " << yRot << endl;
		cout << "zRot = " << zRot << endl;
		cout << "xDist = " << xDist << endl;
		cout << "yDist = " << yDist << endl;
		cout << "zDist = " << zDist << endl;
		cout << "scaleFactor = " << scaleFactor << endl;
		break;
	case 'q':
		exit(0);
	default:
		break;
	}
}

void mouseCB(int button, int state, int x, int y) {

	if (button == GLUT_LEFT_BUTTON) {
		if (state == GLUT_DOWN) {
			zRot += getZRotInc();
		}
	} else if (button == GLUT_RIGHT_BUTTON) {
		if (state == GLUT_DOWN) {
			zRot -= getZRotInc();
		}
	} else if (button == 3) { // Scroll wheel up
		if (state == GLUT_UP) {
			return;
		}
		zDist += getZDistInc();
	} else if (button == 4) { // Scroll wheel down
		if (state == GLUT_UP) {
			return;
		}
		zDist -= getZDistInc();
	}

	glutPostRedisplay();
}

int main(int argc, char **argv) {

	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB);
	glutInitWindowSize(800, 600);
	glutCreateWindow("Ten Square Surface Viewer");
	init();
	glutDisplayFunc(display);
	glutReshapeFunc(reshape);
	glutKeyboardFunc(keyboard);
	glutMouseFunc(mouseCB);
	glutMainLoop();
	return 0;
}
