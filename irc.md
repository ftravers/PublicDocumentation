Install irssi with: `sudo pacman -S irssi`

* [IRC Reference / Tutorial](http://www.irchelp.org/irchelp/irctutorial.html)
* [http://quadpoint.org/articles/irssi/](http://quadpoint.org/articles/irssi/)  

Join server: `irc.freenode.net` with:

    /server irc.freenode.net

join a chat room called: `#clojure` with:

    /join #clojure
    
see who is in the chatroom `#clojure`:

    /names #clojure
    
to not see JOINS PARTS QUITS NICKS for irc chat room `#clojure`:

    /ignore #clojure MODES JOINS PARTS QUITS
        
to quit:

    /quit

To auto-connect to freenode server on irssi startup, while using irssi
run command: 

    /server add -auto -network Freenode irc.freenode.net 6667
    
That will add the Freenode IRC network to your IRSSI config, and will
automatically connect when you run irssi.

To setup your nick automatically on the Freenode network do:

    /network add -nick <your-nick> Freenode

Since my nick is: fenton do: 

    /network add -nick fenton Freenode
    
If you want to automatically join a channel, say #clojure, do:

    /channel add -auto #clojure Freenode
    
To auto ignore joins/parts/quits, etc add:    


```
ignores = (
  {
    level = "JOINS PARTS QUITS";
    channels = ( "#archlinux", "#clojure", "#emacs" );
    network = "Freenode";
  }
);
```

To switch between windows `C-p` (previous) and `C-n` (next).

On the Irssi screen, what does 'Act: 3' mean?
In your irssi, you will see [2:#seneca] [Act:3,4]. What do those number mean? That means 2 is the current chat screen that you are in. 3 and 4 are the other chat that is open.
If 3 is in blue, which mean someone login, logoff, changing status.
If 3 is white, that's mean conversation is going on in that chat.
If 3 is pink, meaning you got messenger specificly sent to you plus other reason that might need your attention.

To turn on autologging, this helps if you want to search a chat do:

```
settings = {
  core = { real_name = "Fenton Travers"; user_name = "fenton"; nick = "fenton"; };
  "fe-text" = { actlist_sort = "refnum"; scrollback_time = "48h"; };
  "fe-common/core" = { autolog = "yes"; };
};
```

Where the key elements are: `scrollback_time = "48h";` and
`"fe-common/core" = { autolog = "yes"; };`
