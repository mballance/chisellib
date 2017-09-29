package chisellib.blackbox

import firrtl.annotations.Named
import firrtl.annotations._

object SourceAnnotation {
  def apply(target: Named, value: String): Annotation = Annotation(target, classOf[SourceTransform], value)
  def unapply(a: Annotation): Option[(Named, String)] = a match {
    case Annotation(named, t, value) if t == classOf[SourceTransform] => Some((named, value))
    case _ => None
  }
}