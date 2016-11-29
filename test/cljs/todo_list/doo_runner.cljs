(ns todo-list.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [todo-list.core-test]))

(doo-tests 'todo-list.core-test)

