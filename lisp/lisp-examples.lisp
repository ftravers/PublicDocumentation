(in-package :cl)
(defpackage :lib-pkg 
  (:use :cl) 
  (:export :say-hello)
  (:nicknames :lp)
  )
(defun say-hello () "hello")

(in-package :lib-pkg)

(defpackage :app (:use :cl))

(in-package :app)

(defun get-all-symbols (&optional package)
  (let ((lst ())
        (package (find-package package)))
    (do-all-symbols (s lst)
      (when (fboundp s)
        (if package
            (when (eql (symbol-package s) package)
              (push s lst))
            (push s lst))))
    lst))

(get-all-symbols 'sb-thread)

;; call an external program
(sb-ext:run-program "/usr/bin/ls" (list "-l"))

;; call an external program in a certain directory
(uiop/filesystem:with-current-directory 
    ("~") 
  (uiop/run-program:run-program 
   (list "git" "status") 
   :output :string))


;; PWD/CWD
*DEFAULT-PATHNAME-DEFAULTS*
;; => #P"/home/fenton/projects/lisp/urban-farmer/"
(truename ".")
;; => #P"/home/fenton/projects/lisp/urban-farmer/"

;; changing directory
(sb-posix:chdir #P"/home/fenton")
;; => 0
;; truename doesn't really work 
(truename ".")
;; => #P"/home/fenton/projects/lisp/urban-farmer/"


;; Get an environment variable:
(sb-unix::posix-getenv "HOME")
;; => "/home/fenton"
