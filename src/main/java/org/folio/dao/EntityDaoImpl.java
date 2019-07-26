package org.folio.dao;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.folio.rest.jaxrs.model.Entity;
import org.folio.rest.persist.Criteria.Criteria;
import org.folio.rest.persist.Criteria.Criterion;
import org.folio.rest.persist.interfaces.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.folio.dataimport.util.DaoUtil.constructCriteria;

@Repository
public class EntityDaoImpl implements EntityDao {

  private static final Logger LOG = LoggerFactory.getLogger(EntityDaoImpl.class);

  private static final String TABLE = "entities";
  private static final String ID_FIELD = "'id'";

  @Autowired
  private PostgresClientFactory pgClientFactory;

  @Override
  public Future<Optional<Entity>> getById(String entityId, String tenantId) {
    Future<Results<Entity>> future = Future.future();
    try {
      Criteria idCrit = constructCriteria(ID_FIELD, entityId);
      pgClientFactory
        .createInstance(tenantId)
        .get(TABLE, Entity.class, new Criterion(idCrit), true, false, future.completer());
    } catch (Exception e) {
      LOG.error("Error querying entity by id", e);
      future.fail(e);
    }
    return future
      .map(Results::getResults)
      .map(entities -> entities.isEmpty() ? Optional.empty() : Optional.of(entities.get(0)));
  }
}
