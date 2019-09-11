package br.com.emmmanuelneri.verticles;

import br.com.emmmanuelneri.Application;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.kafka.client.consumer.KafkaConsumer;

import java.util.HashMap;
import java.util.Map;

public class KafkaConsumerVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private static final String TOPIC = "Topic";

    @Override
    public void start(final Promise<Void> startPromise) {
        final Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("group.id", "talk-vertx");
        config.put("auto.offset.reset", "latest");

        final KafkaConsumer<String, String> consumer = KafkaConsumer.create(vertx, config);
        consumer.subscribe(TOPIC, handler -> {
            if (handler.failed()) {
                startPromise.fail(handler.cause());
                LOGGER.error("subscribe error", handler.cause());
                return;
            }

            startPromise.complete();
        });


        consumer.handler(kafkaConsumerRecord -> {
            LOGGER.info("kafka consumer: {0}", kafkaConsumerRecord);
        });
    }
}
