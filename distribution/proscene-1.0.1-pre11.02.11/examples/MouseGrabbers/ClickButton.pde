public class ClickButton extends Button2D {
  boolean addBox;

  public ClickButton(Scene scn, PVector p, String t, int fontSize, boolean addB) {
    super(scn, p, t, fontSize);
    addBox = addB;
  }

  void mouseClicked(Button button, int numberOfClicks, Camera camera) {
    if(numberOfClicks == 1) {
      if(addBox)
        addBox();
      else
        removeBox();
    }
  }
}