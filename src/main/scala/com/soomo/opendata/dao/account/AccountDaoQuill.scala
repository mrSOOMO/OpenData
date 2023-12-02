package com.soomo.opendata.dao.account

import com.soomo.opendata.domain.ServiceError
import com.soomo.opendata.domain.ServiceError.DaoError
import com.soomo.opendata.domain.model.account.Account
import io.getquill.*
import zio.*

import scala.concurrent.Future

final class AccountDaoQuill(ctx: PostgresJAsyncContext[SnakeCase]) extends AccountDao:

  import ctx.*

  // TODO Fix Acc update
  private inline def insertUser(account: Account): Quoted[Insert[Account]] = quote {
    query[Account]
      .insert(_.login          -> lift(account.login),
              _.hashedPassword -> lift(account.hashedPassword),
              _.info           -> lift(account.info),
              _.accessLevel    -> lift(account.accessLevel)
      )
      .onConflictUpdate(acc => acc.id)(
        (t, e) => t.login -> e.login,
        (t, e) => t.hashedPassword -> e.hashedPassword,
        (t, e) => t.info -> e.info,
        (t, e) => t.accessLevel -> e.accessLevel
      )
  }

  def addAll(accounts: Seq[Account]): IO[ServiceError, Unit] =
    for {
      _ <- ZIO.fromFuture { implicit ec =>
        Future.sequence(accounts.map(user => run(insertUser(user))))
      }.mapError(DaoError)
    } yield ()

  def getAll(): IO[ServiceError, Seq[Account]] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[Account]))
    }.mapError(DaoError)

  def getByLogin(login: String): IO[ServiceError, Account] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[Account]).filter(_.login == lift(login))).map(_.head)
    }.mapError(DaoError)

  def getById(id: Int): IO[ServiceError, Account] = ZIO.fromFuture { implicit ec =>
    run(quote(query[Account]).filter(_.id == lift(id))).map(_.head)
  }.mapError(DaoError)

  def deleteById(id: Int): IO[ServiceError, Long] = ZIO.fromFuture { implicit ec =>
    run(quote(query[Account].filter(_.id == lift(id))).delete)
  }.mapError(DaoError)
object AccountDaoQuill:

  val live: ZLayer[PostgresJAsyncContext[SnakeCase], Nothing, AccountDaoQuill] =
    ZLayer(
      for ctx <- ZIO.service[PostgresJAsyncContext[SnakeCase]]
      yield AccountDaoQuill(ctx)
    )
