import AssemblyKeys._

seq(assemblySettings: _*)

name := "CollectionLab"

version := "v2014-05-15"

scalaVersion := "2.11.2"

scalacOptions ++= Seq("-unchecked", "-deprecation" , "-feature")

mainClass in assembly := Some("collectionlab.Dummy")

jarName in assembly := "collectionlab.jar"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.1.+" % "test"

libraryDependencies += "junit" % "junit" % "4.+" % "test"

initialCommands in console := """import collectionlab._"""

sourceGenerators in Compile <+= 
 (sourceManaged in Compile, version, name, jarName in assembly) map {
  (dir, version, projectname, jarexe) =>
  val file = dir / "collectionlab" / "MetaInfo.scala"
  IO.write(file,
  """package collectionlab
    |object MetaInfo { 
    |  val version="%s"
    |  val project="%s"
    |  val jarbasename="%s"
    |}
    |""".stripMargin.format(version, projectname, jarexe.split("[.]").head) )
  Seq(file)
}
