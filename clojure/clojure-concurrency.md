Atoms:

Used when only need to manage state of a single item.

Define:

```clojure
(def my-atom (atom "value"))
```

Reset value

```clojure
(reset! my-atom "new-value")
```

Get value of atom (or ref, agent, etc...), use `@`

```clojure
(println @my-atom)  # prints: "new-value"
```



```clojure
user=> (def apple-price (atom nil))
#'user/apple-price
user=> (defn update-apple-price [new-price] (reset! apple-price new-price))
#'user/update-apple-price
user=> @apple-price
nil
user=> (update-apple-price 300.00)
300.0
user=> @apple-price
300.0
user=> (update-apple-price 301.00)
301.0
user=> (update-apple-price 302.00)
302.0
user=> @apple-price               
302.0
```
