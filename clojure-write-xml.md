Now I want to write XML.  Here is some starting code:

```clojure
(ns my-project.core
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip]))

(defn parse-str [s]
  (zip/xml-zip (xml/parse (new org.xml.sax.InputSource
                               (new java.io.StringReader s)))))

(def my-xml (parse-str "<f>fenton <b>oliver</b> travers</f>"))
```

I'd like to have xml that looks like:

```xml
<f>fenton <b>oliver</b> <b>joey</b> travers</f>
```

so just adding `<b>joey</b>`, after the peer `<b>oliver</b>`

In the REPL we see that `my-xml` looks like:

```
my-project.core> (println my-xml)
[{:tag :f, :attrs nil, :content [fenton  {:tag :b, :attrs nil, :content [oliver]}  travers]} nil]
```