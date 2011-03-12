/**
 * Cajas Orientadas.
 * by Jean Pierre Charalambos.
 * 
 * This example illustrates some basic Frame properties, particularly how to orient them.
 * Select and move the sphere (holding the right mouse button pressed) to see how the
 * boxes will immediately be oriented towards it. You can also pick and move the boxes
 * and still they will be oriented towards the sphere.
 *
 * Press 'h' to display the global shortcuts in the console.
 * Press 'H' to display the current camera profile keyboard shortcuts
 * and mouse bindings in the console.
 */

import remixlab.proscene.*;

Scene scene;
Box [] cajas;
Sphere esfera;

void setup() {
  size(640, 360, P3D);
  scene = new Scene(this);
  // press 'f' to display frame selection hints
  scene.setShortcut('f', Scene.KeyboardAction.DRAW_FRAME_SELECTION_HINT);
  scene.setAxisIsDrawn(false);
  scene.setRadius(160);
  scene.showAll();
  
  esfera = new Sphere();
  esfera.setPosition(new PVector(0, 140, 0));
  esfera.setColor(color(0,0,255));
  
  // create an array of boxes with random positions, sizes and colors 
  cajas = new Box[30];
  for (int i = 0; i < cajas.length; i++)
    cajas[i] = new Box();
}

void draw() {
  //Proscene sets the background to black by default. If you need to change
  //it, don't call background() directly but use scene.background() instead.
  esfera.draw();
  for (int i = 0; i < cajas.length; i++) {
    // orient the boxes according to the sphere position
    cajas[i].setOrientation(esfera.getPosition());
    cajas[i].draw(true);
  }
}