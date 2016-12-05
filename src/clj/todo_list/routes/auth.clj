(ns todo-list.routes.auth
  (:require [todo-list.layout :as layout]
            [compojure.core :refer [defroutes GET POST context]]
            [ring.util.http-response :as response]
            [todo-list.db.users :as user-model]
            [todo-list.utils :as utils]))

(defn create-user
  [name password]
  (let [user {:name name
              :password (utils/encrypt-password password)
              :token (utils/generate-random-token)}]
    (if (user-model/username-exists? name)
      (layout/render-json {:status false
                           :message "User name already exists!"})
      (do
        (let [result (user-model/create-user user)]
        (layout/render-json {:status true
                              :user result}))))))


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
  (POST "/create" [name password] (create-user name password))
  (POST "/login" [name password] (login-user name password))))
