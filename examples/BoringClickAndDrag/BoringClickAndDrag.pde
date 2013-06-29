import remixlab.tersehandling.core.*;
import remixlab.tersehandling.event.*;

int w = 800;
int h = 500;
public class MouseAgent extends AbstractAgent {
  GenericDOF2Event event;

  public MouseAgent(BasicScene scn, String n) {
    super(scn, n);
  }

  public void mouseEvent(processing.event.MouseEvent e) {
    event = new GenericDOF2Event(e.getX(), e.getY());
    if ( e.getAction() == processing.event.MouseEvent.MOVE )
      updateGrabber(event);
    if ( e.getAction() == processing.event.MouseEvent.DRAG )
      handle(event);
  }
}

public class GrabbableCircle extends AbstractGrabber {
  public float radius;
  public PVector center;
  public int colour;

  public GrabbableCircle(AbstractAgent agent) {
    agent.addInPool(this);
    setColor();
    setPosition();
  }

  public GrabbableCircle(AbstractAgent agent, PVector c, float r) {
    agent.addInPool(this);
    radius = r;
    center = c;    
    setColor();
  }

  public void setColor() {
    setColor(color(random(0, 255), random(0, 255), random(0, 255)));
  }

  public void setColor(int myC) {
    colour = myC;
  }

  public void setPosition(PVector p, float r) {
    center = p;
    radius = r;
  }

  public void setPosition() {
    float maxRadius = 50;
    float low = maxRadius;
    float highX = w - maxRadius;
    float highY = h - maxRadius;
    setPosition(new PVector(random(low, highX), random(low, highY)), random(20, maxRadius));
  }

  public void draw() {
    draw(colour);
  }

  public void draw(int c) {
    pushStyle();
    fill(c);
    ellipse(center.x, center.y, 2*radius, 2*radius);
    popStyle();
  }

  @Override
  public boolean checkIfGrabsInput(GenericEvent event) {
    if (event instanceof GenericDOF2Event) {
      float x = ((GenericDOF2Event)event).getX();
      float y = ((GenericDOF2Event)event).getY();
      return(pow((x - center.x), 2) + pow((y - center.y), 2) <= pow(radius, 2));
    }      
    return false;
  }

  @Override
  public void performInteraction(GenericEvent event) {
    setColor();
    setPosition();
  }
}

MouseAgent agent;
BasicScene scene;
GrabbableCircle [] circles;

void setup() {
  size(w, h);
  //scene = new SimpleScene();
  scene = new BasicScene();
  agent = new MouseAgent(scene, "my_mouse");
  registerMethod("mouseEvent", agent);
  circles = new GrabbableCircle[50];
  for (int i = 0; i < circles.length; i++)
    circles[i] = new GrabbableCircle(agent);
}

void draw() {
  background(0);
  for (int i = 0; i < circles.length; i++) {
    if ( circles[i].grabsAgent(agent) )
      circles[i].draw(color(255, 0, 0));
    else
      circles[i].draw();
  }
  scene.terseHandling();
}

