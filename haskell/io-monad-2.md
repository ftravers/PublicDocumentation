# Going Pure

How to move from the monad in main :: IO (), to pure functions,
i.e. without the IO part!

If you haven't done so, please read [Intro to IO Monad](io-monad.md)
first.

In the last tutorial we left off with the following code snippet.

```haskell
main :: IO ()
main = do
   putStrLn "What is your name?"
   name <- getLine
   putStrLn ("Welcome, " ++ name ++ "!")
```

Now say we want to call out to a pure function, one that doesn't have
`IO` in it's signature, how do we do that?  Here is how I do it:

Say we want to call a method with signature:

```haskell
greeting :: String -> String
greeting name = "Hello " ++ name ++ "."
```

we could do the following:

```haskell
main :: IO ()
main = do
   putStrLn "What is your name?"
   name <- getLine
   greeting <- return $ greeting name
   putStrLn greeting
```

and here it is working:

```haskell
> main
What is your name?
Fenton
Hello Fenton.
```
