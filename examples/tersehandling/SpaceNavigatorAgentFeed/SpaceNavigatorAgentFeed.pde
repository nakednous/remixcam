import remixlab.tersehandling.core.*;
import remixlab.tersehandling.generic.agent.*;
import remixlab.tersehandling.generic.event.*;
import remixlab.tersehandling.generic.profile.*;
import remixlab.tersehandling.event.*;

import procontroll.*;
import net.java.games.input.*;

public class HIDAgent extends GenericMotionAgent<GenericMotionProfile<SpaceAction>, GenericClickProfile<ClickAction>> implements EventConstants {
  public HIDAgent(TerseHandler h, String n) {
    super(new GenericMotionProfile<SpaceAction>(), new GenericClickProfile<ClickAction>(), h, n);
    //default bindings
    profile().setBinding(SpaceAction.CHANGE_POS_SHAPE);
    disableTracking();
    setSensitivities(0.01, 0.01, 0.01, 0.0001, 0.0001, 0.0001);
  }

  @Override
  public GenericDOF6Event<SpaceAction> feed() {
    return new GenericDOF6Event<SpaceAction>(sliderXpos.getValue(), sliderYpos.getValue(), sliderZpos.getValue(),
                                             sliderXrot.getValue(), sliderYrot.getValue(), sliderZrot.getValue(), 0, 0);
  }
}

public class MouseAgent extends GenericMotionAgent<GenericMotionProfile<MotionAction>, GenericClickProfile<ClickAction>> implements EventConstants {
  GenericDOF2Event<MotionAction> event, prevEvent;
  public MouseAgent(TerseHandler h, String n) {
    super(new GenericMotionProfile<MotionAction>(), new GenericClickProfile<ClickAction>(), h, n);
    //default bindings
    clickProfile().setClickBinding(TH_LEFT, 1, ClickAction.CHANGE_COLOR);
    clickProfile().setClickBinding(TH_META, TH_RIGHT, 1, ClickAction.CHANGE_STROKE_WEIGHT);
    clickProfile().setClickBinding((TH_META | TH_SHIFT), TH_RIGHT, 1, ClickAction.CHANGE_STROKE_WEIGHT);
    profile().setBinding(TH_LEFT, MotionAction.CHANGE_POSITION);
    profile().setBinding(TH_SHIFT, TH_LEFT, MotionAction.CHANGE_SHAPE);
    profile().setBinding(TH_META, TH_RIGHT, MotionAction.CHANGE_SHAPE);
  }
 
  public void mouseEvent(processing.event.MouseEvent e) {      
    if ( e.getAction() == processing.event.MouseEvent.MOVE ) {
      event = new GenericDOF2Event<MotionAction>(prevEvent, e.getX(), e.getY(), e.getModifiers(), e.getButton());
      updateGrabber(event);
      hidAgent.setDefaultGrabber(trackedGrabber());
      prevEvent = event.get();
    }
    if ( e.getAction() == processing.event.MouseEvent.DRAG ) {
      event = new GenericDOF2Event<MotionAction>(prevEvent, e.getX(), e.getY(), e.getModifiers(), e.getButton());
      handle(event);
      prevEvent = event.get();
    }
    if ( e.getAction() == processing.event.MouseEvent.CLICK ) {
      handle(new GenericClickEvent<ClickAction>(e.getX(), e.getY(), e.getModifiers(), e.getButton(), e.getCount()));
    }
  }
}

public class GrabbableCircle extends AbstractGrabber {
  public float radiusX, radiusY;
  public PVector center;
  public int colour;
  public int contourColour;
  public int sWeight;

  public GrabbableCircle(Agent agent) {
    agent.addInPool(this);
    setColor();
    setPosition();
    sWeight = 4;
    contourColour = color(0, 0, 0);
  }

  public GrabbableCircle(Agent agent, PVector c, float r) {
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
  public boolean checkIfGrabsInput(TerseEvent event) {
    if (event instanceof GenericDOF2Event) {
      float x = ((GenericDOF2Event<?>)event).getX();
      float y = ((GenericDOF2Event<?>)event).getY();
      return(pow((x - center.x), 2)/pow(radiusX, 2) + pow((y - center.y), 2)/pow(radiusY, 2) <= 1);
    }      
    return false;
  }

  @Override
  public void performInteraction(TerseEvent event) {
    if (event instanceof Duoable) {
      switch ((GlobalAction) ((Duoable<?>)event).action().referenceAction()) {
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
      case CHANGE_POSITION:
        setPosition( ((GenericDOF2Event<?>)event).getX(), ((GenericDOF2Event<?>)event).getY() );
        break;
      case CHANGE_SHAPE:
        radiusX += ((GenericDOF2Event<?>)event).getDX();
        radiusY += ((GenericDOF2Event<?>)event).getDY();
        break;
      case CHANGE_POS_SHAPE:
        radiusX += ((GenericDOF6Event<?>)event).getZ();
        radiusY += ((GenericDOF6Event<?>)event).getZ();
        center.x += ((GenericDOF6Event<?>)event).getX();
        center.y += ((GenericDOF6Event<?>)event).getY();
        break;
      }
    }
  }
}

int w = 600;
int h = 600;
MouseAgent agent;
HIDAgent hidAgent;
TerseHandler terseHandler;
GrabbableCircle [] circles;

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

void setup() {
  size(w, h);
  openSpaceNavigator();
  terseHandler = new TerseHandler();
  agent = new MouseAgent(terseHandler, "my_mouse");
  registerMethod("mouseEvent", agent);
  hidAgent = new HIDAgent(terseHandler, "SpaceNavigator");
  circles = new GrabbableCircle[50];
  for (int i = 0; i < circles.length; i++)
    circles[i] = new GrabbableCircle(agent);
}

void draw() {
  background(255);
  for (int i = 0; i < circles.length; i++) {
    if ( circles[i].grabsAgent(agent) )
      circles[i].draw(color(255, 0, 0));
    else
      circles[i].draw();
  }
  terseHandler.handle();
}

void openSpaceNavigator() {
  println(System.getProperty("os.name"));
  controll = ControllIO.getInstance(this);  
  String os = System.getProperty("os.name").toLowerCase();  
  if (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0)
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
}
