/**
 * Animation Handler (originally SwarmingSprites by Andres Colubri)
 * Ported to proscene by Jean Pierre Charalambos.
 * 
 * Swarming points are animated with an animation function which is
 * registered at the scene using addAnimationHandler().
 *
 * When an animation is activated (scene.startAnimation()), your animate
 * function is called in an infinite loop which is synced with the drawing
 * loop by proscene according to scene.animationPeriod().
 * 
 * You can tune the frequency of your animation (default is 60Hz) using
 * setAnimationPeriod(). The frame rate will then be fixed, provided that
 * your animation loop function is fast enough.
 *
 * This example requires version 0.9.9 of the GLGraphics library:
 * http://glgraphics.sourceforge.net/
 *
 * Press 'm' to toggle (start/stop) animation.
 * Press 'x' to decrease the animation period (animation speeds up).
 * Press 'y' to increase the animation period (animation speeds down).
 * Press 'h' to display the global shortcuts in the console.
 * Press 'H' to display the current camera profile keyboard shortcuts
 * and mouse bindings in the console.
 */

import processing.opengl.*;
import codeanticode.glgraphics.*;
import remixlab.proscene.*;

Scene scene;
GLModel model;
GLTexture tex;
float[] coords;
float[] colors;

int numPoints = 10000;

void animateScene(Scene s) {
  for (int i = 0; i < numPoints; i++)
    for (int j = 0; j < 3; j++)
      coords[4 * i + j] += random(-0.5, 0.5);

  model.updateVertices(coords);
}

void setup() {
  size(640, 480, GLConstants.GLGRAPHICS);

  scene = new Scene(this); 
  scene.setAxisIsDrawn(false);
  scene.addAnimationHandler(this, "animateScene");  
  scene.setShortcut('m', Scene.KeyboardAction.ANIMATION);

  model = new GLModel(this, numPoints, GLModel.POINT_SPRITES, GLModel.DYNAMIC);
  model.initColors();
  tex = new GLTexture(this, "particle.png");    

  coords = new float[4 * numPoints];
  colors = new float[4 * numPoints];

  for (int i = 0; i < numPoints; i++) {
    for (int j = 0; j < 3; j++) coords[4 * i + j] = 100.0 * random(-1, 1);
    coords[4 * i + 3] = 1.0; // The W coordinate of each point must be 1.
    for (int j = 0; j < 3; j++) colors[4 * i + j] = random(0, 1);
    colors[4 * i + 3] = 0.9;
  }

  model.updateVertices(coords);
  model.updateColors(colors);

  scene.startAnimation();

   float pmax = model.getMaxPointSize();
   println("Maximum sprite size supported by the video card: " + pmax + " pixels.");   
   model.initTextures(1);
   model.setTexture(0, tex);  
   // Setting the maximum sprite to the 90% of the maximum point size.
   model.setMaxSpriteSize(0.9 * pmax);
   // Setting the distance attenuation function so that the sprite size
   // is 20 when the distance to the camera is 400.
   model.setSpriteSize(20, 400);
   model.setBlendMode(BLEND);
}

void draw() {    
  GLGraphics renderer = (GLGraphics)g;
  renderer.beginGL();  

  scene.background(0);

  // Disabling depth masking to properly render semitransparent
  // particles without need of depth-sorting them.    
  renderer.setDepthMask(false);
  model.render();
  renderer.setDepthMask(true);
    
  renderer.endGL();
}

void keyPressed() {
  if((key == 'x') || (key == 'X'))
    scene.setAnimationPeriod(scene.animationPeriod()-2);
  if((key == 'y') || (key == 'Y'))
    scene.setAnimationPeriod(scene.animationPeriod()+2);
}