# Understanding the IO Monad

This tutorial aims to show you how to use the IO Monad.  It is
absolutely not theoretical.  It will help you get past the first
difficulties you'll have with basic IO, with an understanding
sufficient to not get completely stuck.

I think this is very useful, because after you have done some initial
reading you really have to roll up your sleeves and try something.
Without knowing what the rest of this article teaches, you are going
to wind up scratching your head.

To me this article should be used very close to the beginning of your
path on learning haskell.  I'm a firm believer in learning by doing.
What I find in most haskell tutorials on the monad is they have too
much theory and too little practical, what you need to get started,
information.  This is a shame because it needlessly frustrates
newbies. 

After this tutorial you should understand what you need to know to be
proficient at using the IO monad.  I'm not going to use the word monad
for the rest of the tutorial, until the very last paragraph in
conclusion!  I assume some basic programming background, and also that
you've read some information about *pure* functions, and their lack of
side-effects.

Lets jump right in.  Briefly look at the following basic haskell code,
by the end of the tutorial you'll understand exactly how this code
works.  Don't get too concerned about it right now:

```haskell
main :: IO ()
main = do
   putStrLn "What is your name?"
   name <- getLine
   putStrLn ("Welcome, " ++ name ++ "!")
```

The first part: 

```haskell
main :: IO ()
```

is the type signature of the `main` function.  The format of
signatures are: _function name_ :: _type signature_.

So we can see that the main function, or expression, evaluates to an
`IO ()`.  The `()` part is basically equivalent to a null, void, nil
or whatever word you use to mean nothing.  The `IO` part means that
the `()` is wrapped by the `IO` part.  Lets use the term wrapping,
instead of prefixing.  So when the word `IO` prefixes another type, we
say that it is wrapping it.  When the `IO` part gets removed from in
front of another type, we say it has been *unwrapped*.

Next lets examine a `do` block.  `do` is syntactic sugar.  The
following two blocks are effectively the same, `do` in the first being
short-hand or syntactic sugar for the second example.

```haskell
do
  a "hello"
  b
  c
```

and

```haskell
  a "hello" >>= b >>= c
```

`>>=` is the *bind* operator.  It is an *infix* operator, so it's
arguments are on the left and right hand sides of it, just like the
`+` operator in: `1 + 3`.

What the bind operator does, is it takes the value of the expression
on the left; this means the result of the LHS or output of the LHS;
and applies it to the function on the right; i.e. it becomes the
argument to the function on the RHS.  Lets say that `a`,`b`, and `c`
have the following function signatures:

```haskell
a :: String -> IO ()
b :: IO String
c :: String -> IO ()
```

What this means is `a` takes a `String` and returns an `IO ()`.  `b`
on the other hand doesn't take any arguments, but just results in an
`IO String`.  `c` is the same as `a`.

Bind also does one more thing, when it takes the output of the left
hand side (LHS), say for the case of `a` this would be `IO ()`, it
unwraps it, leaving, in this case, just the `()` part.  So:

```haskell
IO ()
```

becomes

```haskell
()
```

or just null, not `IO ()`.  After bind does this
unpackaging/unwrapping, it then supplies this to function on the right
hand side (RHS).  So lets go back to our example and process it step
by step

```haskell
  a "hello" >>= b >>= c
```

So first we evaluate

```haskell
a "hello"
```

who's signature is: 

```haskell
a :: String -> IO ()
```

so it's result is of type:

```haskell
IO ()
```

now the bind operator, unpackages this, removing the `IO`, leaving:

```haskell
()
```

and we apply this to the function on the right, `b`, who has the
following signature:

```haskell
b :: IO String
```

so this is a function that doesn't have any arguments, and just
evaluates to an `IO String`.  Well kind of appropriate since we were
only going to pass it a `()`, null, anyway!  So continuing with our
bind operator, we must now take the output of the `b` function and
strip the `IO` from it, leaving just `String`.

So the final part looks like this:

```haskell
b >>= c
```

with signatures:

```haskell
b :: IO String
c :: String -> IO ()
```

so the bind operator removes the `IO` from `b` resultant type, leaving
just `String`, which is exactly what `c` is looking for, so we are in
good shape.  Conveniently the `c` evaluates to `IO ()`, which is
exactly the type of the function `main`:

```haskell
main :: IO ()
main = do
  ...
  c :: String -> IO ()
```

great!  Keep in mind that the bind operator only gets inserted between
the function, so since there is no function after `c` we don't remove
the `IO` from it's result, we simply understand that `c`'s result is
the result of the `main` function since it's the last function in the
`do` block.


Now lets apply this to the real code that we
have at the top:

```haskell
main :: IO ()
main = do
   putStrLn "What is your name?"
   name <- getLine
   putStrLn ("Welcome, " ++ name ++ "!")
```

Lets get the method signatures for those functions:

```haskell
putStrLn :: String -> IO ()
getLine :: IO String
putStrLn :: String -> IO ()
```

Now you can step through this code, just as was done above to see how
the inputs and outputs proceed.  Just remember the bind operator takes
the output of the previous line, strips the `IO` part, applies whats
left as the argument to the next function.

So as promised I didn't use the word monad again until now.  Well
everywhere you saw `IO` above, that was the `IO` *MONAD*.  

Next steps, see: [Going Pure](io-monad-2.md) to see how to execute
pure functions from the `main :: IO ()` impure function.










