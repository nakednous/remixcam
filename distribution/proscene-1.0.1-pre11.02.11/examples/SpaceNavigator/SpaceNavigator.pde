/**
 * Textured Sphere 
 * by Mike 'Flux' Chang (cleaned up by Aaron Koblin). 
 * Based on code by Toxi. 
 * Ported to GLGraphics by Andres Colubri.
 *
 * 3DConnexion SpaceNavigator support using procontroll
 * by Ralf Löhmer - rl@loehmer.de
 * Ported to proscene by Jean Pierre Charalambos
 * 
 * A 3D textured sphere with simple rotation control and
 * 3DConnexion SpaceNavigator support.
 *
 * This demo requires the GLGraphics (http://glgraphics.sourceforge.net/)
 * and procontroll (http://www.creativecomputing.cc/p5libs/procontroll/) libraries.
 *
 * Press 'i' (which is a shortcut defined below) to switch the
 * interaction between the camera frame and the interactive frame.
 * 
 * Press 'h' to display the global shortcuts in the console.
 * Press 'H' to display the current camera profile keyboard shortcuts
 * and mouse bindings in the console. 
 */

import processing.opengl.*;
import codeanticode.glgraphics.*;
import remixlab.proscene.*;
import procontroll.*;
import net.java.games.input.*;

Scene scene;
InteractiveCameraFrame cameraFrame;
boolean googleEarth;

ControllIO controll;
ControllDevice device; // my SpaceNavigator
ControllSlider sliderXpos; // Positions
ControllSlider sliderYpos;
ControllSlider sliderZpos;
ControllSlider sliderXrot; // Rotations
ControllSlider sliderYrot;
ControllSlider sliderZrot;
ControllButton button1; // Buttons
ControllButton button2;

ArrayList vertices;
ArrayList texCoords;
ArrayList normals;

int globeDetail = 70;                 // Sphere detail setting.
float globeRadius = 450;              // Sphere radius.
String globeMapName = "world32k.jpg"; // Image of the earth.

GLModel earth;
GLTexture tex;

float distance = 30000; // Distance of camera from origin.
float sensitivity = 1.0;

void setup() {
  size(1024, 768, GLConstants.GLGRAPHICS);
  
  scene = new Scene(this);
  scene.setRadius(globeRadius);
  scene.showAll();
  scene.setMouseTracking(false);
  scene.setGridIsDrawn(false);
  scene.setAxisIsDrawn(false);		
  scene.setInteractiveFrame(new InteractiveFrame(scene));
  scene.interactiveFrame().translate(new PVector(globeRadius/2, globeRadius/2, 0));

  // press 'f' to draw frame selection hint
  //scene.setShortcut('f', Scene.KeyboardAction.DRAW_FRAME_SELECTION_HINT);
  // press 'i' to switch the interaction between the camera frame and the interactive frame
  scene.setShortcut('i', Scene.KeyboardAction.FOCUS_INTERACTIVE_FRAME);
  //setGoogleEarthNavigationMode(false);
  cameraFrame = scene.camera().frame();  
  
  openSpaceNavigator();

  // This function calculates the vertices, texture coordinates and normals for the earth model.
  calculateEarthCoords();

  earth = new GLModel(this, vertices.size(), TRIANGLE_STRIP, GLModel.STATIC);

  // Sets the coordinates.
  earth.updateVertices(vertices);

  // Sets the texture map.
  tex = new GLTexture(this, globeMapName);
  earth.initTextures(1);
  earth.setTexture(0, tex);
  earth.updateTexCoords(0, texCoords);

  // Sets the normals.
  earth.initNormals();
  earth.updateNormals(normals);

  // Sets the colors of all the vertices to white.
  earth.initColors();
  earth.setColors(255);
}

void draw() {
  scene.background(0);

  GLGraphics renderer = (GLGraphics)g;
  renderer.beginGL();   
  renderer.model(earth);
  renderer.endGL();
  
    pushMatrix();
  scene.interactiveFrame().applyTransformation();//very efficient
  // Draw an axis using the Scene static function
  scene.drawAxis(20);
  // Draw a box associated with the iFrame
  stroke(122);
  if (scene.interactiveFrameIsDrawn()) {
    fill(0, 255, 255);
    box(50, 75, 60);
  }
  else {
    fill(0,0,255);
    box(50, 75, 60);
  }		
  popMatrix();
}

void keyPressed() {
  if ((key == 'u') || (key == 'U'))
    setGoogleEarthNavigationMode(!googleEarth);
}

void openSpaceNavigator() {
  println(System.getProperty("os.name"));
  controll = ControllIO.getInstance(this);  
  String os = System.getProperty("os.name").toLowerCase();  
  if(os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0)
    device = controll.getDevice("3Dconnexion SpaceNavigator");// magic name for linux    
  else
    device = controll.getDevice("SpaceNavigator");//magic name, for windows
  device.setTolerance(5.00f);
  sliderXpos = device.getSlider(2);
  sliderYpos = device.getSlider(1);
  sliderZpos = device.getSlider(0);
  sliderXrot = device.getSlider(5);
  sliderYrot = device.getSlider(4);
  sliderZrot = device.getSlider(3);
  button1 = device.getButton(0);
  button2 = device.getButton(1);
  sliderXpos.setMultiplier(0.01f); // sensitivities
  sliderYpos.setMultiplier(0.01f);
  sliderZpos.setMultiplier(0.01f);
  sliderXrot.setMultiplier(0.0001f);
  sliderYrot.setMultiplier(0.0001f);
  sliderZrot.setMultiplier(0.0001f);
}

void updateScene() {
  PVector trans = new PVector();
  Quaternion q = new Quaternion();  

  // 1.
  // We first check if there's any iFrame to be handled
  if( scene.interactiveFrameIsDrawn() ) {
    InteractiveFrame iFrame = scene.interactiveFrame();

    // 1.1. Translate the iFrame
    trans.set(sliderXpos.getValue(), sliderYpos.getValue(), -sliderZpos.getValue());      
    // Transform to world coordinate system
    //trans = scene.camera().frame().orientation().rotate(PVector.mult(trans, iFrame.translationSensitivity()));
    trans = cameraFrame.orientation().rotate(trans);
    // And then down to frame
    if (iFrame.referenceFrame() != null)
      trans = iFrame.referenceFrame().transformOf(trans);
    iFrame.translate(trans);

    // 1.2. Rotate the iFrame
    trans = scene.camera().projectedCoordinatesOf(iFrame.position());
    q.fromEulerAngles(sliderXrot.getValue(), sliderYrot.getValue(), -sliderZrot.getValue()); 
    trans.set(-q.x, -q.y, -q.z);
    trans = cameraFrame.orientation().rotate(trans);
    trans = iFrame.transformOf(trans);
    q.x = trans.x;
    q.y = trans.y;
    q.z = trans.z;
    iFrame.rotate(q);
  }

  // 2. Otherwise translate/rotate the camera:
  else {
    if(!googleEarth) { //first person emulation
      // Translate
      trans.set(sliderXpos.getValue(), sliderYpos.getValue(), -sliderZpos.getValue());
      cameraFrame.translate(scene.camera().frame().localInverseTransformOf(trans));
      // Rotate
      q.fromEulerAngles(-sliderXrot.getValue(), -sliderYrot.getValue(), sliderZrot.getValue());
      cameraFrame.rotate(q);
    }
    else { // Google earth navigation emulation      
      trans = PVector.mult(cameraFrame.position(), -sliderZpos.getValue());    
      cameraFrame.translate(trans);

      q.fromEulerAngles(-sliderYpos.getValue(), sliderXpos.getValue(), 0);
      cameraFrame.rotateAroundPoint(q, scene.camera().arcballReferencePoint());

      q.fromEulerAngles(0, 0, sliderZrot.getValue());
      cameraFrame.rotateAroundPoint(q, scene.camera().arcballReferencePoint());
      
      q.fromEulerAngles(-sliderXrot.getValue(), 0, 0);
      cameraFrame.rotate(q);
    }
  }
}

void setGoogleEarthNavigationMode(boolean gE) {
  googleEarth = gE;
  scene.camera().centerScene();
  if( googleEarth ) {
    scene.setDrawInteractiveFrame(false);
    scene.removeShortcut('i');      
    sliderXpos.setMultiplier(0.0001f);
    sliderYpos.setMultiplier(0.0001f);
    sliderZpos.setMultiplier(0.0001f);
    println("Google earth camera control emulation");
  }
  else {
    scene.setShortcut('i', Scene.KeyboardAction.FOCUS_INTERACTIVE_FRAME);      
    sliderXpos.setMultiplier(0.01f);
    sliderYpos.setMultiplier(0.01f);
    sliderZpos.setMultiplier(0.01f);
    println("First person camera control emulation");
  }
}
