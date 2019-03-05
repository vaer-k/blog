(ns blog.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [blog.core-test]))

(doo-tests 'blog.core-test)

