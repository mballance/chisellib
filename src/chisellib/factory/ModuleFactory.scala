package chisellib.factory

import chisel3._
import scala.collection.mutable.Map
import scala.collection.mutable.HashMap
import scala.reflect.ClassTag
import scala.reflect._
import scala.reflect.api._
import scala.reflect.runtime.universe._
import java.lang.reflect.Constructor

class ModuleFactory[T <: Module]()(implicit tag : ClassTag[T]) {
  
  def init() : ModuleFactory[T] = {
    this;
  }
 
  def apply(args : Object*) : T = {
      
    // returns the actual type
    ModuleFactory[T](this, args);
  }
  
  def create(args : Seq[Object]) : Module = {
    var c : Constructor[_] = null
    
    var ct_i =0
    var cls = tag.runtimeClass
    while (c == null && ct_i < cls.getConstructors.length) {
      val ct = cls.getConstructors()(ct_i)
      if (ct.getParameters.length == args.length) {
        var i=0
        var found=true
        while (found && i < args.length) {
          if (!ct.getParameters()(i).getType.isAssignableFrom(
              args(i).getClass())) {
            found = false;
          }
          i=i+1
        }
        if (found) {
          c = ct
        }
      }
      ct_i=ct_i+1
    }
    
    if (c == null) {
      System.out.println("Error: failed to find constructor for " + cls)
    }
    c.newInstance(args:_*).asInstanceOf[Module]
  }
  
//  def type_tag() : TypeTag[T] = tag
  
  def set_type_override[T2 <: T](t : ModuleFactory[T2]) {
    ModuleFactory.set_type_override(this, t);
  }

  // Register this type with the factory
  ModuleFactory.register_type(this);
}

object ModuleFactory {
  val m_map = new HashMap[ModuleFactory[_], ModuleFactory[_]]
  
  System.out.println("ModuleFactoryObject");
  
  def register_type[T <: Module](t : ModuleFactory[T]) {
    System.out.println("ModuleFactory.register_type " + t);
    if (!m_map.contains(t)) {
      m_map.put(t, t)
    }
  }
  
  def apply[T <: Module](t : ModuleFactory[T], args : Seq[Object]) : T = {
    
    if (m_map.contains(t)) {
      m_map.get(t).getOrElse(t).create(args).asInstanceOf[T]
    } else {
      throw new RuntimeException("Error");
    }
  }
  
  def set_type_override(t1 : ModuleFactory[_], t2 : ModuleFactory[_]) {
    m_map.remove(t1)
    m_map.put(t1, t2)
  }
}