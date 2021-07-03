package renamesim.inrob

import renamesim.archRegNum

class MappingTable {
  val tbl: Array[Option[Int]] = Array.fill(archRegNum)(None: Option[Int])

  def read(idx: Int): (Boolean, Int) = {
    require(idx < archRegNum)

    tbl(idx) match {
      case None => (false, idx)
      case Some(value) => (true, value)
    }
  }

  def write(idx: Int, robIdx: Int): Unit = {
    tbl(idx) = Some(robIdx)
  }

  def clear(idx: Int): Unit = {
    tbl(idx) = None
  }

  override def toString: String = {
    tbl.zipWithIndex map {
      case (None, idx) => s"$idx ARF"
      case (Some(value), idx) => s"$idx ROB#$value"
    } mkString("Mapping Table\n", "\n", "")
  }
}
