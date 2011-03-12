public class ClickButton extends Button2D {
  int path;

  public ClickButton(Scene scn, PVector p, int fontSize, int index) {
    this(scn, p, "", fontSize, index);
  }

  public ClickButton(Scene scn, PVector p, String t, int fontSize, int index) {
    super(scn, p, t, fontSize);
    path = index;
  }

  void mouseClicked(Button button, int numberOfClicks, Camera camera) {
    if(numberOfClicks == 1)
      if(path==0)
        scene.toggleCameraPathsAreDrawn();
      else
        scene.camera().playPath(path);
  }

  void display() {
    String text = new String();
    if(path == 0)
      if(scene.cameraPathsAreDrawn())
        text = "don't edit camera paths";
      else
        text = "edit camera paths";
    else {
      if(grabsMouse()) {
        if (scene.camera().keyFrameInterpolator(path).numberOfKeyFrames() > 1) 
          if (scene.camera().keyFrameInterpolator(path).interpolationIsStarted())
            text = "stop path ";
          else
            text = "play path ";        
        else
          text = "restore position ";
      }
      else {
        if (scene.camera().keyFrameInterpolator(path).numberOfKeyFrames() > 1)
          text = "path ";
        else
          text = "position ";
      }
      text += ((Integer)path).toString();
    }
    setText(text);
    super.display();
  }
}