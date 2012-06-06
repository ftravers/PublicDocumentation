Say I had a file called `tracks.xml` with the following XML:

```xml
<songs>
  <track id="t1"><name>Track one</name></track>
  <track id="t2"><name>Track two</name></track>
</songs>
```

and I wanted to do the following:

1. Get the `id` of the track who's name is: `Track one`?

1. Get the name of the track who's id is: `t2`?

What I've got so far is:

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

Where to go from here?

ANSWER:  Look at the bottom of this:

https://github.com/richhickey/clojure-contrib/blob/81b9e71effbaf6aa2945cd684802d87c762cdcdd/src/clojure/contrib/zip_filter/xml.clj#L57