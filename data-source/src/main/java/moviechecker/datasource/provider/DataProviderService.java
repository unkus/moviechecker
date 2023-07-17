package moviechecker.datasource.provider;

import moviechecker.database.di.DataRecordPublisher;
import moviechecker.di.events.DataErrorEvent;
import moviechecker.di.events.DataReceivedEvent;
import moviechecker.di.events.DataRequestedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class DataProviderService {

    private @Autowired Collection<MovieProvider> movieProviders;

    private @Autowired ApplicationEventPublisher applicationEventPublisher;

    private @Autowired DataRecordPublisher dataRecordPublisher;

    private final ExecutorService dataRetrievalExecutor = Executors.newSingleThreadExecutor();

    @EventListener
    public final void handleDataRequest(DataRequestedEvent event) {
        dataRetrievalExecutor.submit(() -> {
            movieProviders.forEach(provider -> {
                try {
                    provider.retrieveData().forEach(dataRecordPublisher::publishRecord);
                } catch (Exception e) {
                    applicationEventPublisher.publishEvent(new DataErrorEvent(e));
                }
            });
            applicationEventPublisher.publishEvent(new DataReceivedEvent(this));
        });
    }
}
