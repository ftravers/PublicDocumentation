;; Load this file with: C-c C-k

;; Load up elephant, run the following in the REPL
;; CL-USER> (ql:quickload 'elephant)

;; Tell elephant where/how to store the DB
(defparameter *elephant-store* (elephant:open-store '(:clsql (:sqlite3 "/tmp/users.db"))))

;; Define a class
(elephant:defpclass user ()
  ((name :initarg :name
         :accessor name
         :index t)
   (email :initarg :email
          :accessor email
          :index t)))

;; Whenever make-instance is called, it will automatically be saved to the DB
(make-instance 'user :name "Fenton" :email "fenton@gmail.com")

;; Add another to the DB but save a copy too.
(defparameter trav (make-instance 'user :name "Travers" :email "travers@yahoo.com"))

;; As a convenience, make a function to pretty print the objects
(defun pp-user (user)
  (format nil "~A: ~A" (name user) (email user)))

;;;; Try the following in the REPL

;; This will get all (the two) objects from the DB
(elephant:get-instances-by-class 'user)

;; If you want to mapcar a function across all users... HMPH! doesn't work!
(elephant:map-class #'pp-user 'user)

;; Get instances with criteria
(elephant:get-instances-by-value 'user 'name "Fenton")

;; Add another user
(make-instance 'user :name "Oliver" :email "oliver@hotmail.com")

;; Get instances by range
(mapcar #'pp-user (elephant:get-instances-by-range 'user 'name "Fenton" "Oliver"))
