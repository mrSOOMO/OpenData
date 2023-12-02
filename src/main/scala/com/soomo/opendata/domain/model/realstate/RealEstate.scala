package com.soomo.opendata.domain.model.realstate

import zio.json.{DecoderOps, DeriveJsonCodec, EncoderOps, JsonCodec, JsonDecoder, JsonEncoder}
import io.getquill.MappedEncoding


/**
 * Case class for representing information about a real estate property.
 *
 * @param address Address of the property.
 * @param coordinates Geographic coordinates of the property.
 * @param propertyType Type of the property.
 * @param size Size of the property in square meters.
 * @param ownerIds List of IDs of owners.
 * @param additionalInfo Additional information about the property.
 */
case class RealEstate(
                       address: String,
                       coordinates: Option[Coordinates],
                       propertyType: PropertyType,
                       size: Option[Double],
                       ownerIds: Option[List[Int]],
                       additionalInfo: Option[List[String]],
                       id: Int = 0
                     )

object RealEstate:
  given JsonCodec[RealEstate] = DeriveJsonCodec.gen[RealEstate]

/**
 * A class to represent geographic coordinates.
 *
 * @param latitude Latitude.
 * @param longitude Longitude.
 */
case class Coordinates(latitude: Double, longitude: Double)

object Coordinates:
  given JsonCodec[Coordinates] = DeriveJsonCodec.gen[Coordinates]

  given encodeCoordinatesOption: MappedEncoding[Option[Coordinates], String] =
    MappedEncoding {
      case Some(coords) => s"${coords.latitude},${coords.longitude}"
      case None => null
    }
  given decodeCoordinatesOption: MappedEncoding[String, Option[Coordinates]] =
    MappedEncoding {
      case str if str != null && str.nonEmpty =>
        val parts = str.split(",")
        Some(Coordinates(parts(0).toDouble, parts(1).toDouble))
      case _ => None
    }
/**
 * Enumeration for property types.
 */
enum PropertyType:
  case Apartment, House, Commercial, Land, Other

object PropertyType:
  given JsonCodec[PropertyType] = DeriveJsonCodec.gen[PropertyType]

  // Encoder for PropertyType to String
  given encodePropertyType: MappedEncoding[PropertyType, String] =
  MappedEncoding[PropertyType, String] {
    case PropertyType.Apartment => "Apartment"
    case PropertyType.House => "House"
    case PropertyType.Commercial => "Commercial"
    case PropertyType.Land => "Land"
    case PropertyType.Other => "Other"
  }

  // Decoder for String to PropertyType
  given decodePropertyType: MappedEncoding[String, PropertyType] =
  MappedEncoding[String, PropertyType] {
    case "Apartment" => PropertyType.Apartment
    case "House" => PropertyType.House
    case "Commercial" => PropertyType.Commercial
    case "Land" => PropertyType.Land
    case "Other" => PropertyType.Other
  }

/**
 * A class to represent additional information about a real estate property.
 *
 * @param description Description of the property.
 * @param images List of images of the property.
 */
case class PropertyAdditionalInfo(
                                   description: Option[String],
                                   images: Option[List[Array[Byte]]]
                                 )

object PropertyAdditionalInfo:
  given JsonCodec[PropertyAdditionalInfo] = DeriveJsonCodec.gen[PropertyAdditionalInfo]

