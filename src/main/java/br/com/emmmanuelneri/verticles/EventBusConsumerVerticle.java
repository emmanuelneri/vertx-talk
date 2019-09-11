package br.com.emmmanuelneri.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class EventBusConsumerVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventBusConsumerVerticle.class);

    private final String eventBusAddres;

    public EventBusConsumerVerticle(final String eventBusAddres) {
        this.eventBusAddres = eventBusAddres;
    }

    @Override
    public void start() {
        final EventBus eventBus = this.vertx.eventBus();
        eventBus.localConsumer(eventBusAddres, message -> {
            LOGGER.info("msg: {0} / headers: {1} / Address: {2}",
                    message.body(), message.headers(), message.address());
        });
    }
}
