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

trait Cell[T, +V] {
  val time: T
  val value: V
  //override def toString() = s"Cell($time, $value)"
}

case class BasicCell(time: Long, value: Double) extends Cell[Long, Double]

case class OtherCell(time: Long, value: Double, count: Long) extends Cell[Long, Double]


object Series extends IndexedSeqFactory[Series] {
  implicit def canBuildFrom[A]: CanBuildFrom[Coll, A, Series[A]] = new GenericCanBuildFrom[A]
  def newBuilder[A] = new scala.collection.mutable.LazyBuilder[A, Series[A]] {
    def result = {
      val data = parts.foldLeft(List[A]()) { (l, n) => l ++ n }
      new Series(data)
    }
  }
}

class Series[+C](protected val backend: List[C] = List.empty)
    extends AbstractSeq[C]
    with IndexedSeq[C]
    with GenericTraversableTemplate[C, Series]
    with IndexedSeqLike[C, Series[C]]
    with Serializable {
  override def companion: GenericCompanion[Series] = Series
  override def apply(idx: Int): C = backend(idx)
  override def length: Int = backend.length
  
  def +[B >: C](that: B): Series[B] = {
    val sb = companion.newBuilder[B]
    backend.foreach(c => sb += c)
    sb += that
    sb.result()
  }
  def ++[B >: C](that: Iterable[B]): Series[B] = {
    val sb = companion.newBuilder[B]
    backend.foreach(c => sb += c)
    that.foreach(c => sb += c)
    sb.result()
  }

  override def toString: String = {
    val max = 5
    val name = getClass.getName().split("[.]").last
    if (backend.size < max) s"$name(${backend.mkString(",")})"
    else s"$name(${backend.take(max).mkString(",")},...)"
  }
}

object NamedSeries extends IndexedSeqFactory[NamedSeries] {
  
  implicit def canBuildFrom[A]: CanBuildFrom[Coll, A, NamedSeries[A]] = new GenericCanBuildFrom[A]
  def newBuilder[A] = new scala.collection.mutable.LazyBuilder[A, NamedSeries[A]] {
    def result = {
      val data = parts.foldLeft(List[A]()) { (l, n) => l ++ n }
      new NamedSeries("", data)
    }
  }
}

class NamedSeries[+C](val name: String, backend: List[C] = List.empty)
    extends Series[C](backend)
    with IndexedSeq[C]
    with GenericTraversableTemplate[C, NamedSeries]
    with IndexedSeqLike[C, NamedSeries[C]]
    with Serializable {
  override def companion = NamedSeries
  
  override def toString: String = {
    val max = 5
    val sname = getClass.getName().split("[.]").last
    if (backend.size < max) s"$sname($name, ${backend.mkString(",")})"
    else s"$sname($name, ${backend.take(max).mkString(",")},...)"
  }
}

object CollectionLab {
  def main(args: Array[String]) {
    val ns = new NamedSeries[BasicCell]("truc")

    val x = ns + BasicCell(10, 32d) + BasicCell(15, 6) + OtherCell(16,9,2)

    val fx = x.filter(_.value < 32)

    val as = fx ++ (new Series[BasicCell]() + BasicCell(1, 0))

    println(fx)
    println(as)
  }
}
