package com.soomo.opendata.flyway

import zio.*
import javax.sql.DataSource

trait FlywayProvider {
  def flyway: Task[Flyway]
}

object FlywayProvider {

  val live: RLayer[DataSource, FlywayProvider] = ZLayer {
    for {
      dataSource <- ZIO.service[DataSource]
    } yield new FlywayProvider {
      override val flyway: Task[Flyway] = Flyway(dataSource)
    }
  }

  def flyway: RIO[FlywayProvider, Flyway] =
    ZIO.environmentWithZIO[FlywayProvider](_.get.flyway)

}
