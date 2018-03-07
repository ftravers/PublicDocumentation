Recently I had to represent a tree data structure and it seemed
zippers were the way to go to represent the data.  Here are my notes
on the work.

I wanted to maintain a tree that each node contained two elements, a
string representing the human readable value (the label), and a unique
id.  I decided to maintain two data structures.  One kept a mapping
between id and label, and the other the tree structure, which only had
the id's in it.  The mapping between id and label is inherently a
linear structure, while the second is a tree structure.

It was decided to use a map for the id to label structure that looked
like:

```clojure
```
