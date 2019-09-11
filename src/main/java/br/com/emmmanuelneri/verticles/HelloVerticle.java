package br.com.emmmanuelneri.verticles;

import io.vertx.core.AbstractVerticle;

public class HelloVerticle extends AbstractVerticle {
    @Override
    public void start() {
        System.out.println("deployed");
    }
}
