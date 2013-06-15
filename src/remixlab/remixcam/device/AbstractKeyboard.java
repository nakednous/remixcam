package remixlab.remixcam.device;

import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.event.GenericEvent;
import remixlab.remixcam.profile.AbstractKeyboardProfile;

public class AbstractKeyboard extends AbstractDevice {
	protected AbstractKeyboardProfile<?> profile;
	
	public AbstractKeyboard(AbstractScene scn, String n) {
		super(scn, n);
	}
	
	public AbstractKeyboardProfile<?> keyboardProfile() {
		return profile;
	}

	public void setKeyboardProfile(AbstractKeyboardProfile<?> kprofile) {
		profile = kprofile;
	}
	
	// /**
	@Override
	public void handle(GenericEvent<?> event) {
		if(event == null)	return;
		profile.handle(event);
		if( scene.isDeviceRegistered(this) ) event.enqueue(scene);
	}

	public void handleKey(GenericEvent<?> event) {
		if(event == null)	return;
		profile.handleKey(event);
		if( scene.isDeviceRegistered(this) ) event.enqueue(scene);
	}
	// */

	/**
	 * @Override public void handle(DLEvent event) { profile.handle(event);
	 *           event.enqueue(scene); }
	 * 
	 *           // /** public void handleKey(DLEvent event) {
	 *           profile.handleKey(event); event.enqueue(scene); } //
	 */
}
