// 1-bit Full Adder

/* One-bit Full Adder with inputs a, b, cin, and outputs sum and cout
 */

package csa

import chisel3._

class FullAdder extends Module {
  val io = IO(new Bundle { //declare inputs and outputs
    val a    = Input(UInt(1.W))
    val b    = Input(UInt(1.W))
    val cin  = Input(UInt(1.W))
    val sum  = Output(UInt(1.W))
    val cout = Output(UInt(1.W))
  })

  val g = io.a & io.b
  val p = io.a ^ io.b

  io.sum  := p ^ io.cin
  io.cout := g | (io.cin & p)

}
