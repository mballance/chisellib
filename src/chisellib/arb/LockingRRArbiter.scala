package chisellib.arb

import chisel3._
import chisel3.util._

private object ArbiterCtrl {
  def apply(request: Seq[Bool]): Seq[Bool] = request.length match {
    case 0 => Seq()
    case 1 => Seq(true.B)
    case _ => true.B +: request.tail.init.scanLeft(request.head)(_ || _).map(!_)
  }
}

abstract class LockingArbiterLike[T <: Data](gen: T, n: Int, count: Int, needsLock: Option[T => Bool]) extends Module {
  protected def grant: Seq[Bool]
  protected def choice: UInt
  val io = IO(new ArbiterIO(gen, n))

  io.chosen := choice
  io.out.valid := io.in(io.chosen).valid
  io.out.bits := io.in(io.chosen).bits

  if (count > 1) {
    val lockCount = Counter(count)
    val lockIdx = Reg(UInt())
    val locked = RegInit(Bool(), Bool(false)) // lockCount.value =/= 0.U
    val wantsLock = needsLock.map(_(io.out.bits)).getOrElse(true.B)

    when (io.out.fire()) {
      locked := Bool(true)
      lockIdx := io.chosen
//      when (wantsLock) {
//        lockIdx := io.chosen
//      } .otherwise {
//        locked := Bool(false)
//      }
//      lockCount.inc()
    } .otherwise {
      locked := Bool(false)
    }

    when (locked) { io.chosen := lockIdx }
    
    for ((in, (g, i)) <- io.in zip grant.zipWithIndex)
      in.ready := Mux(locked, lockIdx === i.asUInt, g) && io.out.ready
  } else {
    for ((in, g) <- io.in zip grant)
      in.ready := g && io.out.ready
  }
}

class LockingRRArbiter[T <: Data](gen: T, n: Int, count: Int, needsLock: Option[T => Bool] = None)
  extends LockingArbiterLike[T](gen, n, count, needsLock) {
  private lazy val lastGrant = RegEnable(io.chosen, 0.asUInt(), io.out.fire()) // Change to have reset value
  private lazy val grantMask = (0 until n).map(_.asUInt > lastGrant)
  private lazy val validMask = io.in zip grantMask map { case (in, g) => in.valid && g }

  override protected def grant: Seq[Bool] = {
    val ctrl = ArbiterCtrl((0 until n).map(i => validMask(i)) ++ io.in.map(_.valid))
    (0 until n).map(i => ctrl(i) && grantMask(i) || ctrl(i + n))
  }

  override protected lazy val choice = Wire(init=(n-1).asUInt)
  for (i <- n-2 to 0 by -1)
    when (io.in(i).valid) { choice := i.asUInt }
  for (i <- n-1 to 1 by -1)
    when (validMask(i)) { choice := i.asUInt }  
}