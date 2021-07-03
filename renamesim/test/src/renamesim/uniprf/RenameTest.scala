package renamesim.uniprf

import org.scalatest.flatspec.AnyFlatSpec
import renamesim.instgen.InstructionGenerator
import renamesim.{Instruction, Ref, archRegNum, robSize}

import scala.collection.mutable
import scala.io.StdIn.readLine

class RenameTest extends AnyFlatSpec {
  private def debugInfo(dut: Dut, inst: Instruction, isDeq: Boolean): Unit = {
    if (isDeq) {
      println(s"Retire Instruction before rob#${dut.rob.eldest}")
    } else {
      println(s"Input Instruction: src(${inst.src1}, ${inst.src2}) dst(${inst.dst}) op(${inst.op})\n")
    }
    println(dut)
//    readLine() /* press enter to continue */
    ()
  }

  "Dut (Unified PRF)" should "rename instructions and commit correctly" in {
    val testSize = 10 /* test 10 instructions */
    val dut = new Dut
    val ref = new Ref(dut.arf())
    val gen = new InstructionGenerator(testSize)
    val instFlow = new mutable.Queue[Instruction]()
    instFlow.clear()

    /* 1. fill rob */
    for (_ <- 0 until robSize) {
      assert(gen.hasNext)

      dut.enq(gen.next())
      debugInfo(dut, gen.lastInst.get, isDeq = false)
      instFlow.enqueue(gen.lastInst.get)
    }

    /* 2. pop 1 inst then push 1 */
    while (gen.hasNext) {
      dut.deq()
      val inst = instFlow.dequeue()
      ref.exec(inst, dut.arf())
      debugInfo(dut, inst, isDeq = true)

      dut.enq(gen.next())
      debugInfo(dut, gen.lastInst.get, isDeq = false)
      instFlow.enqueue(gen.lastInst.get)
    }

    /* 3. clear rob */
    for (_ <- 0 until robSize) {
      dut.deq()
      val inst = instFlow.dequeue()
      ref.exec(inst, dut.arf())
      debugInfo(dut, inst, isDeq = true)
    }

    assert(instFlow.isEmpty)
  }
}
