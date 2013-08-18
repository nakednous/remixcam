import remixlab.proscene.*;
import remixlab.proscene.Scene.ProsceneKeyboard;
import remixlab.proscene.Scene.ProsceneMouse;
import remixlab.tersehandling.core.*;
import remixlab.tersehandling.generic.event.*;
import remixlab.dandelion.geom.*;
import remixlab.dandelion.core.*;
import remixlab.dandelion.core.Constants.*;

Scene scene;

boolean enforced = false;	
boolean grabsInput;

Constants.KeyboardAction keyAction;
Constants.DOF2Action mouseAction;
GenericDOF2Event<Constants.DOF2Action> prevEvent, event;
GenericDOF2Event<Constants.DOF2Action> gEvent, prevGenEvent;
GenericKeyboardEvent<Constants.KeyboardAction> kEvent;

int count = 4;

ProsceneMouse mouse;
ProsceneKeyboard keyboard;

InteractiveFrame iFrame;

//MyIFrame iFrame;

@Override
public void setup() {
  size(640, 360, P3D);
  scene = new Scene(this);

  mouse = scene.defaultMouseAgent();
  keyboard = scene.defaultKeyboardAgent();

  //scene.setInteractiveFrame(new InteractiveFrame(scene));
  //scene.interactiveFrame().translate(new DLVector(30, 30, 0));
  iFrame = new InteractiveFrame(scene);

  iFrame.translate(new Vec(30, 30, 0));

  //switch timers to awt if not happy with P5 poorly frame rate (see my comment in draw)
  //scene.switchTimers();

  mouseAction = Constants.DOF2Action.ROTATE;
  //keyAction = Constants.DOF_0Action.NO_ACTION;
  //restore spinning at some point in the future
  //scene.interactiveFrame().setSpinningFriction(0);
  frameRate(100);
  //Testing some things out to test whether framework is ill behave or not ;)
  //using tersehandling here:

  // /**
  if ( scene.defaultKeyboardAgent().keyboardProfile().isKeyInUse('f') )
    println("'f' key is in use");
  if ( scene.defaultKeyboardAgent().keyboardProfile().isKeyboardActionBound(KeyboardAction.DRAW_FRAME_SELECTION_HINT ) )
    println("DRAW_FRAME_SELECTION_HINT action is bound");		
  if ( scene.defaultKeyboardAgent().keyboardProfile().isKeyInUse('s') )
    println("'s' key is in use");
  if ( scene.defaultKeyboardAgent().keyboardProfile().isKeyInUse('S') )
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
    return iFrame.grabsAgent(scene.defaultMouseAgent());
  else
    return grabsInput;
}

@Override
public void mouseMoved() {
  if (!scene.terseHandler().isAgentRegistered(scene.defaultMouseAgent())) {
    //if (!scene.isAgentRegistered("proscene_mouse")) {
    event = new GenericDOF2Event<Constants.DOF2Action>(prevEvent, (float) mouseX, (float) mouseY);
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
    event = new GenericDOF2Event<Constants.DOF2Action>(prevEvent, (float) mouseX, (float) mouseY, mouseAction);
    if (grabsInput)
      scene.terseHandler().enqueueEventTuple(new EventGrabberTuple(event, iFrame));
    else
      scene.terseHandler().enqueueEventTuple(new EventGrabberTuple(event, scene.viewport().frame()));
    prevEvent = event.get();
  }
}

@Override
public void keyPressed() {
  if ( key == 'x' || key == 'X' ) {
    println(" size: " + scene.terseHandler().globalGrabberList().size());
    for (Grabbable mg : scene.terseHandler().globalGrabberList()) {
      if (mg instanceof InteractiveFrame) {
        InteractiveFrame iF = (InteractiveFrame) mg;// downcast needed
        if (iF.isInCameraPath()) {
          println("frame is in cam path! win!!!");
        }
        else 
          println("frame is not in cam path!");
      }
      else {
        println("NOT INSTANCE !!??");
      }
    }
    //println("cam listeners: " + scene.pinhole().frame().listeners() + " number: " + scene.pinhole().frame().listeners().size());
  }

  if (!scene.terseHandler().isAgentRegistered("proscene_keyboard")) {
    if (key == 'a' || key == 'g') {
      if (key == 'a')
        keyAction = Constants.KeyboardAction.DRAW_GRID;
      if (key == 'g')
        keyAction = Constants.KeyboardAction.DRAW_AXIS;
      kEvent = new GenericKeyboardEvent<Constants.KeyboardAction>(key, keyAction);
      //scene.terseHandler().enqueueEventTuple(new EventGrabberTuple(kEvent, iFrame));
      scene.terseHandler().enqueueEventTuple(new EventGrabberTuple(kEvent, scene));
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
    if (scene.terseHandler().isAgentRegistered(scene.defaultMouseAgent()))
      if (enforced) {
        scene.defaultMouseAgent().setDefaultGrabber(iFrame);
        scene.defaultMouseAgent().disableTracking();
      }
      else {
        scene.defaultMouseAgent().setDefaultGrabber(scene.viewport().frame());
        scene.defaultMouseAgent().enableTracking();
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
    if (mouseAction == Constants.DOF2Action.ROTATE)
      mouseAction = Constants.DOF2Action.TRANSLATE;
    else
      mouseAction = Constants.DOF2Action.ROTATE;
}
