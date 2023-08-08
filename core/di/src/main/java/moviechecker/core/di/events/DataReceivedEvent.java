package moviechecker.core.di.events;

import org.springframework.context.ApplicationEvent;

public class DataReceivedEvent extends ApplicationEvent {

	public DataReceivedEvent(Object source) {
		super(source);
	}

}
