public class AuxiliarViewer extends NApplet {
  Scene scene;
  
  void setup() {
    size(640, 360, P3D);
    scene = new Scene(this);
    scene.camera().setType(Camera.Type.ORTHOGRAPHIC);
    scene.setAxisIsDrawn(false);
    scene.setGridIsDrawn(false);
    scene.setRadius(200);
    scene.showAll();
    // register the drawing method which was defined externally
    scene.addDrawHandler(parentPApplet, "auxiliarDrawing");
    // hack to make middle and right buttons work under napplet
    // see issue report: http://forum.processing.org/#Topic/25080000000041027
    CameraProfile [] camProfiles = scene.getCameraProfiles();
    for (int i=0; i<camProfiles.length; i++) {      
      camProfiles[i].setCameraMouseBinding( (Scene.Button.MIDDLE.ID | Scene.Modifier.ALT.ID), Scene.MouseAction.ZOOM );
      camProfiles[i].setCameraMouseBinding( (Scene.Button.RIGHT.ID | Scene.Modifier.META.ID), Scene.MouseAction.TRANSLATE );
      camProfiles[i].setFrameMouseBinding( (Scene.Button.MIDDLE.ID | Scene.Modifier.ALT.ID), Scene.MouseAction.ZOOM );
      camProfiles[i].setFrameMouseBinding( (Scene.Button.RIGHT.ID | Scene.Modifier.META.ID), Scene.MouseAction.TRANSLATE );      
    }
  }
}