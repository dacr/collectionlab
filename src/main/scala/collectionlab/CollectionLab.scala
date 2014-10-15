/*
 * Copyright 2014 David Crosson
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package collectionlab

import collection.IndexedSeq
import collection.IndexedSeqLike

trait Cell[T,+V] {
  val time:T
  val value:V
  //override def toString()=s"Cell($time, $value)"
}

case class BasicCell(time:Long, value:Double) extends Cell[Long,Double]

case class OtherCell(time:Long, value:Double, count:Long) extends Cell[Long,Double]

//
//object Series {
//  def newBuilder[C <: Cell](name: String, tm: TimeModel, alias: String)(implicit builder: CellBuilder[C]): Builder[C, Series[C]] = {
//    new VectorBuilder mapResult { x: Vector[C] => fromVector(name, tm, alias, x) }
//  }
//
//  implicit def canBuildFrom[C <: Cell](implicit builder: CellBuilder[C]): CanBuildFrom[Series[_], C, Series[C]] =
//    new CanBuildFrom[Series[_], C, Series[C]] {
//      def apply(): Builder[C, Series[C]] = newBuilder("default", 1L, "default")
//      def apply(from: Series[_]): Builder[C, Series[C]] = newBuilder(from.name, from.tm, from.alias)
//    }
//
//}

class Series[+C](protected val backend:Vector[C]=Vector.empty)
//  extends IndexedSeq[C]
//  with IndexedSeqLike[C, Series[C]]
{
  
//  import collection.mutable.Builder
//  override protected[this] def newBuilder: Builder[C, Series[C]] = Series.newBuilder(name, tm, alias)

  
  def +[B>:C](that:B):Series[B] = new Series[B](backend :+ that)
  def ++[B>:C](that:Iterable[B]):Series[B] = new Series[B](backend ++ that)
  def filter(cdt: C => Boolean) = new Series[C](backend.filter(cdt))
  override def toString:String = {
    val max=5
    val name=getClass.getName().split("[.]").last
    if (backend.size<max ) s"$name(${backend.mkString(",")})"
    else s"$name(${backend.take(max).mkString(",")},...)"
  }
}

class NamedSeries[+C](val name:String) extends Series[C]



object CollectionLab {
  def main(args:Array[String]) {
    val ns = new NamedSeries[Cell[Long,Double]]("truc")
    
    val x = ns + BasicCell(10,32d) + BasicCell(15,6) + OtherCell(16,9,2)
    
    val fx = x.filter(_.value < 32)
    
    val as = ns ++ (new Series[Cell[Long,Double]]() + BasicCell(1,0))
    
  }
}
