package remixlab.remixcam.dlprofile;

import java.util.Map.Entry;

import remixlab.remixcam.action.Actionable;
import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.core.Constants.DLAction;
import remixlab.remixcam.profile.Bindings;
import remixlab.remixcam.shortcut.AbstractShortcut;
import remixlab.remixcam.shortcut.KeyboardShortcut;

public abstract class AbstractSceneEventProfile<K extends AbstractShortcut> {
	protected String name;
	protected AbstractScene scene;
	protected Bindings<K, DLAction> sceneActions;
	//protected Bindings<K, Actionable> sceneActions;
	
	public AbstractSceneEventProfile(AbstractScene scn, String n) {		
		scene = scn;		
		name = n;
		sceneActions = new Bindings<K, DLAction>(scene);
	}
	
	/**
	 * Returns a String containing the camera keyboard bindings' descriptions.
	 */
	public String bindingsDescription() {
		String description = new String();
		for (Entry<K, DLAction> entry : sceneActions.map().entrySet())
      description += entry.getKey().description() + " -> " + entry.getValue().description() + "\n";
		return description;
	}
	
	/**
	 * Returns true if there is a camera keyboard shortcut for the given action.
	 */
	public boolean isSceneActionBound(DLAction action) {
		return sceneActions.isActionMapped(action);
	}
	
	public void removeAllSceneShortcuts() {
		sceneActions.removeAllBindings();
	}
}
