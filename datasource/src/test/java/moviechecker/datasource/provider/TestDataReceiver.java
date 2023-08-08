package moviechecker.datasource.provider;

import moviechecker.core.di.events.DataReceivedEvent;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.event.EventListener;

@TestComponent
public class TestDataReceiver {

    @EventListener
    public void handleDataReceived(DataReceivedEvent event) {
        // Do nothing
    }
}
