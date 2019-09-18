(ns app.client
  (:require
    ;; external libs
    ;; internal libs
    [com.fulcrologic.fulcro.application :as app]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom]
    [com.fulcrologic.fulcro.algorithms.merge :as merge]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; UTILS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defsc Root [this {:root/keys [people]}]
  {:query         []
   :initial-state {}
  (js/console.log "Render Root")
  (dom/h1 :.ui.header "Hello, Fulcro!"))

(defonce APP (app/fulcro-app))

(defn ^:export init []
  (app/mount! APP Root "app"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(comment
  (shadow/repl :main)

  ;; js hard reset
  (.reload js/location true)

  (::app/state-atom APP)

  (keys APP)

  (-> APP
      (::app/state-atom)
      deref)

  (app/schedule-render! APP {:force-root? true})

  (reset! (::app/state-atom APP) {})

  (app/current-state APP)

  (swap! (::app/state-atom APP) assoc-in [:person/id 3 :person/age] 22))