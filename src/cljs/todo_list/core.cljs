(ns todo-list.core
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [todo-list.ajax :refer [load-interceptors!]]
            [ajax.core :refer [GET POST]])
  (:import goog.History))

(def todos [{:text "first"
             :_id "1"}
            {:text "second"
             :_id "2"}
            {:text "third"
             :_id "3"}])

(def app-state
  (r/atom {:todos todos
           :current-todo-input ""}))

(defn todo-list [todos]
  [:ul#todo-list
   (map (fn [todo]
          [:li {:key (:_id todo)} (:text todo)])
        todos)])

(defn on-create! []
  (swap! app-state update-in [:todos] merge {:text (:current-todo-input @app-state) :_id (rand 100)})
  (swap! app-state assoc-in [:current-todo-input] ""))

(defn todo-input [value]
    [:div#new-todo
     [:input#todo-input {:placeholder "Create new todo"
                         :value value
                         :on-change #(swap! app-state assoc-in [:current-todo-input] (-> % .-target .-value))}]
     [:button#new-todo-button {:on-click on-create!} "Create"]])

(defn header []
  [:div#app-header {}
    [:h4  "Welcome to your Todo Tracker"]])


(defn app []
  [:div#app
   [header]
   [todo-input (:current-todo-input @app-state)]
   [todo-list (:todos @app-state)]])



;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
        (events/listen
          HistoryEventType/NAVIGATE
          (fn [event]
              (secretary/dispatch! (.-token event))))
        (.setEnabled true)))

;; -------------------------
;; Initialize app


(defn mount-components []
  (r/render [#'app] (.getElementById js/document "container")))

(defn init! []
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))
