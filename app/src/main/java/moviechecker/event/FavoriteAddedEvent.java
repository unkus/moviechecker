package moviechecker.event;

import org.springframework.context.ApplicationEvent;

public class FavoriteAddedEvent extends ApplicationEvent {

	public FavoriteAddedEvent(Object source) {
		super(source);
	}

}
