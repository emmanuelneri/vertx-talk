package br.com.emmmanuelneri.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Router;

public class HealthCheckVerticle extends AbstractVerticle {

    private static final String VERTX_TALK_APP = "vertx-talk-app";

    @Override
    public void start() {
        final HealthCheckHandler healthCheckHandler = HealthCheckHandler.create(vertx);

        healthCheckHandler.register(VERTX_TALK_APP,
                future -> future.complete(Status.OK()));

        final Router router = Router.router(vertx);
        router.get("/health").handler(healthCheckHandler);

        this.vertx.createHttpServer()
                .requestHandler(router)
                .listen(8085);
    }
}
