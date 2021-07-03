package renamesim

import java.lang.Math.floorMod
import scala.util.Random

package object instgen {

  object Dependency extends Enumeration {
    val WAR, WAW, RAW = Value
  }

  def nextRegIdx: Int = {
    floorMod(Random.nextInt(), 4)
  }

  def nextDependencyType: Dependency.Value = {
    Dependency(floorMod(Random.nextInt(), 3))
  }

  def nextOpCode: OpCode.Value = {
    OpCode(floorMod(Random.nextInt(), 2))
  }
}
