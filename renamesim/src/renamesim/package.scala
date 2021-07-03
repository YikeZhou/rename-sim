package object renamesim {
  def archRegNum = 4
  def robSize = 5

  object OpCode extends Enumeration {
    val Add, Sub = Value
  }
}
