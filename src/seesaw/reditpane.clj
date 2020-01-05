;  Copyright (c) Dave Ray, 2012. All rights reserved.

;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this
;   distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns ^{:doc "Support for TextEditorPane: http://fifesoft.com/rsyntaxtextarea/index.php"
      :author "Burkhardt Renz A Hack"}
  seesaw.reditpane
  (:require [seesaw.core :as core]
            [seesaw.options :as options]
            [seesaw.widget-options :as widget-options]
            clojure.reflect
            clojure.string)
  (:import [org.fife.ui.rsyntaxtextarea AbstractTokenMakerFactory]))

;;; Go through the available syntax highlighting modes,
;;; e.g. "text/clojure" and then for backwards compatibility map them to
;;; keywords without the text/ namespace, e.g. :clojure
(def ^{:private true} syntax-table
  (let [keys (.keySet (AbstractTokenMakerFactory/getDefaultInstance))]
    (into {} (map (juxt (comp keyword name keyword) identity) keys))))

(def text-area-options
  (merge
    core/text-area-options
    (options/option-map
      (options/bean-option
        [:syntax :syntax-editing-style]
        org.fife.ui.rsyntaxtextarea.TextEditorPane
        syntax-table
        nil
        (keys syntax-table)))))

(widget-options/widget-option-provider
  org.fife.ui.rsyntaxtextarea.TextEditorPane
  text-area-options)

(defn text-area
  "Create a new TextEditorPane."
  [& opts]
  (apply core/config! (org.fife.ui.rsyntaxtextarea.TextEditorPane.) opts))
