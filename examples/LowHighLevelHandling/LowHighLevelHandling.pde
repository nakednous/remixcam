import remixlab.proscene.*;
import remixlab.proscene.Scene.ProsceneKeyboard;
import remixlab.proscene.Scene.ProsceneMouse;
import remixlab.tersehandling.core.*;
import remixlab.tersehandling.duoable.event.*;
import remixlab.dandelion.geom.*;
import remixlab.dandelion.core.*;
import remixlab.dandelion.core.Constants.*;

Scene scene;

boolean enforced = false;  
boolean grabsInput;

Constants.DOF_0Action keyAction;
Constants.DOF_2Action mouseAction;
GenericDOF2Event<Constants.DOF_2Action> prevEvent, event;
GenericDOF2Event<Constants.DOF_2Action> gEvent, prevGenEvent;
GenericKeyboardEvent<Constants.DOF_0Action> kEvent;

int count = 4;

ProsceneMouse mouse;
ProsceneKeyboard keyboard;

InteractiveFrame iFrame;

//MyIFrame iFrame;

@Override
public void setup() {
  size(640, 360, P3D);
  scene = new Scene(this);

  /**
   iFrame = new MyIFrame(scene);
   iFrame.translate(new DLVector(30, 30, 0));  
   // */

  //scene.setInteractiveFrame(new InteractiveFrame(scene));
  //scene.interactiveFrame().translate(new DLVector(30, 30, 0));
  iFrame = new InteractiveFrame(scene);
  iFrame.translate(new Vec(30, 30, 0));

  //switch timers to awt if not happy with P5 poorly frame rate (see my comment in draw)
  scene.switchTimers();

  mouseAction = Constants.DOF_2Action.ROTATE;
  //keyAction = Constants.DOF_0Action.NO_ACTION;
  //restore spinning at some point in the future
  //scene.interactiveFrame().setSpinningFriction(0);
  frameRate(100);
  //Testing some things out to test whether framework is ill behave or not ;)

  // /**
  if ( scene.prosceneKeyboard.keyboardProfile().isKeyInUse('f') )
    println("'f' key is in use");
  if ( scene.prosceneKeyboard.keyboardProfile().isKeyboardActionBound(DOF_0Action.DRAW_FRAME_SELECTION_HINT ) )
    println("DRAW_FRAME_SELECTION_HINT action is bound");    
  if ( scene.prosceneKeyboard.keyboardProfile().isKeyInUse('s') )
    println("'s' key is in use");
  if ( scene.prosceneKeyboard.keyboardProfile().isKeyInUse('S') )
    println("'S' key is in use");
  // */
}

@Override
public void draw() {
  background(0);

  fill(204, 102, 0);
  box(20, 30, 40);

  // /**
  pushMatrix();
  iFrame.applyTransformation();
  scene.drawAxis(20);

  // Draw a second box    
  if (iFrameGrabsInput()) {
    fill(255, 0, 0);
    box(12, 17, 22);
  } 
  else {
    fill(0, 0, 255);
    box(10, 15, 20);
  }

  popMatrix();
}

public boolean iFrameGrabsInput() {
  if (scene.terseHandler().isAgentRegistered("proscene_mouse"))
    return iFrame.grabsAgent(scene.prosceneMouse);
  else
    return grabsInput;
}

@Override
public void mouseMoved() {
  if (!scene.terseHandler().isAgentRegistered(scene.prosceneMouse)) {
    //if (!scene.isAgentRegistered("proscene_mouse")) {
    event = new GenericDOF2Event<Constants.DOF_2Action>(prevEvent, (float) mouseX, (float) mouseY);
    if (enforced)
      grabsInput = true;
    else
      grabsInput = iFrame.checkIfGrabsInput(event);    
    prevEvent = event.get();
  }
}

@Override
public void mouseDragged() {
  if (!scene.terseHandler().isAgentRegistered("proscene_mouse")) {
    event = new GenericDOF2Event<Constants.DOF_2Action>(prevEvent, (float) mouseX, (float) mouseY, mouseAction);
    if (grabsInput)
      scene.terseHandler().enqueueEventTuple(new EventGrabberTuple(event, iFrame));
    else
      scene.terseHandler().enqueueEventTuple(new EventGrabberTuple(event, scene.pinhole().frame()));
    prevEvent = event.get();
  }
}

@Override
public void keyPressed() {
  if (!scene.terseHandler().isAgentRegistered("proscene_keyboard")) {
    if (key == 'a' || key == 'g') {
      if (key == 'a')
        keyAction = Constants.DOF_0Action.DRAW_GRID;
      if (key == 'g')
        keyAction = Constants.DOF_0Action.DRAW_AXIS;
      kEvent = new GenericKeyboardEvent<Constants.DOF_0Action>(key, keyAction);
      scene.terseHandler().enqueueEventTuple(new EventGrabberTuple(kEvent, iFrame));
    }
  }
  if ( key == 'k' || key == 'K' ) {
    if (!scene.terseHandler().isAgentRegistered("proscene_keyboard")) {
      scene.terseHandler().registerAgent(keyboard);
      println("High level key event handling");
    }
    else {
      keyboard = (ProsceneKeyboard)scene.terseHandler().unregisterAgent("proscene_keyboard");
      println("low level key event handling");
    }
  }
  if (key == 'y') {
    enforced = !enforced;
    if (scene.terseHandler().isAgentRegistered(scene.prosceneMouse))
      if (enforced) {
        scene.prosceneMouse.setTrackedGrabber(iFrame);
        scene.prosceneMouse.disableTracking();
      }
      else {
        scene.prosceneMouse.setTrackedGrabber(null);
        scene.prosceneMouse.enableTracking();
      }
    else
      if (enforced)
        grabsInput = true;
      else
        grabsInput = false;
  }
  if ( key == 'm' || key == 'M' ) {
    if (!scene.terseHandler().isAgentRegistered("proscene_mouse")) {
      scene.terseHandler().registerAgent(mouse);
      println("High level mouse event handling");
    }
    else {
      mouse = (ProsceneMouse)scene.terseHandler().unregisterAgent("proscene_mouse");
      println("low level mouse event handling");
    }
  }    
  if (key == 'c')
    if (mouseAction == Constants.DOF_2Action.ROTATE)
      mouseAction = Constants.DOF_2Action.TRANSLATE;
    else
      mouseAction = Constants.DOF_2Action.ROTATE;
}

