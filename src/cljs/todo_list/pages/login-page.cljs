(ns todo-list.login-page
  (:require [reagent.core :as r]
            [ajax.core :refer [GET POST]]
            [secretary.core :refer [dispatch!]]
            [todo-list.store :refer [app-state]]))





(defn login-component []
  (let [local-state (r/atom {:username ""
                :password ""})
        username (:username @local-state)
        password (:password @local-state)]
    (fn []
      [:div#login-component {}
        [:form {:action "#"}
         [:input#login-name {:placeholder "username"
                             :value (:username @local-state)
                             :on-change #(swap! local-state assoc :username (-> % .-target .-value))}]
         [:input#login-password {:placeholder "password"
                                 :value (:password @local-state)
                                 :on-change #(swap! local-state assoc :password (-> % .-target .-value))}]
         [:button#login-button {:on-click #(reset! local-state {:username "" :password ""})}
          "Login"]]])))


