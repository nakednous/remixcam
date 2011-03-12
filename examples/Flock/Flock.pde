/**
 * Flock
 * by Matt Wetmore. Adapted to proscene by Jean Pierre Charalambos. 
 * 
 * A more complex example which interactively enables the selection of a frame
 * "avatar" for the camera to follow.
 * 
 * This example displays the famous artificial life program "Boids", developed by
 * Craig Reynolds in 1986.
 *
 * When an animation is activated (scene.startAnimation()), the
 * scene.animatedFrameWasTriggered boolean variable is updated each frame of your
 * drawing loop by proscene according to scene.animationPeriod().
 * 
 * You can tune the frequency of your animation (default is 60Hz) using
 * setAnimationPeriod(). The frame rate will then be fixed, provided that
 * your animation loop function is fast enough.
 * 
 * Boids under the mouse will be colored blue. If you click on a boid it will be
 * selected as the avatar, useful for the THIRD_PERSON proscene camera mode.
 * 
 * Click the space bar to switch between the different camera modes: ARCBALL,
 * WALKTHROUGH, and THIRD_PERSON.
 *
 * Press 'm' to toggle (start/stop) animation.
 * Press 'x' to decrease the animation period (animation speeds up).
 * Press 'y' to increase the animation period (animation speeds down).
 * Press 'u' to toggle smoothing.
 * Press 'v' to toggle boids' wall skipping.
 * Press 'f' to toggle the drawing of the frame selection hits.
 * Press 'h' to display the global shortcuts in the console.
 * Press 'H' to display the current camera profile keyboard shortcuts
 * and mouse bindings in the console.
 */

import remixlab.proscene.*;

Scene scene;
CameraProfile thirdPersonCP, currentCP;
//flock bounding box
int flockWidth = 1280;
int flockHeight = 720;
int flockDepth = 600;
int initBoidNum = 300; // amount of boids to start the program with
ArrayList flock;
boolean smoothEdges = false;
boolean avoidWalls = true;
float hue = 255;

void setup() {
  size(640, 360, P3D);
  scene = new Scene(this);
  thirdPersonCP =  new CameraProfile(scene, "THIRD_PERSON", CameraProfile.Mode.THIRD_PERSON );
  scene.registerCameraProfile( thirdPersonCP );
  currentCP = scene.currentCameraProfile();
  scene.setAxisIsDrawn(false);
  scene.setGridIsDrawn(false);
  scene.setBoundingBox(new PVector(0,0,0), new PVector(flockWidth,flockHeight,flockDepth));
  scene.showAll();
  // create and fill the list of boids
  flock = new ArrayList();
  for (int i = 0; i < initBoidNum; i++)
    flock.add(new Boid(new PVector(flockWidth/2, flockHeight/2, flockDepth/2 )));

  // press 'f' to display frame selection hints
  scene.setShortcut('f', Scene.KeyboardAction.DRAW_FRAME_SELECTION_HINT);
  // press 'm' to start/stop animation
  scene.setShortcut('m', Scene.KeyboardAction.ANIMATION);  

  scene.startAnimation();
}

void draw() {
  //Proscene sets the background to black by default. If you need to change
  //it, don't call background() directly but use scene.background() instead
  //(which can be called in the setup or here at the beginning of your drawing).  
  ambientLight(128,128,128);
  directionalLight(255, 255, 255, 0, 1, -100);
  noFill();
  stroke(255);

  line(0, 0, 0, 0, flockHeight, 0);
  line(0, 0, flockDepth, 0, flockHeight, flockDepth);
  line(0, 0, 0, flockWidth, 0, 0);
  line(0, 0, flockDepth, flockWidth, 0, flockDepth);

  line(flockWidth, 0, 0, flockWidth, flockHeight, 0);
  line(flockWidth, 0, flockDepth, flockWidth, flockHeight, flockDepth);
  line(0, flockHeight, 0, flockWidth, flockHeight, 0);
  line(0, flockHeight, flockDepth, flockWidth, flockHeight, flockDepth);

  line(0, 0, 0, 0, 0, flockDepth);
  line(0, flockHeight, 0, 0, flockHeight, flockDepth);
  line(flockWidth, 0, 0, flockWidth, 0, flockDepth);
  line(flockWidth, flockHeight, 0, flockWidth, flockHeight, flockDepth);

  if(!currentCP.equals(scene.currentCameraProfile())) { // cameraProfile changed
    adjustFrameRate();
    currentCP = scene.currentCameraProfile();
  }

  for (int i = 0; i < flock.size(); i++) {
    // create a temporary boid to process and make it the current boid in the list
    Boid tempBoid = (Boid) flock.get(i);
    if(scene.animatedFrameWasTriggered)
      tempBoid.run(flock); // tell the temporary boid to execute its run method
    tempBoid.render(); // tell the temporary boid to execute its render method
  }

  if (smoothEdges)
    smooth();
  else
    noSmooth();
}

void adjustFrameRate() {
  if(scene.currentCameraProfile().equals(thirdPersonCP))
    scene.setFrameRate(1000/scene.animationPeriod());//restarts animation
  else
    scene.setFrameRate(60);//restarts animation
}

void keyPressed() {
  switch (key) {
  case 'u':
    smoothEdges = !smoothEdges;
    break;
  case 'v':
    avoidWalls = !avoidWalls;
    break;
  case 'x':
    scene.setAnimationPeriod(scene.animationPeriod()-2, false);
    adjustFrameRate();
    break;
  case 'y':
    scene.setAnimationPeriod(scene.animationPeriod()+2, false);
    adjustFrameRate();
    break;
  }
}