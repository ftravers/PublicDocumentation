I'm a learn by doing person.  As such, this is how this tutorial is
structured.  Pre-requisites are: 

* You've installed Haskell and played with it a bit.
* You've tried to learn haskell by reading a number of articles but
  still haven't really made much progress
  
This tutorial will highlight some common areas where Haskell trips
newbie's up.  We'll start by writing a trivial program that executes
the `ls` system command on Unix to get a directory listing.  The
output from that command looks like:

```bash
total 212
drwxr-xr-x   3 root root 40960 Oct 27 08:08 bin
drwxr-xr-x 321 root root 36864 Oct 26 12:35 include
drwxr-xr-x 145 root root 90112 Oct 26 12:35 lib
```

We'll then try to add up the filesizes, above we see three entries
with sizes of 40, 36 and 90 kB respectively.  Okay lets dive in!

Learn to use some basic system calls.

```haskell
ghci> :module System.Cmd
ghci> rawSystem "ls" ["-l", "/usr"]
total 212
drwxr-xr-x   3 root root 40960 Oct 27 08:08 bin
drwxr-xr-x 321 root root 36864 Oct 26 12:35 include
drwxr-xr-x 145 root root 90112 Oct 26 12:35 lib
...
```

So lets code this:

```haskell
import System.Process
main :: IO ()
main = do rawSystem "ls" ["-l", "/usr"]
```

Compile it:

```bash
$ ghc Main.hs
    Couldn't match type `GHC.IO.Exception.ExitCode' with `()'
    Expected type: IO ()
      Actual type: IO GHC.IO.Exception.ExitCode
```

We see from the
[documentation](http://hackage.haskell.org/packages/archive/process/1.0.1.1/doc/html/System-Process.html)
that the signature to rawSystem is: 

```haskell
rawSystem :: String -> [String] -> IO ExitCode
```

but we know main must return `IO ()`.  Putting in: `return ()`

```haskell
main = do 
  rawSystem "ls" ["-l", "/usr"]
  return ()
```

and compile it:

```bash
$ ghc Main.hs
[1 of 1] Compiling Main             ( Main.hs, Main.o )
Linking Main ...
$ Main
total 212
drwxr-xr-x   3 root root 40960 Oct 27 08:08 bin
drwxr-xr-x 321 root root 36864 Oct 26 12:35 include
drwxr-xr-x 145 root root 90112 Oct 26 12:35 lib
```

however this is quite useless, we'd like to read a directory, put it
into some type of data structure, do some analysis, then print out a
result right?  Why don't we sum the file sizes, so mimic the `du -sh`
command? 

Throughout the tutorial I'll be looking up function signatures, so you
need to know how to do that too.  Here are a few ways to find
information about functions:

* [Hoogle](http://www.haskell.org/hoogle)

* From GHCI: `:info lines` or `:i lines` for short.


At the documentation page, I found a command called: 

```haskell
readProcess :: FilePath -> [String] -> String -> IO String
```

so lets give this one a try:

```haskell
main = do 
  dirList <- readProcess "ls" ["-l", "/usr"] []
  print dirList
  return ()
```

Lets also look at the signature for `print`

```haskell
> :i print
print :: Show a => a -> IO ()
```

The `Show a =>` part means that `a` must conform to the `Show`
typeclass.  The `Show` typeclass basically means that the object `a`
must implement the `toString` method, or be printable, or convertable
to a string format.

The use of `a` is basically a wildcard for any type.  Much like `x`
and `y` are the variables in algebra for numbers.  `a` means any
type.

Finally: `a -> IO ()` means print takes in any type and outputs a type
of `IO ()`.  `()` is basically null.  The `IO` part is what they call
the IO Monad, and basically implies that the function has
side-effects, and therefore isn't a pure function.  

You should have read about the importance of 'pure' functions to
continue using this tutorial.  Once a function is impure, i.e. it's
return type is of type `IO ...` you cannot wrap that function in
another function that is pure.  It _taints_ you all the way up the
function stack calls.  Impure functions can call out to pure
functions, but pure functions CANNOT call out to impure functions.

Back to our our code, remember this is what we have:

```haskell
main = do 
  dirList <- readProcess "ls" ["-l", "/usr"] []
  print dirList
  return ()
```

compiling...

```bash
$ ghc Main
[1 of 1] Compiling Main             ( Main.hs, Main.o )
Linking Main ...
$ Main
"total 212\ndrwxr-xr-x   3 root root 40960 Oct 27 08:08 bin\ndrwxr-xr ...
```

Something very tricky just transpired!  The `<-` operator must have
converted an `IO String` to a `String`, since from the function
signature of `print` is:

```haskell
print :: Show a => a -> IO ()
```

not:

```haskell
print :: Show a => IO a -> IO ()
```

When I was doing my reading, I came across a lot of material that said
you CANNOT convert from `IO a` to `a`, in fact I make that assertion
above!  But here we are doing exactly that!  So I was a bit confused.
Well it turns out that since we are in a function that has side
effects, remember `main`'s signature:

```haskell
main :: IO ()
```

as long as we ultimate evaluate to an `IO ()`, inside the function we
can wrap and unwrap values from the `IO ()` container.

The main function will return an `IO ()`, so we are not escaping from
the IO Monad once entered.  We can temporarily escape, but ultimately
we must go back into it as the method signature demands.

So the `<-` in the following line, unwraps the return type of
`readProcess`, which is `IO String`: 

```haskell
readProcess :: FilePath -> [String] -> String -> IO String
```

from the `IO` monad, leaving just `String`, which *IS* a valid input
for the `print` function.

Back to the task at hand.  `readProcess` returned one large string,
lets break it up to a list of strings by separating the big string on
new line characters `\n`.  The `lines` function can do this for us!
The signature of `lines` is:

```haskell
> :i lines
lines :: String -> [String]
```

So lets try to use it:

```haskell
  dirList <- readProcess "ls" ["-l", "/usr"] []
  myLines = lines dirList
  return ()
```

and compiling:

```bash
 $ ghc Main.hs
Main.hs:7:11: parse error on input `='
```

Damn, it doesn't work!  The reason for this is that the `do` notation
is syntactic sugar, that must be used properly.  Lets look at an
example:

```haskell
do
   putStrLn "What is your name?"
   name <- getLine
   putStrLn ("Welcome, " ++ name ++ "!")
```

is the syntactic sugar for:

```haskell
putStrLn "What is your name?"
>>= (\_ -> getLine)
>>= (\name -> putStrLn ("Welcome, " ++ name ++ "!"))
```

What the `>>=` bind infix operator does, is take the value on the left
and apply it to the function on the right.  Here is the signature for
`putStrLn`: 

```haskell
putStrLn :: String -> IO ()
```

So it results in an `IO ()`.  Lets quickly review how anonymous
function work.

```haskell
ghci> (\x -> x + 1) 4
5 :: Integer
```

The previous example is run in the GHCI environment.  Basically: `(\x
-> x + 1)` is the anonymous function part.  `4` is the argument to the
anonymous function.  We start anonymous functions with a `\` because
it looks a bit like a lambda sign, which is a synonym for anonymous
functions from math.  The next part between `\` and `->` are the
arguments, in this case there is just one: `x`.  Finally the part to
the right of `->` is the function body.  In this case we increment our
argument by one, thus the `5 :: Integer` result.  Okay back to IO!

So we are currently looking at:

```haskell
 putStrLn "What is your name?" >>= (\_ -> getLine)
```

`putStrln`... turns into a: `IO ()` as we can see by it's signature,
and this is applied to: `(\_ -> getLine)`.  The underscore, `_`,
basically means we dont care what our argument is, basically just
throw it away.  So lets look at `getLine`:

```haskell
> :i getLine
getLine :: IO String
```

So the value of `getLine` is simply an `IO String`.  Finally we are
left with:

```haskell
(\_ -> getLine) >>= (\name -> putStrLn ("Welcome, " ++ name ++ "!"))
```

We know the left hand side is of type: `IO String`, and the value of
putStrLn again is:

```haskell
putStrLn :: String -> IO ()
```

Well this takes a `String` not a `IO String`!  The bind operator also
unpackages the monad that it is working with, so `IO String` turns
into `String`.  This finally results in an `IO ()`, just the type that
main must evaluate to!  Recall the type of `main`:

```haskell
main :: IO ()
main = do
...
```

So back to our problem:

```haskell
  dirList <- readProcess "ls" ["-l", "/usr"] []
  myLines = lines dirList
  return ()
```

again the error message:

```bash
 $ ghc Main.hs
Main.hs:7:11: parse error on input `='
```

Well whats the type of: `lines dirList`?

```haskell
> :i lines
lines :: String -> [String]
```

There is no `IO` to be seen anywhere, so it breaks down.  We need to
replace it with a function that returns a type of: `IO` I guess.  Lets
try. 

Our original goal was to sum up the file sizes, so lets continue down
that path.  However, lets go really slow and build up to it!  First,
lets see the return type of the main function here: `readProcess`:

```haskell
> :i readProcess
FilePath -> [String] -> String -> IO String
```

Remember the bind `>>=` operator unpackages the contents from the type
`IO xyz` to just be `xyz`.  So we need to make a function that takes a
`String` in, and returns an `IO String`.  Lets call it:
`dirSpaceUsed`, so:

```haskell
dirSpaceUsed :: String -> IO String
```

Thats our end goal.  But again, lets go slow!

Lets get some helper functions.  `lines` breaks a long string on line
separators: `'\n'` for example.  So we'll end up with a list of
`String`'s, i.e.: `[String]`.

`splitOn`, in the `Data.List.Split` module, splits a string on a given
character:

```haskell
> splitOn "," "my,comma,separated,list"
["my","comma","separated","list"]
```

However, lines returns a list of `String`s, while splitOn takes only a
single `String`!  We can use a handy function called `map` which takes a
function and a list, and applies the function to each element in the
list, and returns the new list.  So we should be able to do something
like: 

```haskell
map (splitOn " ") (lines dirListing)
```

Lets remind ourselves what our basic output of `ls` looks like:

```
drwxr-xr-x   3 root root 40960 Oct 27 08:08 bin
drwxr-xr-x 321 root root 36864 Oct 26 12:35 include
```

Of course there is junk we have to worry about above and below, but
lets keep it simple!  
  
But I don't think we can write it like this.  Lets use a `where`
clause, so we can make a bunch of assignments that aid in legibility.
Here is the function I whipped up:

```haskell
makeArrayFromDirListing :: String -> [[String]]
makeArrayFromDirListing dirListing = 
    map splitOnSpace dirListingLines
    where
      dirListingLines = lines dirListing
      splitOnSpace = splitOn " "
```

Experimenting with this in the `ghci` we get:

```haskell
> let fakeDirList = "drwxr-xr-x   3 root root 40960 Oct 27 08:08 bin\ndrwxr-xr-x 321 root root 36864 Oct 26 12:35 include"
> makeArrayFromDirListing fakeDirList
[["drwxr-xr-x","","","3","root","root","40960","Oct","27","08:08","bin"],["drwxr-xr-x","321","root","root","36864","Oct","26","12:35","include"]]
```

Hey, that looks almost right!  Except there is the weirdness of:

    drwxr-xr-x   3 root root 40960
    
getting turned into:

    "drwxr-xr-x","","","3","root","root","40960"
    
while:

    drwxr-xr-x 321 root root 36864

got turned into

    "drwxr-xr-x","321","root","root","36864"
    
We've got 2 extra "" for the first one.  So I guess what we are
looking for is to split on any number of white space characters, so
1 space and 3 spaces are treated the same!  Well it turns out there's
a function for that!  Thankgs google and hoogle!  It's simply called
`words`!  So lets try to fix this up:

```haskell
map words dirListingLines
where
  dirListingLines = lines dirListing
```      
      
and reloading into the GHCI:

```haskell
> let fakeDirList = "drwxr-xr-x   3 root root 40960 Oct 27 08:08 bin\ndrwxr-xr-x 321 root root 36864 Oct 26 12:35 include"
> makeArrayFromDirListing fakeDirList
[["drwxr-xr-x","3","root","root","40960","Oct","27","08:08","bin"],["drwxr-xr-x","321","root","root","36864","Oct","26","12:35","include"]]
```

Much better!!!

Okay, now all we have is a list of list of `String`'s.  Lets turn this
2 dimensional array of `String`'s into a list of a custom type, lets
call it a: `directoryEntry`!  It should be something like the
following pseudo code:

```
filePermissions = "drwxr-xr-x" :: String
mysteryField = 3 :: Integer
owner = "root" :: String
group = "root" :: String
fileSize = 40960 :: Integer
month = "Oct" :: String
day = 27 :: Integer
time = "08:08" :: String
name = "bin"
```

Well thats a bit long, lets just use filesize and name.  Lets recall
in haskell how to make a data type.

```haskell
> data DirEntry = DirEntry String Integer  -- name, filesize
> myDirEntry = DirEntry "bin" 40960
> :t myDirEntry
myDirEntry :: DirEntry
```

Okay cool!  Now how do we convert that 2D array of strings into a list
of `DirEntry`'s???  Google to the rescue!  Get the Nth element out of
a list.  `xs !! n`  So perhaps a function like the following would do
it: 

```haskell
makeDirEntry :: [String] -> DirEntry 
makeDirEntry entry = 
    DirEntry name filesize
    where 
      name = entry !! 8
      filesize = entry !! 4
```

but before we go any further, we know this wont work because the
filesize is still a string, lets confirm this problem in the GHCI. 

```
Couldn't match type `[Char]' with `Integer'
Expected type: Integer
  Actual type: String
In the second argument of `DirEntry', namely `filesize'
...
```

What a great compiler!  Cool...so lets convert the `String` to an
`Integer`! 

```haskell
...
   where 
      name = entry !! 8
      filesize = read $ entry !! 4
```

`read` reads in a string and changes it to the required type, in this
case an integer.  Lets verify this works in the GHCI:

```haskell
> :load "/home/fenton/projects/cur-DIR/hBabySteps/Main.hs"
[1 of 1] Compiling Main             ( /home/fenton/projects/cur-DIR/hBabySteps/Main.hs, interpreted )
Ok, modules loaded: Main.
> let dirEntry = ["drwxr-xr-x","3","root","root","40960","Oct","27","08:08","bin"]
> :t dirEntry
dirEntry :: [[Char]]
```

(aside): remember `String` and `[Char]` are the same!

```haskell
> let myDirEntry = makeDirEntry dirEntry
> :t myDirEntry
myDirEntry :: DirEntry
> show myDirEntry
<interactive>:42:1:
    No instance for (Show DirEntry) arising from a use of `show'
    Possible fix: add an instance declaration for (Show DirEntry)
    In the expression: show myDirEntry
    In an equation for `it': it = show myDirEntry
>
```

So we successfully created an instance of our type, but it failed when
we went to `show` it.  The error message guides us on how to fix it
again.  Here's the fix on the data type declaration:

```haskell
data DirEntry = DirEntry String Integer deriving (Show) -- name, filesize
```

and again in the GHCI:

```
> let dirEntry = ["drwxr-xr-x","3","root","root","40960","Oct","27","08:08","bin"]
> let myDirEntry = makeDirEntry dirEntry
> show myDirEntry
"DirEntry \"bin\" 40960"
```

Great.  Ok, now lets look at one handy operator, the `$` operator.  It
basically just means run the function on the right, then apply the
result to function on the left.  So:

```haskell
read $ entry !! 4
```

means get the 5th element from the array entry, remember arrays are
indexed starting from zero, then `read` it. 

Okay, lets make a function to bring these two functions together:

```haskell
makeDirEntryList :: [[String]] -> [DirEntry]
makeDirEntryList twoDarray =
    map makeDirEntry twoDarray
```

Applying it:

```haskell
> let fakeDirList = "drwxr-xr-x   3 root root 40960 Oct 27 08:08 bin\ndrwxr-xr-x 321 root root 36864 Oct 26 12:35 include"
> let twoDarray = makeArrayFromDirListing fakeDirList
> let myDirList = makeDirEntryList twoDarray
> show myDirList
"[DirEntry \"bin\" 40960,DirEntry \"include\" 36864]"
```

Now we just need a function that adds up the file sizes, so the
signature would look something like:

```haskell
addDirEntries :: [DirEntry] -> Integer
```

but first lets write a function that extracts the `filesize` from a
`DirEntry`.  Here we use this nifty function of pattern matching, or
deconstructing the `DirEntry` data type like so:

```haskell
dirEntryFileSize :: DirEntry -> Integer
dirEntryFileSize (DirEntry name filesize) =
    filesize
```

We can use the `sum` function to sum up a list of integers, here is
the signature:

```haskell
sum :: Num a => [a] -> a
```

so lets do this all in a GHCI session:

```haskell
> :load "/home/fenton/projects/cur-DIR/hBabySteps/Main.hs"
[1 of 1] Compiling Main             ( /home/fenton/projects/cur-DIR/hBabySteps/Main.hs, interpreted )
Ok, modules loaded: Main.
> let fakeDirList = "drwxr-xr-x   3 root root 40960 Oct 27 08:08 bin\ndrwxr-xr-x 321 root root 36864 Oct 26 12:35 include"
> let twoDarray = makeArrayFromDirListing fakeDirList
> let myDirList = makeDirEntryList twoDarray
> let myFileSizeList = map dirEntryFileSize myDirList
> show myFileSizeList
"[40960,36864]"
> sum myFileSizeList
77824
```

but this is kind of ugly, we can thread operations together using the
`.` or `$` operators.  Lets call it the `sumFileSizes` function:

So here is a complete code listing for now:

```haskell
import System.Process
import Data.List.Split 

main :: IO ()
main = do 
  dirListing  <- readProcess "ls" ["-l", "/usr"] []
  dirSize <- sumFileSizes dirListing
  putStrLn dirSize

data DirEntry = DirEntry String Integer deriving (Show) -- name, filesize

sumFileSizes :: String -> IO String
sumFileSizes dirListing = return $ show $ sumFileSizeList $ filesizeList $ makeDirEntryList $ makeArrayFromDirListing $ tail dirListing

sumFileSizeList filesizeList = sum filesizeList
filesizeList myDirList = map dirEntryFileSize myDirList

makeDirEntryList :: [[String]] -> [DirEntry]
makeDirEntryList twoDarray =
    map makeDirEntry twoDarray

dirEntryFileSize :: DirEntry -> Integer
dirEntryFileSize (DirEntry name filesize) =
    filesize

makeArrayFromDirListing :: String -> [[String]]
makeArrayFromDirListing dirListing = 
    map words dirListingLines
    where
      dirListingLines = lines dirListing

makeDirEntry :: [String] -> DirEntry 
makeDirEntry entry =  
    DirEntry name filesize
    where 
      name = entry !! 8
      filesize = read $ entry !! 4
```

Compile and run:

```bash
$ ghc Main.hs
[1 of 1] Compiling Main             ( Main.hs, Main.o )
Linking Main ...
$ ./Main
Main: Prelude.(!!): index too large
```

Hmmm...  Well, lets make this code a bit more testable, to see if we
can figure out whats going on here!
