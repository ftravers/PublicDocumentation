# install process

[Reference - usrlib](https://wiki.archlinux.org/index.php/DeveloperWiki:usrlib)

* Fresh install from media

* upgrade

```bash
# pacman -Syu --ignore glibc
# rm -rf /usr/sbin/tzselect /usr/sbin/zic /usr/sbin/zdump
# pacman -U http://pkgbuild.com/~allan/glibc-2.16.0-1-x86_64.pkg.tar.xz
# pacman-key --init; pacman-key --populate archlinux
``` 
 
generate randomness for your own key

```bash
# pacman -Su
# grep '^lib/' /var/lib/pacman/local/*/files
# find /lib -exec pacman -Qo -- {} +
```

    
