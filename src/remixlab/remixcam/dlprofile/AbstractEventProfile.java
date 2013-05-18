package remixlab.remixcam.dlprofile;

import java.util.Map.Entry;

import remixlab.remixcam.action.*;
import remixlab.remixcam.core.*;
import remixlab.remixcam.event.*;
import remixlab.remixcam.profile.Bindings;
import remixlab.remixcam.shortcut.*;

public abstract class AbstractEventProfile<K extends AbstractShortcut> extends AbstractSceneEventProfile<K> implements Constants {	
	DLEvent event;
	protected Bindings<K, DLAction> iFrameActions;
	protected Bindings<K, DLAction> camFrameActions;
	
	public AbstractEventProfile(AbstractScene scn, String n, DLEvent e) {
		super(scn, n);
		event = e;
		iFrameActions = new Bindings<K, DLAction>(scene);
		camFrameActions = new Bindings<K, DLAction>(scene);
	}
	
	public abstract int dofs();
	
	/**
	 * Returns the camera profile name.
	 * <p>
	 * The camera profile name is defined at instantiation time and cannot be modified later.
	 * 
	 * @see #CameraProfile(AbstractScene, String, Mode)
	 */
	public String name() {
		return name;
	}
}
