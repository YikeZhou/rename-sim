package renamesim.instgen

import renamesim.Instruction
import renamesim.OpCode

import scala.util.Random

class InstructionGenerator(instCnt: Int) extends Iterator[Instruction] {
  require(instCnt > 0)

  private var nextIdx = 0

  var lastInst: Option[Instruction] = None

  override def hasNext: Boolean = nextIdx < instCnt

  override def next(): Instruction = {
    require(hasNext)
    nextIdx += 1

    lastInst match {
      case None => /* Initial Instruction */
        lastInst = Some(Instruction(0, 1, 2, OpCode.Add)) /* TODO change into random inst */

      case Some(inst) => /* Following Instruction */
        val dep = nextDependencyType /* pick a dependency type */

        dep match {
          case Dependency.RAW =>
            lastInst = Some(Instruction(inst.dst, nextRegIdx, nextRegIdx, nextOpCode))
          case Dependency.WAR =>
            lastInst = Some(Instruction(nextRegIdx, nextRegIdx,
              if(Random.nextBoolean()) inst.src1 else inst.src2, nextOpCode))
          case Dependency.WAW =>
            lastInst = Some(Instruction(nextRegIdx, nextRegIdx, inst.dst, nextOpCode))
        }
    }
    lastInst.get
  }
}
