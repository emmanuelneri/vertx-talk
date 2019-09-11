package br.com.emmmanuelneri.verticles;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class HelloVerticleTest {

    @Test
    public void shouldDeployVerticleSuccess(final TestContext context) {
        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new HelloVerticle(), context.asyncAssertSuccess());
    }
}