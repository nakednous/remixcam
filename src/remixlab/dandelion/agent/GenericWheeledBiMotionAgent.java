package remixlab.dandelion.agent;

import remixlab.dandelion.core.*;
import remixlab.tersehandling.core.*;
import remixlab.tersehandling.generic.agent.*;
import remixlab.tersehandling.generic.profile.*;

public class GenericWheeledBiMotionAgent<P extends GenericMotionProfile<?>> extends GenericWheeledMotionAgent<GenericMotionProfile<Constants.WheelAction>,
                                                                           																		P,
                                                                           																		GenericClickProfile<Constants.ClickAction>> {
	protected P camProfile;
	protected GenericMotionProfile<Constants.WheelAction> camWheelProfile;
	protected GenericClickProfile<Constants.ClickAction> camClickProfile;
	
	public GenericWheeledBiMotionAgent(GenericMotionProfile<Constants.WheelAction> fWProfile,
			                               GenericMotionProfile<Constants.WheelAction> cWProfile,
			                               P fProfile,
			                               P cProfile,
			                               GenericClickProfile<Constants.ClickAction> c,
																		 GenericClickProfile<Constants.ClickAction> d,
																		 TerseHandler scn, String n) {
		super(fWProfile, fProfile, c, scn, n);
		camProfile = cProfile;
		camWheelProfile = cWProfile;
		camClickProfile = d;
	}
	
	public P cameraProfile() {
		return camProfile;
	}
	
	public P frameProfile() {
		return profile();
	}
	
	public void setCameraProfile(P profile) {
		camProfile = profile;
	}
	
	public void setFrameProfile(P profile) {
		setProfile(profile);
	}
	
	public GenericClickProfile<Constants.ClickAction> cameraClickProfile() {
		return camClickProfile;
	}
	
	public GenericClickProfile<Constants.ClickAction> frameClickProfile() {
		return clickProfile;
	}
	
	public void setCameraClickProfile(GenericClickProfile<Constants.ClickAction> profile) {
		camClickProfile = profile;
	}
	
	public void setFrameClickProfile(GenericClickProfile<Constants.ClickAction> profile) {
		setClickProfile(profile);
	}
	
	public GenericMotionProfile<Constants.WheelAction> cameraWheelProfile() {
		return camWheelProfile;
	}
	
	public GenericMotionProfile<Constants.WheelAction> frameWheelProfile() {
		return wheelProfile;
	}
	
	public void setCameraWheelProfile(GenericMotionProfile<Constants.WheelAction> profile) {
		camWheelProfile = profile;
	}
	
	public void setFrameWheelProfile(GenericMotionProfile<Constants.WheelAction> profile) {
		setWheelProfile(profile);
	}
	
	@Override
	public P motionProfile() {
		if( grabber() instanceof InteractiveCameraFrame )
			return cameraProfile();
		if( grabber() instanceof InteractiveFrame )
			return frameProfile();					
		return null;
	}
	
	@Override
	public GenericClickProfile<Constants.ClickAction> clickProfile() {
		if( grabber() instanceof InteractiveCameraFrame )
			return cameraClickProfile();
		if( grabber() instanceof InteractiveFrame )
			return frameClickProfile();					
		return null;
	}
	
	@Override
	public GenericMotionProfile<Constants.WheelAction> wheelProfile() {
		if( grabber() instanceof InteractiveCameraFrame )
			return cameraWheelProfile();
		if( grabber() instanceof InteractiveFrame )
			return frameWheelProfile();					
		return null;
	}
	
	@Override
	protected boolean foreignGrabber() {
		return !( grabber() instanceof InteractiveFrame ) && !( grabber() instanceof AbstractScene);
	}
}