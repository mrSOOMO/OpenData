package com.soomo.opendata.domain.model.person

import zio.json.{DeriveJsonCodec, JsonCodec}
import io.getquill.MappedEncoding
import zio.json.EncoderOps
import zio.json.DecoderOps

import io.getquill.MappedEncoding
import java.util.Base64

/**
 * Case class for presenting information about a person.
 *
 * @param firstName Optional first name of the person.
 * @param lastName Optional last name of the person.
 * @param middleName Optional middle name of the person.
 * @param phones Optional list of phone numbers associated with the person.
 * @param addresses Optional list of addresses associated with the person.
 * @param socialIds Optional list of social identifiers (like tax number, social security number, etc.) associated with the person.
 * @param additionalInfo Optional list of additional information or details about the person.
 * @param photos Optional list of photos (stored as byte arrays) associated with the person.
 * @param associatedPersonIds Optional list of IDs of associated persons (e.g., family members, colleagues).
 * @param id Unique identifier for the person; defaults to 0 and is typically assigned when the person is saved to the database.
 */
case class Person(
                   firstName: Option[String],
                   lastName: Option[String],
                   middleName: Option[String],
                   phones: Option[List[String]],
                   addresses: Option[List[String]],
                   socialIds: Option[List[SocialId]],
                   additionalInfo: Option[List[String]],
                   photos: List[Photo],
                   associatedPersonIds: Option[List[Int]],
                   id: Int = 0
                 )

object Person:
  given JsonCodec[Person] = DeriveJsonCodec.gen[Person]

/**
 * A class to represent a person's social identifier document.
 *
 * @param docName document name.
 * @param docNum document number.
 * @param docInfo additional document data.
 */
case class SocialId(
                     docName: Option[String],
                     docNum: Option[String],
                     docInfo: Option[List[String]]
                   )
object SocialId:
  given JsonCodec[SocialId] = DeriveJsonCodec.gen[SocialId]

  given encodeSocialIds: MappedEncoding[Option[List[SocialId]], String] =
    MappedEncoding(ids => ids.toJson)

  given decodeSocialIds: MappedEncoding[String, Option[List[SocialId]]] =
    MappedEncoding(jsonString =>
      jsonString.fromJson[Option[List[SocialId]]] match {
        case Right(idsList) => idsList
        case Left(error) =>
          // TODO: error handling
          None
      }
    )

  given decodeListSocialIds: MappedEncoding[String, List[SocialId]] =
    MappedEncoding(jsonString =>
      jsonString.fromJson[List[SocialId]] match {
        case Right(idsList) => idsList
        case Left(error) =>
          // TODO: error handling
          List.empty
      }
    )

case class Photo(
                  data: Array[Byte]
                )
object Photo {
  given JsonCodec[Photo] = DeriveJsonCodec.gen[Photo]
  given encodeByteArray: MappedEncoding[List[Photo], String] =
    MappedEncoding(ids => ids.toJson)
  given decodeListSocialIds: MappedEncoding[String, List[Photo]] =
    MappedEncoding(jsonString =>
      jsonString.fromJson[List[Photo]] match {
        case Right(photo) => photo
        case Left(error) =>
          // TODO: error handling
          List.empty
      }
    )
}
