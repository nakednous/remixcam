public class ClickButton extends Button2D {
  int path;

  public ClickButton(Scene scn, PVector p, int index) {
    this(scn, p, "", index);
  }

  public ClickButton(Scene scn, PVector p, String t, int index) {
    super(scn, p, t, 16);
    path = index;
  }

  @Override
  public void performInteraction(TerseEvent event) {
    if (event instanceof ClickEvent)
      if (((ClickEvent) event).getClickCount() == 1)
        if (path == 0)
          scene.toggleViewportPathsAreDrawn();
        else
          scene.viewport().playPath(path);
  }

  public void display() {
    String text = new String();
    if (path == 0)
      if (scene.viewportPathsAreDrawn())
        text = "don't edit camera paths";
      else
        text = "edit camera paths";
    else {
      if (grabsAgent(scene.defaultMouseAgent())) {
        if (scene.viewport().keyFrameInterpolator(path)
            .numberOfKeyFrames() > 1)
          if (scene.viewport().keyFrameInterpolator(path)
              .interpolationIsStarted())
            text = "stop path ";
          else
            text = "play path ";
        else
          text = "restore position ";
      } else {
        if (scene.viewport().keyFrameInterpolator(path)
            .numberOfKeyFrames() > 1)
          text = "path ";
        else
          text = "position ";
      }
      text += ((Integer) path).toString();
    }
    setText(text);
    super.display();
  }
}
