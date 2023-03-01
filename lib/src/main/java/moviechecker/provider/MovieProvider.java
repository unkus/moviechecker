package moviechecker.provider;

import org.springframework.context.event.EventListener;

import moviechecker.event.DataRequestedEvent;
import moviechecker.model.Site;

public interface MovieProvider {

	void retrieveData() throws Exception;

	@EventListener
	void handleDataRequest(DataRequestedEvent event);
}
