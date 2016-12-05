(ns todo-list.login-page
  (:require [reagent.core :as r]
            [ajax.core :refer [GET POST]]
            [secretary.core :refer [dispatch!]]
            [todo-list.store :refer [app-state]]))


(defn request-login
  [username password response-handler]
  (POST "/auth/login" {:params {:name username
                                :password password}
                       :response-format :json
                       :handler #(response-handler %)
                       :keywords? true}))

(defn handle-login
  [data]
  (println data)
  (let [status (:status data)
        user (:user data)]
    (if (false? status)
        false
        (do
          (println (:token user))
          (swap! app-state assoc :token (:token user))
          (dispatch! "/todos")))))





(defn login-component []
  (let [local-state (r/atom {:username ""
                             :password ""})]

    (fn []
      [:div.login-div {}
        [:form#login-component {:action "#"}
         [:h2#login-header "Login"]
         [:input#login-name {:placeholder "username"
                             :value (:username @local-state)
                             :on-change #(swap! local-state assoc :username (-> % .-target .-value))}]
         [:input#login-password {:placeholder "password"
                                 :type "password"
                                 :value (:password @local-state)
                                 :on-change #(swap! local-state assoc :password (-> % .-target .-value))}]
         [:button#login-button {:on-click #(do (request-login (:username @local-state)
                                                              (:password @local-state)
                                                               handle-login)
                                             (reset! local-state {:username "" :password ""}))}
          "Go!"]]])))


