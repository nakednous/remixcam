/**
 * Screen Drawing.
 * by Jean Pierre Charalambos.
 * 
 * This example illustrates how to combine 2D and 3D drawing.
 * 
 * All screen drawing should be enclosed between Scene.beginScreenDrawing() and
 * Scene.endScreenDrawing(). Then you can just begin drawing your screen shapes
 * (defined between beginShape() and endShape()). Just note that the (x,y) vertex
 * screen coordinates should be specified as:
 * vertex(Scene.xCoord(x), Scene.yCoord(y), Scene.zCoord()).
 * 
 * Press 'x' to toggle the screen drawing.
 * Press 'y' to clean your screen drawing.
 * Press 'h' to display the global shortcuts in the console.
 * Press 'H' to display the current camera profile keyboard shortcuts
 * and mouse bindings in the console.
 */

import remixlab.proscene.*;

Scene scene;
Box [] boxes;
ArrayList points;
	
void setup() {
  size(640, 360, P3D);
  scene = new Scene(this);
  // press 'f' to display frame selection hints
  scene.setShortcut('f', Scene.KeyboardAction.DRAW_FRAME_SELECTION_HINT);
  scene.setRadius(150);
  scene.showAll();
  
  boxes = new Box[50];
  for (int i = 0; i < boxes.length; i++)
    boxes[i] = new Box();
  
  points = new ArrayList();  // Create an empty ArrayList
}

void draw() {
  //Proscene sets the background to black by default. If you need to change
  //it, don't call background() directly but use scene.background() instead.
  // A. 3D drawing
  for (int i = 0; i < boxes.length; i++)
    boxes[i].draw();
    
  // B. 2D drawing
  // All screen drawing should be enclosed between Scene.beginScreenDrawing() and
  // Scene.endScreenDrawing(). Then you can just begin drawing your screen shapes
  // (defined between beginShape() and endShape()). Just note that the (x,y) vertex
  // screen coordinates should be specified as:
  // vertex(Scene.xCoord(x), Scene.yCoord(y), Scene.zCoord()).
  scene.beginScreenDrawing();
  pushStyle();
  strokeWeight(8);
  stroke(183,67,158,127);
  noFill();
  beginShape();
  for (int i = 0; i < points.size(); i++)
    vertex(scene.xCoord((float) ((Point) points.get(i)).x ),
           scene.yCoord((float) ((Point) points.get(i)).y ), scene.zCoord());
  endShape();
  popStyle();
  scene.endScreenDrawing();
}

void keyPressed() {
  if ((key == 'x') || (key == 'x'))
    scene.toggleMouseHandling();
  if ((key == 'y') || (key == 'Y'))
    points.clear();
}

void mouseDragged() {
  if(!scene.mouseIsHandled())
    points.add(new Point(mouseX, mouseY));
}
