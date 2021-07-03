package renamesim.inrob

import renamesim.{Instruction, OpCode}

/**
 * Containing ROB, ARF and Mapping Table
 * input: Instruction Object
 * output: Current state of each component
 */
class Dut {
  val rob = new Rob
  val arf = new ArchRegFile
  val tbl = new MappingTable

  def enq(inst: Instruction): Unit = {
    inst match {
      case Instruction(src1, src2, dst, op) =>
        val entry = RobEntry(valid = true, None, -1, None, -1, None, dst, op)
      /* 1. read src value */
        Seq(src1, src2).zipWithIndex foreach { case (src, id) =>
          tbl.read(src) match {
            case (false, arfIdx) =>
              if (id == 0) entry.src1Val = Some(arf.read(arfIdx))
              else entry.src2Val = Some(arf.read(arfIdx))
            case (true, robIdx) =>
              if (id == 0) entry.src1Idx = robIdx
              else entry.src2Idx = robIdx
          }
        }
      /* 2. Init rob entry */
        val latestRobIdx = rob.enq(entry)
        assert(latestRobIdx >= 0)
      /* 3. update Mapping Table */
        tbl.write(dst, latestRobIdx)
    }
  }

  def deq(): Unit = {
    val entry = rob.deq()
    entry match {
      case (_, None) => assert(false) /* This should not happen */

      case (robIdx, Some(robEntry)) => robEntry match {
        case RobEntry(valid, src1Val, _, src2Val, _, dst, dstIndex, op) =>
          assert(!valid && src1Val.nonEmpty && src2Val.nonEmpty && dst.isEmpty)

          /* calculate dst value */
          val result = op match {
            case OpCode.Add =>
              src1Val.get + src2Val.get
            case OpCode.Sub =>
              src1Val.get - src2Val.get
          }
          /* write back to other depending entries */
          rob.writeBack(robIdx, result)
          /* write back to arf */
          arf.write(dstIndex, result)
          /* update mapping table */
          tbl.clear(dstIndex)
      }
    }
  }

  override def toString: String = {
    s"""${rob.toString}
       |
       |${arf.toString}
       |
       |${tbl.toString}
       |""".stripMargin
  }
}
