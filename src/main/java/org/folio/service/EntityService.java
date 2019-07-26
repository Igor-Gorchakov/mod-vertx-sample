package org.folio.service;

import io.vertx.core.Future;
import org.folio.rest.jaxrs.model.Entity;

import java.util.Optional;

public interface EntityService {

  Future<Optional<Entity>> getById(String entityId, String tenantId);
}
