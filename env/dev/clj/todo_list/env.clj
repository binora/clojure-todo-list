(ns todo-list.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [todo-list.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[todo-list started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[todo-list has shut down successfully]=-"))
   :middleware wrap-dev})
