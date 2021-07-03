package renamesim.uniprf

import renamesim.{archRegNum, universalPrfSize}

class MappingTable {
  val tbl: Array[Int] = Array.range(0, archRegNum) /* Note: This is not necessary in real hardware */

  /**
   * given arch reg index, return phy reg index
   * @param idx arch index
   * @return real/phy reg index
   */
  def read(idx: Int): Int = {
    require(idx < archRegNum && idx >= 0)

    tbl(idx)
  }

  def write(idx: Int, value: Int): Unit = {
    require(idx < archRegNum && idx >= 0)
    require(value < universalPrfSize && value >= 0)

    tbl(idx) = value
  }

  override def toString: String = {
    tbl.zipWithIndex.map {
      case (pIdx, aIdx) => s"(arch)$aIdx -> $pIdx"
    } mkString("Mapping Table\n", "\n", "")
  }

}
