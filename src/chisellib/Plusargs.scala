package chisellib



import scala.collection.mutable.ListBuffer

class Plusargs(args : Array[String]) {
  val plusargs = args.filter(_.startsWith("+")).map(p => p.substring(1))
  val nonplusargs = args.filter(!_.startsWith("+"))

  def hasPlusarg(v : String) : Boolean = {
    plusargs.filter(
        p => (p.startsWith(v) && (p.length() == v.length() || p.charAt(v.length()) == '='))
    ).length > 0
  }
 
  def plusarg(v : String) : String = {
    val l = plusargs.filter(
        p => (p.startsWith(v) && p.length() > v.length() && p.charAt(v.length()) == '=')
    ).map(p => p.substring(p.indexOf('=')+1))
    
    if (l.length > 0) l(0) else null
  }
  
  def plusarg(v : String, d : String) : String = {
    val f = plusarg(v);
    if (f != null) f else d
  }
  
  def plusargs(v : String) : Array[String] = {
    plusargs.filter(
        p => (p.startsWith(v) && p.length() > v.length() && p.charAt(v.length()) == '=')
    ).map(p => p.substring(p.indexOf('=')+1))
  }
  
  def plusargs(v : String, d : Array[String]) : Array[String] = {
    val f = plusargs(v);
    if (f.length > 0) f else d
  }
}

object Plusargs {
  
  def apply(args : Array[String]) : Plusargs = {
    new Plusargs(args)
  }
  
}