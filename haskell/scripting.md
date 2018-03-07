# Shell Scripting with Haskell for Newbies

## Problems & Solutions

### Installation of Arch OS

I have been installing my OS, Arch Linux, many times lately and I
wanted to write some scripts to speed up the process.  Here is a
sample of some bash code I used:

```bash
#!/bin/bash

function install_yaourt_package {
    local package_name=$1
    local package_prefix=$2
    local yaourt_url="${package_prefix}/${package_name}/${package_name}.tar.gz"    
    cmd="wget http://aur.archlinux.org/packages/${yaourt_url}"
    eval $cmd

    cmd="tar xvfz ${package_name}.tar.gz"
    eval $cmd

    cmd="cd $package_name"
    eval $cmd

    su $USER
    makepkg -s
    exit

    cmd="sudo pacman -U ${package_name}${package_query_version}_x86_64.pkg.tar.xz"
    eval $cmd

    cd
}

package_name="package_query"
package_prefix="pa"
install_yaourt_package $package_name $package_prefix

package_name="yaourt"
package_prefix="ya"
#install_yaourt_package $package_name $package_prefix
```

This package has some issues, but lets just try to accomplish each
step.  First thing is we want to identify an AUR package and download
it with `wget`.

First stop was the Systems Programming chapter from Real World
Haskell.  They mention using something like:

:module System.Cmd
ghci> rawSystem "ls" ["-l", "/usr"]

### Wifi Networks

I have a problem where I scan for wireless networks with the `iw`
command.  This command returns a lot of junk and I want to parse it
and present it in a way that is useful for me.  Perhaps later
extending the script so that after I can easily see which networks are
available I can also easily join one of them.  The joining part may
require a password if the network is locked down.

To 'remember' a network a file needs to be created in
`/etc/network.d/`, so the 'joining' part would require writing a file
out to that location.  But we'll leave that to a later time.  For now
we just concern ourself with reading which networks are available,
their wireless signal strength and their name.  The unix command that
we'll be running is: `sudo iw wlan0 scan`.  I have setup the `iw`
command to not require a password using `visudo`.

Since running this command is slow, we'll run it once and save it to a
file called `networks.txt` and pass that as a command line argument to
our program for now.  Speed up testing! :)

Ideas from this scripting came from:
http://donsbot.wordpress.com/2010/08/17/practical-haskell/ however I
found that website to be over my head, being a newbie haskeller.
Therefore, some cut and paste isn't necessarily well understood and
therefore I may not explain properly???  We'll see.

First lets get a very basic haskell setup going.  I've got a folder
called: `net-hask` with a single file called `Main.hs` in it.  Like
so: 

```
net-hask/
|-- Main.hs
`-- networks.txt
```

import Text.Printf
import Process 

main = do
  s <-run "sysctl hw.setperf"
  let old = clean s
       new | old == 100 = 0
           | otherwise = 100
  run $ "sudo sysctl -w hw.setperf=" ++ show new 
  printf ³cpu: %d -> %d\n´ old new

  s <-run "sysctl hw.setperf"
  let clock = fromIntegral (clean s') / 1000
  printf "clock: %f Ghz\n" clock
    where 
      clean = read . init . tail . dropWhile (/= `=`)


