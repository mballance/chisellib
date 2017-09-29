package chisellib.blackbox

import firrtl.LowForm
import firrtl.CircuitForm
import firrtl.CircuitState
import firrtl.Transform

class SourceTransform extends Transform {
  override def inputForm: CircuitForm = LowForm
  override def outputForm: CircuitForm = LowForm
  
  override def execute(state: CircuitState): CircuitState = {
    getMyAnnotations(state) match {
      case Nil => state
      case myAnnotations =>
        state
    }
  }
  
}