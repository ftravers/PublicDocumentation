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

so just adding `<b>joey</b>`, i.e. after the peer `<b>oliver</b>`