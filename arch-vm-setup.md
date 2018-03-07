after `arch-chroot`

```bash
# pacman -S sudo openssh net-tools rxvt-unicode git emacs
```

Enable sshd:

```bash
# systemctl enable sshd.service


when says reboot, dont, just shutdown: 

```bash
# shutdown -h now
```

Now in VirtualBox remove the CD from the virtual CD Drive.  Set the vm
to use bridge networking.

Setup your Arch Linux host so that it can do bridge networking with
the Arch linux guest.

Now startup the vm.  Run ifconfig to determine your ip address.

```bash
# ifconfig
```

Now ssh into it.

```bash
# visudo
```

add the following line:

```
fenton ALL=(ALL) NOPASSWD: ALL
```

Get my normal files into my home dir.

```bash
$ cd /home
$ sudo scp ftravers@spicevan.com:/home/ftravers/fenton.tgz .
$ sudo tar xvfz fenton
$ sudo chown -R fenton:users fenton
$ cd
$ git remote rm origin
$ git remote add origin ft_git3@spicevan.com:ft.home.dir.git
$ chmod 600 .ssh/config .ssh/id_rsa
$ git pull origin master
$ mkdir projects; cd projects;
$ git clone ft_git3@spicevan.com:documentation.git
$ cd documentation
$ ./arch-packages.md 
$ cd
$ scp ftravers@spicevan.com:/home/ftravers/ssh.tgz .
$ tar xvfz ssh.tgz
$ chmod 600 .ssh/config .ssh/id_rsa
$ sudo pacman -S xf86-video-vesa
$ sudo systemctl enable slim.service
```

modify `/etc/slim.conf` with the following:

```
default_user        fenton
auto_login          yes
```

reboot

```bash
$ sudo reboot
```
