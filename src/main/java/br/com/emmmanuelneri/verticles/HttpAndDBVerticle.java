package br.com.emmmanuelneri.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.RowSet;

import java.util.ArrayList;
import java.util.List;

/*
    Disponilizando API para consulta de pessoas através de endpoint e acessando o banco de dados
 */
public class HttpAndDBVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpAndDBVerticle.class);

    @Override
    public void start() {
        final PgPool client = createClient();
        final Router router = Router.router(this.vertx);
        router.get("/pessoas")
                .handler(getPessoasRoutingContextHandler(client))
                .failureHandler(failureHandler());

        this.vertx.createHttpServer()
                .requestHandler(router)
                .listen(8091);
    }

    private Handler<RoutingContext> getPessoasRoutingContextHandler(final PgPool client) {
        return routingContext -> {
            findPessoas(client, pessoas -> {
                routingContext.response()
                        .end(Json.encode(pessoas));
            }, error -> {
                LOGGER.error("erro ao executar select", error);
                routingContext.response()
                        .setStatusCode(500)
                        .setStatusMessage(error.getMessage());
            });
        };
    }

    private void findPessoas(final PgPool client, final Handler<List<JsonObject>> pessoasHandler, final Handler<Throwable> errorHandler) {
        client.query("SELECT * FROM pessoa;", rowSetAsyncResult -> {
            if (rowSetAsyncResult.failed()) {
                errorHandler.handle(rowSetAsyncResult.cause());
                return;
            }

            final List<JsonObject> pessoas = new ArrayList<>();

            final RowSet result = rowSetAsyncResult.result();
            result.forEach(row -> {
                final JsonObject pessoa = new JsonObject();
                pessoa.put("id", row.getValue("id"));
                pessoa.put("nome", row.getValue("nome"));
                pessoas.add(pessoa);
            });
            pessoasHandler.handle(pessoas);
        });
    }

    private Handler<RoutingContext> failureHandler() {
        return routingContext -> {
            LOGGER.error("Router error", routingContext.failure());
            routingContext.response()
                    .setStatusCode(routingContext.statusCode())
                    .end();
        };
    }

    private PgPool createClient() {
        final PgConnectOptions connectOptions = new PgConnectOptions()
                .setPort(5432)
                .setHost("localhost")
                .setDatabase("vertx")
                .setUser("postgres")
                .setPassword("postgres");

        final PoolOptions poolOptions = new PoolOptions()
                .setMaxSize(20);

        return PgPool.pool(this.vertx, connectOptions, poolOptions);
    }

}
