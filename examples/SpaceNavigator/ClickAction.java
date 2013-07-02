import remixlab.tersehandling.duoable.profile.*;

public enum ClickAction implements Actionable<GlobalAction> {
  CHANGE_COLOR(GlobalAction.CHANGE_COLOR), 
  CHANGE_STROKE_WEIGHT(GlobalAction.CHANGE_STROKE_WEIGHT);

  @Override
  public GlobalAction referenceAction() {
    return act;
  }

  @Override
  public String description() {
    return "A simple click action";
  }

  @Override
    public boolean is2D() {
    return true;
  }

  @Override
  public int dofs() {
    return 0;
  }

  GlobalAction act;

  ClickAction(GlobalAction a) {
    act = a;
  }
}

