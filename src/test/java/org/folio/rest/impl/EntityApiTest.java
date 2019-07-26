package org.folio.rest.impl;

import io.restassured.RestAssured;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.http.HttpStatus;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.PostgresClient;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

@RunWith(VertxUnitRunner.class)
public class EntityApiTest extends AbstractRestVerticleTest {

  private static final String RESOURCE_PATH = "/testResource";
  private static final String TABLE_NAME = "entities";

  @Test
  public void shouldReturnNotFoundOnGetWhenEntityDoesNotExist() {
    String id = UUID.randomUUID().toString();
    RestAssured.given()
      .spec(spec)
      .when()
      .get(RESOURCE_PATH + "/" + id)
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Override
  public void clearTables(TestContext context) {
    Async async = context.async();
    PostgresClient.getInstance(vertx, TENANT_ID).delete(TABLE_NAME, new Criterion(), event -> {
      if (event.failed()) {
        context.fail(event.cause());
      }
      async.complete();
    });
  }

}
