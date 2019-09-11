package br.com.emmmanuelneri.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import java.util.Arrays;

public class HttpVerticle extends AbstractVerticle {

    @Override
    public void start() {
        final Router router = Router.router(this.vertx);
//        router.route().handler(BodyHandler.create());
        router.get("/pessoas").handler(routingContext -> {
            final JsonObject joao = new JsonObject()
                    .put("id", 1)
                    .put("nome", "Joao");

            final JsonObject maria = new JsonObject()
                    .put("id", 2)
                    .put("nome", "Maria");

            routingContext.response()
                    .end(Json.encode(Arrays.asList(joao, maria)));
        });

        this.vertx.createHttpServer()
                .requestHandler(router)
                .listen(8081);
    }

    ;
}
