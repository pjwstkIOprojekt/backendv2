package com.gary.backendv2.event.publisher;

import com.gary.backendv2.event.PrimitiveEntitiesCreatedEvent;
import com.gary.backendv2.utils.demodata.impl.ObjectInitializationVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class PrimitiveEntitiesCreatedEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(final ObjectInitializationVisitor visitor) {
        PrimitiveEntitiesCreatedEvent entitiesCreatedEvent = new PrimitiveEntitiesCreatedEvent(this, visitor);
        applicationEventPublisher.publishEvent(entitiesCreatedEvent);
    }
}
