/**
 * View Frustum Culling.
 * by Jean Pierre Charalambos.
 * 
 * This example illustrates a basic view frustum culling implementation which is performed
 * by analytically solving the frustum plane equations.
 * 
 * A hierarchical octree structure is clipped against the camera's frustum clipping planes.
 * A second viewer displays an external view of the scene that exhibits the clipping
 * (using DrawingUtils.drawCamera() to display the frustum). 
 * 
 * This example requires the napplet library (http://github.com/acsmith/napplet)
 * (download it here: http://github.com/acsmith/napplet/downloads).
 * 
 * Press 'h' to display the global shortcuts in the console.
 * Press 'H' to display the current camera profile keyboard shortcuts
 * and mouse bindings in the console.
 */

import remixlab.proscene.*;
import napplet.*;

NAppletManager nappletManager;
NApplet mainNApplet;
OctreeNode Root;

void setup() {
  size(640, 720, P3D);
  // declare and build the octree hierarchy
  PVector p = new PVector(100, 70, 130);  
  Root = new OctreeNode(p, PVector.mult(p, -1.0f));
  Root.buildBoxHierarchy(4);
  // instantiate the viewers and embed them into a napplet manager
  nappletManager = new NAppletManager(this);
  mainNApplet = nappletManager.createNApplet("MainViewer", 0, 0);  
  nappletManager.createNApplet("AuxiliarViewer", 0, 360);
}

void draw() {  
  background(50);
}
