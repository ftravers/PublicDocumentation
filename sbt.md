Learning to use sbt (Simple Build Tool) with IDEA IntelliJ

# Setup

[sbt setup instructions](https://github.com/harrah/xsbt/wiki/Setup)

Drop the sbt-launcher.jar file into `~/bin/`

Create the file: `~/bin/sbt`:

```
java -Xmx512M -jar `dirname $0`/sbt-launch.jar "$@"
```

Create the file: `~/.sbt/plugins/build.sbt`:

```scala
resolvers += "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"

libraryDependencies += "com.github.mpeltonen" %% "sbt-idea" % "0.10.0"
```

# Usage

Go into an empty folder that you intend to be your project folder and
type: `sbt`
