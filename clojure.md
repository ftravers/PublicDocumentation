
# Table of Contents

1.  [References:](#org15c2dac)
2.  [CIDER REPL](#org5d91ff0)
3.  [WebSockets](#orgb2e323a)
    1.  [Client](#org60fc9bc)
    2.  [Server](#org5bd677f)
4.  [core.async - pub/sub](#orgd8b24b5)
5.  [fast repl start](#org30ed5ad)
6.  [sayid](#orgf3629ef)
7.  [lein repl](#org7c963ec)
8.  [destructuring](#org37b627b)
9.  [transducers](#org0e017c1)
10. [publishing to clojars](#orgbad0a9a)
11. [clojure spec](#orgec5c474)
    1.  [setup](#org6a67e26)
    2.  [map with required keys](#org858a90e)
12. [dirac devtools clojurescript](#orgc020621)
13. [vinyasa](#orga9dd3b0)
14. [transducers](#orge6e523e)
    1.  [Transduce](#org644e37d)
15. [phraser](#org4d928fd)
16. [atom editor](#orgeba06ef)
17. [deps.edn](#orgaa2fc8e)
18. [reagent/reframe](#org7fa9379)
19. [spectre](#org49b0436)
20. [postmodern - logging](#orge412369)
21. [reframe](#org4ac4702)
22. [protocols, deftype](#orge944f95)
23. [Ring](#org8e755ed)
    1.  [hot reload](#org7d36193)
24. [compojure](#orgb3ca8be)
25. [Shadow CLJS](#orgf564bcf)



<a id="org15c2dac"></a>

# References:

<https://cljdoc.org/>
<https://crossclj.info/>


<a id="org5d91ff0"></a>

# CIDER REPL

<table border="2" cellspacing="0" cellpadding="6" rules="groups" frame="hsides">


<colgroup>
<col  class="org-left" />

<col  class="org-left" />
</colgroup>
<thead>
<tr>
<th scope="col" class="org-left">Keys</th>
<th scope="col" class="org-left">Meaning</th>
</tr>
</thead>

<tbody>
<tr>
<td class="org-left">C-c A-j</td>
<td class="org-left">Start REPL</td>
</tr>


<tr>
<td class="org-left">C-c A-n</td>
<td class="org-left">Change Namespace to that of file</td>
</tr>
</tbody>
</table>


<a id="orgb2e323a"></a>

# WebSockets


<a id="org60fc9bc"></a>

## Client

In `project.clj` put:

[org.clojure/google-closure-library "0.0-20140226-71326067"]

    (:require [goog.net.WebSocket :as ws]))
    (defn do-websocket []
      (let [url "ws://localhost:8080"
            ws (js/WebSocket. url)]
        (set! (.-onmessage ws) (fn [evt] (js/alert (.-data evt))))
        (set! (.-onclose ws) (fn [evt] (js/alert "closed")))
        (set! (.-onopen ws) (fn [evt] (.send ws "hello")))))


<a id="org5bd677f"></a>

## Server

In `project.clj` put:

[http-kit "2.1.19"]

This comes from the httpkit example server.  This an echo server that
you can also test from a chrome WebSocket tester app.

    (ns pcbe.httpkit-server
      (:require
       [org.httpkit.server
        :refer [with-channel on-close websocket? on-receive send! run-server]]))
    (defonce server (atom nil))
    (defn stop-server []
      (when-not (nil? @server)
        (@server :timeout 100)
        (reset! server nil)))
    (defn handler [req]
      (with-channel req channel
        (on-close channel (fn [status]
                            (println "channel closed")))
        (if (websocket? channel)
          (println "WebSocket channel")
          (println "HTTP channel"))
        (on-receive
         channel
         (fn [data]                   
           (send! channel data)))))   
    (defn -main [&args]
      (reset! server (run-server handler {:port 8080})))


<a id="orgd8b24b5"></a>

# core.async - pub/sub

The requires:

    (:require-macros [cljs.core.async.macros :refer [go]])
    (:require [cljs.core.async :refer [<! >! chan pub sub]])

Create a channel

    (def input-chan (chan))

Now overlay that channel with a \`publication\`

    (def our-pub (pub input-chan :msg-type))

Here **:msg-type** is our function that is used to filter messages sent
over the channel.

To use the publish-channel we just write messages to the original
channel: 

    (go (>! input-chan {:msg-type :greeting :text "hello"}))

This doesn't block, because we have overlayed the publication flavor
onto the channel.  Since there are no subscribers as yet, this message
just dissappears into the ether! :)

To receive messages of :msg-type :greeting we need to to subscribe to
our publication with the **sub** function.

    (def output-chan (chan))
    (sub our-pub :greeting output-chan)

sub takes a publication, a topic, and a subscribing channel. The
subscribing channel will receive all values from the publication for
which (= (topic-fn pub-msg) topic):

We can handle the received values like so:

    (go-loop []
      (let [{:keys [text]} (<! output-chan)]
        (println text)
        (recur)))

We create a new channel.  Then we subscribe, passing our publication
as the first arg, the value to match on, and finally the channel to
put our matches on.

Now when we put a message on the original channel it will be 'caught'
by the subscribed channel

    (go (>! input-chan {:msg-type :greeting :text "Hi there"}))


<a id="org30ed5ad"></a>

# fast repl start

You can create a classpath file with

    lein classpath > my.cp

Run java quickly with: drip
<https://github.com/ninjudd/drip>

drip -cp $(cat my.cp) clojure.main -r

DRIP<sub>INIT</sub><sub>CLASS</sub>="pcbe.http-test" drip -cp $(cat my.cp) clojure.main

rlwrap -a,, -pBlue drip -cp clojure.jar clojure.main

I'm not sure if there is way to use drip to speed up the launching and running of tests, but that would be great if there was.


<a id="orgf3629ef"></a>

# sayid

ref: <http://bpiel.github.io/sayid/>

add `[com.billpiel/sayid "0.0.10"]` as a PLUGIN dependency to project.cljc

eval the namespace you care about in the REPL

trace the namespace with `C-c s t p`, eg: `om.*`

run some code that uses that namespace.

get report with `C-c s w`

Go to any line and hit `i` (inspect).  `BACKSPACE` to collapse.

C-c s t D &#x2013; Disable all traces


<a id="org7c963ec"></a>

# lein repl

When I start the repl I land in this namespace:

    omn1be.core=> 

Here is the src tree:

    % tree src      
    src
    `-- omn1be
        |-- core.clj
        `-- websocket.clj

Now I can load the websocket file with:

    omn1be.core=> (load "websocket")

if I was only in the `omn1be` namespace then the load command would
look like:

    omn1be=> (load "omn1be/websocket")

I can move around to different namespaces with the `in-ns` function:

    omn1be=> (in-ns 'omn1be.websocket)
    #namespace[omn1be.websocket]
    omn1be.websocket=>


<a id="org37b627b"></a>

# destructuring


<a id="org0e017c1"></a>

# transducers

Normally, we'd process a sequence like so:

    (->> aseq (map inc) (filter even?))

Instead we gather up our list processing functions like so:

    (def xform (comp (map inc) (filter even?)))

Then we can use `xform` in a couple of ways:

lazily transform the data (one lazy sequence, not three as with composed sequence functions)

    (sequence xform data)

reduce with a transformation (no laziness, just a loop)

    (transduce xform + 0 data)

build one collection from a transformation of another, again no laziness

    (into [] xform data)

create a recipe for a transformation, which can be subsequently sequenced, iterated or reduced

    (iteration xform data)

or use the same transducer to transform everything that goes through a channel

    (chan 1 xform)


<a id="orgbad0a9a"></a>

# publishing to clojars


<a id="orgec5c474"></a>

# clojure spec


<a id="org6a67e26"></a>

## setup

project.clj dependencies 

    [org.clojure/clojure "1.9.0-alpha16"]

namespace require

    (:require [clojure.spec.alpha :as s])


<a id="org858a90e"></a>

## map with required keys

    (s/def :event/type keyword?)
    (s/def :event/timestamp int?)
    (s/def :search/url string?)
    (s/def :error/message string?)
    (s/def :error/code int?)
    (defmulti event-type :event/type)
    (defmethod event-type :event/search [_]
      (s/keys :req [:event/type :event/timestamp :search/url]))
    (defmethod event-type :event/error [_]
      (s/keys :req [:event/type :event/timestamp :error/message :error/code]))


<a id="orgc020621"></a>

# dirac devtools clojurescript

steps taken to setup

% git clone <https://github.com/binaryage/dirac-sample.git>
% cd dirac-sample
% lein demo

in another console:

% cd dirac-sample
% lein repl
&#x2026;
user=> 
Dirac Agent v1.2.17
Connected to nREPL server at nrepl://localhost:8230.
Agent is accepting connections at ws://localhost:8231.

Open Chromium with command:

% *usr/bin/chromium &#x2013;remote-debugging-port=9222 &#x2013;no-first-run &#x2013;user-data-dir=~*.dirac 

Dirac Devtools already installed to this Chromium

In Chromium open page: 

<http://localhost:9977>

When there click on the 'Click to open Dirac Devtools' green squiggly
line icon.

Devtools is opened with the green underline highlight not blue.

On webpage click on 'demo a breakpoint'

Nothing shows up in the green underline hightlight devtools, but a
'Paused in Debugger' overlay is on the webpage.

Not sure what I'm doing wrong


<a id="orga9dd3b0"></a>

# vinyasa

in `project.clj`:

    (defproject abc "1.0"
      :description "FIXME: write description"
      :profiles
      {:dev
       {:source-paths ["src/clj" "dev"]
        :dependencies [[im.chit/vinyasa "0.4.7"]]
        :injections
        [(require '[vinyasa.inject :as inject])
         (inject/in ;; the default injected namespace is `.`
          [clojure.pprint pprint]
          [clojure.repl apropos dir dir-fn doc find-doc pst root-cause source]
          [clojure.tools.namespace.repl refresh refresh-all]
          [dev config]
          [clojure.java.shell sh])]}})

Not sure if above works with CLJS, but in CLJ, you can now do
`(./refresh)`, as default namespace is `.`  


<a id="orge6e523e"></a>

# transducers

We have a data structure like so:

    (def hand1
      ({:suit :heart, :rank 12}
       {:suit :heart, :rank 11}
       {:suit :heart, :rank 10}
       {:suit :heart, :rank 9}
       {:suit :heart, :rank 8}))

And we'd like to create a function that can tell us if this is a
straight or not.  So here is some pseudo code:

1 - Find out the distinct rank, and count that, if it = 5, then there
are no duplicate cards.

2 - Find the max and min cards, and ensure their difference is =
to 4. 

So some clojure code to encapsulate this could be:

    (defn straight? [cards]
      (let [ranks (map :ranks cards)]
        (and
         (= 5 (count (into #{} ranks)))
         (= 4 (- (apply max ranks) (apply min ranks))))))


<a id="org644e37d"></a>

## Transduce

    (transduce xform f coll)
    (transduce xform f init coll)

`f` should be a reducing step function that accepts both 1 and 2 arguments

If `init` is not supplied, `f` will be called to produce it.

curl -u "fentontravers" <https://api.github.com/repos/mojombo/grit/stats/contributors>


<a id="org4d928fd"></a>

# phraser

`deps.edn:` 

    phrase {:mvn/version "0.3-alpha4"}

given the spec: 

    (s/def ::email
      (s/and #(re-find #"@" %) #(re-find #"\." %)))

when we run explain-data

    (s/explain-data
     :arenberg.specs.shared/email
     "abc")
    ;; ... (clojure.core/fn [%] (clojure.core/re-find #"@" %)), ...

we get a predicate clause indicating why the failure

Creating a phraser like:

    (defphraser
      #(re-find re %)
      {:via [:arenberg.specs.shared/email]}
      [_ _ re]
      (str
       "Email must include "
       (do
         (println (str "re: " re))
         (case (str/replace (str re) #"/" "")
           "@" "an ampersand symbol, @."
           "\\." "a period."
           (str "dunno blah: " re)))))

and calling it like:

    (phrase-first {} :arenberg.specs.shared/email "abc")
    ;; => "must include: an ampersand symbol, @." 

because the `defphraser` predicate (the first arg) matches the
predicate of explain-data AND we said this `defphraser` is for the
`:arenberg.specs.shared/email` spec, it matches.

Here again is the `explain-data` predicate 

    (clojure.core/fn [%] (clojure.core/re-find #"@" %))

and here is the phraser predicate:

    #(re-find re %)

So #"@" gets stuffed into `re`, and we match on it.  here is another
output of explain-data (it's predicate part):

    (clojure.core/fn [%] (clojure.core/re-find #"\." %))

so `re` gets #"\\.", which the phraser function case matches on above.


<a id="orgeba06ef"></a>

# atom editor

apm install proto-repl ink parinfer lisp-paredit rainbow-delimiters atom-beautify atom-file-icons hasklig


<a id="orgaa2fc8e"></a>

# deps.edn

make a new project, we'll call it `ds-queue`, for your own project,
replace: `ds-queue` with your project name below&#x2026;

    cd ~/projects
    mkdir -p ds_queue/{src,test}/ds_queue ds_queue/resources/public
    cd ds_queue

mkdir -p supps/{src,test}/supps supps/resources/public

    cat > .dir-locals.el 

    1  ((nil . ((cider-default-cljs-repl . figwheel-main)
    2           (cider-figwheel-main-default-options . "fe_dev")
    3           (cider-clojure-cli-global-options . "-A:dev"))))

    cat > fe_dev.cljs.edn 

    4  {:main supps.core}

    cat > deps.edn

     5  {:paths ["src" "resources" "target"]
     6   :deps {rum {:mvn/version "0.11.3"}}
     7   :aliases
     8   {:dev {:extra-paths ["test"]
     9          :extra-deps
    10          {com.bhauman/figwheel-main {:mvn/version "0.2.0"}}}
    11    :fig {:main-opts ["-m" "figwheel.main"]}}}

    cat > resources/public/index.html

    12  <!DOCTYPE html>
    13  <html>
    14    <body>
    15      <div id="app">
    16        App Loading...
    17      </div>
    18      <script src="cljs-out/fe_dev-main.js" type="text/javascript"></script>
    19    </body>
    20  </html>

Here line 18 is connected to line 2 and the filename
`fe_dev.cljs.edn`. (note run org-preview-html-mode to see line
numbers.)  The compiled \*.js file is created by the
`cider-figwheel-main-default-options` parameter, specified in the
`.dir-locals.el` file.

Lines 3 & 8 are connected too.  That is we specify which aliases clj
will run by setting the `cider-clojure-cli-global-options` in the
`.dir-locals.el` file.

    cat > src/supps/core.cljs

    (ns supps.core
      (:require
       [rum.core :refer [defc mount] :as rum]))
    
    (defc hello []
      [:div "Hello!"])
    
    (defn main-page [comp]
      (mount
       (comp)
       (js/document.getElementById "app")))
    
    (main-page hello)

    cat > .gitignore

    target/
    .nrepl-port
    .cpcache

Test with:

    clj -A:dev:fig -b fe_dev -r


<a id="org7fa9379"></a>

# reagent/reframe

`=====`


<a id="org49b0436"></a>

# spectre

      (setval)
    
    (transform ALL inc [1 2 3])
    
    ;; path example
    ;; begin with following data:
    (def data [{:a 1}
               {:b 2}])
    
    ;; ALL ==>
    {:a 1}
    {:b 2}
    
    ;; MAP-VALS ==>
    1 2
    
    ;; 
    
    ;; (transform [ALL] inc [[1 2 3] [4 5 6]])


<a id="orge412369"></a>

# postmodern - logging

    ;; deps.edn
    postmortem {:mvn/version "0.2.0"}
    
    ;; require
    (:require [postmortem.core :as pm]
              [postmortem.xforms :as xf])

    (defn abc [x y]
      (pm/dump :abc-log (xf/take-last 2))
      (+ x y))
    
    (abc 1 2)
    
    (pm/log-for :abc-log)


<a id="org4ac4702"></a>

# reframe

>>>>>>> 33303d68a1a5e28f3768cf3fd36b3f047f557db0


<a id="orge944f95"></a>

# protocols, deftype

first we define a protocol, basically this is a list of methods
(functions) that an object should implement

    (defprotocol MyProtocol
      (foo
        [this]
        [this x])
      (bar [this]))
    
    (deftype MyClass [a b]
      MyProtocol
      (foo [this] (+ 1 a b))
      (foo [this x] (+ 1 a b x))
      (bar [this] (+ a b)))
    
    (foo (MyClass. 1 2)) ;; --> 4
    (foo (MyClass. 1 2) 3) ;; --> 7

In our `defprotocol` we specified two arities for foo function.


<a id="org8e755ed"></a>

# Ring

Add to deps

    [ring/ring-core "1.6.3"]
    [ring/ring-jetty-adapter "1.6.3"]

in `my_ns/core.clj` (as an example):

    (ns my-ns.core
      (:require [ring.adapter.jetty :refer [run-jetty]]))
    
    (defn handler [request]
      {:status 200
       :headers {"Content-Type" "text/html"}
       :body "Hello World"})
    
    ;; It's a good idea to set the :join? option is to false in
    ;; REPLs. Otherwise the Jetty server will block the thread and the
    ;; REPL won't process input anymore.
    (defonce server (run-jetty #'handler {:port 3000 :join? false}))
    
    (defn stop [] (.stop server))


<a id="org7d36193"></a>

## hot reload

We want the server to auto-reload anytime we change source files.

    deps.edn

    {ring/ring-devel {:mvn/version "1.6.3"} ; hot-reload ring
     ;; ...
     } 

    my-ns/core.clj

    (ns my-ns.core
      (:require
       [ring.adapter.jetty :refer [run-jetty]]
       [ring.middleware.reload :refer [wrap-reload]]))
    
    (defn handler [request]
      {:status 200
       :headers {"Content-Type" "text/html"}
       :body "Hello World"})
    
    (def dev-handler
      (wrap-reload #'handler))
    
    ;; It's a good idea to set the :join? option is to false in
    ;; REPLs. Otherwise the Jetty server will block the thread and the
    ;; REPL won't process input anymore.
    (comment
      (run-jetty dev-handler {:port 3000 :join? false}))

Now, the server will automatically reload any modified files in your source directory.


<a id="orgb3ca8be"></a>

# compojure

    deps.edn

    compojure {:mvn/version "1.6.1" }

    my-ns/core.cljc

    (ns my-ns.core
      (:require
       [ring.adapter.jetty :refer [run-jetty]]
       [ring.middleware.reload :refer [wrap-reload]]
       [compojure.core :refer [defroutes GET]]
       [compojure.route :as route]))
    
    (defroutes app
      (GET "/" [] "<h1>Hello World</h1>")
      (route/not-found "<h1>Page not found</h1>"))
    
    (def dev-handler
      (wrap-reload #'app))
    
    (defn start
      ([]
       (run-jetty #'dev-handler {:port 3000 :join? false}))
      ([server]
       (reset! server (start))))
    
    (defonce server (atom (start)))
    
    (defn stop [] (.stop @server))
    
    (comment
      (stop)
      (start server))


<a id="orgf564bcf"></a>

# Shadow CLJS

add a dirlocals like:

    ((nil . ((cider-default-cljs-repl . shadow)
             (cider-shadow-default-options . "browser-repl"))))

when inside the repl run the elisp command:

    (cider-set-repl-type "cljs")

This is bound to: 

    , c

