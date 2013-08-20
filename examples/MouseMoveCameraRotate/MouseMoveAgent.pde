public class MouseMoveAgent extends MouseAgent {
  GenericDOF2Event<Constants.DOF2Action> event, prevEvent;
  public MouseMoveAgent(AbstractScene scn, String n) {
    super(scn, n);
    // while camera rotation requires no mouse button press:
    cameraProfile().setBinding(DOF2Action.ROTATE); // -> MouseEvent.MOVE
    // camera translation requires a mouse left button press:
    cameraProfile().setBinding(TH_LEFT, DOF2Action.TRANSLATE); // -> MouseEvent.DRAG
    // Disable center and right button camera actions (inherited from MouseAgent):
    cameraProfile().setBinding(TH_RIGHT, null);
    cameraProfile().setBinding(TH_CENTER, null);
  }
  public void mouseEvent(processing.event.MouseEvent e) {
    //don't even necessary :P
    //if( e.getAction() == processing.event.MouseEvent.MOVE || e.getAction() == processing.event.MouseEvent.DRAG) {
    event = new GenericDOF2Event<Constants.DOF2Action>(prevEvent, e.getX() - scene.upperLeftCorner.getX(), e.getY() - scene.upperLeftCorner.getY(), e.getModifiers(), e.getButton());
    handle(event);
    prevEvent = event.get();
    //}
  }
}
