package renamesim

import org.scalatest.flatspec.AnyFlatSpec
import renamesim.instgen.InstructionGenerator

import scala.io.StdIn.readLine

class RenameTest extends AnyFlatSpec {
  private def debugInfo(dut: Dut, inst: Instruction, isDeq: Boolean): Unit = {
    if (isDeq) {
      println(s"Retire Instruction at rob#${dut.rob.eldest}")
    } else {
      println(s"Input Instruction: src(${inst.src1}, ${inst.src2}) dst(${inst.dst}) op(${inst.op})\n")
    }
    println(dut)
    readLine() /* press enter to continue */
    ()
  }

  "Dut" should "rename instructions and commit" in {
    /* TODO add checker in arf when deq */
    val testSize = 10 /* test 10 instructions */
    val dut = new Dut
    val ref = new Ref(dut.arf.rf)
    val gen = new InstructionGenerator(testSize)

    /* 1. fill rob */
    for (_ <- 0 until robSize) {
      assert(gen.hasNext)
      dut.enq(gen.next())
      debugInfo(dut, gen.lastInst.get, isDeq = false)
    }

    /* 2. pop 1 inst then push 1 */
    while (gen.hasNext) {
      dut.deq()
      ref.exec(gen.lastInst.get, dut.arf)
      debugInfo(dut, gen.lastInst.get, isDeq = true)
      dut.enq(gen.next())
      debugInfo(dut, gen.lastInst.get, isDeq = false)
    }

    /* 3. clear rob */
    for (_ <- 0 until robSize) {
      dut.deq()
      ref.exec(gen.lastInst.get, dut.arf)
      debugInfo(dut, gen.lastInst.get, isDeq = true)
    }
  }
}
