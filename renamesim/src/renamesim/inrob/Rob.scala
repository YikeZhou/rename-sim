package renamesim.inrob

import renamesim.{OpCode, robSize}

class Rob {
  private val rob = Array.fill(robSize)(RobEntry(valid = false, None, -1, None, -1, None, -1, OpCode.Add))
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

  def writeBack(robIdx: Int, value: Int): Unit = {
    require(robIdx < robSize)

    rob.zipWithIndex foreach { case (entry, i) =>
      if (i == robIdx) {
        assert(!entry.valid && entry.dst.isEmpty)

        entry.dst = Some(value)
      } else if (entry.valid) {
        if (entry.src1Idx == robIdx) {
          assert(entry.src1Val.isEmpty)
          rob(i).src1Val = Some(value)
        }
        if (entry.src2Idx == robIdx) {
          assert(entry.src2Val.isEmpty)
          rob(i).src2Val = Some(value)
        }
      }
    }
  }

  override def toString: String = {
    rob.mkString("[Reorder Buffer]\n", "\n", "")
  }
}
