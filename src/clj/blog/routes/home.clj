(ns blog.routes.home
  (:require
   [blog.layout :as layout]
   [blog.db.core :as db]
   [clojure.java.io :as io]
   [blog.middleware :as middleware]
   [ring.util.response]
   [ring.util.http-response :as response]))

(defn home-page [request]
  (layout/render request "home.html"))

(defn construction [& request]
  (layout/render request "construction.html"))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]])

