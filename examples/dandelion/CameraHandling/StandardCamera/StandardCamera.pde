/**
 * Standard Camera.
 * by Jean Pierre Charalambos.
 * 
 * A 'standard' Camera with fixed near and far planes.
 * 
 * Proscene supports a STANDARD camera kind which may be need by some applications.
 * Its near and far planes distances are set to fixed values, instead of being fit
 * to scene dimensions as is done in the PROSCENE camera kind (default).
 * 
 * Note, however, that the precision of the z-Buffer highly depends on how the zNear()
 * and zFar() values are fitted to your scene (as it is done with the PROSCENE camera
 * kind). Loose boundaries will result in imprecision along the viewing direction.
 * 
 * Press 'v' in the main viewer (the upper one) to toggle the camera kind.  
 * Press 'u'/'U' in the main viewer to change the frustum size (when the
 * camera kind is "STANDARD"). 
 * Press 'h' to display the global shortcuts in the console.
 * Press 'H' to display the current camera profile keyboard shortcuts
 * and mouse bindings in the console.
 */

import remixlab.dandelion.core.Constants.KeyboardAction;
import remixlab.proscene.*;
import remixlab.dandelion.core.*;
import remixlab.dandelion.geom.*;

Scene scene, auxScene;
PGraphics canvas, auxCanvas;

public void setup() {
  size(640, 720, P3D);

  canvas = createGraphics(640, 360, P3D);
  scene = new Scene(this, canvas);

  scene.defaultKeyboardAgent().profile().setShortcut('v', KeyboardAction.CAMERA_KIND);
  // enable computation of the frustum planes equations (disabled by default)
  scene.enableFrustumEquationsUpdate();
  scene.setGridIsDrawn(false);
  scene.addDrawHandler(this, "mainDrawing");

  auxCanvas = createGraphics(640, 360, P3D);
  // Note that we pass the upper left corner coordinates where the scene
  // is to be drawn (see drawing code below) to its constructor.
  auxScene = new Scene(this, auxCanvas, 0, 360);
  auxScene.camera().setType(Camera.Type.ORTHOGRAPHIC);
  auxScene.setAxisIsDrawn(false);
  auxScene.setGridIsDrawn(false);
  auxScene.setRadius(200);
  auxScene.showAll();
  auxScene.addDrawHandler(this, "auxiliarDrawing");

  handleMouse();
}

public void mainDrawing(Scene s) {
  PGraphicsOpenGL p = s.pggl();
  p.background(0);
  p.noStroke();
  // the main viewer camera is used to cull the sphere object against its frustum
  switch (scene.camera().sphereIsVisible(new Vec(0, 0, 0), 40)) {
  case VISIBLE:
    p.fill(0, 255, 0);
    p.sphere(40);
    break;
  case SEMIVISIBLE:
    p.fill(255, 0, 0);
    p.sphere(40);
    break;
  case INVISIBLE:
    break;
  }
}

public void auxiliarDrawing(Scene s) {
  mainDrawing(s);		
  s.pg3d().pushStyle();
  s.pg3d().stroke(255, 255, 0);
  s.pg3d().fill(255, 255, 0, 160);
  s.drawCamera(scene.camera());
  s.pg3d().popStyle();
}

public void draw() {
  handleMouse();
  canvas.beginDraw();
  scene.beginDraw();
  scene.endDraw();
  canvas.endDraw();
  image(canvas, 0, 0);

  auxCanvas.beginDraw();
  auxScene.beginDraw();
  auxScene.endDraw();
  auxCanvas.endDraw();
  // We retrieve the scene upper left coordinates defined above.
  image(auxCanvas, auxScene.upperLeftCorner.x, auxScene.upperLeftCorner.y);
}

public void handleMouse() {
  if (mouseY < 360) {
    scene.enableDefaultMouseAgent();
    scene.enableDefaultKeyboardAgent();
    auxScene.disableDefaultMouseAgent();
    auxScene.disableDefaultKeyboardAgent();
  } 
  else {
    scene.disableDefaultMouseAgent();
    scene.disableDefaultKeyboardAgent();
    auxScene.enableDefaultMouseAgent();
    auxScene.enableDefaultKeyboardAgent();
  }
}
