package com.soomo.opendata.flyway

import org.flywaydb.core.Flyway as FlywayCore
import zio.*
import javax.sql.DataSource

final class Flyway private (private val underlying: FlywayCore) {
  def migrate: Task[MigrateResult] =
    ZIO
      .attempt(underlying.migrate())
      .map(MigrateResult(_))
}

object Flyway {
  def apply(ds: DataSource): Task[Flyway] =
    ZIO
      .attempt(FlywayCore.configure().dataSource(ds).load())
      .map(flywayCore => new Flyway(flywayCore))

}
