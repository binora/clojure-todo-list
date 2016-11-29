(ns todo-list.routes.auth
  (:require [todo-list.layout :as layout]
            [compojure.core :refer [defroutes GET POST context]]
            [ring.util.http-response :as response]
            [todo-list.db.users :as user-model]
            [todo-list.utils :as utils]))

(defn create-user
  []
  (let [user {:name "binny2"
              :password (utils/encrypt-password "hello")
              :token (utils/generate-random-token)}]
    (user-model/create-user user)
    (layout/render-json {:status true
                    :body "DONE"})))


(defn login-user [name password]
  (let [user (user-model/valid-password? name password)]
    (if (nil? user)
      (layout/render-json {:status false
                      :message "unknown user"})
      (layout/render-json {:status true
                      :message "User logged in successfully"
                      :user (select-keys user [:token :name])}))))

(defroutes auth-routes
  (context "/auth" []
  (POST "/login" [name password] (login-user name password))))
