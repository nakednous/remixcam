import remixlab.tersehandling.generic.profile.*;

public enum SpaceAction implements Actionable<GlobalAction> {
  CHANGE_POS_SHAPE(GlobalAction.CHANGE_POS_SHAPE);

  @Override
  public GlobalAction referenceAction() {
    return act;
  }

  @Override
  public String description() {
    return "A simple motion action";
  }
  
  @Override
  public int dofs() {
    return 6;
  }

  GlobalAction act;

  SpaceAction(GlobalAction a) {
    act = a;
  }
}
