To add mirrors that portage can use.

mirrorselect -i -o -r >> /etc/portage/makeconf

Sometimes you'll issue a command which will update a config file for
you.  Well thats not exactly true.  It'll create a copy of it that
with the two commands: `dispatch-conf`, and `etc-update` will assist
you to approve the changes.

## Modify Kernel

```bash
# cd /usr/src/linux
# make menuconfig
```

## find a package:

Say you want to find the package for emacs do:

```bash
$ emerge --search emacs
```

Say you want to enable use flags for `mutt` email client, edit:
`/etc/portage/package.use` with:

```

```

Restart your network

```bash
$ /etc/init.d/net.ethX restart
```

Enable the sshd deamon to start on boot:

```bash
# rc-update add sshd default
```

Install `sudo`:

```bash
# emerge sudo
```

install `vi` and make default editor

```bash
# emerge vim
```

create file called: `/etc/env.d/99local`, with the following contents:

```
EDITOR=/usr/bin/vi
```

update your environment with:

```bash
# env-update
```

Log out and back in for the update to take.

Add user to `sudo` with `visudo`, my username is `fenton`

```bash
# visudo
```

put a line that looks like the following in it:

```
fenton ALL=(ALL) NOPASSWD: ALL
```

now u can run stuff as your local user instead of root, exit out as
root and become your local user.  For me thats: `fenton`, notice the
`$` prompt versus the root prompt of: `#`

install `git` the DVCS:

```
$ sudo emerge git
```


###### This is where the VM WAS STOPPED...pickup from here...
http://www.haskell.org/haskellwiki/Gentoo
http://www.gentoo.org/proj/en/overlays/userguide.xml
http://www.gentoo.org/doc/en/handbook/handbook-x86.xml?part=2&chap=2

# Install Gentoo Overlays



install `layman`

```bash
$ sudo emerge layman
```



cabal unpack cabal-dev
cd cabal-dev
vim cabal-dev.cabal
(fix the dep)
cabal install
