(ns todo-list.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [todo-list.layout :refer [error-page]]
            [todo-list.routes.home :refer [home-routes]]
            [todo-list.routes.auth :refer [auth-routes]]
            [todo-list.routes.todo :refer [todo-routes]]
            [compojure.route :as route]
            [todo-list.env :refer [defaults]]
            [mount.core :as mount]
            [todo-list.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-formats))
    (-> #'auth-routes
        (wrap-routes middleware/wrap-formats))
    (-> #'todo-routes
        (wrap-routes middleware/wrap-formats)
        (wrap-routes middleware/authenticate-user))
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))

(defn app [] (middleware/wrap-base #'app-routes))
