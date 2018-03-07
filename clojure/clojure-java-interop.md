Objective:

Create a library (jar) that can be called from Java.  In this example
we'll create the following method signature:

* package com.oracle.git.aps

* public class ApsController

* public String CREATE_ACCOUNT (
  String user_name, 
  String password, 
  String role, 
  String isManager )

So first lets just get a basic library going.

```bash
$ lein new aps-cx
```

creates a new project and I get the file:

```
-- src
   `-- aps_cx
       `-- core.clj
```

with contents:

```clojure
(ns aps-cx.core)
(defn -main
  "I don't do a whole lot."
  [& args]
  (println "Hello, World!"))
```

We need to modify this a bit to look like:

```clojure
(ns aps-cx.core
  (:gen-class)
(defn -main
  "I don't do a whole lot."
  [& args]
  (println "Hello, World!"))
```

Also, `project.clj` should look like:

```clojure
(defproject aps-cx "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :main aps-cx.core)
```

Create the 'uberjar':

```bash
aps-cx $ lein uberjar
Compiling aps-cx.core
Compilation succeeded.
Created /home/fenton/projects/aps-cx/target/aps-cx-0.1.0-SNAPSHOT.jar
Including aps-cx-0.1.0-SNAPSHOT.jar
Including clojure-1.4.0.jar
Created /home/fenton/projects/aps-cx/target/aps-cx-0.1.0-SNAPSHOT-standalone.jar
```

Now run it from your command line:

```bash
aps-cx $ java -jar target/aps-cx-0.1.0-SNAPSHOT-standalone.jar aps-cx.core
Hello, World!
```

So lets get this to have correct package/class names.  Create the
file:

`src/com/oracle/git/aps/ApsController.clj`

```bash
aps-cx $ mkdir -p src/com/oracle/git/aps
```

update the `project.clj`:

```clojure
(defproject aps-cx "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :main com.oracle.git.aps.ApsController)
```

run with the new package/class names:

```bash
aps-cx $ lein clean; lein uberjar; java -jar target/aps-cx-0.1.0-SNAPSHOT-standalone.jar com.oracle.git.aps.ApsController
Hello, World!
```

Lets create a simple java project that drives this library.

```bash
projects $ mvn archetype:create -DgroupId=com.oracle.git.middleware -DartifactId=aps-cx-driver
```

Lets install our library to our local maven repo:

```bash
aps-cx $ lein install
Compiling com.oracle.git.aps.ApsController
Compilation succeeded.
Created /home/fenton/projects/aps-cx/target/aps-cx-0.1.0-SNAPSHOT.jar
Wrote /home/fenton/projects/aps-cx/pom.xml
```

Check that its there:

```bash
aps-cx $ ls -l ~/.m2/repository/com/oracle/git/middleware/aps-cx/0.1.0-SNAPSHOT/
total 20K
-rw-r--r-- 1 fenton users 6.8K Aug 28 12:40 aps-cx-0.1.0-SNAPSHOT.jar
-rw-r--r-- 1 fenton users 2.0K Aug 28 12:40 aps-cx-0.1.0-SNAPSHOT.pom
-rw-r--r-- 1 fenton users  717 Aug 28 12:40 maven-metadata-local.xml
-rw-r--r-- 1 fenton users  182 Aug 28 12:40 _maven.repositories
```

Lets reference this in the `pom.xml` of our Java driver program:

```xml
<project ... >
  ...
  <dependencies>
    ...
      <dependency>
      <groupId>com.oracle.git.middleware</groupId>
      <artifactId>aps-cx</artifactId>
      <version>0.1.0-SNAPSHOT</version>
    </dependency>
  </dependencies>
</project>
```

Lets setup the project so we can import into eclipse:

```bash
aps-cx-driver $ mvn eclipse:eclipse
```

In our Java driver we ensure to include at least the following:

```java
import com.oracle.git.aps.ApsController;
void doIt() {
  String [] args = new String[0];
  ApsController.main(args);
}
```

Now lets turn these into instance methods, not just the main method!
Add instance and static methods.  Rewrite ApsController to look like:

```clojure
(ns com.oracle.git.aps.ApsController
  (:gen-class
   :methods [[fenton [String] String]
             #^{:static true} [foo [int] String]] ))
(defn -fenton [this name] (str "Hello " name))
(defn -foo [nmbr] (str "Hello " nmbr))
```             

Thats it for now!
