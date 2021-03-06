(ns playground.dtypes
  (:require [clojure.string :as s])
  (:gen-class))

(defprotocol IPlay
  (start [game])
  (setv [x v])
  (getv [x]))

;; (defprotocol IRule
;;   (fariplay [x]))

;; extend
(extend String
  IPlay
  {:start (fn [x] "Playing some string!")})

(deftype Footbal [x]
                 ;;[^{:volatile-mutable true} x]
  IPlay
  (start [this] "Kick a ball with your foot")
  ;(setv [this v] (set! x v))
  ;(getv [this] x)
  )


;; initialize
(def f1 (Footbal. 3)) ;; syntatic sugar (new Footbal)

(def f2 (->Footbal 3))

;; use
(defn use-football []
  (do ;(prn (.x f1)) ;; should fail
      (prn (.-x f2))
      (prn (.toString f1))
      (prn (.toString f2))
      (prn (start f2))
      (prn (.start f1))
      ;(prn (setv f1 100))
      (prn (.-x f1))
      ;(prn (.getv f1))
      (.. f1 start toUpperCase)
      ))








(defrecord Chess [x y]
  IPlay
  (start [this] (str x " plays chess with " y)))

;; initialize
(def c1 (Chess. "Alex" "DevTalks"))
(def c2 (->Chess "DevTalks" "Alex"))
(def c3 (map->Chess {:x "Alex" :y "Alex"}))
(def c4 #playground.dtypes.Chess{:x "Alex" :y "Some Other Name"})

;; use
(defn use-chess []
  (do ;(prn (.x f1)) ;; should fail
      (prn (.-x c1))
      (prn (.toString c1))
      (prn (start c1))
      (prn (.start c1))
      (prn (.. c1 start toUpperCase))
      ;; map access and usage
      (prn (:x c2))
      (prn (type (assoc c2 :y "DevTalks"))) ;; immutable new record
      (prn c2)
      (prn (dissoc c4 :x))
      (prn (type (dissoc c4 :x)))
      (into c2 {:move "pawn" :to "C4"})))







(defmulti play-game (fn [game players]
                      (-> game
                          class
                          str
                          (s/split #"\.")
                          last
                          keyword)))


(defmethod play-game :Footbal [game players]
  (if (-> players count (= 11))
    "Game on!"
    "Goops.."))

(defmethod play-game :Chess [game players]
  (if (-> players count (= 2))
    "Game on!"
    "Goops.."))

(defmethod play-game :IPlay [game players]
  (if (-> players count (= 12))
    "Game on!"
    "Goops.."))

(play-game (->Footbal 34) (range 11))

(play-game (->Chess "Alex" 35) (range 2))

(isa? Footbal IPlay)

(derive Footbal ::IPlay)

(isa? Footbal ::IPlay)
