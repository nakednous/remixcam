package remixlab.proscene;

import processing.core.*;
import remixlab.remixcam.core.InteractiveFrame;

public class TempUtils {
	/**
	 * Convenience wrapper function that simply calls {@code applyTransformation( (PGraphics3D) p.g )}.
	 * 
	 * @see #applyTransformation(PGraphics3D)
	 */
	public static void applyTransformation(InteractiveFrame iFrame, PApplet p) {
		applyTransformation( iFrame, (PGraphics3D) p.g );
	}
	
	/**
	 * Apply the transformation defined by this Frame to {@code p3d}. The Frame is
	 * first translated and then rotated around the new translated origin.
	 * <p>
	 * Same as:
	 * <p>
	 * {@code p3d.translate(translation().x, translation().y, translation().z);} <br>
	 * {@code p3d.rotate(rotation().angle(), rotation().axis().x,
	 * rotation().axis().y, rotation().axis().z);} <br>
	 * <p>
	 * This method should be used in conjunction with PApplet to modify the
	 * processing modelview matrix from a Frame hierarchy. For example, with this
	 * Frame hierarchy:
	 * <p>
	 * {@code Frame body = new Frame();} <br>
	 * {@code Frame leftArm = new Frame();} <br>
	 * {@code Frame rightArm = new Frame();} <br>
	 * {@code leftArm.setReferenceFrame(body);} <br>
	 * {@code rightArm.setReferenceFrame(body);} <br>
	 * <p>
	 * The associated processing drawing code should look like:
	 * <p>
	 * {@code p3d.pushMatrix();//p is the PApplet instance} <br>
	 * {@code body.applyTransformation(p);} <br>
	 * {@code drawBody();} <br>
	 * {@code p3d.pushMatrix();} <br>
	 * {@code leftArm.applyTransformation(p);} <br>
	 * {@code drawArm();} <br>
	 * {@code p3d.popMatrix();} <br>
	 * {@code p3d.pushMatrix();} <br>
	 * {@code rightArm.applyTransformation(p);} <br>
	 * {@code drawArm();} <br>
	 * {@code p3d.popMatrix();} <br>
	 * {@code p3d.popMatrix();} <br>
	 * <p>
	 * Note the use of nested {@code pushMatrix()} and {@code popMatrix()} blocks
	 * to represent the frame hierarchy: {@code leftArm} and {@code rightArm} are
	 * both correctly drawn with respect to the {@code body} coordinate system.
	 * <p>
	 * <b>Attention:</b> When drawing a frame hierarchy as above, this method
	 * should be used whenever possible (one can also use {@link #matrix()}
	 * instead).
	 * 
	 * @see #matrix()
	 */
	public static void applyTransformation(InteractiveFrame iFrame, PGraphics3D p3d) {
		p3d.translate( iFrame.translation().x, iFrame.translation().y, iFrame.translation().z );
		p3d.rotate( iFrame.rotation().angle(), iFrame.rotation().axis().x, iFrame.rotation().axis().y, iFrame.rotation().axis().z);
	}

	/**
	 * Convenience function that simply calls {@code applyTransformation(
	 * scene.pg3d)}
	 * 
	 * @see #applyTransformation(PApplet)
	 */
	public static void applyTransformation(InteractiveFrame iFrame, Scene scene) {
		applyTransformation(iFrame, scene.pg3d);
	}
}
