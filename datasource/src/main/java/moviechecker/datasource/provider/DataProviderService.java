package moviechecker.datasource.provider;

import moviechecker.core.di.events.DataErrorEvent;
import moviechecker.core.di.events.DataReceivedEvent;
import moviechecker.core.di.events.DataRequestedEvent;
import moviechecker.datasource.di.DataRecordProvider;
import moviechecker.datasource.di.DataRecordPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class DataProviderService {

    private @Autowired Collection<DataRecordProvider> movieProviders;

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
