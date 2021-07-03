package renamesim.uniprf

import renamesim.{OpCode, robSize}

class Rob {
  private val rob = Array.fill(robSize)(RobEntry.empty)
  var eldest = 0

  def enq(robEntry: RobEntry): Int = {
    val next = rob.indexWhere(!_.valid) /* first empty entry */
    if (next < 0) /* rob is full */ return -1

    rob(next) = robEntry

    next
  }

  def deq(): (Int, Option[RobEntry]) = {
    /* Find the eldest inst, not the first valid inst */
    val head = eldest
    eldest = (eldest + 1) % robSize

    if (head < 0) {
      (0, None)
    } else {
      rob(head).valid = false
      (head, Some(rob(head)))
    }
  }

  override def toString: String = {
    rob.mkString("[Reorder Buffer]\n", "\n", "")
  }
}
