package csa

import chisel3.iotesters
import chisel3.iotesters.{PeekPokeTester, Driver, ChiselFlatSpec}

/* Full adder tester taken from: https://github.com/ucb-bar/chisel-tutorial/blob/release/src/test/scala/examples/FullAdderTests.scala
 * My implementation of Full Adder differs in source code
 */

class FullAdderUnitTester(c: FullAdder) extends PeekPokeTester(c) {
  for (t <- 0 until 50) {
    val a    = rnd.nextInt(2)
    val b    = rnd.nextInt(2)
    val cin  = rnd.nextInt(2)
    val res  = a + b + cin
    val sum  = res & 1
    val cout = (res >> 1) & 1

    poke(c.io.a, a)
    poke(c.io.b, b)
    poke(c.io.cin, cin)

    step(1)
    
    expect(c.io.sum, sum)
    expect(c.io.cout, cout)
  }
}


/**
  * This is adapted from GCDTester
  * From command line, run
  * sbt 'testOnly csa.FullAdderTester'
  */


class FullAdderTester extends ChiselFlatSpec {

  "Basic test using Driver.execute" should "be used as an alternative way to run specification" in {
    iotesters.Driver.execute(Array(), () => new FullAdder) {
      c => new FullAdderUnitTester(c)
    } should be (true)
  }

  //chisel3.Driver.execute(Array[String](), () => new csa.FullAdder)
  //run this in the sbt's console

  //OR

  // object HelloWorld extends App {
  //chisel3.Driver.execute(args, () => new HelloWorld)
  //}

  //OR (untested)
  //sbt "run --backend v --genHarness"

}