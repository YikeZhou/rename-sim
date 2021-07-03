package renamesim.inrob

import renamesim.OpCode

case class RobEntry(
                     var valid: Boolean,
                     var src1Val: Option[Int], var src1Idx: Int, /* index of rob entry */
                     var src2Val: Option[Int], var src2Idx: Int,
                     var dst: Option[Int], var dstIndex: Int, /* index of architectural register */
                     var op: OpCode.Value
                   ) {
  override def toString: String = {
    s"${if (valid) 'V' else '_'} | src1($src1Val, $src1Idx) | src2($src2Val, $src2Idx) | dst($dst, $dstIndex) | op(${op.toString}) |"
  }
}
