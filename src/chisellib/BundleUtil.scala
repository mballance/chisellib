package chisellib

import chisel3._

class BundleUtil extends Bundle {

  def assign_2b(o : BundleUtil) { // Really want this to be <this.type>
    val o_el = o.elements;
    
    for (i <- elements.iterator) {
      i._2 := o_el.get(i._1).get
    }
  }
  
  def assign_b2(o : BundleUtil) {
    
    if (o.isInstanceOf[this.type]) {
    val o_el = o.elements;
    for (i <- elements.iterator) {
      o_el.get(i._1).get := i._2
    } 
    } else {
      println("Error: assign_b2 type mismatch")
    }
  }
}

class ParametersBase {
 
  def cloneType() = {
      println("cloneType");
      for (c <- this.getClass.getConstructors) {
        println("c: " + c)
        for (p <- c.getParameters) {
          println("p: " + p.getName)
        }
      }
  }
}

class ParameterizedBundle(val p : Object) extends BundleUtil {
  
  override def cloneType() : this.type = {
    val constructor = this.getClass.getConstructors.head
    try {
      val args = Seq.fill(constructor.getParameterTypes.size)(p)
      constructor.newInstance(p).asInstanceOf[this.type]
    } catch {
      case e: java.lang.Exception => this
    }
  }
  
}