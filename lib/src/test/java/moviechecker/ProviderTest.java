package moviechecker;

import moviechecker.event.DataReceivedEvent;
import moviechecker.event.DataRequestedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@RecordApplicationEvents
public class ProviderTest {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ApplicationEvents applicationEvents;

    @Test
    public void dataRequestProcessed() {
        applicationEvents.clear();

        DataRequestedEvent dataRequestedEvent = new DataRequestedEvent(this);
        // Send indication to TestProvider that the data should be read.
        applicationEventPublisher.publishEvent(dataRequestedEvent);

        // Checks whether the provider is able to process the DataRequestedEvent and send back the DataReceivedEvent.
        assertTrue(applicationEvents.stream().anyMatch(event -> event instanceof DataReceivedEvent),
                "DataReceivedEvent event was not sent");
    }

}
