### Clojure-Emacs setup

This setup uses:

* the latest emacs (24.x as of this writing).  
* a linux (should be okay for most linuxes)
* leiningen 2 for building/packaging (like ant/maven)

### Official Documentation References

* [clojure-mode. for A-x clojure-jack-in](https://github.com/technomancy/swank-clojure)
* [swank cdt. the clojure debugger](http://georgejahad.com/clojure/swank-cdt.html)
* [Leiningen](https://github.com/technomancy/leiningen)
* [Marmelade.  To get latest version of clojure-mode](http://marmalade-repo.org/)

### Leiningen

Install

```bash
$ cd ~/bin; wget -c https://raw.github.com/technomancy/leiningen/preview/bin/lein; chmod a+x lein; ./lein self-install
```

Add `lein` to your path, put a line like the following into `~/.bashrc`:

    export PATH=$PATH:~/bin
    
install rlwrap (needed for arch linux):

    $ sudo pacman -S rlwrap

install java:

    $ sudo pacman -S jdk7-openjdk

### Emacs

* Download latest version of emacs from here:

http://gnu.mirror.iweb.com/gnu/emacs/

```bash
$ tar xvfz emacs-24.1.tar.gz
$ cd emacs-24.1
$ ./configure
$ make
$ sudo make install
$ emacs-24.1 &
```

Now you must put the following into either `~/.emacs` or
`~/.emacs.d/init.el`:

```
;; put paths onto load path to load *.el files from them
(add-to-list 'load-path "~/.emacs.d/")
(require 'package)
;; Add the original Emacs Lisp Package Archive
(add-to-list 'package-archives
             '("elpa" . "http://tromey.com/elpa/"))
;; Add the user-contributed repository
(add-to-list 'package-archives
             '("marmalade" . "http://marmalade-repo.org/packages/"))
(package-initialize)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; The above is essential, the stuff below is nice to have.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Auto reload saved source files and send them to the repl
(defun ed/clojure-compile-on-save (&optional args)
  "Compile with slime on save"
  (interactive)
  (if (and (eq major-mode 'clojure-mode)
      (slime-connected-p))
      (slime-compile-and-load-file)))
(add-hook 'after-save-hook 'ed/clojure-compile-on-save)

;; ;; paredit
(require 'paredit)
(require 'highlight-parentheses)
(add-hook 'emacs-lisp-mode-hook 'enable-paredit-mode)
(add-hook 'clojure-mode-hook
          (lambda ()
            (highlight-parentheses-mode t)
            (paredit-mode t)
            (slime-mode t)))
(setq hl-paren-colors
      '("red1" "orange1" "yellow1" "green1" "cyan1"
        "slateblue1" "magenta1" "purple"))

;; paredit in the REPL
(autoload 'paredit-mode "paredit"   
  "Minor mode for pseudo-structurally editing Lisp code."   
  t)   
;(add-hook 'lisp-mode-hook (lambda () (paredit-mode +1)))   
(mapc (lambda (mode)   
     (let ((hook (intern (concat (symbol-name mode)   
                   "-mode-hook"))))   
      (add-hook hook (lambda () (paredit-mode +1)))))   
    '(emacs-lisp lisp inferior-lisp slime slime-repl))                       

;; Stop SLIME's REPL from grabbing DEL,
;; which is annoying when backspacing over a '('
(defun override-slime-repl-bindings-with-paredit ()
  (define-key slime-repl-mode-map
    (read-kbd-macro paredit-backward-delete-key) nil))
(add-hook 'slime-repl-mode-hook 'override-slime-repl-bindings-with-paredit)
```

Startup emacs.

Inside emacs do: `A-x package-list-packages`, search for
`clojure-mode`, and while on the line, press `i` for prepare to
install, then press `x` for begin the install for all packages
selected with `i`.

Do the same thing with the packages: `paredit` and
`highlight-parentheses`.

    
### New Project

Then create a project (I'm calling my project: `my-project`), with:

```bash
$ lein new my-project
$ cd my-project
```

put `[lein-swank "1.4.4"]` into the `:plugins` section of
`project.clj`, here is a sample:

```clojure
(defproject my-project "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :plugins [[lein-swank "1.4.4"]]                 
  :dependencies [[org.clojure/clojure "1.4.0"]])
```

Now pull in dependencies:

```
$ lein deps
```

### Emacs

Startup emacs, ensure you are running 24.1.1

Load the file you want to work in

Fire up swank/slime: `A-x clojure-jack-in`

`C-c C-k` in your file to compile your changes and send them to the
repl.

In file you are editing do: `C-c A-p`, you will be prompted for
namespace of that file to switch to.  Press `Enter` to accept.

Now you can modify your file and the changes will be reflected in your REPL


### Debugging

[ref](http://georgejahad.com/clojure/swank-cdt.html)

To setup debugging, modify your `project.clj`, add: `swank-clojure
"1.4.0"`, it should look like:

```clojure
(defproject my-project "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :plugins [[lein-swank "1.4.4"]] 
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [swank-clojure "1.4.0"]])
```

Re-run `lein deps` to get the required libs.  Your directory tree
should look something like:

```bash
$ tree
.
|-- classes
|-- lib
|   |-- cdt-1.2.6.2.jar
|   |-- clj-stacktrace-0.2.4.jar
|   |-- clojure-1.4.0.jar
|   |-- clojure-contrib-1.2.0.jar
|   |-- debug-repl-0.3.1.jar
|   `-- swank-clojure-1.4.0.jar
|-- project.clj
|-- README
|-- src
|   `-- my_project
|       `-- core.clj
`-- test
    `-- my_project
        `-- test
            `-- core.clj
```

Ensure you are using a jdk not a jre.

```bash
$ java -version
java version "1.7.0_05"
Java(TM) SE Runtime Environment (build 1.7.0_05-b05)
Java HotSpot(TM) 64-Bit Server VM (build 23.1-b03, mixed mode)
```

Have a file that looks like this:

```clojure
(ns my-project.core
  (:use [clojure.set]
        [swank.cdt]))
(set-bp clojure.set/difference)
```

`A-x clojure-jack-in` as usual.  Get your namespace into the REPL with
`C-c A-p`.  In a REPL now trigger the breakpoint by evaluating the
following:

```
my-project.core> (difference #{1 2} #{2 3})
CDT location is clojure/set.clj:53:0:/home/fenton/projects/tmp/my-project/lib/clojure-1.4.0.jar
```

You'll get an emacs buffer called: `sldb clojure`, which has the keys
for (s)tepping, ne(x)ting, etc...

```
CDT BreakpointEvent in thread Swank REPL Thread
From here you can: e/eval, v/show source, s/step, x/next, o/exit func

Restarts:
 0: [QUIT] Quit to the SLIME top level

Backtrace:
  0:                set.clj:48 clojure.set/difference
  1:          NO_SOURCE_FILE:1 my-project.core/eval3027
  2:        Compiler.java:6511 clojure.lang.Compiler.eval
  3:        Compiler.java:6477 clojure.lang.Compiler.eval
```

