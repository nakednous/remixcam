import remixlab.tersehandling.core.*;
import remixlab.tersehandling.duoable.agent.*;
import remixlab.tersehandling.duoable.profile.*;
import remixlab.tersehandling.event.*;
import remixlab.tersehandling.shortcut.*;

public class ClickProfile extends AbstractClickProfile<ClickAction> implements EventConstants {
  public ClickProfile() {
    super();
  }

  protected ClickProfile(ClickProfile other) {
    throw new RuntimeException("Processing is not supporting the following code which is perfectly fine in Java");
    /**
     bindings = new Bindings<ClickShortcut, ClickAction>();   
     for (Map.Entry<ClickShortcut, ClickAction> entry : other.bindings.map().entrySet()) {
     ClickShortcut key = entry.getKey().get();
     ClickAction value = entry.getValue();
     bindings.setBinding(key, value);
     }
     */
  }

  @Override
  public ClickProfile get() {
    return new ClickProfile(this);
  }
}

public class MotionProfile extends AbstractMotionProfile<MotionAction> implements EventConstants {
  public MotionProfile() {
    super();
  }

  protected MotionProfile(MotionProfile other) {
    throw new RuntimeException("Processing is not supporting the following code which is perfectly fine in Java");
    /**
     bindings = new Bindings<ButtonShortcut, MotionAction>();
     for (Map.Entry<ButtonShortcut, MotionAction> entry : other.bindings.map().entrySet()) {
     ButtonShortcut key = entry.getKey().get();
     MotionAction value = entry.getValue();
     bindings.setBinding(key, value);
     }
     */
  }

  @Override
  public MotionProfile get() {
    return new MotionProfile(this);
  }
}

public class DOF2Event extends GenericDOF2Event implements Duoble<MotionAction> {
  MotionAction action;

  public DOF2Event(DOF2Event prevEvent, float x, float y, int modifiers, int button) {
    super(prevEvent, x, y, modifiers, button);
  }

  public DOF2Event(float x, float y, int modifiers, int button) {
    super(x, y, modifiers, button);
  }

  protected DOF2Event(DOF2Event other) {
    super(other);
  }

  @Override
  public MotionAction getAction() {
    return action;
  }

  @Override
  public void setAction(Actionable<?> a) {
    if ( a instanceof MotionAction ) action = (MotionAction)a;
  }

  @Override
  public DOF2Event get() {
    return new DOF2Event(this);
  }
}

public class ClickEvent extends GenericClickEvent implements Duoble<ClickAction> {
  public ClickEvent(Integer modifiers, int b, int clicks) {
    super(modifiers, b, clicks);
  }

  ClickAction action;

  @Override
  public ClickAction getAction() {
    return action;
  }

  @Override
  public void setAction(Actionable<?> a) {
    if ( a instanceof ClickAction ) action = (ClickAction)a;
  }
}

public class MouseAgent extends AbstractMotionAgent implements EventConstants {
  DOF2Event event, prevEvent;
  public MouseAgent(BasicScene scn, String n) {
    super(scn, n);
    clickProfile = new ClickProfile();
    profile = new MotionProfile();
    //default bindings
    clickProfile().setClickBinding(TH_LEFT, 1, ClickAction.CHANGE_COLOR);
    clickProfile().setClickBinding(TH_RIGHT, 1, ClickAction.CHANGE_STROKE_WEIGHT);
    clickProfile().setClickBinding(TH_SHIFT, TH_RIGHT, 1, ClickAction.CHANGE_STROKE_WEIGHT);
    profile().setBinding(TH_LEFT, MotionAction.CHANGE_POSITION);
    profile().setBinding(TH_RIGHT, MotionAction.CHANGE_SHAPE);
  }

  public void mouseEvent(processing.event.MouseEvent e) {      
    if ( e.getAction() == processing.event.MouseEvent.MOVE ) {
      event = new DOF2Event(prevEvent, e.getX(), e.getY(), e.getModifiers(), e.getButton());
      updateGrabber(event);
      prevEvent = event.get();
    }
    if ( e.getAction() == processing.event.MouseEvent.DRAG ) {
      event = new DOF2Event(prevEvent, e.getX(), e.getY(), e.getModifiers(), e.getButton());
      handle(event);
      prevEvent = event.get();
    }
    if ( e.getAction() == processing.event.MouseEvent.CLICK ) {
      handle(new ClickEvent(e.getModifiers(), e.getButton(), e.getCount()));
    }
  }

  @Override
  public ClickProfile clickProfile() {
    return (ClickProfile)clickProfile;
  }

  @Override
  public MotionProfile profile() {
    return (MotionProfile)profile;
  }
}

public class GrabbableCircle extends AbstractGrabber {
  public float radiusX, radiusY;
  public PVector center;
  public int colour;
  public int contourColour;
  public int sWeight;

  public GrabbableCircle(AbstractAgent agent) {
    agent.addInPool(this);
    setColor();
    setPosition();
    sWeight = 4;
    contourColour = color(255, 255, 255);
  }

  public GrabbableCircle(AbstractAgent agent, PVector c, float r) {
    agent.addInPool(this);
    radiusX = r;
    radiusY = r;
    center = c;    
    setColor();
    sWeight = 4;
  }

  public void setColor() {
    setColor(color(random(0, 255), random(0, 255), random(0, 255)));
  }

  public void setColor(int myC) {
    colour = myC;
  }

  public void setPosition(float x, float y) {
    setPositionAndRadii(new PVector(x, y), radiusX, radiusY);
  }

  public void setPositionAndRadii(PVector p, float rx, float ry) {
    center = p;
    radiusX = rx;
    radiusY = ry;
  }

  public void setPosition() {
    float maxRadius = 50;
    float low = maxRadius;
    float highX = w - maxRadius;
    float highY = h - maxRadius;
    float r = random(20, maxRadius);
    setPositionAndRadii(new PVector(random(low, highX), random(low, highY)), r, r);
  }

  public void draw() {
    draw(colour);
  }

  public void draw(int c) {
    pushStyle();
    stroke(contourColour);
    strokeWeight(sWeight);
    fill(c);
    ellipse(center.x, center.y, 2*radiusX, 2*radiusY);
    popStyle();
  }

  @Override
  public boolean checkIfGrabsInput(GenericEvent event) {
    if (event instanceof GenericDOF2Event) {
      float x = ((GenericDOF2Event)event).getX();
      float y = ((GenericDOF2Event)event).getY();
      return(pow((x - center.x), 2)/pow(radiusX, 2) + pow((y - center.y), 2)/pow(radiusY, 2) <= 1);
    }      
    return false;
  }

  @Override
  public void performInteraction(GenericEvent event) {
    if ( event instanceof ClickEvent ) {
      switch (((ClickEvent) event).getAction()) {
        case CHANGE_COLOR:
        contourColour = color(random(100, 255), random(100, 255), random(100, 255));      
        break;
      case CHANGE_STROKE_WEIGHT:
        if (event.isShiftDown()) {          
          if (sWeight > 1)
            sWeight--;
        }
        else      
          sWeight++;    
        break;
      default:
        break;
      }
    }
    if ( event instanceof DOF2Event ) {
      switch (((DOF2Event) event).getAction()) {
      case CHANGE_POSITION:
        setPosition( ((DOF2Event) event).getX(), ((DOF2Event) event).getY() );
        break;
      case CHANGE_SHAPE:
        radiusX += ((DOF2Event) event).getDX();
        radiusY += ((DOF2Event) event).getDY();
        break;
      }
    }
  }
}

int w = 800;
int h = 500;

MouseAgent agent;
BasicScene scene;
GrabbableCircle [] circles;

public void setup() {
  size(w, h);
  scene = new BasicScene();
  agent = new MouseAgent(scene, "my_mouse");
  registerMethod("mouseEvent", agent);
  circles = new GrabbableCircle[50];
  for (int i = 0; i < circles.length; i++)
    circles[i] = new GrabbableCircle(agent);
}

public void draw() {
  background(0);
  for (int i = 0; i < circles.length; i++) {
    if ( circles[i].grabsAgent(agent) )
      circles[i].draw(color(255, 0, 0));
    else
      circles[i].draw();
  }
  scene.terseHandling();
}

