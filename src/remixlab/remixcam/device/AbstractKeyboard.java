package remixlab.remixcam.device;

import remixlab.remixcam.core.*;
import remixlab.remixcam.event.*;
import remixlab.remixcam.profile.*;

public class AbstractKeyboard extends AbstractDevice {
	KeyboardProfile profile;

	public AbstractKeyboard(AbstractScene scn, String n) {
		super(scn, n);
		profile = new KeyboardProfile();
	}

	/**
	@Override
	public void handle(DLEvent<?> event) {
		profile.handle(event);
		event.enqueue(scene);
	}
	
	public void handleKey(DLEvent<?> event) {
		profile.handleKey(event);
		event.enqueue(scene);
	}
	// */
	
	// /**
	@Override
	public void handle(DLEvent event) {
		profile.handle(event);
		event.enqueue(scene);
	}
	
	// /**
	public void handleKey(DLEvent event) {
		profile.handleKey(event);
		event.enqueue(scene);
	}
	// */
}
