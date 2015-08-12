name := "CollectionLab"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

scalacOptions ++= Seq("-unchecked", "-deprecation" , "-feature")

mainClass in assembly := Some("collectionlab.Dummy")

jarName in assembly := "collectionlab.jar"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.+" % "test"

initialCommands in console := 
"""|
   |import collectionlab._
   |""".stripMargin

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
