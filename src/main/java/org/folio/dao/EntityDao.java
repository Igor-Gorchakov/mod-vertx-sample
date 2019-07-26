package org.folio.dao;

import io.vertx.core.Future;
import org.folio.rest.jaxrs.model.Entity;

import java.util.Optional;

public interface EntityDao {

  Future<Optional<Entity>> getById(String entityId, String tenantId);
}
