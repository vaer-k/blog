(ns blog.routes.home
  (:require [blog.layout :as layout]
            [blog.db.core :as db]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))

(defn home-page [request]
  (layout/render request "home.html"))

(defn construction [request]
  (layout/render request "construction.html"))

(defroutes home-routes
  ;; (GET "/" request (home-page request))
  (GET "/" request (construction request))
  (GET "/docs" []
       (-> (response/ok (-> "docs/docs.md" io/resource slurp))
           (response/header "Content-Type" "text/plain; charset=utf-8"))))

