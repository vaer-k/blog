(ns user
  (:require [blog.config :refer [env]]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [mount.core :as mount]
            [blog.figwheel :refer [start-fw stop-fw cljs]]
            [blog.core :refer [start-app]]
            [blog.db.core]
            [conman.core :as conman]
            [luminus-migrations.core :as migrations]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(defn start []
  (mount/start-without #'blog.core/repl-server))

(defn stop []
  (mount/stop-except #'blog.core/repl-server))

(defn restart []
  (stop)
  (start))

(defn restart-db []
  (mount/stop #'blog.db.core/*db*)
  (mount/start #'blog.db.core/*db*)
  (binding [*ns* 'blog.db.core]
    (conman/bind-connection blog.db.core/*db* "sql/queries.sql")))

(defn reset-db []
  (migrations/migrate ["reset"] (select-keys env [:database-url])))

(defn migrate []
  (migrations/migrate ["migrate"] (select-keys env [:database-url])))

(defn rollback []
  (migrations/migrate ["rollback"] (select-keys env [:database-url])))

(defn create-migration [name]
  (migrations/create name (select-keys env [:database-url])))


