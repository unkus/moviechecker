package moviechecker.ui.controller;

import moviechecker.datasource.event.DataRequestedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class MainViewController {

    private Logger logger = LoggerFactory.getLogger(MainViewController.class);

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void checkUpdates() {
        applicationEventPublisher.publishEvent(new DataRequestedEvent(this));
    }

}
