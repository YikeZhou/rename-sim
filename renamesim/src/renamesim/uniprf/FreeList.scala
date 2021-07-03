package renamesim.uniprf

import renamesim.{archRegNum, universalPrfSize}

import scala.collection.mutable

class FreeList {
  val fifo = new mutable.Queue[Int]()

  for (i <- archRegNum until universalPrfSize) fifo.enqueue(i)

  def release(reg: Int): Unit = {
    assert(!fifo.contains(reg))
    fifo.enqueue(reg)
  }

  def alloc: Int = {
    if (fifo.isEmpty) -1 /* no free reg available */
    else fifo.dequeue()
  }

  override def toString: String = {
    "[Free List]\n" + fifo.mkString(", ") + "\n"
  }
}
