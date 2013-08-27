import remixlab.dandelion.geom.*;
import remixlab.dandelion.agent.KeyboardAgent;
import remixlab.dandelion.agent.MouseAgent;
import remixlab.dandelion.core.*;
import remixlab.dandelion.core.Constants.DOF2Action;
import remixlab.proscene.*;
import remixlab.tersehandling.generic.event.GenericDOF2Event;
import remixlab.tersehandling.generic.event.GenericKeyboardEvent;

Scene scene;
MouseMoveAgent agent;

public void setup() {
  size(640, 360, P3D);
  scene = new Scene(this);
  scene.enableFrustumEquationsUpdate();
  scene.setRadius(150);
  scene.showAll();
  agent = new MouseMoveAgent(scene, "MyMouseAgent");
  // agents creation registers it at the terseHandler.
  // we unregister it here, keeping the default mouse agent
  scene.terseHandler().unregisterAgent(agent);
}

public void draw() {	
  background(0);	
  noStroke();
  if ( scene.camera().sphereIsVisible(new Vec(0, 0, 0), 40) == Camera.Visibility.SEMIVISIBLE )
    fill(255, 0, 0);
  else
    fill(0, 255, 0);
  sphere(40);
}

public void keyPressed() {
  // We switch between the default mouse agent and the one we created:
  if ( key != ' ') return;
  if ( !scene.terseHandler().isAgentRegistered(agent) ) {
    scene.terseHandler().registerAgent(agent);
    scene.parent.registerMethod("mouseEvent", agent);
    scene.disableDefaultMouseAgent();
  }
  else {
    scene.terseHandler().unregisterAgent(agent);
    scene.parent.unregisterMethod("mouseEvent", agent);
    scene.enableDefaultMouseAgent();
  }
}
