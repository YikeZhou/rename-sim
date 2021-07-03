package renamesim

class Ref(initVal: Array[Int]) {
  require(initVal.length == archRegNum)

  private val reg = initVal

  def exec(inst: Instruction, dut: Array[Int]): Unit = {
    inst match {
      case Instruction(src1, src2, dst, OpCode.Add) => reg(dst) = reg(src1) + reg(src2)
      case Instruction(src1, src2, dst, OpCode.Sub) => reg(dst) = reg(src1) - reg(src2)
      case _ => /* only to satisfy compiler */
    }

    dut zip reg foreach {
      case (a, b) => assert(a == b)
    }
  }
}
