Things I don't like about IntelliJ (from an Eclipse developer)

* The multi project is the first one...but many people have covered that before so I'll leave it.

* The splitting of windows.  In Eclipse you simply drag the window wherever you want it and it goes there.

* The font sucks.  I changed to the next best one i could find: Liberation Mono, size 14. (Setting>Editor>Colors&Font>Font)

* Highlighting for XML is nasty...removed the bolds.

* Syncing the editor window with the file selected in the project navigator.  I can't make this happen by default, I have to press the button each time???  Found it: 

* Double clicking in project navigator sometimes doesn't open the file.  Sometimes even tripple clicking won't do it.  Only a quadruple click opens it up!
** Here is an answer...it's just that the double click speed is too freakin fast: In regards to the double clicking in IDEA the reason why it’s hard to open files by double clicking is that Java programs e.g. IDEA reads the double click speed from X11 resources. If it is not explicitly set then it defaults to 200 ms which is lightning fast.
In order to have a more sane threshold create ~/.Xresources and add:
*.multiClickSpeed: 400 


* Can't figure out how to get the suggestion: "Assign statement to local variable" that Ctrl-1 gives in eclipse.  I use that all the time.

** Yep! This is the Introduce Variable refactoring. By default, select some text, and then hit Ctrl + Alt + V. If the expression is incomplete or invalid, IntelliJ will still make a good guess about what you meant and try to fix it for you.

* Tab doesn't take line to correct spot right away.

* Couldn't get it to auto complete properly
** Ctrl-Shift-Space helps...

* Hot code re-run.  In eclipse I can modify a method while in the debugger, and it reloads the frame.
** Right-click: drop-frame

* Double clicking a variable to highlight it, doesn't highlight other occurrences of the variable.

* Deprecated methods are not crossed out.

Things I do like:

* Paste history is nice:

Ctrl-Shift-V

* Simply clicking the debug/run icons actually runs the last configuration, eclipse isn't that smart with it ( or it's too smart??? ), and I don't like the way it does it.

* Autocomplete is very good.