xTerm settings are found in: `~/.Xresources`, it might look like:

```
XTerm*background:       black
XTerm*foreground:       PeachPuff3
XTerm*cursorColor:      white
XTerm.vt100.geometry:   79x25
XTerm*scrollBar:        true
XTerm*scrollTtyOutput:  false
XTerm*font: -*-terminus-medium-*-*-*-14-*-*-*-*-*-cp1251
```

add a line to your .xinitrc before you run your window manager, so it
loads your `.Xresources` file, like so:

```
[[ -f ~/.Xresources ]] && xrdb -merge ~/.Xresources
```

as you are testing your ~/.Xresources file you can reload your
settings by running the following command and launching a new xterm:

    xrdb -merge ~/.Xresources; xterm
    
Some fonts like terminus are installed in /usr/share/fonts/local,
which is not added to the font path by default. By adding the
following lines to ~/.xinitrc

```
xset +fp /usr/share/fonts/local
xset fp rehash
``` 

Good list of xterm colors [here](http://critical.ch/xterm/)







