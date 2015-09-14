(ns softwarebears.nav)

(defn nav-body []
  (.querySelector js/document "body > nav ul"))

(defn nav-toggle []
  (.getElementById js/document "show-nav"))

(defn show [nav-ul nav-div]
  (set! (.-display (.-style nav-ul)) "block")
  (set! (.-innerHTML nav-div) "X")
  false)

(defn hide [nav-ul nav-div]
  (set! (.-display (.-style nav-ul)) "none")
  (set! (.-innerHTML nav-div) "&#9776;")
  false)

(defn ^:export toggle []
  (let [nav-ul (nav-body) nav-div (nav-toggle)]
    (if (= (.-display (.-style nav-ul)) "none")
      (show nav-ul nav-div)
      (hide nav-ul nav-div))))