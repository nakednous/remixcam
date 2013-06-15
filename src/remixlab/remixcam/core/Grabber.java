package remixlab.remixcam.core;

import remixlab.remixcam.event.GenericEvent;

public class Grabber implements Grabbable {
	protected AbstractScene scene;
  protected boolean grabsCursor;
  public boolean keepsGrabbingCursor;
  
  /**
   * The constructor takes a scene instance and
   * {@link remixlab.proscene.Scene#addInMouseGrabberPool(MouseGrabbable)} this MouseGrabber object.
   * 
   * @param scn Scene instance
   */
	public Grabber(AbstractScene scn) {
		scene = scn;
    grabsCursor = false;
    keepsGrabbingCursor = false;
    scene.addInDeviceGrabberPool(this);     
	}

	@Override
	public void checkIfGrabsInput() {}

	@Override
	public boolean grabsInput() {
		return grabsCursor;
	}

	@Override
	public void setGrabsInput(boolean grabs) {
		grabsCursor = grabs;		
	}

	/**
   * Callback method called when the MouseGrabber {@link #grabsMouse()} and a
   * mouse button is pressed. Once a mouse grabber grabs the mouse and the mouse is
   * pressed the default implementation will return that the mouse grabber
   * keepsGrabbingMouse even if the mouse grabber loses focus.
   * <p>
   * The previous behavior is useful when you are planning to implement a mouse
   * pressed event followed by mouse released event, e.g., 
   * <p>
   * The body of your {@code mousePressed(Point pnt, Camera cam)} method should look like: <br>
   * {@code   super.mousePressed(pnt, cam); //sets the class variable keepsGrabbingMouse to true} <br>
   * {@code   myMousePressedImplementation;} <br>
   * {@code   ...} <br>
   * <p>
   * The body of your {@code mouseReleased(Point pnt, Camera cam)} method should look like: <br>
   * {@code   super.mouseReleased(pnt, cam); //sets the class variable keepsGrabbingMouse to false} <br>
   * {@code   myMouseReleasedImplementation;} <br>
   * {@code   ...} <br>
   * <p>
   * Finally, the body of your {@code checkIfGrabsMouse(int x, int y, Camera camera)} method should look like: <br>
   * {@code   setGrabsMouse( keepsGrabbingMouse   || myCheckCondition); //note the use of the class variable keepsGrabbingMouse} <br>
   * {@code   ...} <br>
   * 
   * @see #mouseReleased(Point, Camera)
   */
	@Override
	public void performInteraction(GenericEvent<?> motionEvent) {
		if (grabsInput()) keepsGrabbingCursor = true;		
	}

}
