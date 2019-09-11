package br.com.emmmanuelneri.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class EventBusProducerVerticle extends AbstractVerticle {

    private final String eventBusAddres;

    public EventBusProducerVerticle(final String eventBusAddres) {
        this.eventBusAddres = eventBusAddres;
    }

    @Override
    public void start() {
        final EventBus eventBus = this.vertx.eventBus();
        this.vertx.setPeriodic(10_000, handler -> {
            eventBus.send(eventBusAddres, "Hello Event Bus!");
        });
    }
}
