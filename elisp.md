
# Table of Contents

1.  [REPL](#org62ff909)
2.  [Function definition](#org7440f32)
3.  [Debugger EDebug](#org04a186c)
4.  [Useful](#orgf3f3717)
5.  [Function Reference](#org8ad20da)
    1.  [positions](#orgc16da47)
6.  [IELM](#org365d91c)
7.  [Command Log Mode](#org8e801e1)
8.  [Unit Tests](#org90b8075)


<a id="org62ff909"></a>

# REPL

    A-x ielm


<a id="org7440f32"></a>

# Function definition

    (defun foo () 5)
    (foo) => 5

Functions you'll call interactively, i.e. you can bind to a key
sequence, the above function can only be called from other functions.

    (defun foo () (interactive "r") 5)

    ~abc~


<a id="org04a186c"></a>

# Debugger EDebug

Instrument your function with the function

    edebug-eval-top-level-form

<table border="2" cellspacing="0" cellpadding="6" rules="groups" frame="hsides">


<colgroup>
<col  class="org-left" />

<col  class="org-left" />
</colgroup>
<tbody>
<tr>
<td class="org-left">SPC</td>
<td class="org-left">(step) move through sexp's, result displayed in mini-buffer</td>
</tr>


<tr>
<td class="org-left">n</td>
<td class="org-left">(next)</td>
</tr>


<tr>
<td class="org-left">&#xa0;</td>
<td class="org-left">&#xa0;</td>
</tr>
</tbody>
</table>

SPC             edebug-step-mode

-   negative-argument

=               edebug-temp-display-freq-count
?               edebug-help
B               edebug-next-breakpoint
C               edebug-Continue-fast-mode
E               edebug-visit-eval-list
G               edebug-Go-nonstop-mode
I               edebug-instrument-callee
P               edebug-view-outside
Q               edebug-top-level-nonstop
S               edebug-stop
T               edebug-Trace-fast-mode
W               edebug-toggle-save-windows
X               edebug-set-global-break-condition
a               abort-recursive-edit
b               edebug-set-breakpoint
c               edebug-continue-mode
d               edebug-backtrace
e               edebug-eval-expression
f               edebug-forward-sexp
g               edebug-go-mode
h               edebug-goto-here
i               edebug-step-in
n               edebug-next-mode
o               edebug-step-out
p               edebug-bounce-point
q               top-level
r               edebug-previous-result
t               edebug-trace-mode
u               edebug-unset-breakpoint
v               edebug-view-outside
w               edebug-where
x               edebug-set-conditional-breakpoint
DEL             backward-delete-char-untabify
<emacs-state>   Prefix Command
<intercept-state>               all
<normal-state>                  Prefix Command
<visual-state>                  Prefix Command

<emacs-state> ,                 hydra-edebug-comma/body
<emacs-state> i                 edebug-step-in
<emacs-state> n                 edebug-step-mode
<emacs-state> o                 edebug-step-out
<emacs-state> q                 top-level

<visual-state> ,                hydra-edebug-comma/body
<visual-state> i                edebug-step-in
<visual-state> n                edebug-step-mode
<visual-state> o                edebug-step-out
<visual-state> q                top-level

<normal-state> ,                hydra-edebug-comma/body
<normal-state> i                edebug-step-in
<normal-state> n                edebug-step-mode
<normal-state> o                edebug-step-out
<normal-state> q                top-level

C-c C-c         edebug-go-mode
C-c C-d         edebug-unset-breakpoint
C-c C-l         edebug-where
C-c C-n         edebug-next-mode
C-c C-s         edebug-step-mode
C-c C-t         ??

C-x C-a         Prefix Command
C-x C-e         edebug-eval-last-sexp
C-x SPC         edebug-set-breakpoint

C-M-i           completion-at-point
C-M-q           indent-pp-sexp
C-M-x           eval-defun

C-M-q           indent-sexp
  (that binding is currently shadowed by another mode)

C-M-q           prog-indent-sexp
  (that binding is currently shadowed by another mode)

C-x C-a C-c     edebug-go-mode
C-x C-a C-l     edebug-where
C-x C-a RET     edebug-set-initial-mode
C-x C-a C-n     edebug-next-mode
C-x C-a C-s     edebug-step-mode

Global commands prefixed by ‘global-edebug-prefix’:
key             binding
&#x2014;             --&#x2013;&#x2014;

SPC             edebug-step-mode
=               edebug-display-freq-count
C               edebug-Continue-fast-mode
G               edebug-Go-nonstop-mode
Q               edebug-top-level-nonstop
T               edebug-Trace-fast-mode
W               edebug-toggle-save-windows
X               edebug-set-global-break-condition
a               abort-recursive-edit
b               edebug-set-breakpoint
c               edebug-continue-mode
g               edebug-go-mode
q               top-level
t               edebug-trace-mode
u               edebug-unset-breakpoint
w               edebug-where
x               edebug-set-conditional-breakpoint

<table border="2" cellspacing="0" cellpadding="6" rules="groups" frame="hsides">


<colgroup>
<col  class="org-left" />

<col  class="org-left" />
</colgroup>
<tbody>
<tr>
<td class="org-left">i (into)</td>
<td class="org-left">step into function call</td>
</tr>


<tr>
<td class="org-left">o</td>
<td class="org-left">step out</td>
</tr>


<tr>
<td class="org-left">&#xa0;</td>
<td class="org-left">&#xa0;</td>
</tr>
</tbody>
</table>

<table border="2" cellspacing="0" cellpadding="6" rules="groups" frame="hsides">


<colgroup>
<col  class="org-left" />

<col  class="org-left" />
</colgroup>
<tbody>
<tr>
<td class="org-left">v</td>
<td class="org-left">toggle local variables</td>
</tr>


<tr>
<td class="org-left">q</td>
<td class="org-left">quit</td>
</tr>


<tr>
<td class="org-left">g (go)</td>
<td class="org-left">go to end of func without quitting</td>
</tr>


<tr>
<td class="org-left">h (go here)</td>
<td class="org-left">breakpoint to where cursor is</td>
</tr>


<tr>
<td class="org-left">b</td>
<td class="org-left">set breakpoint here</td>
</tr>


<tr>
<td class="org-left">G</td>
<td class="org-left">go and dont stop at breakpoints</td>
</tr>
</tbody>
</table>

re-eval function to uninstrument it

<https://www.youtube.com/watch?v=odkYXXYOxpo>

for macros use macrostep


<a id="orgf3f3717"></a>

# Useful

Set a variable

:(setq a 123)

<table border="2" cellspacing="0" cellpadding="6" rules="groups" frame="hsides">


<colgroup>
<col  class="org-left" />

<col  class="org-left" />
</colgroup>
<tbody>
<tr>
<td class="org-left">function</td>
<td class="org-left">description</td>
</tr>


<tr>
<td class="org-left">symbol-function</td>
<td class="org-left">get definition of function</td>
</tr>


<tr>
<td class="org-left">&#xa0;</td>
<td class="org-left">&#xa0;</td>
</tr>
</tbody>
</table>


<a id="org8ad20da"></a>

# Function Reference


<a id="orgc16da47"></a>

## positions

    line-beginning-position

the position of the beginning of the current line

    point-max/point-min

the last/first point (position) in the buffer.

    point

current point position


<a id="org365d91c"></a>

# IELM

`C-c C-b`

Can select a buffer to bind the IELM to, so that functions such as
(point) report the point in that buffer and not the ielm buffer.


<a id="org8e801e1"></a>

# Command Log Mode

Trace

With command log mode, it shows which functions are run.

Install:

    (use-package command-log-mode)

Enable for current buffer:

    M-x command-log-mode

Show the log:

    M-x clm/open-command-log-buffer


<a id="org90b8075"></a>

# Unit Tests

can run:

, t t

in either the impl file or test file and should run the tests that are
loaded

