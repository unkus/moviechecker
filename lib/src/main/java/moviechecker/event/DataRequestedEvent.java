package moviechecker.event;

import org.springframework.context.ApplicationEvent;

public class DataRequestedEvent extends ApplicationEvent {

	public DataRequestedEvent(Object source) {
		super(source);
	}

}
