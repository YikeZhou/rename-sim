package renamesim

/**
 * Instructions before renaming
 * @param src1 reg index of src1
 * @param src2 reg index of src2
 * @param dst reg index of destination
 * @param op operation code
 */
case class Instruction(src1: Int, src2: Int, dst: Int, op: OpCode.Value) {
  override def toString: String = {
    op match {
      case OpCode.Add => s"%$dst = %$src1 + %$src2"
      case OpCode.Sub => s"%$dst = %$src1 - %$src2"
      case _ => ""
    }
  }
}

/* renamed instructions lie in ROB */