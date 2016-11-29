(ns todo-list.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[todo-list started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[todo-list has shut down successfully]=-"))
   :middleware identity})
