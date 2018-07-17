package chisellib



import scala.collection.Map
import scala.util.matching.Regex
import java.util.regex.Pattern
import chisellib.factory.ModuleFactory

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
  
  def plusarg(v : String, d : Int) : Int = {
    val f = plusarg(v);
    if (f != null) {
      f.toInt
    } else {
      d
    }
  }
  
  def plusargs(v : String) : Array[String] = {
    plusargs.filter(
        p => (p.startsWith(v) && p.length() > v.length() && p.charAt(v.length()) == '=')
    ).map(p => p.substring(p.indexOf('=')+1))
  }
 
  /**
   * For each '+set_type_override', apply the relevant override 
   */
  def apply_overrides {
    for (o <- plusargs("+set_type_override")) {
      
    }
  }
 
  /**
   * plusarg_scanf
   * 
   * Scans the specified plusarg according to the specified 
   * pattern
   */
  def plusarg_scanf(
      p : String,
      t : String 
      ) : Seq[Object] = {
    var typelist = Array[String]()
    var pattern = ""
    var i=0
    var n_elems = 0;
    
    while (i < t.length()) {
      if (t.charAt(i) == '%') {
        i=i+1
        typelist = typelist :+ t.charAt(i).toString()
        if (t.charAt(i) == 'd') {
          pattern += "([0-9]+)"
        } else if (t.charAt(i) == 's') {
          pattern += "(\\S+)"
        } else {
          throw new RuntimeException("Unknown format specifier " ++ 
              t.charAt(i).toString())
        }
      } else {
        if (t.charAt(i) == '(' || t.charAt(i) == ')' ||
            t.charAt(i) == '*') {
          pattern += "\\"
          pattern += t.charAt(i)
        } else {
          pattern += t.charAt(i)
        }
      }
      i=i+1
    }
    var ret = Array[Object]()
    
    if (hasPlusarg(p)) {
      val regex_p = Pattern.compile(pattern)
      val regex_m = regex_p.matcher(plusarg(p))
     
      if (regex_m.find()) {
        for (i <- 0 until typelist.length) {
          val t = typelist(i)
          if (t.equals("d")) {
            try {
              ret = ret :+ new Integer(regex_m.group(i+1).toInt)
            } catch {
              case e : NumberFormatException => throw new RuntimeException(e.toString())
            }
          } else if (t.equals("s")) {
            ret = ret :+ new String(regex_m.group(i+1))
          }
        }
      } else {
        println("Find error")
      }
    }
    
    ret;
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

object PlusargsTests extends App {
  val p = Plusargs(args)

  val pv = p.plusarg_scanf("TOPOLOGY", "%d_%d_%dx%d")

  for (k <- pv) {
    println("option=" ++ k.toString())
  }
}

