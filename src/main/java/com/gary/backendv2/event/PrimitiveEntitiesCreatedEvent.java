package com.gary.backendv2.event;

import com.gary.backendv2.utils.demodata.impl.ObjectInitializationVisitor;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class PrimitiveEntitiesCreatedEvent extends ApplicationEvent {
    private final ObjectInitializationVisitor visitor;

    public PrimitiveEntitiesCreatedEvent(Object source, ObjectInitializationVisitor message) {
        super(source);
        this.visitor = message;
    }
    public ObjectInitializationVisitor getVisitor() {
        return visitor;
    }
}
