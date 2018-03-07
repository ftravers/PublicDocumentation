# Installation

Maybe none of the below, until leiningen is needed as `lein deps`
seems to bring down the required clojure 1.2 files into the maven
repository.

[download clojure & clojure-contrib 1.2 for now](http://clojure.org/downloads)

```bash
$ mkdir ~/bin/; cd ~/bin/
$ curl http://repo1.maven.org/maven2/org/clojure/clojure/1.4.0/clojure-1.4.0.zip --O clojure-1.4.0.zip
$ unzip clojure-1.4.0.zip; rm -f clojure-1.4.0.zip
$ cd; mkdir .clojure; cp ~/bin/clojure-1.4.0/clojure-1.4.0.jar ~/.clojure/
```

## Leiningen:

```bash
$ cd ~/bin; wget -c https://raw.github.com/technomancy/leiningen/stable/bin/lein; chmod a+x lein; ./lein self-install
```

Add `lein` to your path, put a line like the following into `~/.bashrc`:

    export PATH=$PATH:~/bin


## Emacs setup

[ref](https://github.com/technomancy/swank-clojure)

Referencing the URL above, you are going to install `clojure-mode`.

### New Project

Then create a project with (I'm calling my project: `my-project`):

```bash
$ lein new my-project
$ cd my-project
```

put `[lein-swank "1.4.4"]` into the `:plugins` section of
`project.clj`, here is a sample:

```clojure
(defproject my-project "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :plugins [[lein-swank "1.4.4"]]                 
  :dependencies [[org.clojure/clojure "1.4.0"]])
```

Now pull in dependencies:

```
$ lein deps
```

### Basic Workflow

Fire up Emacs

Load the file you want to work in

Fire up swank/slime: `A-x clojure-jack-in`

`C-c C-k` in your file to compile your changes and send them to the
repl.

In file you are editing do: `C-c A-p`, you will be prompted for
namespace of that file to switch to.  Press `Enter` to accept.

Now you can modify your file and the changes will be reflected in your REPL

### Editing sexp's in Emacs

Check out
[this good tutorial](http://cl-cookbook.sourceforge.net/emacs-ide.html#Slide-10)
for learning how to edit sexp's.

### Use libraries

Here we'll add in the use of an XML library, org.clojure/data.zip "0.1.0".

Change `project.clj` to look like: 

```clojure
(defproject test3 "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :plugins [[lein-swank "1.4.4"]]                 
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/data.zip "0.1.0"]])
```

Now edit file: `my-project/src/my-project/core.clj`, changing the top
of the file too look like:

```
(ns my-project.core
  (:use [clojure.data.zip.xml]))
```




[ref](http://tromey.com/elpa/install.html)

Setup ELPA for emacs.  Throw the following into any emacs buffer:

```
(let ((buffer (url-retrieve-synchronously
	       "http://tromey.com/elpa/package-install.el")))
  (save-excursion
    (set-buffer buffer)
    (goto-char (point-min))
    (re-search-forward "^$" nil 'move)
    (eval-region (point) (point-max))
    (kill-buffer (current-buffer))))
```

Type `C-x C-e` after it.  Once done, type: `M-x
package-list-packages`.  Press `i` next to the packages:

    clojure-mode, clojure-test-mode, paredit, slime, slime-repl, swank-clojure

Then press `x` to install them.

[ref](http://data-sorcery.org/2009/12/20/getting-started/)

# Getting started

[Reference](http://corfield.org/blog/post.cfm/getting-started-with-clojure)

    $ cd ~/tmp; lein new test1

Insert contents *like* the following into the project file: `~/tmp/test1/project.clj`:

```
(defproject test1 "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.4.0"]]                 
  :plugins [[lein-swank "1.4.4"]]                 
  :dev-dependencies [[swank-clojure "1.3.4"]])
```

Retrieve dependencies, and start swank server:

    $ cd test1; lein deps; lein swank

In emacs attach to swank: `M-x slime-connect`

Append the following to file: `src/test-project/core.clj`:

    (System/getProperty "java.class.path")
    
And do a `C-x C-e` to evaluate it and see that you get the correct
results.

## Workflow

Fire up Emacs

Load the file you want to work in

Fire up swank/slime: `A-x clojure-jack-in`

`C-c C-k` in your file to compile your changes and send them to the
repl.

In file you are editing do: `C-c A-p`, you will be prompted for
namespace of that file to switch to.  Press `Enter` to accept.

Now you can modify your file and the changes will be reflected in your REPL



`C-c C-z` will take you to the repl.  It should have the correct
namespace there now.


```
;; Auto reload saved source files and send them to the repl
(defn ed/clojure-compile-on-save (&optional args)
  "Compile with slime on save"
  (interactive)
  (if (and (eq major-mode 'clojure-mode)
      (slime-connected-p))
      (slime-compile-and-load-file)))
(add-hook 'after-save-hook 'ed/clojure-compile-on-save)
```

[Ref](https://github.com/technomancy/swank-clojure)

what         | effect
------------ | -------------------------------
A-.          | to go to a definition.
A-,          | return from definition above.
C-c C-k      | to compile the current buffer.
A-p & A-n    | to go forwards and backward in REPL history.
<tab>        | for completion.
C-c I        | Inspect a variable
C-c C-w c    | List all callers of a given function

[Slime Manual](http://common-lisp.net/project/slime/doc/html/)

## Best Practices

### Source Files

You can provide documentation about your source file like so:

```clojure
(ns
```

## Main method and Java AOT Compile Classes

AOT = Ahead of Time

File: `src/test-project/core.clj`, contents:

```
(ns test1.core
  (:gen-class))
(defn greet[who] (println "Hello" who "!"))
(defn -main[] (greet "Fenton"))
```

Run it.

```
$ lein run -m test1.core
Hello Fenton !
```

Now let's modify our script so we can compile it and run it via Java.
Add `:main test1.core` to the end of `project.clj`:

```
(defproject test1 "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]]
  :dev-dependencies [[swank-clojure "1.3.4"]]
  :main test1.core)
```

Create a single **big** (3.6M!!!) jar file:

    lein uberjar

Run it from java:

    $ java -cp test1-1.0.0-SNAPSHOT-standalone.jar test1.core

# Testing

[ref](http://blog.darevay.com/2010/10/remedial-clojure-leiningen-lazytest-and-some-code/)

    test1/project.clj

```
(defproject test1 "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]]
  :dev-dependencies [[swank-clojure "1.3.4"]
                     [com.stuartsierra/lazytest "1.1.2"]]
  :repositories {"stuartsierra-releases" "http://stuartsierra.com/maven2"}
  :main test1.core)
```

    test1/test/test1/test/core.clj

```
(ns test1.test.core
  (:use [test1.core])
  (:use [lazytest.describe :only (describe it)]))
(describe create-xml
          (it "creates a tag pair with no body"
              ( = "<the-tag></the-tag>" (create-xml "the-tag")))
          (it "takes a tag and a body and creates the xml fragment"
              ( = "<the-tag>The body</the-tag>" (create-xml "the-tag" "The Body"))))
```

The way to use `describe` and `it` is first you put the function you
want to test after `describe`, then after `it`, you indicate what your
test should be doing, then you do it.  Your test should evaluate to true.

Now lets put Lazytest in “watch” mode so every time we save a file,
it’ll re-run our tests automatically.

```bash
$ cd test1
$ java -cp "src:test:lib/*:lib/dev/*" lazytest.watch src test
```

This tells Lazytest to watch for changes in the src/ and test/
directories.

    test1/src/test1/core.clj
    
```
(ns test1.core)
(defn create-xml
  "creates an xml node"
  [tag]
  (str "<" tag ">" "</" tag ">"))
```

So the above gets us through one of our tests, but we still fail the
second test.  The one that takes the body part too.  So lets extend
our function.

```
(ns test1.core)
(defn create-xml
  "creates an xml node"
  [tag & body]
  (if (= 1 (count body))
    (str "<" tag ">" (first body) "</" tag ">")
    (str "<" tag ">" "</" tag ">")))
```

Our tests now both pass!  Goto
[http://clojuredocs.org/](http://clojuredocs.org/) and search `str`,
`count`, and `first` to see what those functions do.

# Calling Clojure from Java

## Bare Bones

Making a clojure library you can use from java

```
(ns test1.core
  (:gen-class
   :name test1.core
   :methods [#^{:static true} [greet [] String]]))

(defn greet[] (println "Hello Fenton!"))

(defn -greet
  "A 'static' wrapper around 'greet' function."
  []
  (greet))
```

Corresponding calling java code:

```java
import test1.core;
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( core.greet() );
    }
}
```

## Adding input params

```
(ns test1.core
  (:gen-class
   :name test1.core
   :methods [#^{:static true} [greet [String] String]]))

(defn greet[name] (println "Hello" name "!"))

(defn -greet
  "A 'static' wrapper around 'greet' function."
  [name]
  (greet name))
```

## Deploy to Maven repo

Install to your local repo:

```bash
$ lein install
Compiling test1.core
Compilation succeeded.
Created /home/fenton/tmp/test1/test1-1.0.0-SNAPSHOT.jar
Wrote pom.xml
[INFO] Installing /home/fenton/tmp/test1/test1-1.0.0-SNAPSHOT.jar to /home/fenton/.m2/repository/test1/test1/1.0.0-SNAPSHOT/test1-1.0.0-SNAPSHOT.jar
fenton@fenton-Latitude-E6320 ~/tmp/test1 $
```

Rsync up to your private maven repo

    rsync -avP --stats ~/.m2/repository/test1 root@linux1.hk.oracle.com:/var/www/html/maven2/
    
In your java/maven project refer to this artifact as:

    pom.xml

```
    <dependency>
      <groupId>test1</groupId>
      <artifactId>test1</artifactId>
      <version>1.0.0-SNAPSHOT</version>
    </dependency>
```

```java
import test1.core;
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( core.greet("Fenton") );
    }
}
```


# Installation

[Ref Website](http://riddell.us/ClojureSwankLeiningenWithEmacsOnLinux.html)

This tutorial is provided as a very specific approach to installing a
clojure environment on linux. Although an attempt is made to keep it
up to date, the various technologies used are moving very fast.  The
following list contains links to the offical documentation of the most
relevant packages.

[Tutorial](http://java.ociweb.com/mark/clojure/article.html)

* Clojure Wiki–Getting Started:
    http://www.assembla.com/wiki/show/clojure/Getting\_Started
* Leiningen: http://github.com/technomancy/leiningen

## Command line support

Install Java, Ant, Maven, Git

    $ sudo apt-get install sun-java6-jdk ant maven2 git-core

### Install Clojure

```bash
$ mkdir ~/bin
$ cd ~/bin; git clone git://github.com/clojure/clojure.git
$ cd ~/bin/clojure; ant
$ mkdir ~/.clojure; cp clojure.jar ~/.clojure
```

* Test Clojure

Gentlemen, start your REPLs.

```bash
$ cd ~/.clojure; java -cp clojure.jar clojure.main
user=> (+ 1 41) 
42
```

Ctrl-d will exit the REPL.

### Install clojure-contrib

```bash
$ cd ~/bin; git clone git://github.com/clojure/clojure-contrib.git 
$ cd ~/bin/clojure-contrib; mvn install; cp modules/complete/target/complete*.jar ~/.clojure/clojure-contrib.jar 
```

* Configure Bash Start-up Script

clojure-contrib contains a bash script called clj-env-dir for starting
clojure with various options. Edit your \~/.bashrc file to configure
this script.

```bash
export CLOJURE_EXT=~/.clojure
export PATH=$PATH:~/bin/clojure-contrib/launchers/bash
alias clj=clj-env-dir
```

The last line added to the file above sets an alias to the clj-env-dir
script. This example uses clj but it could be set to anything.

See the file at `~/bin/clojure-contrib/launchers/bash/clj-env-dir` for
more options.

### Add JLine support

* Download JLine from `http://jline.sourceforge.net/` and unzip and
  copy jar to the `~/.clojure` directory.

```bash
cd ~/Downloads; unzip jline-1.0.zip; cp jline-1.0/jline-1.0.jar ~/.clojure
```

* Modify the last line in
  `~/bin/clojure-contrib/launchers/bash/clj-env-dir` by adding
  jline.ConsoleRunner to add JLine functionality.

```bash
$ vi ~/bin/clojure-contrib/launchers/bash/clj-env-dir
exec $JAVA $OPTS jline.ConsoleRunner $MAIN "$@"
```

* Test the Script

To test the new script and verify access to the clojure-contrib
library, open a new terminal window and try this:

    $ clj
    user=> (System/getProperty "java.class.path")

If any other jars are needed either copy or link them to the  
`~/.clojure` directory.

### Emacs support

* Install Emacs

```bash
$ sudo apt-get install emacs-snapshot-gtk
```

* download Slime & clojure-mode

```bash
$ cd ~/bin; git clone git://github.com/nablaone/slime.git; git clone git://github.com/technomancy/clojure-mode.git
```

### Install leiningen

```bash
$ cd ~/bin; mkdir leiningen; cd leiningen; wget --no-check-certificate http://github.com/technomancy/leiningen/raw/stable/bin/lein; chmod +x lein; ./lein self-install
```

* Add the following to .bashrc.

```bash
export PATH=$PATH:~/bin/leiningen
```

### Configure Emacs

* Add these specifics to the .emacs file.

```
;; clojure-mode
(add-to-list 'load-path "~/bin/clojure-mode")
(require 'clojure-mode)
;; slime
(eval-after-load "slime" 
  '(progn (slime-setup '(slime-repl))))
(add-to-list 'load-path "~/bin/slime")
(require 'slime)
(slime-setup)
```

### Test Configuration

#### Create a test project.

```bash
$ rm -rf ~/tmp; mkdir ~/tmp; cd ~/tmp; lein new test-project; cd test-project; emacs project.clj
```

* Add the following:

```
(defproject test-project "0.1.0"
  :description "Test Project"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]]
  :dev-dependencies [[swank-clojure "1.2.1"]])
```

* Save and exit the file.

```bash
$ lein deps
$ lein swank
```

* Open a new terminal and open the generated file in emacs:

```bash
$ emacs ~/tmp/test-project/src/test_project/core.clj
```

* Connect to the running swank server:

```
M-x slime-connect
```

Add some code to the file:

```
(System/getProperty "java.class.path")
```

And then at the end of the line, evaluate:

```
C-x C-e
```

The output will show the configured jar files and their associated
paths on the Java classpath.

Lastly, compile the file:

```
C-c C-k
```

Keyboard Shortcut | Effect
----------------- | ----------------
C-c C-k           | Compile the current buffer
C-x C-e           | Evaluate the expression under the cursor

## Installation Method 2

source:
[http://www.assembla.com/wiki/show/clojure/Getting\_Started\_with\_Emacs](http://www.assembla.com/wiki/show/clojure/Getting_Started_with_Emacs)

* Use the .emacs starter kit

    $ cd ~
    $ git clone https://github.com/technomancy/emacs-starter-kit.git
    $ mv ~/.emacs.d ~/.emacs.d.orig
    $ mv ~/emacs-starter-kit ~/.emacs.d

* Start Emacs

do: `Alt-x package-list-packages` and select clojure-mode (press
i),  
and then install the selected package (press x).

Go back to the packages list with M-x package-list-packages and
choose  
slime-repl.

* [Install clojure](install_clojure)
* [Install clojure-contrib](install_clojContrib)
* [Install Lein](#install_lein)

* update \~/.emacs with:

    (require 'package)

# Using Clojure

To define a variable:

what                           | java                           | clojure
------------------------------ | ------------------------------ | ---------------------------
To define/declare a variable   | String fenton = "fenton";      | (def fenton "fenton")
print to console               | System.out.println(fenton);    | (println fenton)

* Import a library

```java
import com.oracle.git.ClassName;
```

```clojure
(ns com.my-project
   (:use [some.namespace :only (func1 func2)]))
```

A good middle ground between "use" and "require" is to only 'use' the
functions from a name space that you actually use.

When you require a namespace, you still have to refer to methods in a
fully qualified approach.  Use allows you to use the name directly,
like so:

```clojure
(require 'foo)
(foo/bar ...)
```

vs

```clojure
(use 'foo)
(bar ...)
```

As has been mentioned the big difference is that with 
you then refer to names in the lib's namespace like so: (foo/bar ...)
if you do (use 'foo) then they are now in your current namespace
(whatever that may be and provided there are no conflicts) and you can
call them like (bar ...).

## XML Processing

a lot easier to use `clojure.data.zip.xml`

file: `myfile.xml`:

```xml
<songs>
  <track id="t1"><name>Track one</name></track>
  <track id="t2"><name>Track two</name></track>
</songs>
```

```clojure
(ns example
  (:use [clojure.data.zip.xml :only (attr text xml->)]) ; dep: see below
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip]))

(def xml (xml/parse "myfile.xml"))
(def zipped (zip/xml-zip xml))
(xml-> zipped :track :name text)       ; ("Track one" "Track two")
(xml-> zipped :track (attr :id))       ; ("t1" "t2")
```

Unfortunately, you need to pull in a dependency on [data.zip][1] to
get this nice read/filter functionality.  **It's worth the dependency
:)** In [lein][2] it would be (as of 6-Mar-2012):

    [org.clojure/data.zip "0.1.0"]

And as for docs for `data.zip.xml` ... I just look at the relatively
small source file [here][3] to see what is possible.  Another good SO
answer [here][4], too.

  [1]: https://github.com/clojure/data.zip
  [2]: https://github.com/technomancy/leiningen
  [3]: https://github.com/clojure/data.zip/blob/master/src/main/clojure/clojure/data/zip/xml.clj
  [4]: http://stackoverflow.com/a/6471118/69689


[Source of the following info](http://java.ociweb.com/mark/clojure/article.html)

## Data

To convert a list from the standard form to simply data, precede it
with a single quote:

    '(a b c)

## Help

To get help use the `(doc <function-name>)` or
`(find-doc "<a part of the function name>")`

[API](http://clojure.org/api)

## load code

If you have a block of code that is too large to conveniently type at
the REPL, save the code into a file, and then load that file from the
REPL.  You can use an absolute path or a path relative to where you
launched the REPL:

    (load-file "temp.clj")

## refs

`ref` creates reference.

    (ref initial-state)

    (ref #{}) 

Creates a reference to an empty set.

We can bind this to a symbol by using def:

    (def symbol initial-value?)

    (def visitors (ref #{}))

Use ref to create a reference, and use def to bind this reference
to the  
name visitors.

In order to update a reference, you must use a function such as
`alter`.

    (alter ref update-fn & args)

## &

The ampersand means what follows is a collection of arguments. Or
an argument that is a collection.

    (function & expressions)

## state

### Alter

To create a transaction do:

    (dosync & exprs)

Use dosync to add a visitor within a transaction

    (dosync (alter visitors conj "Stu"))

### Examine

At any time, you can peek inside the ref with deref or with the
shorter  
@:

    (deref visitors)

or

    @visitors

## Bindings

The def special form creates a global binding and optionally gives it
a “root value” that is visible in all threads unless a thread-local
value is assigned. def can also be used to change the root value of an
existing binding. However, doing this is frowned upon because it
sacrifices the benefits of working with immutable data. A def can
define functions or data.

    (def v 1) ; v is a global binding

Function parameters are bindings that are local to the function.

### let

let creates a binding that is local that form

    (def v 1) ; v is a global binding
    (defn f2 []
     (println "v = " v)  ;; will print: v = 1
    (defn f1 []
     (println "v = " v)  ;; will print: v = 1
     (let [v 2]          ;; creates a local binding that shadows the global one
      (println "v = " v) ;; will print: v = 2
      (f2)))             
    (f1)                 ;; kick off the program

The call to `f2` above will produce “v = 1” because when you call
the function, you leave the form, and let bindings are only local
to that form.

### sets are functions of their members

    (@visitors name)

`visitors returns the current value of the visitors reference. Sets
are functions of their members, so (`visitors username) checks to see
whether `name` is a member of the current value of visitors.  The let
then binds the result of this check to the name past-visitor.

### map

map applys a function to every item in a list, example: an
anonymous function that adds three to its parameter. map returns a
lazy-sequence.

    (map #(+ % 3) [2 4 7]) ; -> (5 7 10)
    
Here the `#(...)` part is an anonymous function.  The `%` represents
the current element passed into the function.  So the first argument
is the function and the second argument is the list.

### apply

apply takes a function and a collection. The result is when all the
items in a given collection are used as arguments to the supplied
function.

    (apply + [2 4 7]); -> 13

### assoc

The assoc function operates on vectors and maps. When applied to a
vector, it creates a new vector where the item specified by an index
is replaced. If the index is equal to the number of items in the
vector, a new item is added to the end. If it is greater than the
number of items in the vector, an IndexOutOfBoundsException is
thrown. For example:

    (assoc stooges 2 "Shemp") ; -> ["Moe" "Larry" "Shemp"]

### sets

Sets can be used as functions of their items. When used in this
way, they return the item or nil.

    (def stooges (sorted-set "Moe" "Larry" "Curly"))
    (stooges "Moe") ; -> "Moe"

### conj

Add to the back of a sequence

### cons

Add to the start of a sequence

## lists

list literal: `(...)`

    (def stooges (list "Moe" "Larry" "Curly"))
    (def stooges (quote ("Moe" "Larry" "Curly")))
    (def stooges '("Moe" "Larry" "Curly"))

## vectors

vector literal: `[...]`

    (def stooges (vector "Moe" "Larry" "Curly"))
    (def stooges ["Moe" "Larry" "Curly"])

## sets

set literal: `#{...}`

    (def stooges (hash-set "Moe" "Larry" "Curly")) ; not sorted
    (def stooges #{"Moe" "Larry" "Curly"}) ; same as previous
    (def stooges (sorted-set "Moe" "Larry" "Curly"))

## maps

map literal `{...}`

    {:red :cherry, :green :apple, :purple :grape}

Maps store associations between keys and their corresponding values
where both can be any kind of object. Often keywords are used for map
keys. Entries can be stored in such a way that the pairs can be
quickly retrieved in sorted order based on their keys.

Here are some ways to create maps that store associations from
popsicle colors to their flavors where the keys and values are both
keywords. The commas aid readability. They are optional and are
treated as whitespace.

    (def popsicle-map
      (hash-map :red :cherry, :green :apple, :purple :grape))
    (def popsicle-map
      {:red :cherry, :green :apple, :purple :grape}) ; same as previous
    (def popsicle-map
      (sorted-map :red :cherry, :green :apple, :purple :grape))

Maps can be used as functions of their keys. Also, in some cases keys
can be used as functions of maps. For example, keyword keys can, but
string and integer keys cannot. The following are all valid ways to
get the flavor of green popsicles, which is :apple:

(get popsicle-map :green)  
(popsicle-map :green)  
(:green popsicle-map)

When used in the context of a sequence, maps are treated like a
sequence of clojure.lang.MapEntry objects. This can be combined with
the use of doseq and destructuring, both of which are described in
more detail later, to easily iterate through all the keys and
values. The following example iterates through all the key/value pairs
in popsicle-map and binds the key to color and the value to
flavor. The name function returns the string name of a keyword.

    (doseq [[color flavor] popsicle-map]
      (println (str "The flavor of " (name color)
        " popsicles is " (name flavor) ".")))

The output produced by the code above follows:

    The flavor of green popsicles is apple.
    The flavor of purple popsicles is grape.
    The flavor of red popsicles is cherry.

    (def person {
      :name "Mark Volkmann"
      :address {
        :street "644 Glen Summit"
        :city "St. Charles"
        :state "Missouri"
        :zip 63304}
      :employer {
        :name "Object Computing, Inc."
        :address {
          :street "12140 Woodcrest Executive Drive, Suite 250"
          :city "Creve Coeur"
          :state "Missouri"
          :zip 63141}}})

The get-in function takes a map and a key sequence. It returns the
value of the nested map key at the end of the sequence. The → macro
and the reduce function can also be used for this purpose. All of
these are demonstrated below to retrieve the employer city which is
“Creve Coeur”.

    (get-in person [:employer :address :city])
    (-> person :employer :address :city) ; explained below
    (reduce get person [:employer :address :city]) ; explained below

The → macro, referred to as the “thread” macro, calls a series of  
functions, passing the result of each as an argument to the next.
For  
example the following lines have the same result:

    (f1 (f2 (f3 x)))
    (-> x f3 f2 f1)

There is also a -?\> macro in the clojure.contrib.core namespace that
stops and returns nil if any function in the chain returns nil.  This
avoids getting a NullPointerException.

## StructMaps

StructMaps are similar to regular maps, but are optimized to take
advantage of common keys in multiple instances so they don’t have to
be repeated. Their use is similar to that of Java Beans. Proper equals
and hashCode methods are generated for them. Accessor functions that
are faster than ordinary map key lookups can easily be created.

The create-struct function and defstruct macro, which uses  
create-struct, both define StructMaps. The keys are normally
specified  
with keywords. For example:

    (def car-struct (create-struct :make :model :year :color)) ; long way
    (defstruct car-struct :make :model :year :color) ; short way

The struct function creates an instance of a given StructMap.  Values
must be specified in the same order as their corresponding keys were
specified when the StructMap was defined. Values for keys at the end
can be omitted and their values will be nil. For example:

    (def car (struct car-struct "Toyota" "Prius" 2009))

The accessor function creates a function for accessing the value of a
given key in instances that avoids performing a hash map lookup.  For
example:

    ; Note the use of def instead of defn because accessor returns
    ; a function that is then bound to "make".
    (def make (accessor car-struct :make))
    (make car) ; -> "Toyota"
    (car :make) ; same but slower
    (:make car) ; same but slower

New keys not specified when the StructMap was defined can be added to
instances. However, keys specified when the StructMap was defined
cannot be removed from instances.

## List Comprehensions

for loops:

```
(for [the-current-element the-list-of-elements]
  ; your code)
```

example:

```
(for [word ["the" "quick" "brown" "fox"]]
  (format "<p>%s</p>" word))
("<p>the</p>" "<p>quick</p>" "<p>brown</p>" "<p>fox</p>")
```

Can also support a `when` clause:

```
(for [word ["the" "quick" "brown" "fox"]
      :when (= "quick" word)]
  (format "<p>%s</p>" word))
("<p>quick</p>")
```

The docs for parse say:

> Returns a tree of the xml/element struct-map, which has the keys
:tag, :attrs, and :content. and accessor fns tag, attrs, and content.

```
(for [xml-node (xml-seq (parse input-stream))
        :when (= "siebel:errormsg" (xml-node :tag))]
    (xml-node :content)))
```    

When the xml-node's tag is = to "siebel:errormsg" return the content:




## Defining Functions

The defn macro defines a function. Its arguments are the function
name, an optional documentation string (displayed by the doc macro),
the parameter list (specified with a vector that can be empty) and the
function body. The result of the last expression in the body is
returned. Every function returns a value, but it may be nil. For
example:

```
(defn function-name 
  "documenations" 
  [parameter list] 
  ( ... rest of function ))
```

example:

```
(defn parting
  "returns a String parting"
  [name]
  (str "Goodbye, " name)) ; concatenation
(println (parting "Mark")) ; -> Goodbye, Mark
```

### scope

Functions defined with the defn- macro are private. This means they
are only visible in the namespace in which they are defined.  Other
macros that produce private definitions, such as defmacro- and
defstruct-, are in clojure.contrib.def.

### var args

Functions can take a variable number of parameters. Optional
parameters must appear at the end. They are gathered into a list by
adding an ampersand and a name for the list at the end of the
parameter list.

    (defn power [base & exponents]
      ; Using java.lang.Math static method pow.
      (reduce #(Math/pow %1 %2) base exponents))
    (power 2 3 4) ; 2 to the 3rd = 8; 8 to the 4th = 4096

### function overloading, arity

Function definitions can contain more than one parameter list and
corresponding body. Each parameter list must contain a different
number of parameters. This supports overloading functions based on
arity. Often it is useful for a body to call the same function with a
different number of arguments in order to provide default values for
some of them. For example:

    (defn parting
      "returns a String parting in a given language"
      ([] (parting "World"))
      ([name] (parting name "en"))
      ([name language]
        ; condp is similar to a case statement in other languages.
        ; It is described in more detail later.
        ; It is used here to take different actions based on whether the
        ; parameter "language" is set to "en", "es" or something else.
        (condp = language
          "en" (str "Goodbye, " name)
          "es" (str "Adios, " name)
          (throw (IllegalArgumentException.
            (str "unsupported language " language))))))
    (println (parting)) ; -> Goodbye, World
    (println (parting "Mark")) ; -> Goodbye, Mark
    (println (parting "Mark" "es")) ; -> Adios, Mark
    (println (parting "Mark", "xy"))
    ; -> java.lang.IllegalArgumentException: unsupported language xy

### annonymous functions

`#(...)`, putting a hash in front of the parens is how to make an
anonymous function

Anonymous functions have no name. These are often passed as arguments
to a named function. They are handy for short function definitions
that are only used in one place. There are two ways to define them,
shown below:

    (def years [1940 1944 1961 1985 1987])
    (filter (fn [year] (even? year)) years) ; long way w/ named arguments -> (1940 1944)
    (filter #(even? %) years) ; short way where % refers to the argument

When an anonymous function is defined using the fn special form, the
body can contain any number of expressions.

When an anonymous function is defined in the short way using \#(…), it
can only contain a single expression. To use more than one expression,
wrap them in the do special form. If there is only one parameter, it
can be referred to with %. If there are multiple parameters, they are
referred to with %1, %2 and so on. For example:

    (defn pair-test [test-fn n1 n2]
      (if (test-fn n1 n2) "pass" "fail"))
    ; Use a test-fn that determines whether
    ; the sum of its two arguments is an even number.
    (println (pair-test #(even? (+ %1 %2)) 3 5)) ; -> pass

Java methods can be overloaded based on parameter types. Clojure  
functions can only be overloaded on arity. Clojure multimethods  
however, can be overloaded based on anything.

The defmulti and defmethod macros are used together to define a  
multimethod.

The arguments to defmulti are:

* the method name and
* the dispatch function which returns a value that will be used
    to select a  
    method.

The arguments to defmethod are:

* the method name,
* the dispatch value that triggers use of the method,
* the parameter list and
* the body.

The special dispatch value :default is used to designate a method to
be used when none of the others match. Each defmethod for the same
multimethod name must take the same number of arguments.  The
arguments passed to a multimethod are passed to the dispatch function.

Here’s an example of a multimethod that overloads based on type.

    (defmulti what-am-i class) ; class is the dispatch function
    (defmethod what-am-i Number [arg] (println arg "is a Number"))
    (defmethod what-am-i String [arg] (println arg "is a String"))
    (defmethod what-am-i :default [arg] (println arg "is something else"))
    (what-am-i 19) ; -> 19 is a Number
    (what-am-i "Hello") ; -> Hello is a String
    (what-am-i true) ; -> true is something else

class returns the class of its argument. So basically when we call
what-am-i an argument x, x is passed to the function ‘class’ and the
result of that function is compared to the 3rd value in the defmethod
calls. When a match is found, x is then passed along into the function
defined at the end of defmethod.

Since the dispatch function can be any function, including one you
write, the possibilities are endless. For example, a custom dispatch
function could examine its arguments and return a keyword to indicate
a size such as :small, :medium or :large. One method for each size
keyword can provide logic that is specific to a given size.

Underscores can be used as placeholders for function parameters that
won’t be used and therefore don’t need a name. This is often useful in
callback functions which are passed to another function so they can be
invoked later. A particular callback function may not use all the
arguments that are passed to it. For example:

    (defn callback1 [n1 n2 n3] (+ n1 n2 n3)) ; uses all three arguments
    (defn callback2 [n1 _ n3] (+ n1 n3)) ; only uses 1st & 3rd arguments
    (defn caller [callback value]
      (callback (+ value 1) (+ value 2) (+ value 3)))
    (caller callback1 10) ; 11 + 12 + 13 -> 36
    (caller callback2 10) ; 11 + 13 -> 24

## Java Interoperability

Clojure programs can use all Java classes and interfaces. As in Java,
classes in the java.lang package can be used without importing
them. Java classes in other packages can be used by either specifying
their package when referencing them or using the import function.  For
example:

    (import
      '(java.util Calendar GregorianCalendar)
      '(javax.swing JFrame JLabel))

    (.toUpperCase "hello")

The dot before toUpperCase tells Clojure to treat it as the name of a
Java method instead of a Clojure function.

## Parameter naming conventions

Parameter | Usage
--------- | --------------
a         | A Java array
agt       | An agent
coll      | A collection
expr      | An expression
f         | A function
idx       | Index
r         | A ref
v         | A vector
val       | A value

## form grammar

```
(example-fn required-arg)
(example-fn optional-arg?)
(example-fn zero-or-more-arg*)
(example-fn one-or-more-arg+)
(example-fn & collection-of-variable-args)
```

# Cookbook with Explanations

## file writing

```
(use '[clojure.contrib.duck-streams :only (spit)])
(spit "hello.out" "hello, world")
```

This program writes the string "hello, world" out to the file
`hello.out`.  The first line is like the import line in java.  The
simple form of `use`

    (use 'clojure.contrib.duck-streams)

causes the current namespace to refer to all public vars in
clojure.contrib.duck-streams. This can be confusing, because it does
not make explicit which names are being referred to. Be nice to future
readers of your code, and pass the :only option to use, listing only
the vars you need:

The definition of spit is:

    ; from clojure-contrib
    (defn spit [f content]
     (with-open [#^PrintWriter w (writer f)]
      (.print w content)
     )
    )

The `with-open` function's general form is:

    (with-open [name init-form] & body)

Internally, with-open creates a try block, sets `name` to the result
of `init-form`, and then runs the forms in `body`. Most important,
with-open always closes the object bound to name in a finally block.

# Projects

## Read CSV

I have a comma separate values (CSV) file.  First row is titles, other
rows are data.  The idea is to create a list of struct-maps.  So read
first row is first challenge.

## Parse XML

I have a lot of XML going in and out and often need to parse it.  One
area that needs work is parsing the error messages that come back from
CRMOD.  The following is a classic example of what comes back:

```
<?xml version="1.0" encoding="UTF-8"?>
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
  <SOAP-ENV:Body>
    <SOAP-ENV:Fault>
      <faultcode>SOAP-ENV:Server</faultcode>
      <faultstring>Method 'SetFieldValue' of business component 'Service Request' (integration component 'Service Request') returned the following error:"The value entered in field Priority of buscomp Service Request does not match any value in the bounded pick list PickList SR Priority.(SBL-DAT-00225)"(SBL-EAI-04376)</faultstring>
      <detail>
        <siebelf:siebdetail xmlns:siebelf="http://www.siebel.com/ws/fault">
          <siebelf:logfilename>OnDemandServicesObjMgr_enu_71947.log</siebelf:logfilename>
          <siebelf:errorstack>
            <siebelf:error>
              <siebelf:errorcode>(SBL-DAT-00225)</siebelf:errorcode>
              <siebelf:errorsymbol/>
              <siebelf:errormsg>Method 'SetFieldValue' of business component 'Service Request' (integration component 'Service Request') returned the following error:"The value entered in field Priority of buscomp Service Request does not match any value in the bounded pick list PickList SR Priority.(SBL-DAT-00225)"(SBL-EAI-04376)</siebelf:errormsg>
            </siebelf:error>
          </siebelf:errorstack>
        </siebelf:siebdetail>
      </detail>
    </SOAP-ENV:Fault>
  </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

So lets put this in a file and read it into clojure.

Following [Getting Started][] we create our project like so:

    $ cd ~/projects; lein new soap-helper

update `soap-helper/project.clj`

```
(defproject com.oracle.git.ngsp/SoapHelper "1.0.0-SNAPSHOT"
  :description "Help process SOAP messages"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]]
  :dev-dependencies [[ swank-clojure "1.2.1"]
                     [com.stuartsierra/lazytest "1.1.2"]]
  :repositories {"stuartsierra-releases" "http://stuartsierra.com/maven2"}
  :main com.oracle.git.ngsp.SoapHelper)
```

Notice the part: `com.oracle.git.ngsp/SoapHelper`.  This means the
`groupId` for maven will be: `com.oracle.git.ngsp`, the `artifactId`
will be: `SoapHelper`

Get dependencies:

    $ lein deps
    
    soap-helper/src/com/oracle/git/ngsp/SoapHelper.clj
    
```
(ns com.oracle.git.ngsp.SoapHelper
  (:gen-class
   :name com.oracle.git.ngsp.SoapHelper
   :methods [#^{:static true} [greet [] String]]))
(defn greet[] (println "Hello Fenton!"))
(defn -greet
  "A 'static' wrapper around 'greet' function."
  []
  (greet))
```
    
Lets plug in the testing,     

```
(ns com.oracle.git.ngsp.test.SoapHelper
  (:use [com.oracle.git.ngsp.SoapHelper])
  (:use [lazytest.describe :only (describe it)]))
(describe
 greet
 (it "returns string 'Hello Fenton!'"
     ( = "Hello Fenton!" (greet))))
```

    $ java -cp "src:test:lib/*:lib/dev/*" lazytest.watch src test
    
Okay now that we are all wired up, lets try to start fixing the
original problem.  We'll use the REPL to do some testing
    
```
user> (require 'clojure.xml)
user> (clojure.xml/parse (java.io.File. "test/soap1.xml"))
{:tag :SOAP-ENV:Envelope, :attrs {:xmlns:SOAP-ENV "http://schemas.xmlsoap.org/soap/envelope/"}, :content [{:tag :SOAP-ENV:Body, :attrs nil, :content [{:tag :SOAP-ENV:Fault, :attrs nil, :content [{:tag :faultcode, :attrs nil, :content ["SOAP-ENV:Server"]} {:tag :faultstring, :attrs nil, :content ["Method 'SetFieldValue' of business component 'Service Request' (integration component 'Service Request') returned the following error:\"The value entered in field Priority of buscomp Service Request does not match any value in the bounded pick list PickList SR Priority.(SBL-DAT-00225)\"(SBL-EAI-04376)"]} {:tag :detail, :attrs nil, :content [{:tag :siebelf:siebdetail, :attrs {:xmlns:siebelf "http://www.siebel.com/ws/fault"}, :content [{:tag :siebelf:logfilename, :attrs nil, :content ["OnDemandServicesObjMgr_enu_71947.log"]} {:tag :siebelf:errorstack, :attrs nil, :content [{:tag :siebelf:error, :attrs nil, :content [{:tag :siebelf:errorcode, :attrs nil, :content ["(SBL-DAT-00225)"]} {:tag :siebelf:errorsymbol, :attrs nil, :content nil} {:tag :siebelf:errormsg, :attrs nil, :content ["Method 'SetFieldValue' of business component 'Service Request' (integration component 'Service Request') returned the following error:\"The value entered in field Priority of buscomp Service Request does not match any value in the bounded pick list PickList SR Priority.(SBL-DAT-00225)\"(SBL-EAI-04376)"]}]}]}]}]}]}]}]}
```

(for [x (xml-seq (parse input-stream))
        :when (= :title (:tag x))]
    (:content x))

## Scrape a web page

So for this project, I want to parse some JSON XML that I get by  
calling a URL.

The java code uses the HttpClient library and looks like:

```
String url = "http://search.oracle.com?&search.timezone=-480&group=All&search_p_main_operator=all";
HttpClient client = new HttpClient();
client.getHostConfiguration().setProxy("www-proxy.us.oracle.com", 80);
GetMethod method = new GetMethod(url);
method.getResponseBodyAsString();
```

So the first step is to get the dependent library in place.

## APS to CX web services

To do CX web services for APS (Account Provisioning System), we'll
need to create XML as a central part of the library.  The following
SOAP creates a contact user.

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
  <soapenv:Header>
    <ns7:ClientInfoHeader xmlns:ns7="urn:messages.ws.rightnow.com/v1_2" soapenv:mustUnderstand="0">
      <ns7:AppID></ns7:AppID>
    </ns7:ClientInfoHeader>
    <wsse:Security xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd" mustUnderstand="1">
      <wsse:UsernameToken>
        <wsse:Username>ftravers</wsse:Username>
        <wsse:Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText">secret123</wsse:Password>
      </wsse:UsernameToken>
    </wsse:Security>
  </soapenv:Header>
  <soapenv:Body>
    <ns7:Create xmlns:ns7="urn:messages.ws.rightnow.com/v1_2">
      <ns7:RNObjects xmlns:ns4="urn:objects.ws.rightnow.com/v1_2" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="ns4:Contact">
        <ns4:Emails>
          <ns4:EmailList action="add">
            <ns4:Address>fenton.travers@gmail.com</ns4:Address>
            <ns4:AddressType>
              <ID xmlns="urn:base.ws.rightnow.com/v1_2" id="0" />
            </ns4:AddressType>
          </ns4:EmailList>
        </ns4:Emails>
        <ns4:Name>
          <ns4:First>Fenton</ns4:First>
          <ns4:Last>Travers</ns4:Last>
        </ns4:Name>
      </ns7:RNObjects>
      <ns7:ProcessingOptions>
        <ns7:SuppressExternalEvents>false</ns7:SuppressExternalEvents>
        <ns7:SuppressRules>false</ns7:SuppressRules>
      </ns7:ProcessingOptions>
    </ns7:Create>
  </soapenv:Body>
</soapenv:Envelope>
```

Here we need to parameterize several items, they are:

* Login username (to login to the CX system)
* Login password
* Contact email address (of the contact you want to create)
* Contact first name
* Contact last name

Pragmmatic Programming Clojure, recommends: 

  "If you plan to do significant XML processing with Clojure, check
   out the lazy-xml and zip-filter/xml libraries in clojure-contrib."
   

# Troubleshooting


Goal:

Try to run `clojure-jack-in` in a clojure source file and get the
following error:

Error:

```
Debugger entered--Lisp error: (error "Could not start swank server: /bin/bash: lein: command not found
")
  signal(error ("Could not start swank server: /bin/bash: lein: command not found\n"))
  error("Could not start swank server: %s" "/bin/bash: lein: command not found\n")
  clojure-jack-in-sentinel(#<process swank> "exited abnormally with code 127\n")
```

Diagnosis:

We have a method: `set-exec-path-from-shell-PATH` that puts your shell
path into the execution path that Emacs uses...so it will find
`lein`.  However, I didn't have my home directory on the PATH, and the
method: `shell-command-to-string` executes the login and non-login
scripts, including `.profile`, etc..., however, it must be able to
find these on the path...so `.profile` is called and the path is set.
`.bashrc` is called which references `.bash_aliases`, which if not
defined in the path in `.profile` will throw an error and this
function will not successfully execute.
