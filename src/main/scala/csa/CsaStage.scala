// n-bit CSA Stage

package csa

import chisel3._

class CsaStage(val n: Int) extends Module {
  val io = IO(new Bundle {
    val A    = Input(UInt(n.W))
    val B    = Input(UInt(n.W))
    val Cin  = Input(UInt(1.W))
    val Sum  = Output(UInt(n.W))
    val Cout = Output(UInt(1.W))
  })


  val ca = Module(new CondAdderN(n))
  ca.io.A := io.A
  ca.io.B := io.B
  
  //check using test harnesses if the Muxes work or not
  io.Sum  := Mux(io.Cin.toBool(), ca.io.s1, ca.io.s0)
  io.Cout := Mux(io.Cin.toBool(), ca.io.c1, ca.io.c0)

}  
