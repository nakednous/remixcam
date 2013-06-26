package remixlab.remixcam.core;

import remixlab.remixcam.shortcut.KeyboardShortcut;

public interface KeyDuoble<A extends Actionable<?>> extends Duoble<A> {
	public KeyboardShortcut keyShortcut();
}
