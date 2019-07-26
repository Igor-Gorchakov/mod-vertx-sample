package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.folio.dataimport.util.ExceptionHelper;
import org.folio.rest.jaxrs.model.Entity;
import org.folio.rest.jaxrs.resource.TestResource;
import org.folio.rest.tools.utils.TenantTool;
import org.folio.service.EntityService;
import org.folio.spring.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.Map;

public class TestResourceImpl implements TestResource {

  private static final Logger LOG = LoggerFactory.getLogger(TestResourceImpl.class);
  private static final String NOT_FOUND_MESSAGE = "%s with id '%s' is not found";

  @Autowired
  private EntityService service;
  private String tenantId;

  public TestResourceImpl(Vertx vertx, String tenantId) { //NOSONAR
    SpringContextUtil.autowireDependencies(this, Vertx.currentContext());
    this.tenantId = TenantTool.calculateTenantId(tenantId);
  }

  @Override
  public void getTestResourceById(String id, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    try {
      service.getById(id, tenantId)
        .map(optionalRecord -> optionalRecord.orElseThrow(() ->
          new NotFoundException(String.format(NOT_FOUND_MESSAGE, Entity.class.getSimpleName(), id))))
        .map(GetTestResourceByIdResponse::respond200WithApplicationJson)
        .map(Response.class::cast)
        .otherwise(ExceptionHelper::mapExceptionToResponse)
        .setHandler(asyncResultHandler);
    } catch (Exception e) {
      LOG.error("Failed to get record by id", e);
      asyncResultHandler.handle(Future.succeededFuture(ExceptionHelper.mapExceptionToResponse(e)));
    }
  }
}
