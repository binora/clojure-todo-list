(ns todo-list.store
  (:require [reagent.core :as r]))

(defonce app-state
  (r/atom {:todos []
           :current-todo-input ""
           :token ""}))
