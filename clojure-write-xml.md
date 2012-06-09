Now I want to write XML.  Here is some starting code:

```clojure
(ns my-project.core
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip]))

(defn xmlstr-2-structmap [s] (xml/parse (new org.xml.sax.InputSource (new java.io.StringReader s))))
(defn xmlstr-2-zipper [s] (zip/xml-zip (xmlstr-2-structmap s)))

(def xml-string "<f>fenton <b>oliver</b> travers</f>")
(def xml-structmap (xmlstr-2-structmap xml-string))
(def xml-zipper (xmlstr-2-zipper xml-string))
```

I'd like to make the above xml:

```xml
<f>fenton <b>oliver</b> travers</f>
```

become:

```xml
<f>fenton <b>oliver</b> <b>joey</b> travers</f>
```

so just adding `<b>joey</b>`, after the peer `<b>oliver</b>`

In the REPL we see that an `xml-zipper` looks like:

```
my-project.core> (println xml-zipper)
[{:tag :f, :attrs nil, :content
  [fenton
   {:tag :b, :attrs nil, :content [oliver]}
   travers]} nil]
```
*formatted for readability*

So I want to create another node like the one that is `oliver`, which
should look like:

```clojure
{:tag :b, :attrs nil, :content [joey]}
```   

Basically a structmap `{}`, whose keyword `:content` holds a vector
with the single string `joey`, with two other keywords `:tag` and
`:attrs`, that are equal to keyword `:b` and value nil, respectively.

I can make this in clojure like so:

```clojure
(def new-node {:tag :b,
              :attrs nil,
              :content ["joey"] })
```

Now I need to insert this into the `xml-zipper`.