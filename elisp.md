To determine the value of a symbol do C-x C-e after the symbol

Items ending in p, such as zerop, stand for predicate and evaluate to
true of false.  So (zerop 3) = false, and (zerop 0) = true.

The argument list is followed by the documentation string that describes the
function. This is what you see when you type C-h f and the name of a
function.

(defun multiply-by-seven (number) ; Interactive version.
"Multiply NUMBER by seven."
(interactive "p")
(message "The result is %d" (* 7 number)))
In this function, the expression, (interactive "p"), is a list of two elements.
The "p" tells Emacs to pass the prefix argument to the function and use its value
for the argument of the function.

Then, you can use this code by
typing C-u and a number and then typing M-x multiply-by-seven and pressing
RET. The phrase ‘The result is ...’ followed by the product will appear in the
echo area.

     let

let is used to attach or bind a symbol to a value in such a way that the Lisp
interpreter will not confuse the variable with a variable of the same name that is
not part of the function.

Another way to think about let is that it is like a setq that is temporary and
local.

    (let ((variable value)
      (variable value)
       ...)
       body...)

    (let ((zebra ’stripes)
          (tiger ’fierce))
          (message "One kind of animal has %s and another is %s."
    zebra tiger))

    (if (> 5 4)
        (message "5 is greater than 4!"))

* Keyboard shortcuts

Key binding, keymaps.

*** Mode-specific keymaps

Mode-specific keymaps are bound using the define-key function,

    (define-key texinfo-mode-map "\C-c\C-cg" ’texinfo-insert-@group)

*** Global

When you use `global-set-key` to set the keybinding for a single command
in all parts of Emacs, you are specifying the keybinding in
`current-global-map`.

Specific modes, such as C mode or Text mode, have their own keymaps;
the mode-specific keymaps override the global map that is shared by
all buffers.

    (global-set-key "\C-x\C-b" ’buffer-menu)


