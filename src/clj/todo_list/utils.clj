(ns todo-list.utils
  (:require [digest :refer [md5]]
            [clj-time.core :as t]
            [clj-time.format :as f]))

(def db-formatter (f/formatter "YYYY-MM-dd HH:mm:ss"))

(defn encrypt-password  [password]
  (digest/md5 password))

(defn generate-random-token []
  "Generates of a random token of length 16"
  (let [possible-chars (map char (range 97 123))]
    (apply str (take 16 (repeatedly (rand-nth possible-chars))))))

(defn serialize-date [date]
  (f/unparse db-formatter date))

