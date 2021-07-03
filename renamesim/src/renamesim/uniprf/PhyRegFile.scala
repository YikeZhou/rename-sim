package renamesim.uniprf

import renamesim.universalPrfSize

import java.lang.Math.floorMod
import scala.util.Random

class PhyRegFile {
  /* Initialize with random digit (easy to track value change) */
  val rf: Array[Int] = Array.fill(universalPrfSize)(floorMod(Random.nextInt(), 10))

  def read(idx: Int): Int = {
    require(idx < universalPrfSize)
    rf(idx)
  }

  def write(idx: Int, value: Int): Unit = {
    require(idx < universalPrfSize)
    rf(idx) = value
  }

  override def toString: String = {
    rf.zipWithIndex.map { case (value, idx) =>
      s"#$idx: $value"
    }.mkString("[Physical Register File]\n", "\n", "")
  }
}
