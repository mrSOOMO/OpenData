package com.soomo.opendata.domain

import zio.json.{DeriveJsonCodec, JsonCodec}

 /**
 * The base error type that all service-related errors extend. Each type of
 * ServiceError includes an associated Throwable to provide more information
 * about the error.
 */
enum ServiceError(throwable: Throwable):
  case DataMigrationError(throwable: Throwable)    extends ServiceError(throwable)
  case FlywayError(throwable: Throwable)           extends ServiceError(throwable)
  case DaoError(throwable: Throwable)              extends ServiceError(throwable)
  case LogicError(throwable: Throwable)            extends ServiceError(throwable)
  case ThirdPartyServerError(throwable: Throwable) extends ServiceError(throwable)
  case DecryptionError(throwable: Throwable)       extends ServiceError(throwable)
