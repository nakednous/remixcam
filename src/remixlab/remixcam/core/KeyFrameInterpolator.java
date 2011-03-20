/**
 *                     ProScene (version 1.0.1)      
 *    Copyright (c) 2010-2011 by National University of Colombia
 *                 @author Jean Pierre Charalambos      
 *           http://www.disi.unal.edu.co/grupos/remixlab/
 *                           
 * This java package provides classes to ease the creation of interactive 3D
 * scenes in Processing.
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

package remixlab.remixcam.core;

import java.util.*;

import remixlab.remixcam.geom.*;

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
 * KeyFrameInterpolator}} (see {@link #addKeyFrame(GLFrame)} methods):<br>
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
 * {@link #addKeyFrame(GLFrame)} methods). In this case, the path will
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
 * {@link remixlab.remixcam.core.Camera#keyFrameInterpolator(int)}, that can be used
 * to drive the Camera along a path.
 * <p>
 * <b>Attention:</b> If a Constraint is attached to the {@link #frame()} (see
 * {@link remixlab.remixcam.core.GLFrame#constraint()}), it should be deactivated
 * before {@link #interpolationIsStarted()}, otherwise the interpolated motion
 * (computed as if there was no constraint) will probably be erroneous.
 */
public class KeyFrameInterpolator implements Cloneable {
	public class KeyFrame implements Cloneable {
		private Vector3D p, tgPVec;
		private Quaternion q, tgQuat;
		private float tm;
		private GLFrame frm;

		KeyFrame(GLFrame fr, float t, boolean setRef) {
			tm = t;
			if (setRef) {
				frm = fr;
				updateValues();
			} else {
				frm = null;
				p = new Vector3D(fr.position().x, fr.position().y, fr.position().z);
				q = new Quaternion(fr.orientation());
			}
		}

		public KeyFrame clone() {
			try {
				KeyFrame clonedKeyFrame = (KeyFrame) super.clone();
				if (frm != null)
					clonedKeyFrame.frm = frm.clone();
				else
					clonedKeyFrame.frm = null;
				clonedKeyFrame.p = new Vector3D(position().x, position().y, position().z);
				clonedKeyFrame.tgPVec = new Vector3D(tgP().x, tgP().y, tgP().z);
				clonedKeyFrame.q = new Quaternion(orientation());
				clonedKeyFrame.tgQuat = new Quaternion(tgQ());
				return clonedKeyFrame;
			} catch (CloneNotSupportedException e) {
				throw new Error("Something went wrong when cloning the KeyFrame");
			}
		}

		public void updateValues() {
			if (frame() != null) {
				p = frame().position();
				q = frame().orientation();
			}
		}

		public Vector3D position() {
			return p;
		}

		public Quaternion orientation() {
			return q;
		}

		public Vector3D tgP() {
			return tgPVec;
		}

		public Quaternion tgQ() {
			return tgQuat;
		}

		public float time() {
			return tm;
		}

		GLFrame frame() {
			return frm;
		}

		void flipOrientationIfNeeded(Quaternion prev) {
			if (Quaternion.dotProduct(prev, q) < 0.0f)
				q.negate();
		}

		void computeTangent(KeyFrame prev, KeyFrame next) {
			tgPVec = Vector3D.mult(Vector3D.sub(next.position(), prev.position()), 0.5f);
			tgQuat = Quaternion.squadTangent(prev.orientation(), q, next.orientation());
		}
	}

	private List<KeyFrame> keyFr;
	private ListIterator<KeyFrame> currentFrame0;
	private ListIterator<KeyFrame> currentFrame1;
	private ListIterator<KeyFrame> currentFrame2;
	private ListIterator<KeyFrame> currentFrame3;
	// A s s o c i a t e d f r a m e
	private GLFrame fr;
	
	// D r a w i n g
	private List<GLFrame> path = new ArrayList<GLFrame>();
	private GLFrame myFrame;

	// R h y t h m
	private Timer timer;
  //private TimerTask timerTask;
	private int period;
	private float interpolationTm;
	private float interpolationSpd;
	private boolean interpolationStrt;

	// M i s c
	private boolean lpInterpolation;

	// C a c h e d v a l u e s a n d f l a g s
	private boolean valuesAreValid;
	private boolean currentFrmValid;
	private boolean splineCacheIsValid;
	private Vector3D v1, v2;
	private boolean pathIsValid;

	/**
	 * Creates a KeyFrameInterpolator, with {@code frame} as associated
	 * {@link #frame()}. The {@code p3d} object will be used if
	 * {@link #drawPath(int, int, float)} is called.
	 * <p>
	 * The {@link #frame()} can be set or changed using {@link #setFrame(GLFrame)}.
	 * <p>
	 * {@link #interpolationTime()}, {@link #interpolationSpeed()} and
	 * {@link #interpolationPeriod()} are set to their default values.
	 * 
	 * @see #KeyFrameInterpolator(PApplet)
	 */
	public KeyFrameInterpolator(GLFrame frame) {
		myFrame = new GLFrame();
		path = new ArrayList<GLFrame>();
		keyFr = new ArrayList<KeyFrame>();
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
		
		timer = new Timer();
	}

	/**
	 * Implementation of the clone method.
	 * <p>
	 * The method performs a deep copy of the all the KeyFrameInterpolator {@code
	 * KeyFrames}.
	 */
	public KeyFrameInterpolator clone() {
		try {
			KeyFrameInterpolator clonedKfi = (KeyFrameInterpolator) super.clone();
			clonedKfi.keyFr = new ArrayList<KeyFrame>();
			ListIterator<KeyFrame> it = keyFr.listIterator();
			while (it.hasNext()) {
				clonedKfi.keyFr.add(it.next().clone());
			}
			clonedKfi.currentFrame0 = keyFr.listIterator(currentFrame0.nextIndex());
			clonedKfi.currentFrame1 = keyFr.listIterator(currentFrame1.nextIndex());
			clonedKfi.currentFrame2 = keyFr.listIterator(currentFrame2.nextIndex());
			clonedKfi.currentFrame3 = keyFr.listIterator(currentFrame3.nextIndex());
			//next line added when migrating to java.util.Timer
			clonedKfi.timer = new Timer();
			return clonedKfi;
		} catch (CloneNotSupportedException e) {
			throw new Error(
					"Something went wrong when cloning the KeyFrameInterpolator");
		}
	}
	
	/**
	 * Connection: drawing utils
	 */
	public List<KeyFrame> keyFrame() {
		return keyFr;
	}
	
	/**
	 * Connection: drawing utils
	 */
	public boolean pathIsValid() {
		return pathIsValid;
	}
	
	/**
	 * Connection: drawing utils
	 */
	public void validatePath() {
		pathIsValid = true;
	}
	
	/**
	 * Connection: drawing utils
	 */
	public boolean valuesAreValid() {
		return valuesAreValid;
	}
	
	/**
	 * Connection: drawing utils
	 */
	public GLFrame drawingFrame() {
		return myFrame;
	}
	
	public ArrayList<GLFrame> drawingPath() {
		return (ArrayList<GLFrame>)path;
	}

	/**
	 * Sets the {@link #frame()} associated to the KeyFrameInterpolator.
	 */
	public void setFrame(GLFrame f) {
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
	 * Set using {@link #setFrame(GLFrame)} or with the KeyFrameInterpolator
	 * constructor.
	 */
	public GLFrame frame() {
		return fr;
	}

	/**
	 * Returns the number of keyFrames used by the interpolation. Use
	 * {@link #addKeyFrame(GLFrame)} to add new keyFrames.
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
	 * Sets the {@link #interpolationPeriod()}.
	 */
	public void setInterpolationPeriod(int myPeriod) {
		period = myPeriod;
	}

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
	 * {@link #addKeyFrame(GLFrame, float)}) before you startInterpolation(), or
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
			if(timer != null) {
				timer.cancel();
				timer.purge();
			}
			timer=new Timer();
			TimerTask timerTask = new TimerTask() {
				public void run() {
					update();
				}
			};
			timer.scheduleAtFixedRate(timerTask, 0, interpolationPeriod());

			interpolationStrt = true;
			update();
		}
	}

	/**
	 * Stops an interpolation started with {@link #startInterpolation()}. See
	 * {@link #interpolationIsStarted()} and {@link #toggleInterpolation()}.
	 */
	public void stopInterpolation() {
		if(timer != null) {
			timer.cancel();
			timer.purge();
		}
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
	 * @see #addKeyFrame(GLFrame, boolean)
	 */
	public void addKeyFrame(GLFrame frame) {
		addKeyFrame(frame, true);
	}

	/**
	 * Appends a new keyFrame to the path.
	 * <p>
	 * Same as {@link #addKeyFrame(GLFrame, float, boolean)}, except that the
	 * {@link #keyFrameTime(int)} is set to the previous
	 * {@link #keyFrameTime(int)} plus one second (or 0.0 if there is no previous
	 * keyFrame).
	 */
	public void addKeyFrame(GLFrame frame, boolean setRef) {
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
	 * @see #addKeyFrame(GLFrame, float, boolean)
	 */
	public void addKeyFrame(GLFrame frame, float time) {
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
	public void addKeyFrame(GLFrame frame, float time, boolean setRef) {
		if (frame == null)
			return;

		if (keyFr.isEmpty())
			interpolationTm = time;

		if ((!keyFr.isEmpty()) && (keyFr.get(keyFr.size() - 1).time() > time))
			System.out.println("Error in KeyFrameInterpolator.addKeyFrame: time is not monotone");
		else
			keyFr.add(new KeyFrame(frame, time, setRef));

		// connect(frame, SIGNAL(modified()), SLOT(invalidateValues()));
		if (setRef) // only when setting reference
			frame.addListener(this);

		valuesAreValid = false;
		pathIsValid = false;
		currentFrmValid = false;
		resetInterpolation();
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
	public void removeFramesFromMouseGrabberPool() {
		for (int i = 0; i < keyFr.size(); ++i) {
			if (keyFr.get(i).frame() != null)
				if (((InteractiveFrame) keyFr.get(i).frame()).isInMouseGrabberPool())
					((InteractiveFrame) keyFr.get(i).frame())
							.removeFromMouseGrabberPool();
		}
	}

	/**
	 * Re-adds all the Frames to the mouse grabber pool (if they were provided as
	 * references).
	 * 
	 * @see #removeFramesFromMouseGrabberPool()
	 */
	public void addFramesToMouseGrabberPool() {
		for (int i = 0; i < keyFr.size(); ++i) {
			if (keyFr.get(i).frame() != null)
				if (!((InteractiveFrame) keyFr.get(i).frame()).isInMouseGrabberPool())
					((InteractiveFrame) keyFr.get(i).frame()).addInMouseGrabberPool();
		}
	}

	public void updateModifiedFrameValues() {
		Quaternion prevQ = keyFr.get(0).orientation();

		KeyFrame kf;
		for (int i = 0; i < keyFr.size(); ++i) {
			kf = keyFr.get(i);
			kf.updateValues();
			kf.flipOrientationIfNeeded(prevQ);
			prevQ = kf.orientation();
		}

		KeyFrame prev = keyFr.get(0);
		kf = keyFr.get(0);

		int index = 1;
		while (kf != null) {
			KeyFrame next = (index < keyFr.size()) ? keyFr.get(index) : null;
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
	 * Returns the Frame associated with the keyFrame at index {@code index}.
	 * <p>
	 * See also {@link #keyFrameTime(int)}. {@code index} has to be in the range
	 * 0..{@link #numberOfKeyFrames()}-1.
	 * <p>
	 * <b>Note:</b> If this keyFrame was defined using a reference to a Frame (see
	 * {@link #addKeyFrame(GLFrame, float, boolean)} the current referenced Frame
	 * state is returned.
	 */
	public GLFrame keyFrame(int index) {
		KeyFrame kf = keyFr.get(index);
		return new GLFrame(kf.position(), kf.orientation());
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
		Vector3D delta = Vector3D.sub(
				keyFr.get(currentFrame2.nextIndex()).position(), keyFr.get(
						currentFrame1.nextIndex()).position());
		v1 = Vector3D.add(Vector3D.mult(delta, 3.0f), Vector3D.mult(keyFr.get(
				currentFrame1.nextIndex()).tgP(), (-2.0f)));
		v1 = Vector3D.sub(v1, keyFr.get(currentFrame2.nextIndex()).tgP());
		v2 = Vector3D.add(Vector3D.mult(delta, (-2.0f)), keyFr.get(
				currentFrame1.nextIndex()).tgP());
		v2 = Vector3D.add(v2, keyFr.get(currentFrame2.nextIndex()).tgP());
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
		if (dt == 0.0f)
			alpha = 0.0f;
		else
			alpha = (time - keyFr.get(currentFrame1.nextIndex()).time()) / dt;

		// Linear interpolation - debug
		// Vec pos = alpha*(currentFrame2->peekNext()->position()) +
		// (1.0-alpha)*(currentFrame1->peekNext()->position());
		// Vec pos = currentFrame_[1]->peekNext()->position() + alpha *
		// (currentFrame_[1]->peekNext()->tgP() + alpha * (v1+alpha*v2));
		Vector3D pos = Vector3D.add(keyFr.get(currentFrame1.nextIndex()).position(),
				Vector3D.mult(Vector3D.add(keyFr.get(currentFrame1.nextIndex()).tgP(),
						Vector3D.mult(Vector3D.add(v1, Vector3D.mult(v2, alpha)), alpha)),
						alpha));
		Quaternion q = Quaternion.squad(keyFr.get(currentFrame1.nextIndex())
				.orientation(), keyFr.get(currentFrame1.nextIndex()).tgQ(), keyFr.get(
				currentFrame2.nextIndex()).tgQ(), keyFr.get(currentFrame2.nextIndex())
				.orientation(), alpha);

		frame().setPositionWithConstraint(pos);
		frame().setRotationWithConstraint(q);
	}
}
