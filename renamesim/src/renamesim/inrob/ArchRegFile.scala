package renamesim.inrob

import renamesim.archRegNum

import java.lang.Math.floorMod
import scala.util.Random

class ArchRegFile {
  /* Initialize with random digit (easy to track value change) */
  val rf: Array[Int] = Array.fill(archRegNum)(floorMod(Random.nextInt(), 10))

  def read(idx: Int): Int = {
    require(idx < archRegNum)
    rf(idx)
  }

  def write(idx: Int, value: Int): Unit = {
    require(idx < archRegNum)
    rf(idx) = value
  }

  override def toString: String = {
    rf.zipWithIndex.map { case (value, idx) =>
      s"#$idx: $value"
    }.mkString("[Architecture Register File]\n", "\n", "")
  }
}
