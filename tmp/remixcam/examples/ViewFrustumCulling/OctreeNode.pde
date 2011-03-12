/**
 * Octree Node. 
 * by Jean Pierre Charalambos.
 * 
 * This class is part of the View Frustum Culling example.
 * This class holds the octree hierarchy culled against the main viewer camera.
 *
 * Press 'h' to toggle the mouse and keyboard navigation help.
 */

public class OctreeNode {
  PVector p1, p2;
  OctreeNode child[];
  int level;
  
  OctreeNode(PVector P1, PVector P2) {
    p1 = P1;
    p2 = P2;
    child = new OctreeNode[8];
  }
  
  public void draw(PApplet parent) {
    parent.stroke(color(0.3f*level*255, 0.2f*255, (1.0f-0.3f*level)*255));
    parent.strokeWeight(level+1);
    
    parent.beginShape();
    parent.vertex(p1.x, p1.y, p1.z);
    parent.vertex(p1.x, p2.y, p1.z);
    parent.vertex(p2.x, p2.y, p1.z);
    parent.vertex(p2.x, p1.y, p1.z);
    parent.vertex(p1.x, p1.y, p1.z);
    parent.vertex(p1.x, p1.y, p2.z);
    parent.vertex(p1.x, p2.y, p2.z);
    parent.vertex(p2.x, p2.y, p2.z);
    parent.vertex(p2.x, p1.y, p2.z);
    parent.vertex(p1.x, p1.y, p2.z);
    parent.endShape();
    //parent.endShape(CLOSE);
    
    parent.beginShape(LINES);
    parent.vertex(p1.x, p2.y, p1.z);
    parent.vertex(p1.x, p2.y, p2.z);
    parent.vertex(p2.x, p2.y, p1.z);
    parent.vertex(p2.x, p2.y, p2.z);
    parent.vertex(p2.x, p1.y, p1.z);
    parent.vertex(p2.x, p1.y, p2.z);
    parent.endShape();
  }
  
  public void drawIfAllChildrenAreVisible(PApplet parent, Camera camera) {
    Camera.Visibility vis = camera.aaBoxIsVisible(p1, p2);
    if ( vis == Camera.Visibility.VISIBLE )
      draw(parent);
    else if ( vis == Camera.Visibility.SEMIVISIBLE )
      if (child[0]!=null)
        for (int i=0; i<8; ++i)
          child[i].drawIfAllChildrenAreVisible(parent, camera);
      else
        draw(parent);
  }
  
  public void buildBoxHierarchy(int l) {
    level = l;
    PVector middle = PVector.mult(PVector.add(p1, p2), 1/2.0f);
    for (int i=0; i<8; ++i) {
      // point in one of the 8 box corners
      PVector point = new PVector(((i&4)!=0)?p1.x:p2.x, ((i&2)!=0)?p1.y:p2.y, ((i&1)!=0)?p1.z:p2.z);
      if (level > 0) {
        child[i] = new OctreeNode(point, middle);
        child[i].buildBoxHierarchy(level-1);
      }
      else
        child[i] = null;
    }
  }	
}