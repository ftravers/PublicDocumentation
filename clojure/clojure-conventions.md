## Referencing other source files

Use syntax like:

```clojure
(ns com.oracle.git.cx_ws.core
  (:use [com.oracle.git.cx_ws.utils :only (apply-template read-template)])
  (:use [com.oracle.git.cx_ws.connection :only (service-call)]))
```

as opposed to:

```clojure
(:require ...) ;; requires prefixing functions, which is okay, but 
;; :use allows no prefix.
;; OR
(:use [lib])  ;; no :only clause which is very helpful to know where
;; function come from
```

## Parens

Dont put parens on their own line, indentation should indicate
nesting, not parens on their own lines.

DO:

```clojure
(defn abc [parameter] 
  "docs"
  (if parameter 
    (true)
    (false)))
```

DONT:

```clojure
(defn abc [parameter] 
  "docs"
  (if parameter 
    (true)
    (false)
  )
)
```

## Keep code clean, be functional.

Don't litter code with debug statements, log statements.  The
following also modifies a parameter (this is not a functional best
practice!).  This method should construct a string and return that
instead of appending to a string buffer that is passed in.

The following method could likely be cleaned up.

```clojure
(defn- construct-soap-with-fields
  "constructs soap message with the given fields
   in a map: basically forms tags and values"
  [buffer fields-map]
  (doseq [[key val] fields-map]
    (if (= key :ID)
      (log/debug
       ""
       (.append buffer "<v11")
       (.append buffer key)
       (.append buffer " id=\"")
       (.append buffer val)
       (.append buffer "\"/>"))
      (log/debug
             ""
      (.append buffer "<ns4")
      (.append buffer key)
      (.append buffer ">")
      (if (map? val)
        (construct-soap-with-fields buffer val)
        (.append buffer val))
      (.append buffer "</ns4")
      (.append buffer key)
      (.append buffer ">")))
  (.toString buffer)))
```

here is a possible replacement, I'm not sure it's better???

```clojure
(defn construct-soap-with-fields2
  [fields-map]
  (let [buf (atom "")]
    (for [keyvals fields-map]
      (let [key (first keyvals)
            val (second keyvals)]
        (if (= (first keyvals) :ID)
          (reset! buf (str buf "v11" key " id=\"" val "\"/>" ))
          (reset! buf (str buf "<ns4" key ">"
                           (if (map? val)
                             (construct-soap-with-fields2 val)
                             val)
                           "</ns4" key ">")))))
    buf))
```

possible benefits:

* indentation indicates nesting versus parens on their own lines.
* reference to `buf` is made thread-safe with `atom` syntax...maybe?
  Is string-buffer threadsafe itself already...dunno?
* both versions make nice use of recursion.

Constructs like:

```clojure
      (.append buffer "</ns4")
      (.append buffer key)
      (.append buffer ">")))
```

should be reduced to something more like:

```clojure
      (.append buffer "</ns4" key ">")
```

# no camelCase

Dont use camelCase use hyphen instead to separate words.

DONT:

```clojure
(test-createStaffAccount)
```

DO:

```clojure
(test-create-staff-account)
```
