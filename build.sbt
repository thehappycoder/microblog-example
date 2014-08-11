organization  := "co.cleancode"

version       := "0.1"

scalaVersion  := "2.10.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

EclipseKeys.withSource := true

resolvers += "spray" at "http://repo.spray.io/"

libraryDependencies ++= {
  val akkaV = "2.1.4"
  val sprayV = "1.1.1"
  Seq(
    "io.spray"               %   "spray-can"       % sprayV,
    "io.spray"               %   "spray-routing"   % sprayV,
    "io.spray"               %%  "spray-json"      % "1.2.6",
    "com.typesafe.slick"     %%  "slick"           % "2.0.3",
  	"ch.qos.logback"         %   "logback-classic" % "1.1.2",
  	"org.postgresql"         %   "postgresql"      % "9.2-1004-jdbc41",
  	"com.typesafe.akka"      %%  "akka-actor"      % akkaV,
  	"com.github.t3hnar"      %%  "scala-bcrypt"    % "2.4"
  )
}

Revolver.settings
