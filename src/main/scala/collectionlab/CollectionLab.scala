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


trait Cell[T,+V] {
  val time:T
  val value:V
}

case class BasicCell(time:Long, value:Double) extends Cell[Long,Double]


class Series[+C](backend:Vector[C]=Vector.empty) {
  def +[B>:C](that:B):Series[B] = new Series[B](backend :+ that)
}



object CollectionLab {
  def main(args:Array[String]) {
    val s = new Series[Cell[Long,Double]]()
    
    val x = s + BasicCell(10,32d)
    
    println(x)
  }
}
