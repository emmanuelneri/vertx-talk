package br.com.emmmanuelneri.verticles;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

public class ConfigurationVerticle extends AbstractVerticle {

    @Override
    public void start(final Promise<Void> startPromise) {
        final ConfigStoreOptions store = new ConfigStoreOptions()
                .setType("file")
                .setFormat("properties")
                .setConfig(new JsonObject().put("path", "config/config.properties"));

        final ConfigRetriever configRetriever = ConfigRetriever.create(vertx,
                new ConfigRetrieverOptions().addStore(store));

        configRetriever.getConfig(handler -> {
            if (handler.failed()) {
                startPromise.fail(handler.cause());
                return;
            }

            final JsonObject config = handler.result();
            System.out.println(config.getString("project.name"));
        });
    }
}
