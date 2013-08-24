import remixlab.tersehandling.core.*;
import java.util.Vector;

import TUIO.TuioCursor;
import TUIO.TuioObject;
import TUIO.TuioPoint;
import TUIO.TuioProcessing;
import TUIO.TuioTime;

int w = 600;
int h = 600;
MOUSEAgent mouseAgent;
TUIOAgent tuioAgent;
TerseHandler terseHandler;
GrabbableCircle [] circles;

TuioProcessing tuioClient;

void setup() {
  size(w, h);
  terseHandler = new TerseHandler();
  tuioAgent = new TUIOAgent(terseHandler, "my_tuio", g);
  mouseAgent = new MOUSEAgent(terseHandler, "my_mouse");
  registerMethod("mouseEvent", mouseAgent);
  circles = new GrabbableCircle[50];
  for (int i = 0; i < circles.length; i++) {
    circles[i] = new GrabbableCircle(this, g, mouseAgent);
    tuioAgent.addInPool(circles[i]);
  }
  tuioClient = new TuioProcessing(this, 3333);
}

void draw() {
  background(25,75,125);
  for (int i = 0; i < circles.length; i++) {
    if ( circles[i].grabsAgent(mouseAgent) )
      circles[i].draw(color(255, 0, 0), g);
    else
      circles[i].draw(g);
  }
  drawTuio();
  terseHandler.handle();
}

private void drawTuio() {
  float obj_size = 10;
  float cur_size = 5;

  Vector tuioObjectList = tuioClient.getTuioObjects();
  for (int i = 0; i < tuioObjectList.size(); i++) {
    TuioObject tobj = (TuioObject) tuioObjectList.elementAt(i);
    stroke(0);
    fill(0);
    pushMatrix();
    translate(tobj.getScreenX(width), tobj.getScreenY(height));
    rotate(tobj.getAngle());
    rect(-obj_size / 2, -obj_size / 2, obj_size, obj_size);
    popMatrix();
    fill(255);
    text("" + tobj.getSymbolID(), tobj.getScreenX(width), 
    tobj.getScreenY(height));
  }

  Vector tuioCursorList = tuioClient.getTuioCursors();
  for (int i = 0; i < tuioCursorList.size(); i++) {
    TuioCursor tcur = (TuioCursor) tuioCursorList.elementAt(i);
    Vector pointList = tcur.getPath();

    if (pointList.size() > 0) {
      stroke(0, 0, 255);
      TuioPoint start_point = (TuioPoint) pointList.firstElement();
      ;
      for (int j = 0; j < pointList.size(); j++) {
        TuioPoint end_point = (TuioPoint) pointList.elementAt(j);
        stroke(255, 0, 0, j * 20);
        line(start_point.getScreenX(width), 
        start_point.getScreenY(height), 
        end_point.getScreenX(width), 
        end_point.getScreenY(height));
        start_point = end_point;
      }

      stroke(192, 192, 192);
      fill(192, 192, 192);
      ellipse(tcur.getScreenX(width), tcur.getScreenY(height), 
      cur_size, cur_size);
      fill(0);
      text("" + tcur.getCursorID(), tcur.getScreenX(width) - 5, 
      tcur.getScreenY(height) + 5);
    }
  }
}

// these callback methods are called whenever a TUIO event occurs

// called when an object is added to the scene
public void addTuioObject(TuioObject tobj) {}

// called when an object is removed from the scene
public void removeTuioObject(TuioObject tobj) {}

// called when an object is moved
public void updateTuioObject(TuioObject tobj) {
  println("update object " + tobj.getSymbolID() + " ("
    + tobj.getSessionID() + ") " + tobj.getX() + " " + tobj.getY()
    + " " + tobj.getAngle() + " " + tobj.getMotionSpeed() + " "
    + tobj.getRotationSpeed() + " " + tobj.getMotionAccel() + " "
    + tobj.getRotationAccel());
}

// called when a cursor is added to the scene
public void addTuioCursor(TuioCursor tcur) {
  tuioAgent.addTuioCursor(tcur);
}

// called when a cursor is moved
public void updateTuioCursor(TuioCursor tcur) {
  tuioAgent.updateTuioCursor(tcur);
}

// called when a cursor is removed from the scene
public void removeTuioCursor(TuioCursor tcur) {
  tuioAgent.removeTuioCursor(tcur);
}

// called after each message bundle
// representing the end of an image frame
public void refresh(TuioTime bundleTime) {
  redraw();
}

