package br.com.emmmanuelneri;

import br.com.emmmanuelneri.verticles.*;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(final String[] args) {
        final Vertx vertx = Vertx.vertx();

        vertx.setPeriodic(10_00, handler -> {
            System.out.println("Hello Vert.x");
        });

        vertx.deployVerticle(new HelloVerticle());

        vertx.deployVerticle(new SimpleHttpVerticle());
        vertx.deployVerticle(new HttpVerticle());
        vertx.deployVerticle(new HttpClientVerticle());
        vertx.deployVerticle(new HttpAndDBVerticle());

        final String eventBusAddress = "EVENT_BUS";
        vertx.deployVerticle(new EventBusProducerVerticle(eventBusAddress));
        vertx.deployVerticle(new EventBusConsumerVerticle(eventBusAddress));

        vertx.deployVerticle(new KafkaProducerVerticle());
        vertx.deployVerticle(new KafkaConsumerVerticle());

        vertx.deployVerticle(new DBVerticle());

        vertx.deployVerticle(new HttpClientCircuitBreakerVerticle());
        vertx.deployVerticle(new HealthCheckVerticle());

        vertx.deployVerticle(new ConfigurationVerticle());
    }

}
