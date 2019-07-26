package org.folio.service.impl;

import io.vertx.core.Future;
import org.folio.dao.EntityDao;
import org.folio.rest.jaxrs.model.Entity;
import org.folio.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EntityServiceImpl implements EntityService {

  @Autowired
  private EntityDao dao;

  @Override
  public Future<Optional<Entity>> getById(String entityId, String tenantId) {
    return dao.getById(entityId, tenantId);
  }
}
