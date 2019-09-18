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



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; MUTATIONS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; FIXME
(defmutation reset-click-count [_]
  (action [{:keys [state]}]
          (swap! state assoc :ui/number 0)))

(defmutation decrease-click-count [_]
  (action [{:keys [state]}]
          (swap! state update :ui/number dec)))

(defmutation increase-click-count [_]
  (action [{:keys [state]}]
          (swap! state update :ui/number inc)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Button Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; TODO extract the button component from the Root

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ROOT Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defsc Root [this {:ui/keys [number]}]
  {:query         [:ui/number]
   :initial-state {:ui/number 0}}
  (dom/div
    (dom/div number)
    (dom/div
      (dom/button {:onClick #(comp/transact! this `[(increase-click-count {})])}
                  "Click to increase count "))
    (dom/div
      (dom/button {:onClick #(comp/transact! this `[(decrease-click-count {})])}
                  "Click to decrease count "))
    (dom/div
      (dom/button {:onClick #(comp/transact! this `[(reset-click-count {})])}
                  "Click to reset count "))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; APP definition and init
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defonce APP (app/fulcro-app))

(defn ^:export init []
  (app/mount! APP Root "app"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(comment
  (shadow/repl :main)

  ;; js hard reset
  (.reload js/location true)

  (-> APP
      (::app/state-atom)
      deref)

  (app/schedule-render! APP {:force-root? true})

  (reset! (::app/state-atom APP) {})

  (app/current-state APP)

  (comp/get-query Root)

  )