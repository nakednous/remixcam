import remixlab.tersehandling.core.*;
import remixlab.tersehandling.event.*;

int w = 800;
int h = 500;

public class MouseAgent extends Agent {
  DOF2Event event;

  public MouseAgent(TerseHandler scn, String n) {
    super(scn, n);
  }

  public void mouseEvent(processing.event.MouseEvent e) {
    event = new DOF2Event(e.getX(), e.getY());
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

  public GrabbableCircle(Agent agent) {
    agent.addInPool(this);
    setColor();
    setPositionAndRadius();
  }

  public GrabbableCircle(Agent agent, PVector c, float r) {
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

  public void setPositionAndRadius(PVector p, float r) {
    center = p;
    radius = r;
  }

  public void setPositionAndRadius() {
    float maxRadius = 50;
    float low = maxRadius;
    float highX = w - maxRadius;
    float highY = h - maxRadius;
    setPositionAndRadius(new PVector(random(low, highX), random(low, highY)), random(20, maxRadius));
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
  public boolean checkIfGrabsInput(BaseEvent event) {
    if (event instanceof DOF2Event) {
      float x = ((DOF2Event)event).getX();
      float y = ((DOF2Event)event).getY();
      return(pow((x - center.x), 2) + pow((y - center.y), 2) <= pow(radius, 2));
    }      
    return false;
  }

  @Override
  public void performInteraction(BaseEvent event) {
    setColor();
    setPositionAndRadius();
  }
}

MouseAgent agent;
TerseHandler terseHandler;
GrabbableCircle [] circles;

void setup() {
  size(w, h);
  terseHandler = new TerseHandler();
  agent = new MouseAgent(terseHandler, "my_mouse");
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
  terseHandler.handle();
}