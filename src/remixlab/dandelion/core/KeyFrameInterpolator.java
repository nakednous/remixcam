/**
 *                     Dandelion (version 0.70.0)      
 *          Copyright (c) 2013 by Jean Pierre Charalambos
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This library provides classes to ease the creation of interactive
 * frame-based, 2d and 3d scenes.
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

package remixlab.dandelion.core;

import java.util.*;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.dandelion.geom.*;
import remixlab.dandelion.util.AbstractTimerJob;
import remixlab.tersehandling.core.Copyable;
import remixlab.tersehandling.core.Util;

/**
 * A keyFrame Catmull-Rom Frame interpolator.
 * <p>
 * A KeyFrameInterpolator holds keyFrames (that define a path) and, optionally,
 * a reference to a Frame of your application (which will be interpolated). In
 * this case, when the user {@link #startInterpolation()}, the
 * KeyFrameInterpolator regularly updates the {@link #frame()} position and
 * orientation along the path.
 * <p>
 * Here is a typical utilization example (see also the examples FrameInterpolation
 * and CameraInterpolation):
 * <p>
 * {@code //PApplet.setup() should look like:}<br>
 * {@code size(640, 360, P3D);}<br>
 * {@code // The KeyFrameInterpolator kfi is given the Frame that it will drive
 * over time.}<br>
 * {@code myFrame = new Frame());}
 * {@code kfi = new KeyFrameInterpolator( myFrame, this );} 
 * //or an anonymous Frame may also be given: {@code kfi = new KeyFrameInterpolator( this );}<br>
 * {@code //By default the Frame is provided as a reference to the
 * KeyFrameInterpolator}} (see {@link #addKeyFrame(GeomFrame)} methods):<br>
 * {@code kfi.addKeyFrame( new Frame( new Vector3D(1,0,0), new Quaternion() ) );}<br>
 * {@code kfi.addKeyFrame( new Frame( new Vector3D(2,1,0), new Quaternion() ) );}<br>
 * {@code // ...and so on for all the keyFrames.}<br>
 * {@code kfi.startInterpolation();}<br>
 * <p>
 * {@code //PApplet.draw() should look like:}<br>
 * {@code background(0);}<br>
 * {@code scene.beginDraw();}<br>
 * {@code pushMatrix();}<br>
 * {@code kfi.frame().applyTransformation(this);}<br>
 * {@code
 * // Draw your object here. Its position and orientation are interpolated.}<br>
 * {@code popMatrix();}<br>
 * {@code scene.endDraw();}<br>
 * <p>
 * The keyFrames are defined by a Frame and a time, expressed in seconds.
 * Optionally, the Frame can be provided as a reference (see the
 * {@link #addKeyFrame(GeomFrame)} methods). In this case, the path will
 * automatically be updated when the Frame is modified.
 * <p>
 * The time has to be monotonously increasing over keyFrames. When
 * {@link #interpolationSpeed()} equals 1.0 (default value), these times
 * correspond to actual user's seconds during interpolation (provided that your
 * main loop is fast enough). The interpolation is then real-time: the keyFrames
 * will be reached at their {@link #keyFrameTime(int)}.
 * <p>
 * <h3>Interpolation details</h3>
 * <p>
 * When the user {@link #startInterpolation()}, a timer is started which will
 * update the {@link #frame()}'s position and orientation every
 * {@link #interpolationPeriod()} milliseconds. This update increases the
 * {@link #interpolationTime()} by {@link #interpolationPeriod()} *
 * {@link #interpolationSpeed()} milliseconds.
 * <p>
 * Note that this mechanism ensures that the number of interpolation steps is
 * constant and equal to the total path {@link #duration()} divided by the
 * {@link #interpolationPeriod()} * {@link #interpolationSpeed()}. This is
 * especially useful for benchmarking or movie creation (constant number of
 * snapshots).
 * <p>
 * The interpolation is stopped when {@link #interpolationTime()} is greater
 * than the {@link #lastTime()} (unless loopInterpolation() is {@code true}).
 * <p>
 * Note that a Camera has
 * {@link remixlab.dandelion.core.Camera#keyFrameInterpolator(int)}, that can be used
 * to drive the Camera along a path.
 * <p>
 * <b>Attention:</b> If a Constraint is attached to the {@link #frame()} (see
 * {@link remixlab.dandelion.geom.GeomFrame#constraint()}), it should be deactivated
 * before {@link #interpolationIsStarted()}, otherwise the interpolated motion
 * (computed as if there was no constraint) will probably be erroneous.
 */
public class KeyFrameInterpolator implements Copyable {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
		append(currentFrmValid).
		append(fr).
		append(interpolationSpd).
		append(interpolationStrt).
		append(interpolationTm).
		append(keyFr).
		append(lpInterpolation).
		append(myFrame).
		append(path).
		append(pathIsValid).
		append(period).
		append(valuesAreValid).
    toHashCode();
	}

	@Override
	public boolean equals(Object obj) {		
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		KeyFrameInterpolator other = (KeyFrameInterpolator) obj;
	   return new EqualsBuilder()		
		.append(currentFrmValid, other.currentFrmValid)
		.append(fr, other.fr)
		.append(interpolationSpd, other.interpolationSpd)
		.append(interpolationStrt, other.interpolationStrt)
		.append(interpolationTm, other.interpolationTm)
		.append(keyFr, other.keyFr)
		.append(lpInterpolation, other.lpInterpolation)
		.append(myFrame, other.myFrame)
		.append(path, other.path )
		.append(pathIsValid, other.pathIsValid)
		.append(period, other.period)
		.append(valuesAreValid, other.valuesAreValid)
		.isEquals();
	}

	protected abstract class AbstractKeyFrame implements Copyable {
		@Override
		public int hashCode() {
	    return new HashCodeBuilder(17, 37).
			append(frm).
	    toHashCode();
		}
		
		@Override
		public boolean equals(Object obj) {		
			if (obj == null) return false;
			if (obj == this) return true;		
			if (obj.getClass() != getClass()) return false;
			
			AbstractKeyFrame other = (AbstractKeyFrame) obj;
		   return new EqualsBuilder()		
			.append(frm, other.frm)
			.isEquals();
		}
		
		protected Vec p, tgPVec;
		protected Vec s;		
		//Option 2 (interpolate scaling using a spline)
		protected Vec tgSVec;
		protected Orientable q;
		protected float tm;
		protected GeomFrame frm;

		AbstractKeyFrame(GeomFrame fr, float t, boolean setRef) {
			tm = t;
			if (setRef) {
				frm = fr;
				updateValues();
			} else {
				frm = null;
				p = new Vec(fr.position().vec[0], fr.position().vec[1], fr.position().vec[2]);				
				q = fr.orientation().get();
				s = new Vec(fr.magnitude().vec[0], fr.magnitude().vec[1], fr.magnitude().vec[2]);
			}
		}
		
		protected AbstractKeyFrame(AbstractKeyFrame otherKF) {
			this.tm = otherKF.tm;
			this.frm = otherKF.frm;
			if (this.frm != null) {
				this.p = this.frame().position();
				this.q = this.frame().orientation();
				this.s = this.frame().magnitude();
			} else {
				//p = new Vector3D( otherKF.p.x, otherKF.p.y, otherKF.p.z );
				this.p = new Vec(otherKF.position().vec[0], otherKF.position().vec[1], otherKF.position().vec[2]);				
				this.q = otherKF.orientation().get();
				this.s = new Vec(otherKF.magnitude().vec[0], otherKF.magnitude().vec[1], otherKF.magnitude().vec[2]);
			}
		}		

		void updateValues() {
			if (frame() != null) {
				p = frame().position();
				q = frame().orientation();
				s = frame().magnitude();
			}
		}

		Vec position() {
			return p;
		}

		Orientable orientation() {
			return q;
		}
		
		Vec magnitude() {
			return s;
		}

		float time() {
			return tm;
		}

		GeomFrame frame() {
			return frm;
		}
		
		Vec tgP() {
			return tgPVec;
		}
		
		// /**
	  //Option 2 (interpolate scaling using a spline)
		Vec tgS() {
			return tgSVec;
		}
		// */
		
		abstract void computeTangent(AbstractKeyFrame prev, AbstractKeyFrame next);
	}
	
	private class KeyFrame3D extends AbstractKeyFrame {
		protected Quat tgQuat;
		
		KeyFrame3D(GeomFrame fr, float t, boolean setRef) {
			super(fr, t, setRef);
		}
		
		protected KeyFrame3D(KeyFrame3D other) {
			super(other);
		}
		
		public KeyFrame3D get() {
			return new KeyFrame3D(this);
		}	

		Quat tgQ() {
			return tgQuat;
		}
		
		void flipOrientationIfNeeded(Quat prev) {
			if (Quat.dotProduct(prev, (Quat)q) < 0.0f)
				q.negate();
		}
		
		@Override
		void computeTangent(AbstractKeyFrame prev, AbstractKeyFrame next) {
			tgPVec = Vec.mult(Vec.sub(next.position(), prev.position()), 0.5f);
			tgQuat = Quat.squadTangent((Quat)prev.orientation(), (Quat)q, (Quat)next.orientation());
			////Option 2 (interpolate scaling using a spline)
			tgSVec = Vec.mult(Vec.sub(next.magnitude(), prev.magnitude()), 0.5f);			
		}
	}
	
	private class KeyFrame2D extends AbstractKeyFrame {		
		KeyFrame2D(GeomFrame fr, float t, boolean setRef) {
			super(fr, t, setRef);			
		}
		
		protected KeyFrame2D(KeyFrame2D other) {
			super(other);
		}
		
		public KeyFrame2D get() {
			return new KeyFrame2D(this);
		}		
		
		@Override
		void computeTangent(AbstractKeyFrame prev, AbstractKeyFrame next) {
			tgPVec = Vec.mult(Vec.sub(next.position(), prev.position()), 0.5f);			
		  //Option 2 (interpolate scaling using a spline)
			tgSVec = Vec.mult(Vec.sub(next.magnitude(), prev.magnitude()), 0.5f);			
		}
	}

	private List<AbstractKeyFrame> keyFr;
	private ListIterator<AbstractKeyFrame> currentFrame0;
	private ListIterator<AbstractKeyFrame> currentFrame1;
	private ListIterator<AbstractKeyFrame> currentFrame2;
	private ListIterator<AbstractKeyFrame> currentFrame3;
	private List<GeomFrame> path;
	// A s s o c i a t e d f r a m e
	private GeomFrame fr;
	private GeomFrame myFrame;// needed for drawPath

	// R h y t h m
	private AbstractTimerJob interpolationTimerJob;
	private int period;
	private float interpolationTm;
	private float interpolationSpd;
	private boolean interpolationStrt;

	// M i s c
	private boolean lpInterpolation;

	// C a c h e d v a l u e s a n d f l a g s
	private boolean pathIsValid;
	private boolean valuesAreValid;
	private boolean currentFrmValid;
	private boolean splineCacheIsValid;
	private Vec pv1, pv2;
  //Option 2 (interpolate scaling using a spline)
	private Vec sv1, sv2;

  //S C E N E
  public AbstractScene scene;
  
  /**
   * Convenience constructor that simply calls {@code this(scn, new Frame())}.
   * <p>
   * Creates an anonymous {@link #frame()} to be interpolated by this
   * KeyFrameInterpolator.
   * 
   * @see #KeyFrameInterpolator(AbstractScene, GeomFrame)
   */
  public KeyFrameInterpolator(AbstractScene scn) {
  	this(scn, new GeomFrame());
  }

	/**
	 * Creates a KeyFrameInterpolator, with {@code frame} as associated
	 * {@link #frame()}. The {@code p3d} object will be used if
	 * {@link #drawPath(int, int, float)} is called.
	 * <p>
	 * The {@link #frame()} can be set or changed using {@link #setFrame(GeomFrame)}.
	 * <p>
	 * {@link #interpolationTime()}, {@link #interpolationSpeed()} and
	 * {@link #interpolationPeriod()} are set to their default values.
	 */
	public KeyFrameInterpolator(AbstractScene scn, GeomFrame frame) {
		scene = scn;
		myFrame = new GeomFrame(scene.is3D());
		keyFr = new ArrayList<AbstractKeyFrame>();
		path = new ArrayList<GeomFrame>();
		fr = null;
		period = 40;
		interpolationTm = 0.0f;
		interpolationSpd = 1.0f;
		interpolationStrt = false;
		lpInterpolation = false;
		pathIsValid = false;
		valuesAreValid = true;
		currentFrmValid = false;
		setFrame(frame);

		currentFrame0 = keyFr.listIterator();
		currentFrame1 = keyFr.listIterator();
		currentFrame2 = keyFr.listIterator();
		currentFrame3 = keyFr.listIterator();
		
		interpolationTimerJob = new AbstractTimerJob() {
			public void execute() {
				update();
			}
		};		
		scene.registerJob(interpolationTimerJob);
	}	
	
	protected KeyFrameInterpolator(KeyFrameInterpolator otherKFI) {
		this.scene = otherKFI.scene;
		this.myFrame = otherKFI.myFrame.get();		
		
		this.path = new ArrayList<GeomFrame>();
		ListIterator<GeomFrame> frameIt = otherKFI.path.listIterator();
		while (frameIt.hasNext()) {
			this.path.add(frameIt.next().get());
		}		
		
		if(otherKFI.fr != null)
			this.fr = otherKFI.fr.get();
		else
			this.fr = null;		
		
		this.period = otherKFI.period;
		this.interpolationTm = otherKFI.interpolationTm;
		this.interpolationSpd = otherKFI.interpolationSpd;
		this.interpolationStrt = otherKFI.interpolationStrt;
		this.lpInterpolation = otherKFI.lpInterpolation;
		this.pathIsValid = otherKFI.pathIsValid;
		this.valuesAreValid = otherKFI.valuesAreValid;
		this.currentFrmValid = otherKFI.currentFrmValid;		
		
		this.keyFr = new ArrayList<AbstractKeyFrame>();		
		ListIterator<AbstractKeyFrame> it = otherKFI.keyFr.listIterator();
		while (it.hasNext()) {
			this.keyFr.add((AbstractKeyFrame) it.next().get());
		}		
		
		this.currentFrame0 = keyFr.listIterator(otherKFI.currentFrame0.nextIndex());
		this.currentFrame1 = keyFr.listIterator(otherKFI.currentFrame1.nextIndex());
		this.currentFrame2 = keyFr.listIterator(otherKFI.currentFrame2.nextIndex());
		this.currentFrame3 = keyFr.listIterator(otherKFI.currentFrame3.nextIndex());
		
		this.interpolationTimerJob = new AbstractTimerJob() {
			public void execute() {
				update();
			}
		};		
		scene.registerJob(interpolationTimerJob);		
	}
	
	public KeyFrameInterpolator get() {
		return new KeyFrameInterpolator(this);
	}

	/**
	 * Sets the {@link #frame()} associated to the KeyFrameInterpolator.
	 */
	public void setFrame(GeomFrame f) {
		fr = f;
	}

	/**
	 * Returns the associated Frame that is interpolated by the
	 * KeyFrameInterpolator.
	 * <p>
	 * When {@link #interpolationIsStarted()}, this Frame's position and
	 * orientation will regularly be updated by a timer, so that they follow the
	 * KeyFrameInterpolator path.
	 * <p>
	 * Set using {@link #setFrame(GeomFrame)} or with the KeyFrameInterpolator
	 * constructor.
	 */
	public GeomFrame frame() {
		return fr;
	}

	/**
	 * Returns the number of keyFrames used by the interpolation. Use
	 * {@link #addKeyFrame(GeomFrame)} to add new keyFrames.
	 */
	public int numberOfKeyFrames() {
		return keyFr.size();
	}

	/**
	 * Returns the current interpolation time (in seconds) along the
	 * KeyFrameInterpolator path.
	 * <p>
	 * This time is regularly updated when {@link #interpolationIsStarted()}. Can
	 * be set directly with {@link #setInterpolationTime(float)} or
	 * {@link #interpolateAtTime(float)}.
	 */
	public float interpolationTime() {
		return interpolationTm;
	}

	/**
	 * Returns the current interpolation speed.
	 * <p>
	 * Default value is 1.0f, which means {@link #keyFrameTime(int)} will be
	 * matched during the interpolation (provided that your main loop is fast
	 * enough).
	 * <p>
	 * A negative value will result in a reverse interpolation of the keyFrames.
	 * 
	 * @see #interpolationPeriod()
	 */
	public float interpolationSpeed() {
		return interpolationSpd;
	}

	/**
	 * Returns the current interpolation period, expressed in milliseconds. The
	 * update of the {@link #frame()} state will be done by a timer at this period
	 * when {@link #interpolationIsStarted()}.
	 * <p>
	 * This period (multiplied by {@link #interpolationSpeed()}) is added to the
	 * {@link #interpolationTime()} at each update, and the {@link #frame()} state
	 * is modified accordingly (see {@link #interpolateAtTime(float)}). Default
	 * value is 40 milliseconds.
	 * 
	 * @see #setInterpolationPeriod(int)
	 */
	public int interpolationPeriod() {
		return period;
	}

	/**
	 * Returns {@code true} when the interpolation is played in an infinite loop.
	 * <p>
	 * When {@code false} (default), the interpolation stops when
	 * {@link #interpolationTime()} reaches {@link #firstTime()} (with negative
	 * {@link #interpolationSpeed()}) or {@link #lastTime()}.
	 * <p>
	 * {@link #interpolationTime()} is otherwise reset to {@link #firstTime()} (+
	 * {@link #interpolationTime()} - {@link #lastTime()}) (and inversely for
	 * negative {@link #interpolationSpeed()}) and interpolation continues.
	 */
	public boolean loopInterpolation() {
		return lpInterpolation;
	}

	/**
	 * Sets the {@link #interpolationTime()}.
	 * <p>
	 * <b>Attention:</b> The {@link #frame()} state is not affected by this
	 * method. Use this function to define the starting time of a future
	 * interpolation (see {@link #startInterpolation()}). Use
	 * {@link #interpolateAtTime(float)} to actually interpolate at a given time.
	 */
	public void setInterpolationTime(float time) {
		interpolationTm = time;
	};

	/**
	 * Sets the {@link #interpolationSpeed()}. Negative or null values are
	 * allowed.
	 */
	public void setInterpolationSpeed(float speed) {
		interpolationSpd = speed;
	}

	/**
	 * Sets the {@link #interpolationPeriod()}. Should positive.
	 */
	public void setInterpolationPeriod(int myPeriod) {
		if(myPeriod > 0)
			period = myPeriod;
	}

	/**
	 * Convenience function that simply calls  {@code setLoopInterpolation(true)}. 
	 */
	public void setLoopInterpolation() {
		setLoopInterpolation(true);
	}

	/**
	 * Sets the {@link #loopInterpolation()} value.
	 */
	public void setLoopInterpolation(boolean loop) {
		lpInterpolation = loop;
	}

	/**
	 * Returns {@code true} when the interpolation is being performed. Use
	 * {@link #startInterpolation()}, {@link #stopInterpolation()} or
	 * {@link #toggleInterpolation()} to modify this state.
	 */
	public boolean interpolationIsStarted() {
		return interpolationStrt;
	}

	/**
	 * Calls {@link #startInterpolation()} or {@link #stopInterpolation()},
	 * depending on {@link #interpolationIsStarted()}.
	 */
	public void toggleInterpolation() {
		if (interpolationIsStarted())
			stopInterpolation();
		else
			startInterpolation();
	}

	/**
	 * Updates {@link #frame()} state according to current
	 * {@link #interpolationTime()}. Then adds {@link #interpolationPeriod()}*
	 * {@link #interpolationSpeed()} to {@link #interpolationTime()}.
	 * <p>
	 * This internal method is called by a timer when
	 * {@link #interpolationIsStarted()}. It can be used for debugging purpose.
	 * {@link #stopInterpolation()} is called when {@link #interpolationTime()}
	 * reaches {@link #firstTime()} or {@link #lastTime()}, unless
	 * {@link #loopInterpolation()} is {@code true}.
	 */
	protected void update() {
		interpolateAtTime(interpolationTime());

		interpolationTm += interpolationSpeed() * interpolationPeriod() / 1000.0f;

		if (interpolationTime() > keyFr.get(keyFr.size() - 1).time()) {
			if (loopInterpolation())
				setInterpolationTime(keyFr.get(0).time() + interpolationTm
						- keyFr.get(keyFr.size() - 1).time());
			else {
				// Make sure last KeyFrame is reached and displayed
				interpolateAtTime(keyFr.get(keyFr.size() - 1).time());
				stopInterpolation();
			}
			// emit endReached();
		} else if (interpolationTime() < keyFr.get(0).time()) {
			if (loopInterpolation())
				setInterpolationTime(keyFr.get(keyFr.size() - 1).time()
						- keyFr.get(0).time() + interpolationTm);
			else {
				// Make sure first KeyFrame is reached and displayed
				interpolateAtTime(keyFr.get(0).time());
				stopInterpolation();
			}
			// emit endReached();
		}
	}

	public void invalidateValues() {
		valuesAreValid = false;
		pathIsValid = false;
		splineCacheIsValid = false;
	}

	/**
	 * Convenience function that simply calls {@code startInterpolation(-1)}.
	 * 
	 * @see #startInterpolation(int)
	 */
	public void startInterpolation() {
		startInterpolation(-1);
	}

	/**
	 * Starts the interpolation process.
	 * <p>
	 * A timer is started with an {@link #interpolationPeriod()} period that
	 * updates the {@link #frame()}'s position and orientation.
	 * {@link #interpolationIsStarted()} will return {@code true} until
	 * {@link #stopInterpolation()} or {@link #toggleInterpolation()} is called.
	 * <p>
	 * If {@code period} is positive, it is set as the new
	 * {@link #interpolationPeriod()}. The previous {@link #interpolationPeriod()}
	 * is used otherwise (default).
	 * <p>
	 * If {@link #interpolationTime()} is larger than {@link #lastTime()},
	 * {@link #interpolationTime()} is reset to {@link #firstTime()} before
	 * interpolation starts (and inversely for negative
	 * {@link #interpolationSpeed()}.
	 * <p>
	 * Use {@link #setInterpolationTime(float)} before calling this method to
	 * change the starting {@link #interpolationTime()}.
	 * <p>
	 * <b>Attention:</b> The keyFrames must be defined (see
	 * {@link #addKeyFrame(GeomFrame, float)}) before you startInterpolation(), or
	 * else the interpolation will naturally immediately stop.
	 */
	public void startInterpolation(int myPeriod) {
		if (myPeriod >= 0)
			setInterpolationPeriod(myPeriod);

		if (!keyFr.isEmpty()) {
			if ((interpolationSpeed() > 0.0)
					&& (interpolationTime() >= keyFr.get(keyFr.size() - 1).time()))
				setInterpolationTime(keyFr.get(0).time());
			if ((interpolationSpeed() < 0.0)
					&& (interpolationTime() <= keyFr.get(0).time()))
				setInterpolationTime(keyFr.get(keyFr.size() - 1).time());
			interpolationTimerJob.run(interpolationPeriod());
			interpolationStrt = true;
			update();
		}
	}

	/**
	 * Stops an interpolation started with {@link #startInterpolation()}. See
	 * {@link #interpolationIsStarted()} and {@link #toggleInterpolation()}.
	 */
	public void stopInterpolation() {
		interpolationTimerJob.stop();
		interpolationStrt = false;
	}

	/**
	 * Stops the interpolation and resets {@link #interpolationTime()} to the
	 * {@link #firstTime()}.
	 * <p>
	 * If desired, call {@link #interpolateAtTime(float)} after this method to
	 * actually move the {@link #frame()} to {@link #firstTime()}.
	 */
	public void resetInterpolation() {
		stopInterpolation();
		setInterpolationTime(firstTime());
	}

	/**
	 * Convenience function that simply calls {@code addKeyFrame(frame, false)}.
	 * 
	 * @see #addKeyFrame(GeomFrame, boolean)
	 */
	public void addKeyFrame(GeomFrame frame) {
		addKeyFrame(frame, true);
	}

	/**
	 * Appends a new keyFrame to the path.
	 * <p>
	 * Same as {@link #addKeyFrame(GeomFrame, float, boolean)}, except that the
	 * {@link #keyFrameTime(int)} is set to the previous
	 * {@link #keyFrameTime(int)} plus one second (or 0.0 if there is no previous
	 * keyFrame).
	 */
	public void addKeyFrame(GeomFrame frame, boolean setRef) {
		float time;

		if (keyFr.isEmpty())
			time = 0.0f;
		else
			time = keyFr.get(keyFr.size() - 1).time() + 1.0f;

		addKeyFrame(frame, time, setRef);
	}

	/**
	 * Convenience function that simply calls {@code addKeyFrame(frame, time,
	 * false)}.
	 * 
	 * @see #addKeyFrame(GeomFrame, float, boolean)
	 */
	public void addKeyFrame(GeomFrame frame, float time) {
		addKeyFrame(frame, time, true);
	}

	/**
	 * Appends a new keyFrame to the path, with its associated {@code time} (in
	 * seconds).
	 * <p>
	 * When {@code setRef} is {@code false} the keyFrame is added by value, meaning
	 * that the path will use the current {@code frame} state.
	 * <p>
	 * When {@code setRef} is {@code true} the keyFrame is given as a reference to
	 * a Frame, which will be connected to the KeyFrameInterpolator: when {@code
	 * frame} is modified, the KeyFrameInterpolator path is updated accordingly.
	 * This allows for dynamic paths, where keyFrame can be edited, even during
	 * the interpolation. {@code null} frame references are silently ignored. The
	 * {@link #keyFrameTime(int)} has to be monotonously increasing over
	 * keyFrames.
	 */
	public void addKeyFrame(GeomFrame frame, float time, boolean setRef) {
		if (frame == null)
			return;

		if (keyFr.isEmpty())
			interpolationTm = time;

		if ((!keyFr.isEmpty()) && (keyFr.get(keyFr.size() - 1).time() > time))
			System.out.println("Error in KeyFrameInterpolator.addKeyFrame: time is not monotone");
		else {
			if(scene.is3D())
				keyFr.add(new KeyFrame3D(frame, time, setRef));
			else
				keyFr.add(new KeyFrame2D(frame, time, setRef));
		}

		// connect(frame, SIGNAL(modified()), SLOT(invalidateValues()));
		if (setRef) // only when setting reference
			frame.addListener(this);

		valuesAreValid = false;
		pathIsValid = false;
		currentFrmValid = false;
		resetInterpolation();
	}
	
	/**
	 * Remove KeyFrame according to {@code index} in the list and {@link #stopInterpolation()}
	 * if {@link #interpolationIsStarted()}. If {@code index < 0 || index >= keyFr.size()}
	 * the call is silently ignored. 
	 */
	//TODO testing
	public void removeKeyFrame(int index) {
		if (index < 0 || index >= keyFr.size())
			return;
		valuesAreValid = false;
		pathIsValid = false;
		currentFrmValid = false;
		if( interpolationIsStarted() )
			stopInterpolation();
		AbstractKeyFrame kf = keyFr.remove(index);
		if (kf.frm  instanceof InteractiveFrame)
			scene.terseHandler().removeFromDeviceGrabberPool( (InteractiveFrame) kf.frm );
		  //before:
			//if (((InteractiveFrame) kf.frm).isInDeviceGrabberPool())
				//((InteractiveFrame) kf.frm).removeFromDeviceGrabberPool();
		setInterpolationTime(firstTime());
	}

	/**
	 * Removes all keyFrames from the path. Calls
	 * {@link #removeFramesFromMouseGrabberPool()}. The
	 * {@link #numberOfKeyFrames()} is set to 0.
	 * 
	 * @see #removeFramesFromMouseGrabberPool()
	 */
	public void deletePath() {
		stopInterpolation();
		removeFramesFromMouseGrabberPool();
		keyFr.clear();
		pathIsValid = false;
		valuesAreValid = false;
		currentFrmValid = false;
	}

	/**
	 * Removes all the Frames from the mouse grabber pool (if they were provided
	 * as references).
	 * 
	 * @see #addFramesToMouseGrabberPool()
	 */
	//TODO testing
	protected void removeFramesFromMouseGrabberPool() {
		for (int i = 0; i < keyFr.size(); ++i) {
			if (keyFr.get(i).frame() != null)
				scene.terseHandler().removeFromDeviceGrabberPool((InteractiveFrame) keyFr.get(i).frame());
				//before:
				//if (((InteractiveFrame) keyFr.get(i).frame()).isInDeviceGrabberPool())
					//((InteractiveFrame) keyFr.get(i).frame()).removeFromDeviceGrabberPool();
		}
	}

	/**
	 * Re-adds all the Frames to the mouse grabber pool (if they were provided as
	 * references).
	 * 
	 * @see #removeFramesFromMouseGrabberPool()
	 */
  //TODO testing
	protected void addFramesToMouseGrabberPool() {
		for (int i = 0; i < keyFr.size(); ++i) {
			if (keyFr.get(i).frame() != null)
				scene.terseHandler().addInDeviceGrabberPool((InteractiveFrame) keyFr.get(i).frame());
			  //before:
				//if (!((InteractiveFrame) keyFr.get(i).frame()).isInDeviceGrabberPool())
					//((InteractiveFrame) keyFr.get(i).frame()).addInDeviceGrabberPool();
		}
	}

	protected void updateModifiedFrameValues() {
		AbstractKeyFrame kf;
		
		if(scene.is3D()) {
			Quat prevQ = (Quat)keyFr.get(0).orientation();
			for (int i = 0; i < keyFr.size(); ++i) {
				kf = keyFr.get(i);
				kf.updateValues();
				((KeyFrame3D)kf).flipOrientationIfNeeded(prevQ);
				prevQ = (Quat)kf.orientation();
			}
		}
		else {
			for (int i = 0; i < keyFr.size(); ++i) {
				kf = keyFr.get(i);
				kf.updateValues();
			}
		}

		AbstractKeyFrame prev = keyFr.get(0);
		kf = keyFr.get(0);

		int index = 1;
		while (kf != null) {
			AbstractKeyFrame next = (index < keyFr.size()) ? keyFr.get(index) : null;
			index++;
			if (next != null)
				kf.computeTangent(prev, next);
			else
				kf.computeTangent(prev, kf);
			prev = kf;
			kf = next;
		}
		valuesAreValid = true;
	}

	/**
	 * Convenience function that simply calls {@code drawPath(1, 6, 100)}
	 */
	public void drawPath() {
		drawPath(1, 6, 100);
	}

	/**
	 * Convenience function that simply calls {@code drawPath(1, 6, scale)}
	 */
	public void drawPath(float scale) {
		drawPath(1, 6, scale);
	}

	/**
	 * Convenience function that simply calls {@code drawPath(mask, nbFrames,
	 * 100)}
	 */
	public void drawPath(int mask, int nbFrames) {
		drawPath(mask, nbFrames, 100);
	}

	/**
	 * Draws the path used to interpolate the {@link #frame()}.
	 * <p>
	 * {@code mask} controls what is drawn: If ( (mask & 1) != 0 ), the position
	 * path is drawn. If ( (mask & 2) != 0 ), a camera representation is regularly
	 * drawn and if ( (mask & 4) != 0 ), an oriented axis is regularly drawn.
	 * Examples:
	 * <p>
	 * {@code drawPath(); // Simply draws the interpolation path} <br>
	 * {@code drawPath(3); // Draws path and cameras} <br>
	 * {@code drawPath(5); // Draws path and axis} <br>
	 * <p>
	 * In the case where camera or axis is drawn, {@code nbFrames} controls the
	 * number of objects (axis or camera) drawn between two successive keyFrames.
	 * When {@code nbFrames = 1}, only the path KeyFrames are drawn. {@code
	 * nbFrames = 2} also draws the intermediate orientation, etc. The maximum
	 * value is 30. {@code nbFrames} should divide 30 so that an object is drawn
	 * for each KeyFrame. Default value is 6.
	 * <p>
	 * {@code scale} controls the scaling of the camera and axis drawing. A value
	 * of {@link remixlab.dandelion.core.AbstractScene#radius()} should give good results.
	 */
	public void drawPath(int mask, int nbFrames, float scale) {
		int nbSteps = 30;
		if (!pathIsValid) {
			path.clear();

			if (keyFr.isEmpty())
				return;

			if (!valuesAreValid)
				updateModifiedFrameValues();

			if (keyFr.get(0) == keyFr.get(keyFr.size() - 1))
				path.add(new GeomFrame(keyFr.get(0).orientation(), keyFr.get(0).position(), keyFr.get(0).magnitude()));
			else {
				AbstractKeyFrame[] kf = new AbstractKeyFrame[4];
				kf[0] = keyFr.get(0);
				kf[1] = kf[0];

				int index = 1;
				kf[2] = (index < keyFr.size()) ? keyFr.get(index) : null;
				index++;
				kf[3] = (index < keyFr.size()) ? keyFr.get(index) : null;

				while (kf[2] != null) {
					Vec pdiff = Vec.sub(kf[2].position(), kf[1].position());
					Vec pvec1 = Vec.add(Vec.mult(pdiff, 3.0f), Vec.mult(kf[1].tgP(), (-2.0f)));
					pvec1 = Vec.sub(pvec1, kf[2].tgP());
					Vec pvec2 = Vec.add(Vec.mult(pdiff, (-2.0f)), kf[1].tgP());
					pvec2 = Vec.add(pvec2, kf[2].tgP());
					
					// /**
					//Option 2 (interpolate scaling using a spline)
					Vec sdiff = Vec.sub(kf[2].magnitude(), kf[1].magnitude());
					Vec svec1 = Vec.add(Vec.mult(sdiff, 3.0f), Vec.mult(kf[1].tgS(), (-2.0f)));
					svec1 = Vec.sub(svec1, kf[2].tgS());
					Vec svec2 = Vec.add(Vec.mult(sdiff, (-2.0f)), kf[1].tgS());
					svec2 = Vec.add(svec2, kf[2].tgS());
					// */
					
					for (int step = 0; step < nbSteps; ++step) {
						float alpha = step / (float) nbSteps;
						myFrame.setPosition(Vec.add(kf[1].position(), Vec.mult(Vec.add(kf[1].tgP(), Vec.mult(Vec.add(pvec1, Vec.mult(pvec2, alpha)), alpha)), alpha)));
					  if( scene.is3D()) {
						  myFrame.setOrientation(Quat.squad((Quat)kf[1].orientation(), ((KeyFrame3D)kf[1]).tgQ(), ((KeyFrame3D)kf[2]).tgQ(), (Quat)kf[2].orientation(), alpha));
					  }
					  else {
					    //linear interpolation
							float start = kf[1].orientation().angle();
							float stop = kf[2].orientation().angle();
							myFrame.setOrientation(new Rotation(start + (stop-start) * alpha) );
					  }
					  //myFrame.setMagnitude(magnitudeLerp(kf[1], kf[2], alpha));
					  //Option 2 (interpolate scaling using a spline)
					  myFrame.setMagnitude(Vec.add(kf[1].magnitude(), Vec.mult(Vec.add(kf[1].tgS(), Vec.mult(Vec.add(svec1, Vec.mult(svec2, alpha)), alpha)), alpha)));					  
						path.add(myFrame.get());
					}

					// Shift
					kf[0] = kf[1];
					kf[1] = kf[2];
					kf[2] = kf[3];

					index++;
					kf[3] = (index < keyFr.size()) ? keyFr.get(index) : null;
				}
				// Add last KeyFrame
				path.add(new GeomFrame(kf[1].orientation(), kf[1].position(), kf[1].magnitude()));
			}
			pathIsValid = true;
		}
		
		scene.drawPath(path, mask, nbFrames, nbSteps, scale);
	}

	/**
	 * Returns the Frame associated with the keyFrame at index {@code index}.
	 * <p>
	 * See also {@link #keyFrameTime(int)}. {@code index} has to be in the range
	 * 0..{@link #numberOfKeyFrames()}-1.
	 * <p>
	 * <b>Note:</b> If this keyFrame was defined using a reference to a Frame (see
	 * {@link #addKeyFrame(GeomFrame, float, boolean)} the current referenced Frame
	 * state is returned.
	 */
	public GeomFrame keyFrame(int index) {
		AbstractKeyFrame kf = keyFr.get(index);
		return new GeomFrame(kf.orientation(), kf.position(), kf.magnitude());
	}

	/**
	 * Returns the time corresponding to the {@code index} keyFrame. index has to
	 * be in the range 0..{@link #numberOfKeyFrames()}-1.
	 * 
	 * @see #keyFrame(int)
	 */
	public float keyFrameTime(int index) {
		return keyFr.get(index).time();
	}

	/**
	 * Returns the duration of the KeyFrameInterpolator path, expressed in
	 * seconds.
	 * <p>
	 * Simply corresponds to {@link #lastTime()} - {@link #firstTime()}. Returns
	 * 0.0 if the path has less than 2 keyFrames.
	 * 
	 * @see #keyFrameTime(int)
	 */
	public float duration() {
		return lastTime() - firstTime();
	}

	/**
	 * Returns the time corresponding to the first keyFrame, expressed in seconds.
	 * <p>
	 * Returns 0.0 if the path is empty.
	 * 
	 * @see #lastTime()
	 * @see #duration()
	 * @see #keyFrameTime(int)
	 */
	public float firstTime() {
		if (keyFr.isEmpty())
			return 0.0f;
		else
			return keyFr.get(0).time();
	}

	/**
	 * Returns the time corresponding to the last keyFrame, expressed in seconds.
	 * <p>
	 * 
	 * @see #firstTime()
	 * @see #duration()
	 * @see #keyFrameTime(int)
	 */
	public float lastTime() {
		if (keyFr.isEmpty())
			return 0.0f;
		else
			return keyFr.get(keyFr.size() - 1).time();
	}

	protected void updateCurrentKeyFrameForTime(float time) {
		// Assertion: times are sorted in monotone order.
		// Assertion: keyFrame_ is not empty

		// TODO: Special case for loops when closed path is implemented !!
		if (!currentFrmValid)
			// Recompute everything from scratch
			currentFrame1 = keyFr.listIterator();

		// currentFrame_[1]->peekNext() <---> keyFr.get(currentFrame1.nextIndex());
		while (keyFr.get(currentFrame1.nextIndex()).time() > time) {
			currentFrmValid = false;
			if (!currentFrame1.hasPrevious())
				break;
			currentFrame1.previous();
		}

		if (!currentFrmValid)
			// *currentFrame_[2] = *currentFrame_[1]; <---> currentFrame2 =
			// keyFr.listIterator( currentFrame1.nextIndex() );
			currentFrame2 = keyFr.listIterator(currentFrame1.nextIndex());

		while (keyFr.get(currentFrame2.nextIndex()).time() < time) {
			currentFrmValid = false;

			if (!currentFrame2.hasNext())
				break;

			currentFrame2.next();
		}

		if (!currentFrmValid) {
			currentFrame1 = keyFr.listIterator(currentFrame2.nextIndex());

			if ((currentFrame1.hasPrevious())
					&& (time < keyFr.get(currentFrame2.nextIndex()).time()))
				currentFrame1.previous();

			currentFrame0 = keyFr.listIterator(currentFrame1.nextIndex());

			if (currentFrame0.hasPrevious())
				currentFrame0.previous();

			currentFrame3 = keyFr.listIterator(currentFrame2.nextIndex());

			if (currentFrame3.hasNext())
				currentFrame3.next();

			currentFrmValid = true;
			splineCacheIsValid = false;
		}
	}

	public void updateSplineCache() {
		Vec deltaP = Vec.sub(keyFr.get(currentFrame2.nextIndex()).position(), keyFr.get(currentFrame1.nextIndex()).position());
		pv1 = Vec.add(Vec.mult(deltaP, 3.0f), Vec.mult(keyFr.get(currentFrame1.nextIndex()).tgP(), (-2.0f)));
		pv1 = Vec.sub(pv1, keyFr.get(currentFrame2.nextIndex()).tgP());
		pv2 = Vec.add(Vec.mult(deltaP, (-2.0f)), keyFr.get(currentFrame1.nextIndex()).tgP());
		pv2 = Vec.add(pv2, keyFr.get(currentFrame2.nextIndex()).tgP());
		
		// /**
		//Option 2 (interpolate scaling using a spline)
		Vec deltaS = Vec.sub(keyFr.get(currentFrame2.nextIndex()).magnitude(), keyFr.get(currentFrame1.nextIndex()).magnitude());
		sv1 = Vec.add(Vec.mult(deltaS, 3.0f), Vec.mult(keyFr.get(currentFrame1.nextIndex()).tgS(), (-2.0f)));
		sv1 = Vec.sub(sv1, keyFr.get(currentFrame2.nextIndex()).tgS());
		sv2 = Vec.add(Vec.mult(deltaS, (-2.0f)), keyFr.get(currentFrame1.nextIndex()).tgS());
		sv2 = Vec.add(sv2, keyFr.get(currentFrame2.nextIndex()).tgS());
		// */
		
		splineCacheIsValid = true;
	}

	/**
	 * Interpolate {@link #frame()} at time {@code time} (expressed in seconds).
	 * {@link #interpolationTime()} is set to {@code time} and {@link #frame()} is
	 * set accordingly.
	 * <p>
	 * If you simply want to change {@link #interpolationTime()} but not the
	 * {@link #frame()} state, use {@link #setInterpolationTime(float)} instead.
	 */
	public void interpolateAtTime(float time) {
		setInterpolationTime(time);

		if ((keyFr.isEmpty()) || (frame() == null))
			return;

		if (!valuesAreValid)
			updateModifiedFrameValues();

		updateCurrentKeyFrameForTime(time);

		if (!splineCacheIsValid)
			updateSplineCache();

		float alpha;
		float dt = keyFr.get(currentFrame2.nextIndex()).time()
				- keyFr.get(currentFrame1.nextIndex()).time();
		if (Util.zero(dt))
			alpha = 0.0f;
		else
			alpha = (time - keyFr.get(currentFrame1.nextIndex()).time()) / dt;		

		// Linear interpolation - debug
		// Vec pos = alpha*(currentFrame2->peekNext()->position()) +
		// (1.0-alpha)*(currentFrame1->peekNext()->position());
		// Vec pos = currentFrame_[1]->peekNext()->position() + alpha *
		// (currentFrame_[1]->peekNext()->tgP() + alpha * (v1+alpha*v2));
		Vec pos = Vec.add(keyFr.get(currentFrame1.nextIndex()).position(),
				                        Vec.mult(Vec.add(keyFr.get(currentFrame1.nextIndex()).tgP(),
						                    Vec.mult(Vec.add(pv1, Vec.mult(pv2, alpha)), alpha)), alpha));
		
		/**
		//Option 1
		Vector3D mag = magnitudeLerp((keyFr.get(currentFrame1.nextIndex())),
                                 (keyFr.get(currentFrame2.nextIndex())),
                                  (alpha));
		// */
		
		// /**
		//Option 2 (interpolate scaling using a spline)
		Vec mag = Vec.add(keyFr.get(currentFrame1.nextIndex()).magnitude(),
                                Vec.mult(Vec.add(keyFr.get(currentFrame1.nextIndex()).tgS(),
                                Vec.mult(Vec.add(sv1, Vec.mult(sv2, alpha)), alpha)), alpha));
    // */		

		
	  //TODO 2d Case pending
		Orientable q;
		if(scene.is3D()) {
		  q = Quat.squad((Quat)keyFr.get(currentFrame1.nextIndex()).orientation(), 
			                    ((KeyFrame3D)keyFr.get(currentFrame1.nextIndex())).tgQ(),
			                    ((KeyFrame3D)keyFr.get(currentFrame2.nextIndex())).tgQ(),
			                     (Quat)keyFr.get(currentFrame2.nextIndex()).orientation(), alpha);
		} else {
			q =  new Rotation( rotationLerp(keyFr.get(currentFrame1.nextIndex()),
					                            keyFr.get(currentFrame2.nextIndex()),
					                            ( alpha)));
		}
		
		frame().setPositionWithConstraint(pos);
		frame().setRotationWithConstraint(q);
		frame().setMagnitudeWithConstraint(mag);
	}
	
	protected float rotationLerp(AbstractKeyFrame kf1, AbstractKeyFrame kf2, float alpha) {
		float start = kf1.orientation().angle();
		float stop = kf2.orientation().angle();
		return lerp(start, stop, alpha);
	}
	
	protected Vec magnitudeLerp(AbstractKeyFrame kf1, AbstractKeyFrame kf2, float alpha) {
		return vectorLerp(kf1.magnitude(), kf2.magnitude(), alpha);
	}
	
	protected Vec vectorLerp(Vec start, Vec stop, float alpha) {
		return new Vec( lerp(start.x(), stop.x(), alpha), lerp(start.y(), stop.y(), alpha), lerp(start.z(), stop.z(), alpha) );
	}
	
	protected float lerp(float start, float stop, float alpha) {
		return start + (stop-start) * alpha;
	}
}
