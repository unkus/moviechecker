package moviechecker.datasource.provider;

import moviechecker.core.di.events.DataReceivedEvent;
import moviechecker.core.di.events.DataRequestedEvent;
import moviechecker.datasource.di.DataRecordProvider;
import moviechecker.datasource.di.DataRecordPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestApplication.class)
public class DataProviderServiceTest {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @MockBean
    private DataRecordProvider dataRecordProvider;
    @MockBean
    private DataRecordPublisher dataRecordPublisher;
    @MockBean
    private TestDataReceiver dataReceiver;

    @Test
    public void dataRequestedEventTest() throws Exception {
        assertNotNull(dataRecordProvider);

        // Send indication to TestProvider that the data should be read.
        applicationEventPublisher.publishEvent(new DataRequestedEvent(this));

        // TODO: How can I verify that the data is being retrieved in another thread?

        // Checks whether the method contains data retrieval logic is called.
        verify(dataRecordProvider, timeout(1000).times(1)).retrieveData();

        // Checks whether the provider send back the DataReceivedEvent after data provided.
        verify(dataReceiver, timeout(1000).times(1)).handleDataReceived(any(DataReceivedEvent.class));
    }
}
