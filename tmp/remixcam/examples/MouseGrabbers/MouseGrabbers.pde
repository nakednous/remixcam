/**
 * Mouse Grabbers.
 * by Jean Pierre Charalambos.
 * 
 * This example illustrates the picking mechanism built-in proscene,
 * which represents one of the three interactive mechanisms found in
 * proscene (camera and interactive frame, being the other two).
 * Once you select a box it will be highlighted and you can manipulate
 * it with the mouse. Try the different mouse buttons to see what happens.
 *
 * The displayed texts are interactive. Click on them to see what happens.
 * 
 * Press 'h' to display the global shortcuts in the console.
 * Press 'H' to display the current camera profile keyboard shortcuts
 * and mouse bindings in the console. 
 */

import remixlab.proscene.*;

Scene scene;
ArrayList boxes;
Button2D button1, button2;
int myColor;

void setup() {
  size(640, 360, P3D);
  scene = new Scene(this);
  scene.setShortcut('f', Scene.KeyboardAction.DRAW_FRAME_SELECTION_HINT);
  button1 = new ClickButton(scene, new PVector(10,10), "+", 32, true);
  button2 = new ClickButton(scene, new PVector((10 + button1.myWidth + 5), 10), "-", 32, false);
  scene.setGridIsDrawn(true);		
  scene.setCameraType(Camera.Type.ORTHOGRAPHIC);
  scene.setRadius(150);		
  scene.showAll();

  myColor = 125;
  boxes = new ArrayList();
  addBox();
}

void draw() {
  //Proscene sets the background to black by default. If you need to change
  //it, don't call background() directly but use scene.background() instead.
  button1.display();
  button2.display();	

  for (int i = 0; i < boxes.size(); i++) {
    Box box = (Box) boxes.get(i);
    box.draw(true);
  }
}

void addBox() {
  Box box = new Box();
  box.setSize(20, 20, 20);
  box.setColor(color(0,0,255));
  boxes.add(box);
}

void removeBox() {
  if(boxes.size()>0) {
    scene.removeFromMouseGrabberPool(((Box)boxes.get(0)).iFrame);
    boxes.remove(0);
  }
}