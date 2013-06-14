package remixlab.remixcam.action;

/**
public interface DandelionActionable <E extends Enum<E>> {
	public E getAction();
	public E getDefaultAction();
}
//*/

public interface DandelionActionable {
	public Enum<?> getAction();
	public Enum<?> getDefaultAction();
}
