# Reading a resource file

File/directory layout:

```bash
$ tree
.
|-- pom.xml
|-- project.clj
|-- README.md
`-- src
    |-- test_project
    |   `-- Core.clj
    `-- test.txt
```

Setting up this to be a library for use in Java.  Here is my
`project.clj` and my `Core.clj`

```clojure
(defproject test-package/test-project "0.1.0-SNAPSHOT"
  :plugins [[lein-swank "1.4.4"]]
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :main test-project.Core)
```

```clojure
(ns test-project.Core
  (:gen-class
   :methods [[readFile [] String]]))
(defn read-file []
  (slurp (.getFile (clojure.java.io/resource "test.txt"))))
(defn -readFile [this]
  (read-file))
```

Now if I try to use this in the `REPL`

```clojure
test-project.Core> (read-file)
"abc\n"
```

Works no problem.  However when I try this from Java:

```java
Core c = new Core();
c.readFile();
```

A `FileNotFound` exception is thrown:

```java
java.io.FileNotFoundException: /home/fenton/.m2/repository/test-package/test-project/0.1.0-SNAPSHOT/test-project-0.1.0-SNAPSHOT.jar!/test.txt (No such file or directory)
```

Whereas:

```java
InputStream stream =
this.getClass().getClassLoader().getResourceAsStream("test.txt");
```

Finds the file no problem.  So whats the problem?

Here is the answer from google groups:

slurp is happy to slurp from a URL, no need for the (.getFile) call on 
the resource. In other words, the file returned for a resource that's 
been compiled into a jar isn't very useful. Stick with the URL 
returned bye clojure.java.io/resource. 

Dave 

https://groups.google.com/forum/?fromgroups=#!topic/clojure/Q-C83MrvNCE
