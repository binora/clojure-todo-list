(ns todo-list.login-page
  (:require [reagent.core :as r]
            [ajax.core :refer [GET POST]]
            [secretary.core :refer [dispatch!]]
            [todo-list.validation :refer [validate-login]]
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
  (let [status (:status data)
        user (:user data)]
    (if (false? status)
      false
      (do
        (swap! app-state assoc :token (:token user))
        (dispatch! "/todos/1")))))

(defn on-login-click
  [username password handle-login]
  (let [error-map (validate-login {:name username
                                   :password password})]
    (request-login username password handle-login)))
;;     (if (empty? error-map)

;;       (println  error-map))))



(defn login-component []
  (let [username (r/atom "")
        password (r/atom "")]
    (fn []
      [:div.login-div {}
       [:form#login-component {:action "#"}
        [:h2#login-header "Todo Tracker"]
        [:input#login-name {:placeholder "username"
                            :value @username
                            :on-change #(reset! username (-> % .-target .-value))}]
        [:input#login-password {:placeholder "password"
                                :type "password"
                                :value @password
                                :on-change #(reset! password (-> % .-target .-value))}]
        [:button#login-button {:on-click #(do (on-login-click @username @password handle-login)
                                            (reset! username "")
                                            (reset! password ""))}
         "login"]]
       [:button#signup-button {:on-click #(dispatch! "/signup")}
        "signup"]])))
