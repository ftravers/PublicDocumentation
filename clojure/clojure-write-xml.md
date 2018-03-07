### Write XML

Now I want to write XML.  Say I want to read the following:

```xml
<a>
  <b1>ton</b1>
</a>
```

and want to convert it to:

```xml
<a>
  <b1>ton</b1>
  <b2>joey</b2>
</a>
```

that is add a `b2` node with the text "joey" after the `b1` node.

Here is the project.clj file I'm using:

```clojure
(defproject my-project "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-swank "1.4.4"]]
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/data.zip "0.1.0"]
                 [swank-clojure "1.4.0"]])
```

The main difference from a standard project AND required for xml
processing in this example is the line with the `data.zip` in it.

Now in my `src/my-project/core.clj` file I have: 

```clojure
(ns my-project.core
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.data.zip.xml :as zd] ))

(defn zip-str [s] (zip/xml-zip (xml/parse (new org.xml.sax.InputSource (new java.io.StringReader s)))))

(def xml-string "<a><b1>ton</b1></a>")
(def xml-zipper (zip-str xml-string))
```

So here we can see I create a `zipper` out of the xml.

Now I need to create a new XML node, so I do the following:

```clojure
(def new-node {:tag :b2,
               :attrs nil,
               :content ["joey"]})
```

Now what I do is the following:

```clojure
(def new-xml (-> 
              (zd/xml-> xml-zipper :b1) ;; goto the b1 node
              (first) ;; pull out the first element from the seq
              (zip/insert-right new-node) ;; insert the new node to right of this node
              (zip/root) ;; get back to the root of the xml
              (zip/xml-zip) ;; convert the result of that back into a zipper
              ))
```

Finally lets test the result to pull out the text "joey" from the
zipper. 

```clojure
(def joey-text (zd/xml-> new-xml :b2 zd/text))
```

So in the REPL:

```
my-project.core> joey-text
("joey")         

```

TODO: demonstrate writing the xml out to a string.
