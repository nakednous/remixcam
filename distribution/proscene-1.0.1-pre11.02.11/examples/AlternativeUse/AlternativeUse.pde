/**
 * Alternative Use.
 * by Jean Pierre Charalambos.
 * 
 * This example illustrates how to using proscene through inheritance.
 * 
 * Press 'h' to display the global shortcuts in the console.
 * Press 'H' to display the current camera profile keyboard shortcuts
 * and mouse bindings in the console.
 */

import remixlab.proscene.*;

MyScene scene;

void setup() {
  size(640, 360, P3D);
  // We instantiate our MyScene class defined below
  scene = new MyScene(this);
}

// Make sure to define the draw() method, even if it's empty.
void draw() {
  //Proscene sets the background to black by default. If you need to change
  //it, don't call background() directly but use scene.background() instead.
}

class MyScene extends Scene {
  // We need to call super(p) to instantiate the base class
  public MyScene(PApplet p) {
    super(p);
  }

  // Initialization stuff could have also been performed at
  // setup(), once after the Scene object have been instantiated 
  public void init() {
    setGridIsDrawn(false);
  }

  //Define here what is actually going to be drawn.
  public void proscenium() {
    fill(204, 102, 0);
    box(20, 30, 50);
  }
}
