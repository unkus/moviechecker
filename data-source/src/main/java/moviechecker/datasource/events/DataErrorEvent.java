package moviechecker.datasource.events;

import org.springframework.context.ApplicationEvent;

public class DataErrorEvent extends ApplicationEvent {

	public DataErrorEvent(Object source) {
		super(source);
	}

}
