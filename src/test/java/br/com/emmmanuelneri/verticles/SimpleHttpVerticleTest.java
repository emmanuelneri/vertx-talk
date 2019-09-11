package br.com.emmmanuelneri.verticles;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class SimpleHttpVerticleTest {

    @Test
    public void shouldResponseSuccessAndHelloVertx(final TestContext context) {
        final Vertx vertx = Vertx.vertx();

        final Async async = context.async();

        final DeploymentOptions deploymentWithWorker = new DeploymentOptions();
        deploymentWithWorker.setWorker(true);

        vertx.deployVerticle(new SimpleHttpVerticle(), deploymentWithWorker, handler -> {
            if (handler.failed()) {
                context.fail(handler.cause());
            }

            final WebClient webClient = WebClient.create(vertx);
            webClient.get(8080, "localhost", "/")
                    .send(asyncResult -> {
                        if (asyncResult.failed()) {
                            context.fail(asyncResult.cause());
                        }

                        final HttpResponse<Buffer> result = asyncResult.result();
                        context.assertEquals(200, result.statusCode());
                        context.assertEquals("Hello Vert.x", result.bodyAsString());

                        async.complete();
                    });
        });
    }

}
