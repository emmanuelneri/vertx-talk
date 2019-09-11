package br.com.emmmanuelneri.verticles;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class HttpClientCircuitBreakerVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientCircuitBreakerVerticle.class);

    @Override
    public void start() {
        final CircuitBreaker circuitBreaker = CircuitBreaker.create("web-client-circuit-breaker", vertx,
                new CircuitBreakerOptions()
                        .setFallbackOnFailure(true)
                        .setMaxFailures(2)
                        .setMaxRetries(3)
                        .setTimeout(2000))
                .fallback(error -> {
                    LOGGER.error("Fallback", error);
                    return "Fallback";
                });

        final WebClient webClient = WebClient.create(this.vertx);
        circuitBreaker.execute(promise -> {
            webClient.get("/")
                    .host("localhost")
                    .port(80)
                    .putHeader("X-Mirror-Code", "500")
                    .send(httpResponseAsyncResult -> {
                        final HttpResponse<Buffer> result = httpResponseAsyncResult.result();
                        if (result.statusCode() == 500) {
                            promise.fail(httpResponseAsyncResult.cause());
                            LOGGER.error("error: {0} - {1}", result.statusCode(), result.statusMessage());
                        } else {
                            LOGGER.info("retorno: {0} - {1}", result.statusCode(), result.statusMessage());
                            promise.complete();
                        }
                    });
        });
    }
}
