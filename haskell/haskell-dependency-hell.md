# Fixing Cabal dependency hell.


## Overview 

We would like to strive towards a dependency management solution, for
haskell, that doesn't fail to compile.  Cabal, can fail when you
request it to install a library.

Before fixing a problem, the problem should be well understood.  The
next section will articulate the problem.


## The Context & Background Info

### Cabal Package

### Darcs

Darcs, is a distributed version control system written in Haskell.  It
is similar to git.

### GHC-PKG

Understanding how GHC handles packages.

### Hackage



### darcs


### package deps

[Package Deps](http://packdeps.haskellers.com/)

Enter a search string in the box above. It will find all packages
containing that string in the package name, maintainer or author
fields, and create an Atom feed for restrictive bounds. Simply add
that URL to a news reader, and you're good to go!

All of the code is available on Github . Additionally, there is a
package on Hackage with the code powering this site both as a library
and as an executable, so you can test code which is not uploaded to
the public Hackage server.


## Solution in other Domains

### Nix

[Nix](http://nixos.org/): is a purely functional package manager. This
means that it can ensure that an upgrade to one package cannot break
others, that you can always roll back to previous version, that
multiple versions of a package can coexist on the same system, and
much more.

## Problem

* [The dreaded diamond dependency](http://www.well-typed.com/blog/9) (DDD)

* [Solving the DDD](http://www.well-typed.com/blog/12)

## Solutions

### Sandboxing

cabal-dev, uses sandboxing as a technique


## Tips & Techniques

### Reseting your haskell system

#### Remove old

These instructions have been tested only on an Arch Linux system.
YMMV. 

Remove arch packages:

```bash
$ sudo pacman -R cabal-install ghc
```

* Delete `~/.cabal` and `~/.ghc`

```bash
$ rm -rf ~/.cabal
$ rm -rf ~/.ghc
```

* Delete the GHC package database folders

First find the folders to delete with:

```bash
$ ghc-pkg list | grep package.conf.d
...
```

Next delete them:

```bash
$ sudo rm -rf /usr/lib/ghc-7.6.1/package.conf.d
$ rm -rf ~/.ghc/x86_64-linux-7.6.1/package.conf.d
```


#### Install fresh


```bash
$ sudo pacman -S cabal-install ghc
$ cabal update
$ cabal install cabal-install
$ sudo ghc-pkg init /usr/lib/ghc-7.6.1/package.conf.d
```

Here's the problem:

```bash
$ cabal install cabal-install
Resolving dependencies...
cabal: Could not resolve dependencies:
trying: cabal-install-1.16.0.1
rejecting: base-3.0.3.2 (conflict: base => base>=4.0 && <4.3)
rejecting: base-3.0.3.1 (conflict: base => base>=4.0 && <4.2)
rejecting: base-4.6.0.0, 4.5.1.0, 4.5.0.0, 4.4.1.0, 4.4.0.0, 4.3.1.0, 4.3.0.0,
4.2.0.2, 4.2.0.1, 4.2.0.0, 4.1.0.0, 4.0.0.0 (only already installed instances
can be used)
```
