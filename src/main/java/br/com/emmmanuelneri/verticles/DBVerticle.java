package br.com.emmmanuelneri.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.RowSet;

public class DBVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBVerticle.class);

    @Override
    public void start() {
        final PgConnectOptions connectOptions = new PgConnectOptions()
                .setPort(5432)
                .setHost("localhost")
                .setDatabase("vertx")
                .setUser("postgres")
                .setPassword("postgres");

        final PoolOptions poolOptions = new PoolOptions()
                .setMaxSize(20);

        final PgPool client = PgPool.pool(this.vertx, connectOptions, poolOptions);

        client.query("SELECT * FROM pessoa;", rowSetAsyncResult -> {
            if (rowSetAsyncResult.failed()) {
                LOGGER.error("erro ao executar select", rowSetAsyncResult.cause());
                return;
            }

            final RowSet result = rowSetAsyncResult.result();
            result.forEach(LOGGER::info);

            client.close();
        });

    }
}
