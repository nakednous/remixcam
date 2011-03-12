/**
 * Main Viewer. 
 * by Jean Pierre Charalambos.
 * 
 * This class is part of the View Frustum Culling example.
 * The octree is culled against this PApplet scene camera.
 *
 * Press 'h' to toggle the mouse and keyboard navigation help.
 */

public class MainViewer extends NApplet {
  Scene scene;
  
  void setup() {
    size(640, 360, P3D);
    scene = new Scene(this);
    // enable computation of the frustum planes equations (disabled by default)
    scene.enableFrustumEquationsUpdate();
    scene.setGridIsDrawn(false);
    scene.setAxisIsDrawn(false);
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
  
  // We need to pass the scene to the auxiliar viewer
  Scene getScene() {
    return scene;
  }
  
  void draw() {
    Root.drawIfAllChildrenAreVisible(this, scene.camera());
  }
}
