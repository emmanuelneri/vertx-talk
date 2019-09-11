package br.com.emmmanuelneri.verticles;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class EventBusProducerVerticleTest {

    @Test
    public void shouldProduceMessageOnEventBus(final TestContext context) {
        final Vertx vertx = Vertx.vertx();

        final String ADDRESS = "EVENT_BUS_PRODUCER_TEST";

        final Async async = context.async();
        vertx.deployVerticle(new EventBusProducerVerticle(ADDRESS), handler -> {

            vertx.eventBus().localConsumer(ADDRESS, message -> {
                context.assertEquals("Hello Event Bus!", message.body());
                async.complete();
            });
        });
    }

}
