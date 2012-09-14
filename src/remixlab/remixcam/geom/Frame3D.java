/**
 *                     RemixCam (version 1.0.0)      
 *      Copyright (c) 2012 by National University of Colombia
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This java library provides classes to ease the creation of interactive 3D
 * scenes in various languages and frameworks such as JOGL, WebGL and Processing.
 * 
 * This source file is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 * 
 * A copy of the GNU General Public License is available on the World Wide Web
 * at <http://www.gnu.org/copyleft/gpl.html>. You can also obtain it by
 * writing to the Free Software Foundation, 51 Franklin Street, Suite 500
 * Boston, MA 02110-1335, USA.
 */

package remixlab.remixcam.geom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.remixcam.constraints.*;
import remixlab.remixcam.core.*;

/**
 * A Frame is a 3D coordinate system, represented by a {@link #position()} and
 * an {@link #orientation()}. The order of these transformations is important:
 * the Frame is first translated and then rotated around the new translated
 * origin.
 * <p>
 * In rare situations a frame can be {@link #linkTo(Frame3D)}, meaning that it
 * will share its {@link #translation()}, {@link #rotation()},
 * {@link #referenceFrame()}, and {@link #constraint()} with the other frame,
 * which can useful for some off-screen scenes.
 */
public class Frame3D implements Copyable {	
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).		
		append(krnl).
		append(list).
		append(linkedFramesList).
		append(srcFrame).
    toHashCode();		
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Frame3D other = (Frame3D) obj;
	  return new EqualsBuilder()
    .appendSuper(super.equals(obj))		
		.append(krnl, other.krnl)
		.append(list, other.list)
		.append(linkedFramesList, other.linkedFramesList)
		.append(srcFrame, other.srcFrame)
		.isEquals();
	}
	
	/**
	 * Internal class that holds the main frame attributes. This class is useful
	 * to linking frames (i.e., to share these attributes).
	 */
	public class FrameKernel implements Copyable {
		@Override
		public int hashCode() {
	    return new HashCodeBuilder(17, 37).		
			append(trans).
			append(rot).
			append(refFrame).
			append(constr).
	    toHashCode();		
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			FrameKernel other = (FrameKernel) obj;
		  return new EqualsBuilder()
	    .appendSuper(super.equals(obj))		
			.append(trans, other.trans)
			.append(rot, other.rot)
			.append(refFrame, other.refFrame)
			.append(constr, other.constr)
			.isEquals();
		}		

		// TODO move Frame.modified() here?
		protected Vector3D trans;
		protected Quaternion rot;
		protected Frame3D refFrame;
		protected Constraint constr;
		
		public FrameKernel() {
			trans = new Vector3D(0, 0, 0);
			rot = new Quaternion();
			refFrame = null;
			constr = null;
		}
		
		public FrameKernel(Vector3D p, Quaternion r) {
			trans = new Vector3D(p.vec[0], p.vec[1], p.vec[2]);
			rot = r.get();
			refFrame = null;
			constr = null;
		}
		
		protected FrameKernel(FrameKernel other) {
			trans = new Vector3D(other.translation().vec[0], other.translation().vec[1], other.translation().vec[2]);
			rot = other.rotation().get();
			refFrame = other.referenceFrame();
			constr = other.constraint();
		}
		
		public FrameKernel get() {
			return new FrameKernel(this);
		}		
		
		public final Vector3D translation() {
			return trans;
		}
		
		public final void setTranslation(Vector3D t) {
			trans = t;			
		}
		
		public final Quaternion rotation() {
			return rot;
		}
		
		public final void setRotation(Quaternion r) {
			rot = r;
		}
		
		public Constraint constraint() {
			return constr;
		}
		
		public final Frame3D referenceFrame() {
			return refFrame;
		}
		
		public final void setReferenceFrame(Frame3D rFrame) {
			refFrame = rFrame;
		}
		
		public void setConstraint(Constraint c) {
			constr = c;
		}		
	}	

	protected FrameKernel krnl;
	protected List<KeyFrameInterpolator> list;
	protected List<Frame3D> linkedFramesList;
	protected Frame3D srcFrame;

	/**
	 * Creates a default Frame.
	 * <p>
	 * Its {@link #position()} is (0,0,0) and it has an identity
	 * {@link #orientation()} Quaternion. The {@link #referenceFrame()} and the
	 * {@link #constraint()} are {@code null}.
	 */
	public Frame3D() {
		krnl = new FrameKernel();
		list = new ArrayList<KeyFrameInterpolator>();
		linkedFramesList = new ArrayList<Frame3D>();
		srcFrame = null;
	}

	/**
	 * Creates a Frame with a {@link #position()} and an {@link #orientation()}.
	 * <p>
	 * See the Vector3D and Quaternion documentations for convenient constructors
	 * and methods.
	 * <p>
	 * The Frame is defined in the world coordinate system (its
	 * {@link #referenceFrame()} is {@code null}). It has a {@code null}
	 * associated {@link #constraint()}.
	 */
	public Frame3D(Vector3D p, Quaternion r) {
		krnl = new FrameKernel(p, r);
		list = new ArrayList<KeyFrameInterpolator>();
		linkedFramesList = new ArrayList<Frame3D>();
		srcFrame = null;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param other
	 *          the Frame containing the object to be copied
	 */
	protected Frame3D(Frame3D other) {
		krnl = new FrameKernel( other.kernel() );
		list = new ArrayList<KeyFrameInterpolator>();
		Iterator<KeyFrameInterpolator> it = other.listeners().iterator();
		while (it.hasNext())
			list.add(it.next());
		linkedFramesList = new ArrayList<Frame3D>();
		Iterator<Frame3D> iterator = other.linkedFramesList.iterator();
		while (iterator.hasNext())
			linkedFramesList.add(iterator.next());
		srcFrame = other.srcFrame;
	}

	/**
	 * Calls {@code SimpleFrame(SimpleFrame)} (which is private) and returns a copy of
	 * {@code this} object.
	 * 
	 * @see #SimpleFrame(Frame3D)
	 */
	public Frame3D get() {
		return new Frame3D(this);
	}
	
	// TODO document me
	public FrameKernel kernel() {
		return krnl;
	}
	
	public void setKernel(FrameKernel k) {
		krnl = k;
	}

	/**
	 * Returns the Frame translation, defined with respect to the
	 * {@link #referenceFrame()}.
	 * <p>
	 * Use {@link #position()} to get the result in the world coordinates. These
	 * two values are identical when the {@link #referenceFrame()} is {@code null}
	 * (default).
	 * 
	 * @see #setTranslation(Vector3D)
	 * @see #setTranslationWithConstraint(Vector3D)
	 */
	public final Vector3D translation() {
		return kernel().translation();
	}

	/**
	 * Returns the Frame rotation, defined with respect to the
	 * {@link #referenceFrame()} (i.e, the current Quaternion orientation).
	 * <p>
	 * Use {@link #orientation()} to get the result in the world coordinates.
	 * These two values are identical when the {@link #referenceFrame()} is
	 * {@code null} (default).
	 * 
	 * @see #setRotation(Quaternion)
	 * @see #setRotationWithConstraint(Quaternion)
	 */
	public final Quaternion rotation() {
		return kernel().rotation();
	}

	/**
	 * Returns the reference Frame, in which coordinates system the Frame is
	 * defined.
	 * <p>
	 * The {@link #translation()} {@link #rotation()} of the Frame are defined
	 * with respect to the reference Frame coordinate system. A {@code null}
	 * reference Frame (default value) means that the Frame is defined in the
	 * world coordinate system.
	 * <p>
	 * Use {@link #position()} and {@link #orientation()} to recursively convert
	 * values along the reference Frame chain and to get values expressed in the
	 * world coordinate system. The values match when the reference Frame {@code
	 * null}.
	 * <p>
	 * Use {@link #setReferenceFrame(Frame3D)} to set this value and create a Frame
	 * hierarchy. Convenient functions allow you to convert 3D coordinates from
	 * one Frame to another: see {@link #coordinatesOf(Vector3D)},
	 * {@link #localCoordinatesOf(Vector3D)} ,
	 * {@link #coordinatesOfIn(Vector3D, Frame3D)} and their inverse functions.
	 * <p>
	 * Vectors can also be converted using {@link #transformOf(Vector3D)},
	 * {@link #transformOfIn(Vector3D, Frame3D)}, {@link #localTransformOf(Vector3D)}
	 * and their inverse functions.
	 */
	public final Frame3D referenceFrame() {
		return kernel().referenceFrame();
	}

	/**
	 * Returns the current constraint applied to the Frame.
	 * <p>
	 * A {@code null} value (default) means that no Constraint is used to filter
	 * the Frame translation and rotation.
	 * <p>
	 * See the Constraint class documentation for details.
	 */
	public Constraint constraint() {
		return kernel().constraint();
	}

	/**
	 * Returns the list of KeyFrameInterpolators that are currently listening to
	 * this frame. Normally, you should not call this method as the
	 * KeyFrameInterpolator takes care of calling it.
	 * 
	 * @see remixlab.remixcam.core.KeyFrameInterpolator#addKeyFrame(Frame3D, float,
	 *      boolean)
	 */
	public List<KeyFrameInterpolator> listeners() {
		return list;
	}

	/**
	 * Adds {@code kfi} to the list of KeyFrameInterpolators that are
	 * currently listening this frame.
	 */
	public void addListener(KeyFrameInterpolator kfi) {
		list.add(kfi);
	}

	/**
	 * Removes {@code kfi} from the list of KeyFrameInterpolators that are
	 * currently listening to this frame. Normally, you should not call this
	 * method, unless you have added {@code kfi} before (by calling
	 * {@link #addListener(KeyFrameInterpolator)}).
	 * 
	 * @see remixlab.remixcam.core.KeyFrameInterpolator#addKeyFrame(Frame3D, float,
	 *      boolean)
	 */
	public void removeListener(KeyFrameInterpolator kfi) {
		list.remove(kfi);
	}

	/**
	 * Resets the cache of all KeyFrameInterpolators' associated with this Frame.
	 */
	protected void modified() {
		Iterator<KeyFrameInterpolator> it = list.iterator();
		while (it.hasNext()) {
			it.next().invalidateValues();
		}
	}
	
	/**
	 * Links this frame (referred to as the requested frame) to {@code sourceFrame},
	 * meaning that this frame will take (and share by reference) the {@link #translation()},
	 * {@link #rotation()}, {@link #referenceFrame()}, and {@link #constraint()} from the
	 * {@code sourceFrame}. This can useful for some off-screen scenes, e.g., to link a
	 * frame defined in one scene to the camera frame defined in other scene
	 * (see the CameraCrane example).
	 * <p>
	 * <b>Note:</b> Linking frames has the following properties:
	 * <ol>
   * <li>A frame can be linked only to another frame (referred to as the source
   * frame).</li> 
   * <li>A source frame can be linked by from many (requested) frames.</li>
   * <li>A source frame can't be linked to another (source) frame, i.e., it
   * can only receive links form other frames.</li>
   * </ol>
	 * 
	 * @param sourceFrame the frame to link this frame with.
	 * @return true if this frame can successfully being linked to the frame. False otherwise.
	 * 
	 * @see #linkFrom(Frame3D)
	 * @see #unlink()
	 * @see #unlinkFrom(Frame3D)
	 * @see #isLinked()
	 * @see #areLinkedTogether(Frame3D)
	 */
	public boolean linkTo(Frame3D sourceFrame) {
		// avoid loops		
		if( (!linkedFramesList.isEmpty()) || sourceFrame.linkedFramesList.contains(this) || (sourceFrame == this) )
			return false;		
		
		if(sourceFrame.linkedFramesList.add(this)) {
			srcFrame = sourceFrame;
			setKernel(srcFrame.kernel());
			return true;
		}
		
		return false;
	}	
	
	/**
	 * Attempts to link the {@code requestedFrame} to this frame.
	 * <p>
	 * See {@link #linkTo(Frame3D)} for the rules and terminology applying to the linking process.
	 * 
	 * @param requestedFrame the frame that is requesting a link to this frame.
	 * @return true if the requested frame can successfully being linked to this frame. False otherwise.
	 * 
	 * @see #linkTo(Frame3D)
	 * @see #unlink()
	 * @see #unlinkFrom(Frame3D)
	 * @see #isLinked()
	 * @see #areLinkedTogether(Frame3D)
	 */
	public boolean linkFrom(Frame3D requestedFrame) {
	  // avoid loops		
		if( (!requestedFrame.linkedFramesList.isEmpty()) || linkedFramesList.contains(this) || (requestedFrame == this) )
			return false;		
		
		if(linkedFramesList.add(requestedFrame)) {
			requestedFrame.srcFrame = this;
			requestedFrame.setKernel(kernel());
			return true;
		}
		
		return false;
	}
	
	/**
	 * Unlinks this frame from its source frame. Does nothing if this frame is not
	 * linked to another frame.
	 * <p>
	 * See {@link #linkTo(Frame3D)} for the rules and terminology applying to the linking process.
	 * 
	 * @return true if succeeded otherwise returns false.
	 * 
	 * @see #linkTo(Frame3D)
	 * @see #linkFrom(Frame3D) 
	 * @see #unlinkFrom(Frame3D)
	 * @see #isLinked()
	 * @see #areLinkedTogether(Frame3D)
	 */
	public boolean unlink() {
		boolean result = false;
		if(srcFrame != null) {
			result = srcFrame.linkedFramesList.remove(this);
			if(result) {
				setKernel(new FrameKernel(srcFrame.translation(), srcFrame.rotation()));
				srcFrame = null;
			}
		}
		return result;
	}
	
	/**
	 * Unlinks the requested frame from this frame. Does nothing if the frames are
	 * not linked together ({@link #areLinkedTogether(Frame3D)}).
	 * <p>
	 * See {@link #linkTo(Frame3D)} for the rules and terminology applying to the linking process.
	 * 
	 * @return true if succeeded otherwise returns false.
	 * 
	 * @see #linkTo(Frame3D)
	 * @see #linkFrom(Frame3D)
	 * @see #unlink()
	 * @see #isLinked()
	 * @see #areLinkedTogether(Frame3D)
	 */
	public boolean unlinkFrom(Frame3D requestedFrame) {
		boolean result = false;
		if ( (srcFrame == null) && (requestedFrame != this) ) {
			result = linkedFramesList.remove(requestedFrame);
			if (result) {
				requestedFrame.setKernel(new FrameKernel(translation(), rotation()));
				requestedFrame.srcFrame = null;
			}
		}
		return result;
	}
	
	/**
	 * Returns true if this frame is linked to a source frame or if this frame
	 * acts as the source frame of other frames. Otherwise returns false.
	 * <p>
	 * See {@link #linkTo(Frame3D)} for the rules and terminology applying to the linking process.
	 * 
	 * @see #linkTo(Frame3D)
	 * @see #linkFrom(Frame3D)
	 * @see #unlink()
	 * @see #unlinkFrom(Frame3D)
	 * @see #areLinkedTogether(Frame3D)
	 */
	public boolean isLinked() {
		if ((srcFrame != null) || (!linkedFramesList.isEmpty()) )
			return true;
		return false;
	}
	
	/**
	 * Returns true if this frame is linked with {@code sourceFrame}. Otherwise
	 * returns false.
	 * <p>
	 * See {@link #linkTo(Frame3D)} for the rules and terminology applying to the linking process.
	 * 
	 * @see #linkTo(Frame3D)
	 * @see #linkFrom(Frame3D)
	 * @see #unlink()
	 * @see #unlinkFrom(Frame3D)
	 * @see #isLinked() 
	 */
	public boolean areLinkedTogether(Frame3D sourceFrame) {
		if (sourceFrame == srcFrame)			
			return true;
		if (linkedFramesList.contains(sourceFrame))
			return true;
		return false;
	}
	
	/**
	 * Sets the {@link #translation()} of the frame, locally defined with respect
	 * to the {@link #referenceFrame()}. Calls {@link #modified()}.
	 * <p>
	 * Use {@link #setPosition(Vector3D)} to define the world coordinates
	 * {@link #position()}. Use {@link #setTranslationWithConstraint(Vector3D)} to
	 * take into account the potential {@link #constraint()} of the Frame.
	 */
	public final void setTranslation(Vector3D t) {
		kernel().setTranslation(t);
		modified();
	}

	/**
	 * Same as {@link #setTranslation(Vector3D)}, but with {@code float}
	 * parameters.
	 */
	public final void setTranslation(float x, float y, float z) {
		setTranslation(new Vector3D(x, y, z));
	}

	/**
	 * Same as {@link #setTranslation(Vector3D)}, but if there's a
	 * {@link #constraint()} it is satisfied (without modifying {@code
	 * translation}).
	 * 
	 * @see #setRotationWithConstraint(Quaternion)
	 * @see #setPositionWithConstraint(Vector3D)
	 */
	public final void setTranslationWithConstraint(Vector3D translation) {
		Vector3D deltaT = Vector3D.sub(translation, this.translation());
		if (constraint() != null)
			deltaT = constraint().constrainTranslation(deltaT, this);

		translation().add(deltaT);

		/**
		 * translation.x = this.translation().x; translation.y =
		 * this.translation().y; translation.z = this.translation().z;
		 */
	}

	/**
	 * Set the current rotation Quaternion and Calls {@link #modified()}. See the
	 * different Quaternion constructors.
	 * <p>
	 * Sets the {@link #rotation()} of the Frame, locally defined with respect to
	 * the {@link #referenceFrame()}.
	 * <p>
	 * Use {@link #setOrientation(Quaternion)} to define the world coordinates
	 * {@link #orientation()}. The potential {@link #constraint()} of the Frame is
	 * not taken into account, use {@link #setRotationWithConstraint(Quaternion)}
	 * instead.
	 * 
	 * @see #setRotationWithConstraint(Quaternion)
	 * @see #rotation()
	 * @see #setTranslation(Vector3D)
	 */
	public final void setRotation(Quaternion r) {
		kernel().setRotation(r);
		modified();
	}

	/**
	 * Same as {@link #setRotation(Quaternion)} but with {@code float} Quaternion
	 * parameters.
	 */
	public final void setRotation(float x, float y, float z, float w) {
		setRotation(new Quaternion(x, y, z, w));
	}

	/**
	 * Same as {@link #setRotation(Quaternion)}, if there's a
	 * {@link #constraint()} it's satisfied (without modifying {@code rotation}).
	 * 
	 * @see #setTranslationWithConstraint(Vector3D)
	 * @see #setOrientationWithConstraint(Quaternion)
	 */
	public final void setRotationWithConstraint(Quaternion rotation) {
		Quaternion deltaQ = Quaternion.multiply(this.rotation().inverse(), rotation);
		if (constraint() != null)
			deltaQ = constraint().constrainRotation(deltaQ, this);

		deltaQ.normalize(); // Prevent numerical drift

		rotation().multiply(deltaQ);
		rotation().normalize();
		// rotation.x = this.rotation().x;
		// rotation.y = this.rotation().y;
		// rotation.z = this.rotation().z;
		// rotation.w = this.rotation().w;
	}

	/**
	 * Sets the {@link #referenceFrame()} of the Frame and calls
	 * {@link #modified()}.
	 * <p>
	 * The Frame {@link #translation()} and {@link #rotation()} are then defined
	 * in the {@link #referenceFrame()} coordinate system.
	 * <p>
	 * Use {@link #position()} and {@link #orientation()} to express these in the
	 * world coordinate system.
	 * <p>
	 * Using this method, you can create a hierarchy of Frames. This hierarchy
	 * needs to be a tree, which root is the world coordinate system (i.e.,
	 * {@code null} {@link #referenceFrame()}). No action is performed if setting
	 * {@code refFrame} as the {@link #referenceFrame()} would create a loop in
	 * the Frame hierarchy.
	 * 
	 * @see #settingAsReferenceFrameWillCreateALoop(Frame3D)
	 */
	public final void setReferenceFrame(Frame3D rFrame) {
		if (settingAsReferenceFrameWillCreateALoop(rFrame))
			System.out.println("Frame.setReferenceFrame would create a loop in Frame hierarchy");
		else {
			boolean identical = (kernel().referenceFrame() == rFrame);
			kernel().setReferenceFrame(rFrame);
			if (!identical)
				modified();
		}
	}

	/**
	 * Sets the {@link #constraint()} attached to the Frame.
	 * <p>
	 * A {@code null} value means no constraint.
	 */
	public void setConstraint(Constraint c) {
		kernel().setConstraint(c);
	}

	/**
	 * Returns {@code true} if setting {@code frame} as the Frame's
	 * {@link #referenceFrame()} would create a loop in the Frame hierarchy.
	 */
	public final boolean settingAsReferenceFrameWillCreateALoop(Frame3D frame) {
		Frame3D f = frame;
		while (f != null) {
			if (f == this)
				return true;
			f = f.referenceFrame();
		}
		return false;
	}

	/**
	 * Returns the orientation of the Frame, defined in the world coordinate
	 * system.
	 * 
	 * @see #position()
	 * @see #setOrientation(Quaternion)
	 * @see #rotation()
	 */
	public final Quaternion orientation() {
		Quaternion res = rotation();
		Frame3D fr = referenceFrame();
		while (fr != null) {
			res = Quaternion.multiply(fr.rotation(), res);
			fr = fr.referenceFrame();
		}
		return res;
	}

	/**
	 * Sets the {@link #position()} of the Frame, defined in the world coordinate
	 * system.
	 * <p>
	 * Use {@link #setTranslation(Vector3D)} to define the local Frame translation
	 * (with respect to the {@link #referenceFrame()}). The potential
	 * {@link #constraint()} of the Frame is not taken into account, use
	 * {@link #setPositionWithConstraint(Vector3D)} instead.
	 */
	public final void setPosition(Vector3D p) {
		if (referenceFrame() != null)
			setTranslation(referenceFrame().coordinatesOf(p));
		else
			setTranslation(p);
	}

	/**
	 * Same as {@link #setPosition(float, float, float)}, but with {@code float}
	 * parameters.
	 */
	public final void setPosition(float x, float y, float z) {
		setPosition(new Vector3D(x, y, z));
	}

	/**
	 * Same as {@link #setPosition(Vector3D)}, but if there's a
	 * {@link #constraint()} it is satisfied (without modifying {@code position}).
	 * 
	 * @see #setOrientationWithConstraint(Quaternion)
	 * @see #setTranslationWithConstraint(Vector3D)
	 */
	public final void setPositionWithConstraint(Vector3D position) {
		if (referenceFrame() != null)
			position = referenceFrame().coordinatesOf(position);

		setTranslationWithConstraint(position);
	}

	/**
	 * Sets the {@link #orientation()} of the Frame, defined in the world
	 * coordinate system.
	 * <p>
	 * Use {@link #setRotation(Quaternion)} to define the local frame rotation
	 * (with respect to the {@link #referenceFrame()}). The potential
	 * {@link #constraint()} of the Frame is not taken into account, use
	 * {@link #setOrientationWithConstraint(Quaternion)} instead.
	 */
	public final void setOrientation(Quaternion q) {
		if (referenceFrame() != null)
			setRotation(Quaternion.multiply(referenceFrame().orientation().inverse(),	q));
		else
			setRotation(q);
	}

	/**
	 * Same as {@link #setOrientation(Quaternion)}, but with {@code float}
	 * parameters.
	 */
	public final void setOrientation(float x, float y, float z, float w) {
		setOrientation(new Quaternion(x, y, z, w));
	}

	/**
	 * Same as {@link #setOrientation(Quaternion)}, but if there's a
	 * {@link #constraint()} it is satisfied (without modifying {@code
	 * orientation}).
	 * 
	 * @see #setPositionWithConstraint(Vector3D)
	 * @see #setRotationWithConstraint(Quaternion)
	 */
	public final void setOrientationWithConstraint(Quaternion orientation) {
		if (referenceFrame() != null)
			orientation = Quaternion.multiply(referenceFrame().orientation()
					.inverse(), orientation);

		setRotationWithConstraint(orientation);
	}

	/**
	 * Returns the position of the Frame, defined in the world coordinate system.
	 * 
	 * @see #orientation()
	 * @see #setPosition(Vector3D)
	 * @see #translation()
	 */
	public final Vector3D position() {
		return inverseCoordinatesOf(new Vector3D(0, 0, 0));
	}

	/**
	 * Same as {@code translate(t, true)}. Calls {@link #modified()}.
	 * 
	 * @see #translate(Vector3D, boolean)
	 * @see #rotate(Quaternion)
	 */
	public final void translate(Vector3D t) {
		if (constraint() != null)
			kernel().translation().add(constraint().constrainTranslation(t, this));
		else
			kernel().translation().add(t);
		modified();
	}

	/**
	 * Translates the Frame according to {@code t}, locally defined with respect
	 * to the {@link #referenceFrame()}. Calls {@link #modified()}.
	 * <p>
	 * If there's a {@link #constraint()} it is satisfied. Hence the translation
	 * actually applied to the Frame may differ from {@code t} (since it can be
	 * filtered by the {@link #constraint()}). Use {@code translate(t, false)} to
	 * retrieve the filtered translation value and {@code translate(t, true)} to
	 * keep the original value of {@code t}. Use {@link #setTranslation(Vector3D)}
	 * to directly translate the Frame without taking the {@link #constraint()}
	 * into account.
	 * 
	 * @see #rotate(Quaternion)
	 */
	public final void translate(Vector3D t, boolean keepArg) {
		Vector3D o = new Vector3D(t.vec[0], t.vec[1], t.vec[2]);
		if (constraint() != null) {
			o = constraint().constrainTranslation(t, this);
			if (!keepArg) {
				t.vec[0] = o.vec[0];
				t.vec[1] = o.vec[1];
				t.vec[2] = o.vec[2];
			}
		}
		kernel().translation().add(o);
		modified();
	}

	/**
	 * Same as {@link #translate(Vector3D)} but with {@code float} parameters.
	 */
	public final void translate(float x, float y, float z) {
		translate(new Vector3D(x, y, z));
	}

	/**
	 * Same as {@code rotate(q, true)}. Calls {@link #modified()}.
	 * 
	 * @see #rotate(Quaternion, boolean)
	 * @see #translate(Vector3D)
	 */
	public final void rotate(Quaternion q) {
		if (constraint() != null)
			kernel().rotation().multiply(constraint().constrainRotation(q, this));
		else
			kernel().rotation().multiply(q);

		kernel().rotation().normalize(); // Prevents numerical drift
		modified();
	}

	/**
	 * Rotates the Frame by {@code q} (defined in the Frame coordinate system):
	 * {@code R = R*q}. Calls {@link #modified()}.
	 * <p>
	 * If there's a {@link #constraint()} it is satisfied. Hence the rotation
	 * actually applied to the Frame may differ from {@code q} (since it can be
	 * filtered by the {@link #constraint()}). Use {@code rotate(q, false)} to
	 * retrieve the filtered rotation value and {@code rotate(q, true)} to keep
	 * the original value of {@code q}. Use {@link #setRotation(Quaternion)} to
	 * directly rotate the Frame without taking the {@link #constraint()} into
	 * account.
	 * 
	 * @see #translate(Vector3D)
	 */
	public final void rotate(Quaternion q, boolean keepArg) {
		Quaternion o = q.get();
		if (constraint() != null) {
			o = constraint().constrainRotation(q, this);
			if (!keepArg) {
				q.quat[0] = o.quat[0];
				q.quat[1] = o.quat[1];
				q.quat[2] = o.quat[2];
				q.quat[3] = o.quat[3];
			}
		}
		kernel().rotation().multiply(o);

		kernel().rotation().normalize(); // Prevents numerical drift
		modified();
	}

	/**
	 * Same as {@link #rotate(Quaternion)} but with {@code float} Quaternion
	 * parameters.
	 */
	public final void rotate(float x, float y, float z, float w) {
		rotate(new Quaternion(x, y, z, w));
	}

	/**
	 * Same as {@code rotateAroundPoint(rotation, point, true)}. Calls
	 * {@link #modified()}.
	 */
	public final void rotateAroundPoint(Quaternion rotation, Vector3D point) {
		if (constraint() != null)
			rotation = constraint().constrainRotation(rotation, this);

		this.kernel().rotation().multiply(rotation);
		this.kernel().rotation().normalize(); // Prevents numerical drift

		Quaternion q = new Quaternion(inverseTransformOf(rotation.axis()), rotation.angle());
		Vector3D t = Vector3D.add(point, q.rotate(Vector3D.sub(position(), point)));
		t.sub(kernel().translation());

		if (constraint() != null)
			kernel().translation().add(constraint().constrainTranslation(t, this));
		else
			kernel().translation().add(t);
		modified();
	}

	/**
	 * Same as {@link #rotateAroundPoint(Quaternion, Vector3D)} but if there's a
	 * {@link #constraint()} {@code rotation} is modified to satisfy it.
	 */

	/**
	 * Makes the Frame {@link #rotate(Quaternion)} by {@code rotation} around
	 * {@code point}. Calls {@link #modified()}.
	 * <p>
	 * {@code point} is defined in the world coordinate system, while the {@code
	 * rotation} axis is defined in the Frame coordinate system.
	 * <p>
	 * If the Frame has a {@link #constraint()}, {@code rotation} is first
	 * constrained using
	 * {@link remixlab.remixcam.constraints.Constraint#constrainRotation(Quaternion, Frame3D)}.
	 * Hence the rotation actually applied to the Frame may differ from {@code
	 * rotation} (since it can be filtered by the {@link #constraint()}). Use
	 * {@code rotateAroundPoint(rotation, point, false)} to retrieve the filtered
	 * rotation value and {@code rotateAroundPoint(rotation, point, true)} to keep
	 * the original value of {@code rotation}.
	 * <p>
	 * The translation which results from the filtered rotation around {@code
	 * point} is then computed and filtered using
	 * {@link remixlab.remixcam.constraints.Constraint#constrainTranslation(Vector3D, Frame3D)}.
	 */
	public final void rotateAroundPoint(Quaternion rotation, Vector3D point, boolean keepArg) {
		Quaternion q = rotation.get();
		if (constraint() != null) {
			q = constraint().constrainRotation(rotation, this);
			if (!keepArg) {
				rotation.quat[0] = q.quat[0];
				rotation.quat[1] = q.quat[1];
				rotation.quat[2] = q.quat[2];
				rotation.quat[3] = q.quat[3];
			}
		}
		this.kernel().rotation().multiply(q);
		this.kernel().rotation().normalize(); // Prevents numerical drift

		q = new Quaternion(inverseTransformOf(rotation.axis()), rotation.angle());
		Vector3D t = Vector3D.add(point, q.rotate(Vector3D.sub(position(), point)));
		t.sub(kernel().translation());

		if (constraint() != null)
			kernel().translation().add(constraint().constrainTranslation(t, this));
		else
			kernel().translation().add(t);
		modified();
	}

	/**
	 * Convenience function that simply calls {@code alignWithFrame(frame, false,
	 * 0.85f)}
	 */
	public final void alignWithFrame(Frame3D frame) {
		alignWithFrame(frame, false, 0.85f);
	}

	/**
	 * Convenience function that simply calls {@code alignWithFrame(frame, move,
	 * 0.85f)}
	 */
	public final void alignWithFrame(Frame3D frame, boolean move) {
		alignWithFrame(frame, move, 0.85f);
	}

	/**
	 * Convenience function that simply calls {@code alignWithFrame(frame, false,
	 * threshold)}
	 */
	public final void alignWithFrame(Frame3D frame, float threshold) {
		alignWithFrame(frame, false, threshold);
	}

	/**
	 * Aligns the Frame with {@code frame}, so that two of their axis are
	 * parallel.
	 * <p>
	 * If one of the X, Y and Z axis of the Frame is almost parallel to any of the
	 * X, Y, or Z axis of {@code frame}, the Frame is rotated so that these two
	 * axis actually become parallel.
	 * <p>
	 * If, after this first rotation, two other axis are also almost parallel, a
	 * second alignment is performed. The two frames then have identical
	 * orientations, up to 90 degrees rotations.
	 * <p>
	 * {@code threshold} measures how close two axis must be to be considered
	 * parallel. It is compared with the absolute values of the dot product of the
	 * normalized axis.
	 * <p>
	 * When {@code move} is set to {@code true}, the Frame {@link #position()} is
	 * also affected by the alignment. The new Frame {@link #position()} is such
	 * that the {@code frame} frame position (computed with
	 * {@link #coordinatesOf(Vector3D)}, in the Frame coordinates system) does not
	 * change.
	 * <p>
	 * {@code frame} may be {@code null} and then represents the world coordinate
	 * system (same convention than for the {@link #referenceFrame()}).
	 */
	public final void alignWithFrame(Frame3D frame, boolean move, float threshold) {
		Vector3D[][] directions = new Vector3D[2][3];
		for (int d = 0; d < 3; ++d) {
			Vector3D dir = new Vector3D((d == 0) ? 1.0f : 0.0f, (d == 1) ? 1.0f : 0.0f,
					(d == 2) ? 1.0f : 0.0f);
			if (frame != null)
				directions[0][d] = frame.inverseTransformOf(dir);
			else
				directions[0][d] = dir;
			directions[1][d] = inverseTransformOf(dir);
		}

		float maxProj = 0.0f;
		float proj;
		short[] index = new short[2];
		index[0] = index[1] = 0;

		Vector3D vec = new Vector3D(0.0f, 0.0f, 0.0f);
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				vec.set(directions[0][i]);
				proj = Math.abs(vec.dot(directions[1][j]));
				if ((proj) >= maxProj) {
					index[0] = (short) i;
					index[1] = (short) j;
					maxProj = proj;
				}
			}
		}

		//Frame old = new Frame(this);
		Frame3D old = this.get();
		//Frame old = this.clone();

		vec.set(directions[0][index[0]]);
		float coef = vec.dot(directions[1][index[1]]);

		if (Math.abs(coef) >= threshold) {
			vec.set(directions[0][index[0]]);
			Vector3D axis = vec.cross(directions[1][index[1]]);
			float angle = (float) Math.asin(axis.mag());
			if (coef >= 0.0)
				angle = -angle;
			// setOrientation(Quaternion(axis, angle) * orientation());
			Quaternion q = new Quaternion(axis, angle);
			q = Quaternion.multiply(rotation().inverse(), q);
			q = Quaternion.multiply(q, orientation());
			rotate(q);

			// Try to align an other axis direction
			short d = (short) ((index[1] + 1) % 3);
			Vector3D dir = new Vector3D((d == 0) ? 1.0f : 0.0f, (d == 1) ? 1.0f : 0.0f,
					(d == 2) ? 1.0f : 0.0f);
			dir = inverseTransformOf(dir);

			float max = 0.0f;
			for (int i = 0; i < 3; ++i) {
				vec.set(directions[0][i]);
				proj = Math.abs(vec.dot(dir));
				if (proj > max) {
					index[0] = (short) i;
					max = proj;
				}
			}

			if (max >= threshold) {
				vec.set(directions[0][index[0]]);
				axis = vec.cross(dir);
				angle = (float) Math.asin(axis.mag());
				vec.set(directions[0][index[0]]);
				if (vec.dot(dir) >= 0.0)
					angle = -angle;
				// setOrientation(Quaternion(axis, angle) * orientation());
				q.fromAxisAngle(axis, angle);
				q = Quaternion.multiply(rotation().inverse(), q);
				q = Quaternion.multiply(q, orientation());
				rotate(q);
			}
		}

		if (move) {
			Vector3D center = new Vector3D(0.0f, 0.0f, 0.0f);
			if (frame != null)
				center = frame.position();

			vec = Vector3D
					.sub(center, orientation().rotate(old.coordinatesOf(center)));
			vec.sub(translation());
			translate(vec);
		}
	}

	/**
	 * Translates the Frame so that its {@link #position()} lies on the line
	 * defined by {@code origin} and {@code direction} (defined in the world
	 * coordinate system).
	 * <p>
	 * Simply uses an orthogonal projection. {@code direction} does not need to be
	 * normalized.
	 */
	public final void projectOnLine(Vector3D origin, Vector3D direction) {
		Vector3D shift = Vector3D.sub(origin, position());
		Vector3D proj = shift;
		// float directionSquaredNorm = (direction.x * direction.x) + (direction.y *
		// direction.y) + (direction.z * direction.z);
		// float modulation = proj.dot(direction) / directionSquaredNorm;
		// proj = Vector3D.mult(direction, modulation);
		proj = Vector3D.projectVectorOnAxis(proj, direction);
		translate(Vector3D.sub(shift, proj));
	}

	/**
	 * Returns the Frame coordinates of a point {@code src} defined in the world
	 * coordinate system (converts from world to Frame).
	 * <p>
	 * {@link #inverseCoordinatesOf(Vector3D)} performs the inverse conversion.
	 * {@link #transformOf(Vector3D)} converts 3D vectors instead of 3D
	 * coordinates.
	 */
	public final Vector3D coordinatesOf(Vector3D src) {
		if (referenceFrame() != null)
			return localCoordinatesOf(referenceFrame().coordinatesOf(src));
		else
			return localCoordinatesOf(src);
	}

	/**
	 * Returns the world coordinates of the point whose position in the Frame
	 * coordinate system is {@code src} (converts from Frame to world).
	 * <p>
	 * {@link #coordinatesOf(Vector3D)} performs the inverse conversion. Use
	 * {@link #inverseTransformOf(Vector3D)} to transform 3D vectors instead of 3D
	 * coordinates.
	 */
	public final Vector3D inverseCoordinatesOf(Vector3D src) {
		Frame3D fr = this;
		Vector3D res = src;
		while (fr != null) {
			res = fr.localInverseCoordinatesOf(res);
			fr = fr.referenceFrame();
		}
		return res;
	}

	/**
	 * Returns the Frame coordinates of a point {@code src} defined in the
	 * {@link #referenceFrame()} coordinate system (converts from
	 * {@link #referenceFrame()} to Frame).
	 * <p>
	 * {@link #localInverseCoordinatesOf(Vector3D)} performs the inverse
	 * conversion.
	 * 
	 * @see #localTransformOf(Vector3D)
	 */
	public final Vector3D localCoordinatesOf(Vector3D src) {
		return rotation().inverseRotate(Vector3D.sub(src, translation()));
	}

	/**
	 * Returns the {@link #referenceFrame()} coordinates of a point {@code src}
	 * defined in the Frame coordinate system (converts from Frame to
	 * {@link #referenceFrame()}).
	 * <p>
	 * {@link #localCoordinatesOf(Vector3D)} performs the inverse conversion.
	 * 
	 * @see #localInverseTransformOf(Vector3D)
	 */
	public final Vector3D localInverseCoordinatesOf(Vector3D src) {
		return Vector3D.add(rotation().rotate(src), translation());
	}

	/**
	 * Returns the Frame coordinates of the point whose position in the {@code
	 * from} coordinate system is {@code src} (converts from {@code from} to
	 * Frame).
	 * <p>
	 * {@link #coordinatesOfIn(Vector3D, Frame3D)} performs the inverse
	 * transformation.
	 */
	public final Vector3D coordinatesOfFrom(Vector3D src, Frame3D from) {
		if (this == from)
			return src;
		else if (referenceFrame() != null)
			return localCoordinatesOf(referenceFrame().coordinatesOfFrom(src, from));
		else
			return localCoordinatesOf(from.inverseCoordinatesOf(src));
	}

	/**
	 * Returns the {@code in} coordinates of the point whose position in the Frame
	 * coordinate system is {@code src} (converts from Frame to {@code in}).
	 * <p>
	 * {@link #coordinatesOfFrom(Vector3D, Frame3D)} performs the inverse
	 * transformation.
	 */
	public final Vector3D coordinatesOfIn(Vector3D src, Frame3D in) {
		Frame3D fr = this;
		Vector3D res = src;
		while ((fr != null) && (fr != in)) {
			res = fr.localInverseCoordinatesOf(res);
			fr = fr.referenceFrame();
		}

		if (fr != in)
			// in was not found in the branch of this, res is now expressed in
			// the world
			// coordinate system. Simply convert to in coordinate system.
			res = in.coordinatesOf(res);

		return res;
	}

	/**
	 * Returns the Frame transform of a vector {@code src} defined in the world
	 * coordinate system (converts vectors from world to Frame).
	 * <p>
	 * {@link #inverseTransformOf(Vector3D)} performs the inverse transformation.
	 * {@link #coordinatesOf(Vector3D)} converts 3D coordinates instead of 3D
	 * vectors (here only the rotational part of the transformation is taken into
	 * account).
	 */
	public final Vector3D transformOf(Vector3D src) {
		if (referenceFrame() != null)
			return localTransformOf(referenceFrame().transformOf(src));
		else
			return localTransformOf(src);

	}

	/**
	 * Returns the world transform of the vector whose coordinates in the Frame
	 * coordinate system is {@code src} (converts vectors from Frame to world).
	 * <p>
	 * {@link #transformOf(Vector3D)} performs the inverse transformation. Use
	 * {@link #inverseCoordinatesOf(Vector3D)} to transform 3D coordinates instead
	 * of 3D vectors.
	 */
	public final Vector3D inverseTransformOf(Vector3D src) {
		Frame3D fr = this;
		Vector3D res = src;
		while (fr != null) {
			res = fr.localInverseTransformOf(res);
			fr = fr.referenceFrame();
		}
		return res;
	}

	/**
	 * Rotates the frame so that its {@link #xAxis()} becomes {@code axis} defined
	 * in the world coordinate system.
	 * <p>
	 * <b>Attention:</b> this rotation is not uniquely defined. See
	 * {@link remixlab.remixcam.geom.Quaternion#fromTo(Vector3D, Vector3D)}.
	 * 
	 * @see #xAxis()
	 * @see #setYAxis(Vector3D)
	 * @see #setZAxis(Vector3D)
	 */
	public void setXAxis(Vector3D axis) {
		rotate(new Quaternion(new Vector3D(1.0f, 0.0f, 0.0f), transformOf(axis)));
	}

	/**
	 * Rotates the frame so that its {@link #yAxis()} becomes {@code axis} defined
	 * in the world coordinate system.
	 * <p>
	 * <b>Attention:</b> this rotation is not uniquely defined. See
	 * {@link remixlab.remixcam.geom.Quaternion#fromTo(Vector3D, Vector3D)}.
	 * 
	 * @see #yAxis()
	 * @see #setYAxis(Vector3D)
	 * @see #setZAxis(Vector3D)
	 */
	public void setYAxis(Vector3D axis) {
		rotate(new Quaternion(new Vector3D(0.0f, 1.0f, 0.0f), transformOf(axis)));
	}

	/**
	 * Rotates the frame so that its {@link #zAxis()} becomes {@code axis} defined
	 * in the world coordinate system.
	 * <p>
	 * <b>Attention:</b> this rotation is not uniquely defined. See
	 * {@link remixlab.remixcam.geom.Quaternion#fromTo(Vector3D, Vector3D)}.
	 * 
	 * @see #zAxis()
	 * @see #setYAxis(Vector3D)
	 * @see #setZAxis(Vector3D)
	 */
	public void setZAxis(Vector3D axis) {
		rotate(new Quaternion(new Vector3D(0.0f, 0.0f, 1.0f), transformOf(axis)));
	}

	/**
	 * Returns the x-axis of the frame, represented as a normalized vector defined
	 * in the world coordinate system.
	 * 
	 * @see #setXAxis(Vector3D)
	 * @see #yAxis()
	 * @see #zAxis()
	 */
	public Vector3D xAxis() {
		return inverseTransformOf(new Vector3D(1.0f, 0.0f, 0.0f));
	}

	/**
	 * Returns the y-axis of the frame, represented as a normalized vector defined
	 * in the world coordinate system.
	 * 
	 * @see #setYAxis(Vector3D)
	 * @see #xAxis()
	 * @see #zAxis()
	 */
	public Vector3D yAxis() {
		return inverseTransformOf(new Vector3D(0.0f, 1.0f, 0.0f));
	}

	/**
	 * Returns the z-axis of the frame, represented as a normalized vector defined
	 * in the world coordinate system.
	 * 
	 * @see #setZAxis(Vector3D)
	 * @see #xAxis()
	 * @see #yAxis()
	 */
	public Vector3D zAxis() {
		return inverseTransformOf(new Vector3D(0.0f, 0.0f, 1.0f));
	}

	/**
	 * Returns the Frame transform of a vector {@code src} defined in the
	 * {@link #referenceFrame()} coordinate system (converts vectors from
	 * {@link #referenceFrame()} to Frame).
	 * <p>
	 * {@link #localInverseTransformOf(Vector3D)} performs the inverse
	 * transformation.
	 * 
	 * @see #localCoordinatesOf(Vector3D)
	 */
	public final Vector3D localTransformOf(Vector3D src) {
		return rotation().inverseRotate(src);
	}

	/**
	 * Returns the {@link #referenceFrame()} transform of a vector {@code src}
	 * defined in the Frame coordinate system (converts vectors from Frame to
	 * {@link #referenceFrame()}).
	 * <p>
	 * {@link #localTransformOf(Vector3D)} performs the inverse transformation.
	 * 
	 * @see #localInverseCoordinatesOf(Vector3D)
	 */
	public final Vector3D localInverseTransformOf(Vector3D src) {
		return rotation().rotate(src);
	}

	/**
	 * Returns the Frame transform of the vector whose coordinates in the {@code
	 * from} coordinate system is {@code src} (converts vectors from {@code from}
	 * to Frame).
	 * <p>
	 * {@link #transformOfIn(Vector3D, Frame3D)} performs the inverse transformation.
	 */
	public final Vector3D transformOfFrom(Vector3D src, Frame3D from) {
		if (this == from)
			return src;
		else if (referenceFrame() != null)
			return localTransformOf(referenceFrame().transformOfFrom(src, from));
		else
			return localTransformOf(from.inverseTransformOf(src));
	}

	/**
	 * Returns the {@code in} transform of the vector whose coordinates in the
	 * Frame coordinate system is {@code src} (converts vectors from Frame to
	 * {@code in}).
	 * <p>
	 * {@link #transformOfFrom(Vector3D, Frame3D)} performs the inverse
	 * transformation.
	 */
	public final Vector3D transformOfIn(Vector3D src, Frame3D in) {
		Frame3D fr = this;
		Vector3D res = src;
		while ((fr != null) && (fr != in)) {
			res = fr.localInverseTransformOf(res);
			fr = fr.referenceFrame();
		}

		if (fr != in)
			// in was not found in the branch of this, res is now expressed in
			// the world
			// coordinate system. Simply convert to in coordinate system.
			res = in.transformOf(res);

		return res;
	}

	/**
	 * Returns the Matrix3D associated with this Frame.
	 * <p>
	 * This method could be used in conjunction with {@code applyMatrix()} to
	 * modify the processing modelview matrix from a Frame hierarchy. For example,
	 * with this Frame hierarchy:
	 * <p>
	 * {@code Frame body = new Frame();} <br>
	 * {@code Frame leftArm = new Frame();} <br>
	 * {@code Frame rightArm = new Frame();} <br>
	 * {@code leftArm.setReferenceFrame(body);} <br>
	 * {@code rightArm.setReferenceFrame(body);} <br>
	 * <p>
	 * The associated processing drawing code should look like:
	 * <p>
	 * {@code p.pushMatrix();}//Supposing p is the PApplet instance <br>
	 * {@code p.applyMatrix(body.matrix());} <br>
	 * {@code drawBody();} <br>
	 * {@code p.pushMatrix();} <br>
	 * {@code p.applyMatrix(leftArm.matrix());} <br>
	 * {@code drawArm();} <br>
	 * {@code p.popMatrix();} <br>
	 * {@code p.pushMatrix();} <br>
	 * {@code p.applyMatrix(rightArm.matrix());} <br>
	 * {@code drawArm();} <br>
	 * {@code p.popMatrix();} <br>
	 * {@code p.popMatrix();} <br>
	 * <p>
	 * Note the use of nested {@code pushMatrix()} and {@code popMatrix()} blocks
	 * to represent the frame hierarchy: {@code leftArm} and {@code rightArm} are
	 * both correctly drawn with respect to the {@code body} coordinate system.
	 * <p>
	 * <b>Attention:</b> This technique is inefficient because {@code
	 * p.applyMatrix} will try to calculate the inverse of the transform. Avoid it
	 * whenever possible and instead use {@link #applyTransformation(AbstractScene)}
	 * which is very efficient.
	 * <p>
	 * This matrix only represents the local Frame transformation (i.e., with
	 * respect to the {@link #referenceFrame()}). Use {@link #worldMatrix()} to
	 * get the full Frame transformation matrix (i.e., from the world to the Frame
	 * coordinate system). These two match when the {@link #referenceFrame()} is
	 * {@code null}.
	 * <p>
	 * The result is only valid until the next call to {@code matrix()} or
	 * {@link #worldMatrix()}. Use it immediately (as above).
	 * <p>
	 * <b>Note:</b> The scaling factor of the 4x4 matrix is 1.0.
	 * 
	 * @see #applyTransformation(AbstractScene)
	 */
	// TODO is always inneficient
	public final Matrix3D matrix() {
		Matrix3D pM = new Matrix3D();

		pM = kernel().rotation().matrix();

		pM.mat[12] = kernel().translation().vec[0];
		pM.mat[13] = kernel().translation().vec[1];
		pM.mat[14] = kernel().translation().vec[2];

		return pM;
	}	
	
	/**
	 * Convenience function that simply calls {@code scn.applyTransformation(this)}.
	 * 
	 * @see #matrix()
	 * @see remixlab.remixcam.core.AbstractScene#applyTransformation(Frame3D)
	 */
	public void applyTransformation(AbstractScene scn) {
		scn.applyTransformation(this);
	}

	/**
	 * Returns the transformation matrix represented by the Frame.
	 * <p>
	 * This method should be used in conjunction with {@code applyMatrix()} to
	 * modify the processing modelview matrix from a Frame:
	 * <p>
	 * {@code
	 * // Here the modelview matrix corresponds to the world coordinate system.} <br>
	 * {@code Frame fr = new Frame(pos, Quaternion(from, to));} <br>
	 * {@code pushMatrix();} <br>
	 * {@code applyMatrix(worldMatrix());} <br>
	 * {@code // draw object in the fr coordinate system.} <br>
	 * {@code popMatrix();} <br>
	 * <p>
	 * This matrix represents the global Frame transformation: the entire
	 * {@link #referenceFrame()} hierarchy is taken into account to define the
	 * Frame transformation from the world coordinate system. Use
	 * {@link #matrix()} to get the local Frame transformation matrix (i.e.
	 * defined with respect to the {@link #referenceFrame()}). These two match
	 * when the {@link #referenceFrame()} is {@code null}.
	 * <p>
	 * <b>Attention:</b> The result is only valid until the next call to
	 * {@link #matrix()} or {@code worldMatrix()}. Use it immediately (as above).
	 * <p>
	 * <b>Note:</b> The scaling factor of the 4x4 matrix is 1.0.
	 */
	public final Matrix3D worldMatrix() {
		if (referenceFrame() != null) {
			final Frame3D fr = new Frame3D();
			fr.setTranslation(position());
			fr.setRotation(orientation());
			return fr.matrix();
		} else
			return matrix();
	}

	/**
	 * Sets the Frame from a Matrix3D representation
	 * (rotation in the upper left 3x3 matrix and translation on the last column).
	 * Calls {@link #modified()}.
	 * <p>
	 * Hence, if a code fragment looks like:
	 * <p>
	 * {@code float [] m = new float [16]; m[0]=...;} <br>
	 * {@code gl.glMultMatrixf(m);} <br>
	 * <p>
	 * It is equivalent to write:
	 * <p>
	 * {@code Frame fr = new Frame();} <br>
	 * {@code fr.fromMatrix(m);} <br>
	 * {@code applyMatrix(fr.matrix());} <br>
	 * <p>
	 * Using this conversion, you can benefit from the powerful Frame
	 * transformation methods to translate points and vectors to and from the
	 * Frame coordinate system to any other Frame coordinate system (including the
	 * world coordinate system). See {@link #coordinatesOf(Vector3D)} and
	 * {@link #transformOf(Vector3D)}.
	 * <p>
	 * <b>Attention:</b> A Frame does not contain a scale factor. The possible
	 * scaling in {@code m} will not be converted into the Frame by this method.
	 */
	public final void fromMatrix(Matrix3D pM) {
		// m should be of size [4][4]
		if (Math.abs(pM.mat[15]) < 1E-8) {
			System.out.println("Doing nothing: pM.mat[15] should be non-zero!");
			return;
		}

		/**
		kernel().translation().vec[0] = pM.mat[12] / pM.mat[15];
		kernel().translation().vec[1] = pM.mat[13] / pM.mat[15];
		kernel().translation().vec[2] = pM.mat[14] / pM.mat[15];

		float[][] r = new float[3][3];

		r[0][0] = pM.mat[0] / pM.mat[15];
		r[0][1] = pM.mat[4] / pM.mat[15];
		r[0][2] = pM.mat[8] / pM.mat[15];
		r[1][0] = pM.mat[1] / pM.mat[15];
		r[1][1] = pM.mat[5] / pM.mat[15];
		r[1][2] = pM.mat[9] / pM.mat[15];
		r[2][0] = pM.mat[2] / pM.mat[15];
		r[2][1] = pM.mat[6] / pM.mat[15];
		r[2][2] = pM.mat[10] / pM.mat[15];

		kernel().rotation().fromRotationMatrix(r);
		// */
		
		// /**
		float [][] m = new float[4][4];
		pM.get(m);
		float [][] rot = new float[3][3];
	  for (int i=0; i<3; ++i) {
	  	kernel().translation().vec[i] = m[3][i] / m[3][3];
	  	for (int j=0; j<3; ++j)
	  	  // Beware of the transposition (OpenGL to European math)
	  		rot[i][j] = m[j][i] / m[3][3];
	  }
	  kernel().rotation().fromRotationMatrix(rot);		
		// */	
		
		modified();
	}

	/**
	 * Returns a Frame representing the inverse of the Frame space transformation.
	 * <p>
	 * The {@link #rotation()} the new Frame is the
	 * {@link remixlab.remixcam.geom.Quaternion#inverse()} of the original rotation.
	 * Its {@link #translation()} is the negated inverse rotated image of the
	 * original translation.
	 * <p>
	 * If a Frame is considered as a space rigid transformation (translation and
	 * rotation), the inverse() Frame performs the inverse transformation.
	 * <p>
	 * Only the local Frame transformation (i.e., defined with respect to the
	 * {@link #referenceFrame()}) is inverted. Use {@link #worldInverse()} for a
	 * global inverse.
	 * <p>
	 * The resulting Frame has the same {@link #referenceFrame()} as the Frame and
	 * a {@code null} {@link #constraint()}.
	 * <p>
	 * <b>Note:</b> The scaling factor of the 4x4 matrix is 1.0.
	 */
	public final Frame3D inverse() {
		Frame3D fr = new Frame3D(Vector3D.mult(kernel().rotation().inverseRotate(kernel().translation()), -1), kernel().rotation().inverse());
		fr.setReferenceFrame(referenceFrame());
		return fr;
	}

	/**
	 * 
	 * Returns the {@link #inverse()} of the Frame world transformation.
	 * <p>
	 * The {@link #orientation()} of the new Frame is the
	 * {@link remixlab.remixcam.geom.Quaternion#inverse()} of the original orientation.
	 * Its {@link #position()} is the negated and inverse rotated image of the
	 * original position.
	 * <p>
	 * The result Frame has a {@code null} {@link #referenceFrame()} and a {@code
	 * null} {@link #constraint()}.
	 * <p>
	 * Use {@link #inverse()} for a local (i.e., with respect to
	 * {@link #referenceFrame()}) transformation inverse.
	 */
	public final Frame3D worldInverse() {
		return (new Frame3D(
				Vector3D.mult(orientation().inverseRotate(position()), -1),
				orientation().inverse()));
	}
}