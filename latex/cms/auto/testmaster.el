(TeX-add-style-hook "testmaster"
 (lambda ()
    (TeX-run-style-hooks
     "latex2e"
     "combine10"
     "combine"
     "..."
     "testslave")))

