package renamesim.uniprf

import renamesim.OpCode

case class RobEntry(
                   var valid: Boolean,
//                   var src1Val: Option[Int], /* value of src1; None represents not ready */
                   var pSrc1: Int, /* index in PRF */
//                   var src2Val: Option[Int],
                   var pSrc2: Int,
//                   var dstVal: Option[Int], /* result of corresponding inst; None represents not ready */
                   var dst: Int,
                   var pDest: Int, /* new prf index mapped to ArchReg dst */
                   var oldPDest: Int, /* last prf index mapped to ArchReg dst */
                   var op: OpCode.Value
                   ) {
  override def toString: String = {
    s"${if (valid) 'V' else '_'} | src1($pSrc1) | src2($pSrc2) | dst($dst -> $pDest) old($oldPDest) | op(${op.toString}) |"
  }
}

object RobEntry {
  def empty: RobEntry = {
    new RobEntry(false, -1, -1, -1, -1, -1, OpCode.Add)
  }
}