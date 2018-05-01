// Writing Scala code for Carry-Select Adder
// note: this code uses a cond adder stage with Cin fixed to 0 at the 
// start, so there is an extra multiplexer delay not present in designs 
// otherwise.

// define each stage as having n bits, k stages
// total number of bits is m = k*n

package csa

import chisel3._
import chisel3.util.Cat

class Csa(val m: Int, val n: Int = 4) extends Module {
  val io = IO(new Bundle {
    val A    = Input(UInt(m.W))
    val B    = Input(UInt(m.W))
    val Sum  = Output(UInt(m.W))
    val Cout = Output(UInt(1.W))
  })

  //case 1:
  //total number of bits less than or equal to a csa group size
  if (m <= n) {
    //then use a n-bit adder, no use in using csa

    val adderm = Module(new AdderN(m))

    adderm.io.A := io.A
    adderm.io.B := io.B
    adderm.io.Cin := 0.asUInt(1.W)

    io.Sum := adderm.io.Sum
    io.Cout := adderm.io.Cout

  //case 2:
  //total number of bits greater than csa group size
  //if m%n == 0, # of full adder bits is n, number of groups is k=(m/n - 1)
  //if m%n != 0, # of full adder bits is m%n, number of stage groups is k=m/n
  } else {
    var k = 0
    var addersize = 0
    if (m % n == 0) {
      k = (m/n - 1)
      addersize = n
    } else {
      k = m/n
      addersize = m % n
    }
    val adderf = Module(new AdderN(addersize))
    adderf.io.A := io.A(addersize - 1, 0)
    adderf.io.B := io.B(addersize - 1, 0)
    adderf.io.Cin := 0.asUInt(1.W)

    val stage = VecInit(Seq.fill(k){ Module(new CsaStage(n)).io })
    val carry = Wire(Vec(k+1, UInt(1.W)))
    val sum = Wire(Vec(k, UInt(n.W)))

    carry(0) := adderf.io.Cout
    for(i <- 0 until k) {
      stage(i).A := io.A((i+1)*n + addersize - 1, i*n + addersize)
      stage(i).B := io.B((i+1)*n + addersize - 1, i*n + addersize)
      stage(i).Cin := carry(i)
      carry(i+1) := stage(i).Cout
      sum(i) := stage(i).Sum
    }
    
    io.Sum := Cat(Cat(sum), adderf.io.Sum)
    io.Cout := carry(k)
  }

}
