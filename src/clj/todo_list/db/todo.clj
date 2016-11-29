(ns todo-list.db.todo
    (:require [monger.core :as mg]
              [monger.collection :as mc]
              [todo-list.db.core :refer [db]]
              [monger.operators :refer :all]
              [mount.core :refer [defstate]]
              [todo-list.utils :as utils]
              [clj-time.core :as t]
              [todo-list.constants :as constants]
              [todo-list.config :refer [env]])
  (:import org.bson.types.ObjectId))

(defn create-todo [user text]
  (let [now (utils/serialize-date (t/now))]
    (mc/insert-and-return db "todos" {:user-id (:_id user)
                           :text text
                           :updated-at now
                           :created-at now
                           :status (:PENDING constants/todo-status)})))

(defn get-todos-by-user [user-id]
  (mc/find-one-as-map db "todos" {:user-id user-id}))

(defn update-todo-status [todo-id new-status]
  (let [now (utils/serialize-date (t/now))]
    (mc/update db "todos" {:_id (ObjectId. todo-id)}
               {$set {:status new-status
                      :updated-at now}})))
