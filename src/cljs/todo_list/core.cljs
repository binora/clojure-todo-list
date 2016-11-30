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

(def app-state
  (r/atom {:todos []
           :current-todo-input ""}))

(defn fetch-todos []
  (GET "/todo/all" {:params
                        {:token "pbjysjlpxitmaqji"}
                     :response-format :json
                      :handler #(swap! app-state assoc :todos (:todos %))
                     :keywords? true}))
(defn on-create! [id text]
  (swap! app-state update-in [:todos] merge {:text text :_id id})
  (swap! app-state assoc-in [:current-todo-input] ""))

(defn create-todo []
  (POST "/todo/create" {:params
                          {:token "pbjysjlpxitmaqji"
                           :text (:current-todo-input @app-state)}
                         :response-format :json
                         :handler #(on-create! (:todo-id %) (:current-todo-input @app-state))
                         :keywords? true}))

(defn todo-list [todos]
  [:ul#todo-list
   (map (fn [todo]
          [:li {:key (:_id todo)} (:text todo)])
        todos)])


(defn todo-input [value]
    [:div#new-todo
     [:input#todo-input {:placeholder "Create new todo"
                         :value value
                         :on-change #(swap! app-state assoc-in [:current-todo-input] (-> % .-target .-value))}]
     [:button#new-todo-button {:on-click create-todo} "Create"]])

(defn header []
  [:div#app-header {}
    [:h4  "Welcome to your Todo Tracker"]])


(defn app []
  (fetch-todos)
  (fn []
    [:div#app
     [header]
     [todo-input (:current-todo-input @app-state)]
     [todo-list (:todos @app-state)]]))



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
