I recently came upon the thrush (thread) operators in clojure `->` and
`->>` and wanted to find out what they meant so googled it.  I wasn't
entirely enlightened by what I found so I'll take my own stab at
explaining it.

Clojure operates from the inside out.  So:

```clojure
(+ 3 (* 2 5))
```

Which equates to 2 times 5 which = 10, then you add 3.  NOT 3 times 2
and then maybe add 5.  So clojure doesn't read left to right.  Well
the thrush operators help make things that are reading inside-out and
right to left ish, to go back to normal left to right reading.

So we could rewrite the above like:

```clojure
(-> 2 (* 5) (+ 3))
```

Isn't that a bit more clear?

1. Take 2

1. Multiply by 5

1. Add 3

What we are doing is exactly the way it is written!  Now normally we
write this in a function, so lets do that, because maybe we want to do
the same thing to a number other than 2!

```clojure
(defn math-steps [x]
  (-> x
      (* 5)
      (+ 3)))
```      

In a REPL:

```
user> (math-steps 2)
13
user> (math-steps 3)
18
```

So what does the `->>` operator do?  Well its very similar, but
instead of putting the argument at the front of the list of data, it
puts it at the end.  So graphically this is the difference:

    (-> 2 (* 5) (+ 3))
    
becomes    
    
    (* 2 5) = 10

then take that result, **10** and put it at the front of the data like so:

    (+ 10 3) = 13
    
But notice **WHERE** the 2 and 10 went.  They went right after the * and +
operators respectively.  If we use the `->>` operator, it would put
the 2 and 10 **AFTER** the rest of the data like so:

    (->> 2 (* 5) (+ 3))
    
becomes:    
    
    (* 5 2) = 10

then injecting that result at the **END** of the next operation like
so: 

    (+ 3 10) = 13

Notice the 2 and 10 go at the **END**.  Lets use a few more arguments to
make it clearer.  Lets examine what happens with the following:

    (-> 2  (* 5 4 6) (+ 3 5 8))
    
becomes:    
    
    (* 2 5 4 6) = 240

then take that result, `240` and insert it at the BEGINNING of the
next list.  (well technically not the beginning, actually after the
function, in our case the `+` function).

    (+ 240 3 5 8) = 256
    
Whereas the `->>` operator does the following:    
    
    (->> 2 (* 5 4 6) (+ 3 5 8))
    
becomes:    
    
    (* 5 4 6 2) = 240
    
see the `2` is at the end of the list.  (* 5 4 6 **2**).  The result,
`240` likewise goes at the **END** of the subsequent list.
    
    (+ 3 5 8 240) = 256

Since it doesn't matter which order you add or multiply numbers in a
list up, these examples don't really illustrate when you'd prefer `->`
over `->>`, but they do illustrate what the operators do, which at
least will help you when you come across this operator and want to
know how it works.

#### Why use the thrush operator?

If it's not already clear, the purpose of the operator is to take
*regular* clojure, that is evaluated from the inside out, and present
it in a way that is easier to read, from left to right.  Since code
legibility is very important, aim to use this operator to make your
code more legible.

#### Without an argument

You can also do the following:

    (-> (* 5 4 6) (+ 3 5 8))
    
Which simply becomes:

    (* 5 4 6) = 120

then inject that result into the next operation:

    (+ 120 3 5 8)
    
There is a real world explanation provided by: [Debasish
Ghosh](http://debasishg.blogspot.com/2010/04/thrush-in-clojure.html),
which, of the thrush explanations, I appreciated the most.

#### Addendum / Notes

One astute reader noted that these things are actually not the thrush
operator, pointing to [an
article](http://blog.fogus.me/2010/09/28/thrush-in-clojure-redux/).
To me that is advanced clojure, that hopefully one day I'll
appreciate, for now, my articles are aimed at getting newbies to a
base level of proficiency.