(ns todo-list.reg-page
  (:require [reagent.core :as r]
            [ajax.core :refer [GET POST]]
            [secretary.core :refer [dispatch!]]
            [todo-list.utils :as utils]
            [todo-list.store :refer [app-state]]))


(defn request-reg
  [username password confirm-password response-handler]
  (if (= password confirm-password)
    (POST "/auth/create" {:params {:name username
                                  :password password}
                         :response-format :json
                         :handler #(response-handler %)
                         :keywords? true})
    (js/console.log "Passwords do not match")))

(defn handle-reg
  [data]
  (when (utils/check-response-sanity data)
      (let [user (:user data)]
         (do
           (println user)
           (swap! app-state assoc :token (:token user))
           (dispatch! "/todos")))))




(defn reg-component []
  (let [username (r/atom "")
        password (r/atom "")
        confirm-password (r/atom "")]
    (fn []
      [:div.reg-div {}
       [:form#reg-component {:action "#"}
         [:h2#reg-header {} "Register"]
         [:input#login-name {:placeholder "username"
                             :value @username
                             :on-change #(reset! username (-> % .-target .-value))}]
         [:input#login-password {:placeholder "password"
                                 :type "password"
                                 :value @password
                                 :on-change #(reset! password (-> % .-target .-value))}]
         [:input#login-confirm-password {:placeholder "confirm password"
                                         :type "password"
                                         :value @confirm-password
                                         :on-change #(reset! confirm-password (-> % .-target .-value))}]

         [:button#login-button {:on-click #(do (request-reg @username @password @confirm-password handle-reg)
                                               (reset! username "")
                                               (reset! confirm-password "")
                                               (reset! password ""))}
          "signup"]]])))
