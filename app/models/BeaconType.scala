package models

import play.api.libs.json.Json

/**
 * Created by ahmetkucuk on 28/03/15.
 */
object BeaconType extends Enumeration {

  type BeaconType = Value
  val BedBeacon, EquipmentBeacon, LocationBeacon = Value
}
