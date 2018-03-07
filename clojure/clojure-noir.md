### Noir

Noir is a clojure web framework.

#### Assumptions

This tutorial uses:

* lein 2
* This project will be called `my-proj`, so where ever you
  see that, replace it with your project name.


### Steps

Create a new project:

    lein new noir my-project-name
    
Run it:

    lein run
    
Test it:

    http://localhost:8080/welcome
    
Setup to code in Emacs:


    
### Basics - Tutorial

Lets look at some sample code in file: `src/my-proj/views/welcome.clj`

```clojure
(ns my-proj.views.welcome
  (:require [my-proj.views.common :as common]
            [noir.content.getting-started]
            [hiccup.core :as hiccup])
  (:use [noir.core :only [defpage]]))

(defpage "/my-page" []
  (hiccup/html
    [:h1 "This is my first page!"]))
```

* [hiccup reference](https://github.com/weavejester/hiccup)

So this creates a page at: `http://localhost:8080/my-page` with the
following html:

```html
<h1>This is my first page!</h1>
```

Here is a quick ref table: (**NOTE**: surround the hiccup code with:
`(html hiccup-code)`, i.e. `[:p]` should be: `(html [:p])`


hiccup                               | html                   
------------------------------------ | -------------------------------------------------
[:span {:class "foo"} "bar"]         | <span class=\"foo\">bar</span>
[:script]                            | <script></script>
[:p]                                 | <p />
[:div#foo.bar.baz "bang"]            | <div id=\"foo\" class=\"bar baz\">bang</div>
[:ul (for [x (range 1 4)] [:li x])]  | <ul><li>1</li><li>2</li><li>3</li></ul>

### Conventions

Put your `view` pages in the `views` folder.
    
### Definitions

**partial** is a function that returns html.  Example:

```clojure
(defpage "/my-page" []
  (html
    [:h1 "This is my first page!"]))
```

