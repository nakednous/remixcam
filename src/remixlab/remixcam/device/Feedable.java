package remixlab.remixcam.device;

import remixlab.remixcam.event.DLEvent;

public interface Feedable {
	public DLEvent<?> feed();
	//public AbstractDevice device();
}
