/**
 * Frame Interaction.
 * by Jean Pierre Charalambos.
 * 
 * This example illustrates the three interactive frame mechanisms built-in proscene:
 * Camera, InteractiveFrame and MouseGrabber.
 * 
 * Press 'i' (which is a shortcut defined below) to switch the interaction between the
 * camera frame and the interactive frame. You can also manipulate the interactive
 * frame by picking the blue box passing the mouse next to its axis origin.
 * 
 * Press 'h' to display the global shortcuts in the console.
 * Press 'H' to display the current camera profile keyboard shortcuts
 * and mouse bindings in the console.
 */

//import java.util.Timer;
//import java.util.TimerTask;
import remixlab.remixcam.util.Timer;

import remixlab.proscene.*;
import remixlab.remixcam.core.*;
import remixlab.remixcam.geom.*;
import remixlab.remixcam.devices.Actions.KeyboardAction;

Scene scene;

void setup() {  
  size(640, 360, P3D);
  scene = new Scene(this);
  // A Scene has a single InteractiveFrame (null by default). We set it here.
  scene.setInteractiveFrame(new InteractiveFrame(scene.mouseGrabberPoolObject()));
  //scene.interactiveFrame().translate(new PVector(30, 30, 0));
  scene.interactiveFrame().translate(new Vector3D(30, 30, 0));
  // press 'i' to switch the interaction between the camera frame and the interactive frame
  scene.setShortcut('i', KeyboardAction.FOCUS_INTERACTIVE_FRAME);
  // press 'f' to display frame selection hints
  scene.setShortcut('f', KeyboardAction.DRAW_FRAME_SELECTION_HINT);
}

void draw() {
  //Proscene sets the background to black by default. If you need to change
  //it, don't call background() directly but use scene.background() instead.
  fill(204, 102, 0);
  box(20, 20, 40);
  // Save the current model view matrix
  pushMatrix();
  // Multiply matrix to get in the frame coordinate system.
  // applyMatrix(scene.interactiveFrame().matrix()) is handy but inefficient
  TempUtils.applyTransformation(scene.interactiveFrame(), this); //optimum
  // Draw an axis using the Scene static function
  scene.drawAxis(20);
  // Draw a second box attached to the interactive frame
  if (scene.interactiveFrame().grabsMouse()) {
    fill(255, 0, 0);
    box(12, 17, 22);
  }
  else if (scene.interactiveFrameIsDrawn()) {
    fill(0, 255, 255);
    box(12, 17, 22);
  }
  else {
    fill(0, 0, 255);
    box(10, 15, 20);
  }		
  popMatrix();
}

void keyPressed() {
  if ( key == 'u') {
    /**
    Timer timer=new Timer();
    TimerTask timerTask = new TimerTask() {
      public void run() {
        println("Nifty, eh?");
      }
    };
    //timer.schedule(timerTask, 1000);
    timer.scheduleAtFixedRate(timerTask, 0, 1000);
    */

    // /**     
    Timer timer = new Timer() {      
      public void run() {
        println("Nifty, eh?");
      }
    };
    // Schedule the timer to run once in 5 seconds.
    timer.schedule(5000);
    // */
  }
}