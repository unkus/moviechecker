package moviechecker.ui.events;

import org.springframework.context.ApplicationEvent;

public class FavoriteAddedEvent extends ApplicationEvent {

	public FavoriteAddedEvent(Object source) {
		super(source);
	}

}
