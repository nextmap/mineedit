package guru.nidi.minecraft.mineedit

import org.scalatest.FlatSpec

/**
 *
 */
class LatLngTest extends FlatSpec {

  behavior of "LatLng"

  it should "normalize inside the interval [-180,180)" in {
    assert(LatLng(1, 1).normalize === LatLng(1, 1))
    assert(LatLng(1, -1).normalize === LatLng(1, -1))
    assert(LatLng(1, -180).normalize === LatLng(1, -180))
    assert(LatLng(1, -181).normalize === LatLng(1, 179))
    assert(LatLng(1, -361).normalize === LatLng(1, -1))
    assert(LatLng(1, 179).normalize === LatLng(1, 179))
    assert(LatLng(1, 180).normalize === LatLng(1, -180))
    assert(LatLng(1, 181).normalize === LatLng(1, -179))
  }

}