(ns user
  (:require [mount.core :as mount]
            [todo-list.figwheel :refer [start-fw stop-fw cljs]]
            todo-list.core))

(defn start []
  (mount/start-without #'todo-list.core/repl-server))

(defn stop []
  (mount/stop-except #'todo-list.core/repl-server))

(defn restart []
  (stop)
  (start))


