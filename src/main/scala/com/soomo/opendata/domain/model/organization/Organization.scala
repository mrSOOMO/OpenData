package com.soomo.opendata.domain.model.organization

import io.getquill.MappedEncoding
import zio.json.{DeriveJsonCodec, JsonCodec}
import zio.json.EncoderOps
import zio.json.DecoderOps

/**
 * Case class for representing information about an organization.
 *
 * @param name Name of the organization.
 * @param address Address of the organization.
 * @param identificationDocs List of documents for identification.
 * @param associatedPersonIds List of IDs of associated persons.
 */
case class Organization(
                         name: Option[String],
                         address: Option[List[String]],
                         identificationDocs: Option[List[IdentificationDoc]],
                         associatedPersonIds: Option[List[Int]],
                         additionalInfo: Option[List[String]],
                         id: Int = 0
                       )

object Organization:
  given JsonCodec[Organization] = DeriveJsonCodec.gen[Organization]

/**
 * A class to represent an identification document of an organization.
 *
 * @param docType Type of the document.
 * @param docNumber Document number.
 * @param docInfo Document additional information.
 */
case class IdentificationDoc(
                              docType: Option[String],
                              docNumber: Option[String],
                              docInfo: Option[List[String]]
                            )

object IdentificationDoc:
  given JsonCodec[IdentificationDoc] = DeriveJsonCodec.gen[IdentificationDoc]

  given encodeIdentificationDocs: MappedEncoding[Option[List[IdentificationDoc]], String] =
    MappedEncoding(docs => docs.toJson)

  given decodeIdentificationDocs: MappedEncoding[String, Option[List[IdentificationDoc]]] =
    MappedEncoding(jsonString =>
      jsonString.fromJson[Option[List[IdentificationDoc]]] match {
        case Right(docsList) => docsList
        case Left(error) =>
          // TODO: error handling
          None
      }
    )

  given decodeListIdentificationDocs: MappedEncoding[String, List[IdentificationDoc]] =
    MappedEncoding(jsonString =>
      jsonString.fromJson[List[IdentificationDoc]] match {
        case Right(docsList) => docsList
        case Left(error) =>
          // TODO: error handling
          List.empty
      }
    )