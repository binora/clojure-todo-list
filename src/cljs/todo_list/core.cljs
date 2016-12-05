(ns todo-list.core
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [todo-list.ajax :refer [load-interceptors!]]
            [ajax.core :refer [GET POST]]
            [todo-list.store :refer [app-state]]
            [todo-list.todo-page :refer [todo-component]]
            [todo-list.login-page :refer [login-component]] )
  (:import goog.History))

(defn page []
  [(session/get :current-page)])


;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :current-page login-component))

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
  (r/render [#'page] (.getElementById js/document "container")))

(defn init! []
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))
(init!)
