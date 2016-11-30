(ns todo-list.db.users
    (:require [monger.core :as mg]
              [monger.collection :as mc]
              [todo-list.db.core :refer [db]]
              [monger.operators :refer :all]
              [mount.core :refer [defstate]]
              [todo-list.utils :as utils]
              [clj-time.core :as t]
              [todo-list.config :refer [env]]))

;; A user is a map : {:id :name , :token, :password, created-at, updated-at}
(defn create-user [user]
  (let [now (t/now)]
    (assoc user :created-at now :updated-at now)
    (mc/insert db "users" user)))

(defn update-user [id param-map]
  (assoc param-map :updated-at (t/now))
  (mc/update db "users" {:_id id}
             {$set param-map}))

(defn get-user  [id]
  (mc/find-one-as-map db "users" {:_id id}))


(defn get-user-by-token [token]
  (let [user  (mc/find-one-as-map db "users" {:token token})]
    (assoc user :_id (str (:_id user)))))

(defn valid-password? [name password]
  (let [encp (utils/encrypt-password password)
        user (mc/find-one-as-map db "users" {:name name :password encp})]
    (if (empty? user) nil user)))

