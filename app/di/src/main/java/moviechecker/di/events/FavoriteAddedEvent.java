package moviechecker.di.events;

import moviechecker.di.Favorite;
import org.springframework.context.ApplicationEvent;

public class FavoriteAddedEvent extends ApplicationEvent {

	public FavoriteAddedEvent(Favorite source) {
		super(source);
	}

}
