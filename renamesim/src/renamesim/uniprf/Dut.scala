package renamesim.uniprf

import renamesim.{Instruction, OpCode, archRegNum}

class Dut {
  val rob = new Rob
  val prf = new PhyRegFile
  val speculativeMapping = new MappingTable
  val architecturalMapping = new MappingTable
  val freeList = new FreeList

  def enq(inst: Instruction): Boolean = {
    inst match {
      case Instruction(src1, src2, dst, op) =>
        val entry = RobEntry.empty

        entry.valid = true
        entry.op = op
        entry.dst = dst
        /* 1. looking for pSrc */
        entry.pSrc1 = speculativeMapping.read(src1)
        entry.pSrc2 = speculativeMapping.read(src2)

        /* 2. looking for old pdest */
        entry.oldPDest = speculativeMapping.read(dst)

        /* 3. allocate a new reg as dst */
        val newPDest = freeList.alloc
        if (newPDest < 0) return false

        entry.pDest = newPDest

        /* 4. update spec pat */
        speculativeMapping.write(dst, newPDest)

        /* 5. write rob entry */
        val latestRobIdx = rob.enq(entry)
        assert(latestRobIdx >= 0)

        true
    }
  }

  def deq(): Unit = {
    val entry = rob.deq()
    entry match {
      case (_, None) => assert(false) /* This should not happen */

      case (robIdx, Some(robEntry)) => robEntry match {
        case RobEntry(valid, pSrc1, pSrc2, dst, pDest, oldPDest, op) =>
          assert(!valid && pSrc1 >= 0 && pSrc2 >= 0 && pDest >= 0 && oldPDest >= 0)

          /* read src val from prf using psrc
           * Note: we assume that src is ready */
          val src1Val = prf.read(pSrc1)
          val src2Val = prf.read(pSrc2)

          /* calculate dst value */
          val result = op match {
            case OpCode.Add =>
              src1Val + src2Val
            case OpCode.Sub =>
              src1Val - src2Val
          }

          /* write back to prf */
          prf.write(pDest, result)

          /* update arch mapping table */
          architecturalMapping.write(dst, pDest)

          /* return old pdest to freelist */
          freeList.release(oldPDest)
      }
    }
  }

  def arf(): Array[Int] = {
    Array.tabulate(archRegNum)(i => prf.read(architecturalMapping.read(i)))
  }

  override def toString: String = {
    s"""$rob
       |
       |$prf
       |
       |[Speculative] $speculativeMapping
       |
       |[Architectural] $architecturalMapping
       |
       |$freeList
       |""".stripMargin
  }
}
