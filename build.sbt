name := "pokemon-template-scala"
 
version := "1.0" 
      
lazy val `pokemon-template-scala` = (project in file(".")).enablePlugins(PlayJava)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
scalaVersion := "2.11.11"

libraryDependencies ++= Seq(guice)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

      