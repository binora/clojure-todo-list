(ns todo-list.todo-page
  (:require [reagent.core :as r]
            [ajax.core :refer [GET POST]]
            [todo-list.store :refer [app-state]]))


(defn fetch-todos []
  (GET "/todo/all" {:params
                        {:token (:token @app-state)}
                     :response-format :json
                      :handler #(swap! app-state assoc :todos (:todos %))
                     :keywords? true}))

(defn on-create! [id text]
  (swap! app-state update-in [:todos] merge {:text text :_id id}))

(defn create-todo [value callback]
  (POST "/todo/create" {:params
                          {:token "pbjysjlpxitmaqji"
                           :text value}
                         :response-format :json
                         :handler #(callback (:todo-id %) value)
                         :keywords? true}))

(defn todo-list [todos]
  [:ul#todo-list
   (map (fn [todo]
          [:li {:key (:_id todo)} (:text todo)])
        todos)])


(defn todo-input []
  (let [value (r/atom "")]
    (fn []
      [:div#new-todo
       [:input#todo-input {:placeholder "Create new todo"
                           :value @value
                           :on-change (fn [e]
                                        (let [val (-> e .-target .-value)]
                                          (println val)
                                          (reset! value val)))}]
       [:button#new-todo-button {:on-click #(do  (create-todo @value on-create!) (reset! value "") )} "Create"]])))

(defn header []
  [:div#app-header {}
    [:h4  "Welcome to your Todo Tracker"]])


(defn todo-component []
  (fetch-todos)
  (fn []
    [:div#todo-component
     [header]
     [todo-input]
     [todo-list (:todos @app-state)]]))
