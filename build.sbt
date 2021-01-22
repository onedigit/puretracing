inThisBuild(
  Seq(
    scalaVersion := "2.13.4",
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.3" cross CrossVersion.full)
  )
)

lazy val catsEffectVersion                 = "2.3.1"
lazy val catsMtlCoreVersion                = "0.7.1"
lazy val openTracingApiVersion             = "0.33.0"
lazy val sttpCoreVersion                   = "1.7.2"
lazy val jaegerClientVersion               = "1.5.0"
lazy val asyncHttpClientBackendCatsVersion = "1.7.2"

lazy val api = project

lazy val cats = project
  .dependsOn(api)
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-mtl-core" % catsMtlCoreVersion,
      "org.typelevel" %% "cats-effect"   % catsEffectVersion
    )
  )
lazy val `cats-opentracing` =
  project.dependsOn(cats).settings(libraryDependencies += "io.opentracing" % "opentracing-api" % openTracingApiVersion)

lazy val sttp =
  project.dependsOn(api).settings(libraryDependencies += "com.softwaremill.sttp" %% "core" % sttpCoreVersion)

lazy val exampleLib = project
  .in(file("examples/lib"))
  .dependsOn(cats)
  .settings(libraryDependencies += "org.typelevel" %% "cats-effect" % catsEffectVersion)

lazy val exampleAppNoTrace = project.in(file("examples/app-no-trace")).dependsOn(exampleLib)

lazy val examplePrintlnTracing = project.in(file("examples/app-println-tracing")).dependsOn(exampleLib)

lazy val exampleOpenTracing = project
  .in(file("examples/app-open-tracing"))
  .dependsOn(exampleLib, `cats-opentracing`)
  .settings(libraryDependencies += "io.jaegertracing" % "jaeger-client" % jaegerClientVersion)

lazy val exampleSttpCats = project
  .in(file("examples/app-sttp"))
  .dependsOn(sttp, `cats-opentracing`)
  .settings(
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp" %% "async-http-client-backend-cats" % asyncHttpClientBackendCatsVersion,
      "io.jaegertracing"       % "jaeger-client"                  % jaegerClientVersion
    )
  )
