package moviechecker.datasource.event;

import org.springframework.context.ApplicationEvent;

public class DataReceivedEvent extends ApplicationEvent {

	public DataReceivedEvent(Object source) {
		super(source);
	}

}
