package chisellib

import chisel3._
import std_protocol_if.WishboneReqData

class BundleUtil extends Bundle {

  def assign_2b(o : BundleUtil) { // Really want this to be <this.type>
    val o_el = o.elements;
    
    for (i <- elements.iterator) {
      i._2 := o_el.get(i._1).get
    }
  }
  
  def assign_b2(o : BundleUtil) {
    val o_el = o.elements;
    
    for (i <- elements.iterator) {
      o_el.get(i._1).get := i._2
    }
  }
}

class ParametersUtil {
 
      def cloneType() = {
      println("cloneType");
      for (c <- this.getClass.getConstructors) {
        println("c: " + c)
        for (p <- c.getParameters) {
          println("p: " + p.getName)
        }
      }
//      (new WishboneParameters(ADDR_WIDTH, DATA_WIDTH,
//        TGA_WIDTH, TGD_WIDTH, TGC_WIDTH)).asInstanceOf[this.type]
    }
    
    cloneType()
    
}

class ParameterizedBundleUtil(val _p : Object) extends BundleUtil {
  
  override def cloneType() : this.type = {
    val constructor = this.getClass.getConstructors.head
    try {
      val args = Seq.fill(constructor.getParameterTypes.size)(_p)
      constructor.newInstance(_p).asInstanceOf[this.type]
    } catch {
      case e: java.lang.Exception => this
    }
  }
  
}