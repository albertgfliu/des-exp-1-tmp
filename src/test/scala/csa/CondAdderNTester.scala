package csa

import chisel3.iotesters
import chisel3.iotesters.{PeekPokeTester, Driver, ChiselFlatSpec}

class CondAdderNUnitTester(c: CondAdderN) extends PeekPokeTester(c) {
  for (t <- 0 until 50) {
    val A = rnd.nextInt(1 << c.n) //0 through (2^n - 1)
    val B = rnd.nextInt(1 << c.n)

    poke(c.io.A, A)
    poke(c.io.B, B)

    step(1)

    val result0 = A + B
    val result1 = A + B + 1

    //expect(c.io.Sum, (1 << c.n) % result)
    //val mask = BigInt("1"*c.n, 2) //n-bit 111...., base 2
    //expect(c.io.Sum, result &  mask)

    //conditional sum carry-in assumed 0
    expect(c.io.s0, result0 % (1 << c.n))
    expect(c.io.c0, ((1 << c.n) & result0) >> c.n)
  
    //conditional sum carry-in assumed 1
    expect(c.io.s1, result1 % (1 << c.n))
    expect(c.io.c1, ((1 << c.n) & result1) >> c.n)
  
  }
}

class CondAdderNTester extends ChiselFlatSpec {

  "Basic test using Driver.execute" should "be used as an alternative way to run specification" in {
    iotesters.Driver.execute(Array(), () => new CondAdderN(4)) {
      c => new CondAdderNUnitTester(c)
    } should be (true)
  }

}