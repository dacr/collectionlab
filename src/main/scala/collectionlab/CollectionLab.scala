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

import scala.collection._
import scala.collection.generic._
import collection.mutable.Builder
import collection.generic.CanBuildFrom
import collection.immutable.{ Vector, VectorBuilder }
import scala.annotation.tailrec
import scala.collection.generic.GenericCompanion
import scala.collection.mutable.{ Builder, ListBuffer }


trait Cell[T,+V] {
  val time:T
  val value:V
  override def toString()=s"Cell($time, $value)"
}

case class BasicCell(time:Long, value:Double) extends Cell[Long,Double]

case class OtherCell(time:Long, value:Double, count:Long) extends Cell[Long,Double]




//final class SeriesBuilder[C, Coll <: Series[C]](empty:Coll) extends Builder[C, Coll] {
//  protected var elems:Coll = empty
//  override def +=(cell: C): this.type = {elems = elems + cell ; this}
//  override def clear():Unit = {elems = empty}
//  override def result(): Coll = elems
//}




object Series extends IndexedSeqFactory[Series] {
//  def apply[C](): Series[C] = new Series()
//  implicit def canBuildFrom[C]: CanBuildFrom[Coll, C, Series[C]] = 
//    ReusableCBF.asInstanceOf[GenericCanBuildFrom[C]]
//  override def newBuilder[C]: Builder[C, Series[C]] = new SeriesBuilder(empty)  
//  override def empty[C]: Series[C] = Series[C]()
  
  implicit def canBuildFrom[A]: CanBuildFrom[Coll, A, Series[A]] = new GenericCanBuildFrom[A]
  def newBuilder[A] = new scala.collection.mutable.LazyBuilder[A,Series[A]] {
    def result = {
      val data = parts.foldLeft(List[A]()){(l,n) => l ++ n}
      new Series(data)
    }
  }
}




class Series[+C](protected val backend:List[C]=List.empty)
   extends AbstractSeq[C]
     with IndexedSeq[C]
     with GenericTraversableTemplate[C, Series]
     with IndexedSeqLike[C, Series[C]]
     with Serializable
{
  override def companion:GenericCompanion[Series] = Series
  override def apply(idx: Int): C = backend(idx)
  override def length: Int = backend.length
//  override def filter(cdt: C => Boolean):Series[C] = {
//    val builder = companion.newBuilder[C]
//    backend.filter(cdt).foreach{x => builder+=x}
//    builder.result()
//  }
//
//  
  def +[B>:C](that:B):Series[B] = new Series[B](backend :+ that)
  def ++[B>:C](that:Iterable[B]):Series[B] = new Series[B](backend ++ that)
  
  
  
  override def toString:String = {
    val max=5
    val name=getClass.getName().split("[.]").last
    if (backend.size<max ) s"$name(${backend.mkString(",")})"
    else s"$name(${backend.take(max).mkString(",")},...)"
  }
}



object NamedSeries extends IndexedSeqFactory[NamedSeries] {
//  def apply[C](): NamedSeries[C] = new NamedSeries("")
//  implicit def canBuildFrom[C]: CanBuildFrom[Coll, C, Series[C]] = 
//    ReusableCBF.asInstanceOf[GenericCanBuildFrom[C]]
//  override def newBuilder[C]: Builder[C, NamedSeries[C]] = new SeriesBuilder(empty)  
//  override def empty[C]: NamedSeries[C] = new NamedSeries[C]("")
  implicit def canBuildFrom[A]: CanBuildFrom[Coll, A, NamedSeries[A]] = new GenericCanBuildFrom[A]
  def newBuilder[A] = new scala.collection.mutable.LazyBuilder[A,NamedSeries[A]] {
    def result = {
      val data = parts.foldLeft(List[A]()){(l,n) => l ++ n}
      new NamedSeries("", data)
    }
  }
}

class NamedSeries[+C](val name:String, protected val backend:List[C]=List.empty)
     extends Series[C](backend)
     with IndexedSeq[C]
     with GenericTraversableTemplate[C, NamedSeries]
     with IndexedSeqLike[C, NamedSeries[C]]
     with Serializable {
  override def companion = NamedSeries
  }



object CollectionLab {
  def main(args:Array[String]) {
    val ns = new NamedSeries[BasicCell]("truc")
    
    val x = ns + BasicCell(10,32d) + BasicCell(15,6) //+ OtherCell(16,9,2)
    
    val fx = x.filter(_.value < 32)
    
    val as = fx ++ (new Series[BasicCell]() + BasicCell(1,0))
    
    println(fx)
    println(as)
  }
}
