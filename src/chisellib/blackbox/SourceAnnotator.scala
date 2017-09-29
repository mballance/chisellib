package chisellib.blackbox

import chisel3._
import chisel3.core._
import chisel3.internal.InstanceId

trait SourceAnnotator {
  self: BlackBox =>
    
  def source(component : InstanceId, value: String): Unit = {
    annotate(ChiselAnnotation(component, classOf[SourceTransform], value))
  }
  
}