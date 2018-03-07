Sometimes we want to do some templating.  It should work something
like this:

```clojure
(def greeting-template "hello $name$")
(def parameters {"name" "fenton"})
(def greeting-fenton (fill-in-template greeting-template parameters))
(= "hello fenton" greeting-fenton)
```

Using the `[comb "0.1.0"]` templating library we can do:

```clojure
(ns blah
  (:use [comb.template :only (eval)]))
(def greeting-template "hello <%=name%>")
(def parameters {:name "fenton"})
(def greeting-fenton (eval greeting-template parameters))
(= "hello fenton" greeting-fenton)
```
