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

(defmutation reset-click-count [_]
  (action [{:keys [state]}]
          (swap! state update :ui/number 0)))

(defmutation decrease-click-count [_]
  (action [{:keys [state]}]
          (swap! state update :ui/number inc)))

(defmutation increase-click-count [_]
  (action [{:keys [state]}]
          (swap! state update :ui/number inc)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Button Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defsc ButtonComponent [this {:ui/keys [number] :as props}]
  {:query         [:ui/number]
   :ident         [:root :ui/number]
   :initial-state {:ui/number 0}}
  (dom/button {:onClick #(comp/transact! this `[(increase-click-count {})])}
              "You've clicked the button " number " times"))

(def ui-button-component (comp/factory ButtonComponent))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ROOT Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defsc Root [this {:keys [root] :as props}]
  {:query [:root (comp/get-query ui-button-component)]}
  (ui-button-component))

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