So I wanted to do some xml processing in clojure.  I basically got
some code that looked like:

```clojure
(ns my-project.core
  (:use [clojure.data.zip.xml :only (attr text xml->)])
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip]))
(def myxml (xml/parse "tracks.xml")) 
(def zipped (zip/xml-zip myxml))
(xml-> zipped :track :name text) ; ("Track one" "Track two")
(xml-> zipped :track (attr :id)) ; ("t1" "t2")
```

The `tracks.xml` file contains:

```xml
<songs>
  <track id="t1"><name>Track one</name></track>
  <track id="t2"><name>Track two</name></track>
</songs>
```

So I `A-x clojure-jack-in` to start my swank server.  `C-c A-p [RET]
[RET]` to get your namespace into the REPL, i.e.: `my-project.core`

Doing a `A-.` on `xml/parse` returns documentation that starts off by
saying:

"Parses and loads the source s, which can be a File ... Returns a tree
of the xml/element struct-map, which has the keys :tag, :attrs, and
:content..."

Doing an `A-,` returns me back to my source file.  Okay, I evaluate
the following in the REPL.

```repl
my-project.core> (class myxml)
clojure.lang.PersistentStructMap
my-project.core> (println myxml)
{:tag :songs, 
 :attrs nil, 
 :content 
 [{
   :tag :track, 
   :attrs {:id t1}, 
   :content 
   [{
     :tag :name, 
     :attrs nil, 
     :content 
        [Track one]
   }]
  }{
    :tag :track, 
    :attrs {:id t2}, 
    :content 
    [{
      :tag :name, 
      :attrs nil, 
      :content [Track two]
    }]
  }
]}
nil
my-project.core> 
```

I've expanded out the result of `(println myxml)` so it's marginally
more legible.

Here is a short definition of what a struct-map is:

"StructMaps are similar to regular maps, but are optimized to take
advantage of common keys in multiple instances so they dont have to be
repeated. Their use is similar to that of Java Beans."

So we should be able to do things like:

```
my-project.core> (class (myxml :tag)) 
clojure.lang.Keyword
my-project.core> (println (myxml :tag))
:songs
nil
my-project.core> (class (myxml :content)) 
clojure.lang.PersistentVector
(myxml :tags)
```

So all of this makes sense.  But what if we wanted to do the following
things: 

#### Question 1

Get the `id` of the track who's name is: `Track one`?

(Answer): `(xml-> zipped :track [:name (text= "Track one")] (attr :id))`

The way to read this answer is:

`(xml-> zipped` = Run the `xml->` function, which you might
describe as a function that "gets xml", on the `zipped` xml data
structure

` :track ` = grab the first track node you come upon

`:track [:name (text= "Track one")]` = filter these tracks based on
having a child node called `name`, whose text is equal to: `Track one`.

` (attr :id)` now give me the attribute `id` for the found track.

#### Question 2

Get the name of the track who's id is: `t2`?

(Answer): `(xml-> zipped :track [(attr= :id "t2")] :name text)`

` :track` = get the track nodes, the first ones you
come upon

` [(attr= :id "t2)]` = filter those tracks keeping only the ones
whos attribute `id` is: `t2`

` :name text` = For the found tracks give us the child node named
`name`'s contents (text).