package br.com.emmmanuelneri.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class SimpleHttpVerticle extends AbstractVerticle {
    @Override
    public void start(final Promise<Void> startPromise) {
        this.vertx.createHttpServer()
                .requestHandler(requestHandler -> {
                    requestHandler.response()
                            .putHeader("content-type", "text/plain")
                            .end("Hello Vert.x");
                }).listen(8080, handler -> {
            if (handler.succeeded()) {
                startPromise.complete();
            } else {
                startPromise.fail(handler.cause());
            }
        });
    }
}
