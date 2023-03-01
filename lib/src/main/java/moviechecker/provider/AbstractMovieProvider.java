package moviechecker.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import moviechecker.event.DataErrorEvent;
import moviechecker.event.DataReceivedEvent;
import moviechecker.event.DataRequestedEvent;

public abstract class AbstractMovieProvider implements MovieProvider {

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	public void handleDataRequest(DataRequestedEvent event) {
		try {
			retrieveData();
			applicationEventPublisher.publishEvent(new DataReceivedEvent(this));
		} catch (Exception e) {
			applicationEventPublisher.publishEvent(new DataErrorEvent(e));
		}
	}
}
