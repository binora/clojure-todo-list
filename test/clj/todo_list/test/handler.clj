(ns todo-list.test.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [expectations :refer [expect]]
            [todo-list.routes.auth :refer [create-user]]
            [todo-list.validation :refer [validate-login]]
            [todo-list.handler :refer :all]))

(deftest test-app
  (testing "main route"
    (let [response ((app) (request :get "/"))]
      (is (= 200 (:status response)))))

  (testing "not-found route"
    (let [response ((app) (request :get "/invalid"))]
      (is (= 404 (:status response))))))


(expect false (empty? (validate-login {:name ""})))
(expect false (empty? (validate-login {})))
(expect true (empty? (validate-login {:name "helowr" :password "hellwer"})) )


(expect true
        (let [response ((app) (request :post "/auth/create" {:name "df" :password "s"}))]
          (:status (:body response))))
