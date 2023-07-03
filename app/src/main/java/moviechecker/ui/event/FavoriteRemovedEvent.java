package moviechecker.ui.event;

import org.springframework.context.ApplicationEvent;

public class FavoriteRemovedEvent extends ApplicationEvent {

	public FavoriteRemovedEvent(Object source) {
		super(source);
	}

}
