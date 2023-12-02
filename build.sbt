
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.1"
ThisBuild / organization     := "com.soomo"
ThisBuild / organizationName := "soomo"

val zioV            = "2.0.13"
val zioConfigV      = "4.0.0-RC16"
val zioConfigTSV    = "4.0.0-RC16"
val zioConfigMagV   = "4.0.0-RC16"
val zioHttpV        = "0.0.5"
val zioJsonV        = "0.5.0"
val zioStreamsV     = "2.0.13"
val zioMock         = "1.0.0-RC9"
val zioTest         = "2.0.13"
val zioTestSbt      = "2.0.13"
val zioTestMagnolia = "2.0.13"
val zioLogV         = "2.1.13"
val zioLogSlf4jV    = "2.1.13"
val akkaV           = "2.8.1-M1"
val akkaTestkitV    = "2.8.1-M1"
val akkaTypedV      = "2.8.1-M1"
val quillV          = "4.6.0.1"
val flywayV         = "9.16.0"
val pgsqlV          = "42.5.4"
val jbcryptV        = "0.4"
val logbackClassicV = "1.4.7"
val bouncyCastleV   = "1.70"
val jwtCoreV        = "9.3.0"

lazy val root = (project in file("."))
  .settings(
    name := "OpenData",
    libraryDependencies ++= Seq(
      "dev.zio"              %% "zio"                   % zioV,
      "dev.zio"              %% "zio-config"            % zioConfigV,
      "dev.zio"              %% "zio-config-typesafe"   % zioConfigTSV,
      "dev.zio"              %% "zio-config-magnolia"   % zioConfigMagV,
      "dev.zio"              %% "zio-json"              % zioJsonV,
      "dev.zio"              %% "zio-http"              % zioHttpV,
      "dev.zio"              %% "zio-streams"           % zioStreamsV,
      "dev.zio"              %% "zio-mock"              % zioMock,
      "dev.zio"              %% "zio-test"              % zioTest         % Test,
      "dev.zio"              %% "zio-test-sbt"          % zioTestSbt      % Test,
      "dev.zio"              %% "zio-test-magnolia"     % zioTestMagnolia % Test,
      "dev.zio"              %% "zio-logging-slf4j"     % zioLogSlf4jV,
      "dev.zio"              %% "zio-logging"           % zioLogV,
      "com.typesafe.akka"    %% "akka-actor-typed"      % akkaTypedV,
      "com.typesafe.akka"    %% "akka-actor"            % akkaV,
      "com.typesafe.akka"    %% "akka-testkit"          % akkaTestkitV    % Test,
      "io.getquill"          %% "quill-jdbc-zio"        % quillV,
      "io.getquill"          %% "quill-jasync-postgres" % quillV,
      "org.flywaydb"          % "flyway-core"           % flywayV,
      "org.postgresql"        % "postgresql"            % pgsqlV,
      "org.mindrot"           % "jbcrypt"               % jbcryptV,
      "ch.qos.logback"        % "logback-classic"       % logbackClassicV,
      "org.bouncycastle"      % "bcprov-jdk15on"        % bouncyCastleV,
      "com.github.jwt-scala" %% "jwt-core"              % jwtCoreV
    ),
    Compile / mainClass := Some("com.soomo.opendata.Main")
  )
enablePlugins(JavaAppPackaging)
testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
