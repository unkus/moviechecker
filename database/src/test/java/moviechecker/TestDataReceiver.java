package moviechecker;

import moviechecker.datasource.events.DataReceivedEvent;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.event.EventListener;

@TestComponent
public class TestDataReceiver {

    @EventListener
    public void handleDataReceived(DataReceivedEvent event) {
        // Do nothing
    }
}