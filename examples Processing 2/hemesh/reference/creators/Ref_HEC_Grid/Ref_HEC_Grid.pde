import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;


import processing.opengl.*;

HE_Mesh mesh;
WB_Render render;

void setup() {
  size(800, 800, OPENGL);
  smooth(8);

  // The length of the array is equal to the number of vertices in each direction.
  // the number of vertices = number of cells + 1   
  float[][] values=new float[11][6];
  for (int j = 0; j < 6; j++) {
    for (int i = 0; i < 11; i++) {
      values[i][j]=200*noise(0.35*i, 0.35*j);
    }
  }

  HEC_Grid creator=new HEC_Grid();
  creator.setU(10);// number of cells in U direction
  creator.setV(5);// number of cells in V direction
  creator.setUSize(500);// size of grid in U direction
  creator.setVSize(250);// size of grid in V direction
  creator.setWValues(values);// displacement of grid points (W value)
  // alternatively this can be left out (flat grid). values can also be double[][]
  // or and implementation of the WB_Function2D<Double> interface.
  mesh=new HE_Mesh(creator);

  render=new WB_Render(this);
}

void draw() {
  background(120);
  lights();
  translate(400, 400, 0);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noStroke();
  render.drawFaces(mesh);
  stroke(0);
  render.drawEdges(mesh);
}


