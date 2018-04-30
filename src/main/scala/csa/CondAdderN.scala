// n-bit Conditional Adder

/* Adds N bits together using two N full-adders.
 * Gives two outputs, one assuming Cin of 1 and other assuming Cin of 0.
 */

package csa

import chisel3._

class CondAdderN(val n: Int) extends Module {
  val io = IO(new Bundle {
    val A  = Input(UInt(n.W))
    val B  = Input(UInt(n.W))
    val s0 = Output(UInt(n.W))
    val c0 = Output(UInt(1.W))
    val s1 = Output(UInt(n.W))
    val c1 = Output(UInt(1.W))
  })


  //Adder assuming cin = 0
  val Adder0 = Module(new AdderN(n))

  Adder0.io.A := io.A
  Adder0.io.B := io.B
  Adder0.io.Cin := 0.asUInt(1.W)

  io.s0 := Adder0.io.Sum
  io.c0 := Adder0.io.Cout


  //Adder assuming cin = 1
  val Adder1 = Module(new AdderN(n))

  Adder1.io.A := io.A
  Adder1.io.B := io.B
  Adder1.io.Cin := 1.asUInt(1.W)

  io.s1 := Adder1.io.Sum
  io.c1 := Adder1.io.Cout


}
