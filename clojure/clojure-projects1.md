So the following are notes about projects that I wanted to do in
Clojure.  Perhaps someone will follow them and learn something too?
We'll see.

The first task that came to me was that we needed to accept a hashmap
(in the java sense) of parameters and values.  We also needed those
parameters to be sorted for some soap that we were going to put the
params into.

So we might receive a request like:

    age: 14, name: Fenton, city: vancouver

but we might say we need them sorted like:

    name, age, city 

for soap that might look like:

```xml
<name>Fenton</name>
<age>14</age>
<city>vancouver</city>
```

Well I came across a blog post that did exactly this...however being a
clojure newbie, I needed to really study and disect the example so I
could understand each and every step.

The URL to the site is:

*[sorting-with-clojure-comparator](http://paulosuzart.github.com/blog/2012/04/11/sorting-with-clojure-comparator/) 

You can look at this site, then use the below to understand each step!

`map-indexed` is defined as:

```
(map-indexed f coll)

Returns a lazy sequence consisting of the result of applying f to 0
and the first item of coll, followed by applying f to 1 and the second
item in coll, etc, until coll is exhausted. Thus function f should
accept 2 arguments, index and item.
```

It then shows the following example:

```clojure
user=> (map-indexed (fn [idx itm] [idx itm]) "foobar")
([0 \f] [1 \o] [2 \o] [3 \b] [4 \a] [5 \r])
```

The part that looks like:

```clojure
(fn [idx itm] [idx itm])
```

is creating an anonymous function, that has two arguments `idx` or
index and `itm` or item.  It then returns a vector of those two
arguments. 

So if we have a list of strings like so:

```clojure
(def my-list '("human" "deer" "berry"))
```

We could generate:

```clojure
([0 "human"] [1 "deer"] [2 "berry"])
```

with:

```clojure
(map-indexed (fn [i s] [i s]) my-list)
```

but maybe I'd like to find out which order a given item was, I might
want to do something like:

```clojure
user> (my-collection :deer)
1
```

That is find out that `deer` is the 2nd item.  So lets see how we
could do this.  `map-indexed` will pair up each item with a number,
but now we need to turn that into a record, so lets build up to that. 

We can turn the strings into keywords using the `keyword` function.

```clojure
user> (map-indexed (fn [i s] [i (keyword s)]) my-list)
([0 :human] [1 :deer] [2 :berry])
```

Now lets destructure this with the `flatten` function.

```clojure
user> (flatten (map-indexed (fn [i s] [i (keyword s)]) my-list))
(0 :human 1 :deer 2 :berry)
```

We can use the `hash-map` function to turn the above into a hashmap,
but first we need to put the key first, and value second!

```clojure
user> (flatten (map-indexed (fn [i s] [(keyword s) i]) my-list))
(:human 0 :deer 1 :berry 2)
```

Next `hash-map` works like this:

```clojure
user> (hash-map :key1 "val1" :key2 "val2")
{:key2 "val2", :key1 "val1"}
```

But we have:

```clojure
(:human 0 :deer 1 :berry 2)
```

not

```clojure
:human 0 :deer 1 :berry 2
```

So we need to strip off the parens, this is where the `apply` function
comes in handy.  It takes two args, a function and a sequence, and
then turns the sequence into a simply argument list.  So visually, it becomes:

```clojure
(hash-map :human 0 :deer 1 :berry 2)
```

instead of:

```clojure
(hash-map (:human 0 :deer 1 :berry 2))
```

which won't work.  So lets write that out properly.

```clojure
user> (def my-keyvals (flatten (map-indexed (fn [i s] [(keyword s) i]) my-list)))
#'user/my-keyvals
user> my-keyvals
(:human 0 :deer 1 :berry 2)
user> (apply hash-map my-keyvals)
{:berry 2, :human 0, :deer 1}
```

Lets assign it to a variable and demonstrate using it:

```clojure
user> (def my-struct (apply hash-map my-keyvals))
#'user/my-struct
user> my-struct
{:berry 2, :human 0, :deer 1}
user> (:deer my-struct)
1
```

Okay now on to the sorting part:

```clojure
(defn compar [s] 
  (comparator (fn [x y] (< ((keyword x) s) ((keyword y) s)))))
```

We've learned what `keyword` and `fn` are from the examples above.  So
lets understand now what `comparator` is/does.

The clojure docs say:

```
Returns an implementation of java.util.Comparator based upon pred.
```

Well a `pred` or predicate is a function that returns true or false.
So that is the following part:

```clojure
(fn [x y] (< ((keyword x) s) ((keyword y) s)))
```

In our example if we passed in `:human` and `:deer`, we'd get: 

    (< 0 1) 
    
Since 0 is less than 1, this would equate to true.  Normally the sort
function sorts in an ascending manner, so it's default function is the
`<` function.  I.e. 0 is less than 1 so put 0 first, followed by 1.

The neat aspect of this comparator is that it is a function that takes
MORE than the normal two elements, it also takes in the sizes list!
So you can use the three items to figure out how the two elements
compare. 

So now that we have our comparator, we can pass it to our `sort`
function, and sort these parameters.

```clojure

```


```clojure

```


```clojure

```


```clojure

```


```clojure

```


