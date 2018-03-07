;; Define a couple classes
(defclass person ()
  (name email))

(defclass task ()
  (title assigned-to))

;; Instantiate a couple objects
(defvar p1 (make-instance 'person))
(defvar p2 (make-instance 'person))
(defvar t1 (make-instance 'task))
(defvar t2 (make-instance 'task))

;; Setf slot values
(setf (slot-value p1 'name) "Fenton")
(setf (slot-value p2 'email) "f@gmail.com")

(setf (slot-value p2 'name) "Oliver")
(setf (slot-value p2 'email) "o@yahoo.com")

(setf (slot-value t1 'title) "Buy Milk")
(setf (slot-value t1 'assigned-to) p1)

(setf (slot-value t2 'title) "Walk Dog")
(setf (slot-value t2 'assigned-to) p2)

(defgeneric )
