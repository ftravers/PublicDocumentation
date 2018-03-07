Haskell is called a pure functional programming language.  Functions
simply take inputs and create outputs.  So the follow function add two
to it's argument:

    f(x) = 2 * x
    
Say we wrote the following program:

    f(x) = 2 * x
    a = 2
    b = f(a)
    
We can see that b would equal 4.  This program is pretty useless since
it doesn't even do anything for the user.  Lets make this program a
bit more useful

    f(x) = 2 * x
    print "Enter a number: "
    a = getNumberFromTerminal()
    b = f(a)
    print "Two times " a " is: " b

Now at least this program helps a user double a number.  When your
program interacts with the outside world in ANY way, it is said to be
'impure'.  People say it has 'side effects'.  Side effects make
testing programs almost impossible, since what comes from outside your
program is totally out of your control.  Lets look at another impure
function. 

    a = 5
    f(x) = a + x
    b = f(3)

This function is impure because it looks at the global variable `a` to
calculate it's value.  Say we modified the code a bit:

    a = 5
    spawn thread in background that does: a = 4
    f(x) = a + x
    b = f(3)

So the computer, since the thread is run in the background could
choose to execute line 3 before line 2, or vice versa.  So now we can
never tell what b should be, 7 or 8?

A pure functional language ONLY allows you to read values from your
input parameters and return a result.  So you can't do the following:

    f(x) =
      print "Enter a number: "
      a = getNumberFromTerminal()
      return x * a
    b = f(3)
    
You've calculated a value that you didn't get from your input
parameters, you got it from the user.  Pure functions can only read
values from their input parameters period!  A fancy name for this is
'referencial transparency', which basically means that your function
can be used anywhere, it doesn't have any dependencies beyond it's
input parameters.  This turns out to be critical for 'composability'
and reusability.

In computers we want to reuse other peoples works (libraries) as much
a possible.  No need to reinvent the wheel, lets stand on the
shoulders of giants!  But when you use an imperative language like C,
Java, Ruby, etc..., the language has no built-in safe guards to ensure
that the library doesn't have some weird external dependency.  Lets
write a little more pseudo code.

    calculate-tomorrows-stock-price( 
      stockprice-today,
      stockprice-yesterday) = ...
      
So imagine we have some mythical awesome function that when you give
it yesterday and todays stock price for some company, it calculates
the price of the tomorrow.  Wouldn't that be nice!  So someone gives
you this code, and you start using it.  If it was written in an
imperative language, it might be under the covers referencing all
sorts of external information.  If it's written in Haskell, it simply
isn't allowed to access anything but today's and yesterdays stock price! 



Starting out my journey with learning Haskell, and pretty quickly
realize I need to understand what these things called Monads are.  I
love simple, straight forward explanations, so I hope you'll find this
one to be that way.

I'm coming from Java land, so I'm assuming the audience isn't familiar
with functional concepts, so I'll cover the few that motivate the need
for Monads.

In haskell, the order of execution of two lines of code is NOT
necessarily done from top to bottom.  In this way it is very much like
regular math.

```
f(a) = 3 + a
g(b) = 2 + b
f(g(1)) = f( 2 + 1 ) = 3 + 2 + 1
```

now the computer can choose to do either the 3+2 first or the 2+1
first, like so: 

    ( 3 + 2 ) + 1 = 5 + 1 = 6

or 

    3 + ( 2 + 1 ) = 3 + 3 = 6

however in an imperative language like C or Java, we force the
computer to execute instruction in the order that they are written.
So:

```
print "Hello "
print "World!" 
```

must not print out:

   "World!Hello "
   
