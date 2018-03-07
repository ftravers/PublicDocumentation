### Clojure-Emacs setup

This setup uses:

* the latest emacs (24.x as of this writing).  
* a linux (should be okay for most linuxes)
* leiningen for building/packaging (like ant/maven)

### Leiningen

Now you need to setup `lein`.

```bash
$ cd ~/bin; wget -c https://raw.github.com/technomancy/leiningen/stable/bin/lein; chmod a+x lein; ./lein self-install
```

Add `lein` to your path, put a line like the following into `~/.bashrc`:

    export PATH=$PATH:~/bin
    
install rlwrap (needed for arch linux):

    $ sudo pacman -S rlwrap

install java:

    $ sudo pacman -S jdk7-openjdk

### Emacs

* Download a developer version of emacs from here:

http://alpha.gnu.org/gnu/emacs/pretest/

```bash
$ tar xvfz emacs-24.1-rc.tar.gz
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


### Notes

It's a beta release, but it's used because I couldn't get the 23
version to see the marmelade ELPA repository.
