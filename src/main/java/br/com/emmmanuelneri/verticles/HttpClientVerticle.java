package br.com.emmmanuelneri.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class HttpClientVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientVerticle.class);

    @Override
    public void start() {
        final WebClient webClient = WebClient.create(this.vertx);
        webClient.get("/")
                .host("localhost")
                .port(80)
                .send(httpResponseAsyncResult -> {
                    final HttpResponse<Buffer> result = httpResponseAsyncResult.result();
                    LOGGER.info("retorno: {0} - {1}", result.statusCode(), result.statusMessage());
                });
    }
}
