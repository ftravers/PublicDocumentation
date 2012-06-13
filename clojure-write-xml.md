### Write XML

Now I want to write XML.  Here is some starting code:

```clojure
(ns my-project.core
  (:use  :only (attr text xml->)])
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.data.zip.xml :as zd]))

(def xml-zipper (-> (xml/parse "my.xml")
                    (zip/xml)))
```

`my.xml` looks like:

```xml
<a1>
  <b1>ton</b1>
  <b2>
    <c1>jtr</c1>
    <c1>cone2</c1>
    <c2>
      <d1>dee1</d1>
      <d2>dee2</d2>
    </c2>
    <c3>see3</c3>
  </b2>
</a1>
```

### Get to location in XML

So first I go to where I want to insert XML, say after the first
`<c1>`.  I do this with:

```clojure
(zd/xml-> xml-zipper :b2 :c1)
```

This gives me all the `c1`'s.  I can confirm I got here by telling
`xml->` to give me the text at that location in the REPL:

```clojure
my-project.core> (zd/xml-> xml-zipper :b2 :c1 zd/text)
("jtr" "cone2")
```

Now even if there had only been one `<c1>`, a list of one, I need to
get it out of the list for the next step, the insertion.

I'll also use the thread first `->` operator, so I can write down the
steps in top to bottom fashion.

```clojure
(-> (zd/xml-> xml-zipper :b2 :c1)
    (first))       ;; the <c1> with the text "jtr"
```

Now I can begin my navigation with the `clojure.zip` functions,
`left`, `right`, `up`, `down`, `insert-right`, `insert-left`, etc..

Before we can insert a node we must create one in zipper format, to
understand the format zippers are in, lets output our zipper:

```
my-project.core> xml-zipper
[{:tag :a1, :attrs nil, :content [{:tag :b1, :attrs nil, :content ["ton"]} ...
```

(truncated for brevity)

So here is a sample xml node:

# THE FOLLOWING DOESN'T WORK PROPERLY

```clojure
(def new-node {:tag :b,
              :attrs nil,
               :content ["joey"] })
```

So lets just `insert-right` it:

```clojure
(-> (zd/xml-> xml-zipper :b2 :c1)
    (first)       ;; the <c1> with the text "jtr"
    (zip/insert-right new-node))
```

Finally, we need to return to the root of the zipper, with `zip/root`,
so finally:

```clojure
(-> (zd/xml-> xml-zipper :b2 :c1)
    (first)       ;; the <c1> with the text "jtr"
    (zip/insert-right new-node)
    (zip/root))
```

Lets assign the result to a variable, and then search it to ensure our
modification worked.

```clojure
(def new-xml (-> 
    (zd/xml-> xml-zipper :b2 :c1)
    (first)       ;; the <c1> with the text "jtr"
    (zip/insert-right new-node)
    (zip/root))
```









(-> (zd/xml-> xml-zipper :b2 :c1)(first)(zip/insert-right new-node))





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

Now I need to insert this into the `xml-zipper`.  First lets use the
thread-first operator `->`, so we can write out the steps left to
right.

```clojure
(->

```
