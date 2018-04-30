package csa

import chisel3._

import chisel3.iotesters
import chisel3.iotesters.{PeekPokeTester, Driver, ChiselFlatSpec}

class CsaUnitTester(c: Csa) extends PeekPokeTester(c) {

  val numtests = 300

  for (t <- 0 until numtests) {

    val A = rnd.nextInt(1 << c.m)
    val B = rnd.nextInt(1 << c.m)

    poke(c.io.A, A)
    poke(c.io.B, B)

    step(1)

    val result = A + B
  
    //Sum and Cout multiplexed depending on Cin
    expect(c.io.Sum, result % (1 << c.m))
    expect(c.io.Cout, ((1 << c.m) & result) >> c.m)
  }

}

class CsaTester extends ChiselFlatSpec {

  "Basic test using Driver.execute" should "be used as an alternative way to run specification" in {
    iotesters.Driver.execute(Array(), () => new Csa(32)) {
      c => new CsaUnitTester(c)
    } should be (true)
  }

}
