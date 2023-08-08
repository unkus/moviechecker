package moviechecker.core.di.events;

import moviechecker.core.di.Favorite;
import org.springframework.context.ApplicationEvent;

public class FavoriteAddedEvent extends ApplicationEvent {

	public FavoriteAddedEvent(Favorite source) {
		super(source);
	}

}
