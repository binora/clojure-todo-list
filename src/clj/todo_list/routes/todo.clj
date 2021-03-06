(ns todo-list.routes.todo
  (:require [todo-list.layout :as layout]
            [compojure.core :refer [defroutes GET POST context]]
            [todo-list.db.todo :as todo-model]
            [todo-list.constants :as constants]
            [todo-list.utils :as utils]))


(defn create-todo
  [request]
  (let [text (get-in request [:params :text])
        user (:user request)
        result (todo-model/create-todo user text)]
    (layout/render-json {:status true
                         :message "TODO created successfully"
                         :todo-id (str (:_id result))})))

(defn update-todo
  [request]
  (let [todo-id (get-in request [:params :todo-id])
        new-status (:DONE constants/todo-status)]
    (todo-model/update-todo-status todo-id new-status)
    (layout/render-json {:status true
                         :message "Todo updated successfully"})))

(defn get-all-todos
  [request]
  (let [user-id (get-in request [:user :_id])
        result (todo-model/get-todos-by-user user-id)]
    (layout/render-json {:status true
                         :todos result})))



(defroutes todo-routes
  (context "/todo" []
           (GET  "/all" request (get-all-todos request))
           (POST "/create" request (create-todo request))
           (POST "/complete" request (update-todo request))))


