(ns todo-list.utils)

(defn check-response-sanity
  "Checks for status field in response map"
  [response]
  (= true (:status response)))