//n-bit Adder

/* Adds N bits together using N full-adders.
 * Requires a Cin input bit that can be set externally.
 */

package csa

import chisel3._

class AdderN(val n: Int) extends Module { //assign input n as val member to use in tester
  val io = IO(new Bundle { 
    val A    = Input(UInt(n.W))
    val B    = Input(UInt(n.W))
    val Cin  = Input(UInt(1.W))
    val Sum  = Output(UInt(n.W))
    val Cout = Output(UInt(1.W))
  })

  //generate vector structure
  //val FAs = Vec.fill(n){ Module(new FullAdder()).io } deprecated function, newer function shown below
  val FAs = VecInit(Seq.fill(n){ Module(new FullAdder()).io })
  val carry = Wire(Vec(n+1, UInt(1.W)))
  val sum = Wire(Vec(n, Bool()))

  //generate connections
  carry(0) := io.Cin
  for(i <- 0 until n) {
    FAs(i).a   := io.A(i)
    FAs(i).b   := io.B(i)
    FAs(i).cin := carry(i)
    carry(i+1) := FAs(i).cout
    sum(i)     := FAs(i).sum.toBool()
  }

  io.Sum := sum.asUInt
  io.Cout := carry(n)
 
}
