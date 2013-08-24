import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import remixlab.tersehandling.core.AbstractGrabber;
import remixlab.tersehandling.core.Agent;
import remixlab.tersehandling.generic.event.GenericDOF2Event;
import remixlab.tersehandling.generic.profile.Duoable;
import remixlab.tersehandling.event.TerseEvent;

public class GrabbableCircle extends AbstractGrabber {
  public float radiusX, radiusY;
  public PVector center;
  public int colour;
  public int contourColour;
  public int sWeight;

  PApplet parent;
  PGraphics canvas;

  public GrabbableCircle(PApplet parent, PGraphics canvas, Agent agent) {
    agent.addInPool(this);
    sWeight = 4;
    this.parent = parent;
    this.canvas = canvas;
    contourColour = canvas.color(255, 255, 255);
    setColor();
    setPosition();
  }

  public GrabbableCircle(PApplet parent, PGraphics canvas, Agent agent, PVector c, float r) {
    agent.addInPool(this);
    radiusX = r;
    radiusY = r;
    this.parent = parent;
    this.canvas = canvas;
    center = c;
    setColor();
    sWeight = 4;
  }

  public void setColor() {
    setColor(canvas.color(parent.random(0, 255), parent.random(0, 255), parent.random(0, 255)));
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
    float highX = canvas.width - maxRadius;
    float highY = canvas.height - maxRadius;
    float r = parent.random(20, maxRadius);
    setPositionAndRadii(
    new PVector(parent.random(low, highX), parent.random(low, highY)), r, r);
  }

  public void draw(PGraphics canvas) {
    draw(colour, canvas);
  }

  public void draw(int c, PGraphics canvas) {
    canvas.pushStyle();
    canvas.stroke(contourColour);
    canvas.strokeWeight(sWeight);
    canvas.fill(c);
    canvas.ellipse(center.x, center.y, 2 * radiusX, 2 * radiusY);
    canvas.popStyle();
  }

  @Override
  public boolean checkIfGrabsInput(TerseEvent event) {
    if (event instanceof GenericDOF2Event) {
      float x = ((GenericDOF2Event<?>) event).getX();
      float y = ((GenericDOF2Event<?>) event).getY();
      return (PApplet.pow((x - center.x), 2) / PApplet.pow(radiusX, 2)
        + PApplet.pow((y - center.y), 2) / PApplet.pow(radiusY, 2) <= 1);
    }
    return false;
  }

  @Override
  public void performInteraction(TerseEvent event) {
    if (event instanceof Duoable<?>) {
      switch ((GlobalAction) ((Duoable<?>) event).action().referenceAction()) {
        case CHANGE_COLOR:
        contourColour = canvas.color(parent.random(100, 255), parent.random(100, 255), 
        parent.random(100, 255));
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
        setPosition(((GenericDOF2Event<?>) event).getX(), 
        ((GenericDOF2Event<?>) event).getY());
        break;
        case CHANGE_SHAPE:
        radiusX += ((GenericDOF2Event<?>) event).getDX();
        radiusY += ((GenericDOF2Event<?>) event).getDY();
        break;
      }
    }
  }
}

