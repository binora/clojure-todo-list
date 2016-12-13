(ns todo-list.validation
  (:require [bouncer.core :as b]
            [bouncer.validators :as v]))



(defn validate-login  [user]
  (let [error-map (first (b/validate user
                                     :name v/required
                                     :password v/required))]
    (vals error-map)))

