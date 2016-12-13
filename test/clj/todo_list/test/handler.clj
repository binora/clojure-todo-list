(ns todo-list.test.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [todo-list.validation :refer [validate-login]]
            [todo-list.handler :refer :all]))

(deftest test-app
  (testing "main route"
    (let [response ((app) (request :get "/"))]
      (is (= 200 (:status response)))))

  (testing "not-found route"
    (let [response ((app) (request :get "/invalid"))]
      (is (= 404 (:status response))))))

(deftest test-validation
  (is (= false
         (empty? (validate-login {:name ""})) ) "Testing with no password")
  (is (= false
         (empty? (validate-login {:name "" :password ""})) ) "Both empty")
  (is (=  true
         (empty? (validate-login {:name "hell" :password "hel"})) )))
