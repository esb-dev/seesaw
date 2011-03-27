(ns seesaw.test.core
  (:use seesaw.border)
  (:use [lazytest.describe :only (describe it testing)]
        [lazytest.expect :only (expect)])
  (:import (javax.swing.border EmptyBorder LineBorder MatteBorder TitledBorder)))

(describe empty-border
  (it "creates a 1 pixel border by default"
      (let [b (empty-border)]
        (expect (= EmptyBorder (class b)))
        (expect (= (Insets. 1 1 1 1) (.getBorderInsets b)))))
  (it "creates a border with same thickness on all sides"
    (let [b (empty-border :thickness 11)]
      (expect (= EmptyBorder (class b)))
      (expect (= (Insets. 11 11 11 11) (.getBorderInsets b)))))
  (it "creates a border with specified sides"
    (let [b (empty-border :top 2 :left 3 :bottom 4 :right 5)]
      (expect (= EmptyBorder (class b)))
      (expect (= (Insets. 2 3 4 5) (.getBorderInsets b)))))
  (it "creates a border with specified sides, defaulting to 0"
    (let [b (empty-border :left 3 )]
      (expect (= EmptyBorder (class b)))
      (expect (= (Insets. 0 3 0 0) (.getBorderInsets b))))))

(describe line-border
  (it "creates a black, one pixel border by default"
    (let [b (line-border)]
      (expect (= LineBorder (class b)))
      (expect (= 1 (.getThickness b)))
      (expect (= Color/BLACK (.getLineColor b)))))
  (it "creates a border with desired color and thickness"
    (let [b (line-border :thickness 12 :color Color/YELLOW)]
      (expect (= LineBorder (class b)))
      (expect (= 12 (.getThickness b)))
      (expect (= Color/YELLOW (.getLineColor b)))))
  (it "creates a matte border with specified sides"
    (let [b (line-border :top 2 :left 3 :bottom 4 :right 5 :color Color/YELLOW)]
      (expect (= MatteBorder (class b)))
      (expect (= (Insets. 2 3 4 5) (.getBorderInsets b)))
      (expect (= Color/YELLOW (.getMatteColor b)))))
  (it "creates a matte border with specified sides, defaulting to 0"
    (let [b (line-border :top 2)]
      (expect (= MatteBorder (class b)))
      (expect (= (Insets. 2 0 0 0) (.getBorderInsets b)))
      (expect (= Color/BLACK (.getMatteColor b))))))

(describe compound-border
  (it "creates nested compound borders inner to outer"
    (let [in (line-border)
          mid (line-border)
          out (line-border)
          b (compound-border in mid out)]
      (expect (= out (.getOutsideBorder b)))
      (expect (= mid (.. b (getInsideBorder) (getOutsideBorder))))
      (expect (= in (.. b (getInsideBorder) (getInsideBorder)))))))

(describe to-border
  (it "returns input if it's already a border"
    (let [b (line-border)]
      (expect (= b (to-border b)))))
  (it "creates an empty border with specified thickness for a number"
    (let [b (to-border 11)]
      (expect (= EmptyBorder (class b)))
      (expect (= (Insets. 11 11 11 11) (.getBorderInsets b)))))
  (it "returns a titled border using str if it doesn't know what to do"
    (let [b (to-border "Test")]
      (expect (= TitledBorder (class b)))
      (expect (= "Test" (.getTitle b)))))
  (it "creates a compound border out of multiple args"
      (let [b (to-border "Inner" "Outer")]
        (expect (= "Outer" (.. b getOutsideBorder getTitle)))
        (expect (= "Inner" (.. b getInsideBorder getTitle)))))
  (it "creates a compound border out of a collection arg"
      (let [b (to-border ["Inner" "Outer"])]
        (expect (= "Outer" (.. b getOutsideBorder getTitle)))
        (expect (= "Inner" (.. b getInsideBorder getTitle))))))

