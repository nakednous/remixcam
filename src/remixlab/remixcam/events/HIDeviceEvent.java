package remixlab.remixcam.events;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

public class HIDeviceEvent extends DLEvent {	
	//ArrayList<Button> buttons;
	
  public HIDeviceEvent() {
  	
  }
  
  public HIDeviceEvent(Integer action, Integer modifiers) {
    super(action, modifiers);
    
  }
  
  protected HIDeviceEvent(HIDeviceEvent other) {
  	super(other);
		
	}
  
  @Override
	public HIDeviceEvent get() {
		return new HIDeviceEvent(this);
	}
}
