package renamesim

/**
 * Instructions before renaming
 * @param src1 reg index of src1
 * @param src2 reg index of src2
 * @param dst reg index of destination
 * @param op operation code
 */
case class Instruction(src1: Int, src2: Int, dst: Int, op: OpCode.Value)

/* renamed instructions lie in ROB */