package csa

import chisel3.iotesters
import chisel3.iotesters.{PeekPokeTester, Driver, ChiselFlatSpec}

class AdderNUnitTester(c: AdderN) extends PeekPokeTester(c) {
  for (t <- 0 until 50) {
    val A = rnd.nextInt(1 << c.n) //0 through (2^n - 1)
    val B = rnd.nextInt(1 << c.n)
    val Cin = rnd.nextInt(2)

    poke(c.io.A, A)
    poke(c.io.B, B)
    poke(c.io.Cin, Cin)

    step(1)

    val result = A + B + Cin

    //expect(c.io.Sum, (1 << c.n) % result)
    //val mask = BigInt("1"*c.n, 2) //n-bit 111...., base 2
    //expect(c.io.Sum, result &  mask)

    expect(c.io.Sum, result % (1 << c.n))
    expect(c.io.Cout, ((1 << c.n) & result) >> c.n)
  
  }
}

class AdderNTester extends ChiselFlatSpec {

  "Basic test using Driver.execute" should "be used as an alternative way to run specification" in {
    iotesters.Driver.execute(Array(), () => new AdderN(4)) {
      c => new AdderNUnitTester(c)
    } should be (true)
  }

}