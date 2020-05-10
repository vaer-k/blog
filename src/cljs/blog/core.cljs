(ns blog.core
  (:require
    [day8.re-frame.http-fx]
    [reagent.dom :as rdom]
    [reagent.core :as r]
    [re-frame.core :as rf]
    [goog.events :as events]
    [goog.history.EventType :as HistoryEventType]
    [markdown.core :refer [md->html]]
    [blog.ajax :as ajax]
    [blog.events]
    [reitit.core :as reitit]
    [reitit.frontend.easy :as rfe]
    [clojure.string :as string])
  (:import goog.History))

;; currently unused
(defn home-page []
  [:section.section>div.container>div.content
   (when-let [docs @(rf/subscribe [:docs])]
     [:div "Hello world!"]
     [:div {:dangerouslySetInnerHTML {:__html (md->html docs)}}])])

;; -------------------------
;; My stuff

(defn banner []
  [:section.hero.is-dark
   [:div.hero-body
    [:div.container
     [:h1.title.is-size-1-desktop [:a {:href "/"} "Hello world"] [:div.cursor]]
     [:p.subtitle.appear "I'm Vincent Raerek"]]]])

(defn the-goods []
  [:nav
   [:h3.title "The Goods"]
   [:ul
    [:li.subtitle
     [:a {:href "https://github.com/vaer-k"}
      "Open Source Projects"
      [:h6.subtitle.is-6 "Code that Might be Useful to You"]]]
    [:li.subtitle
     [:a {:href "/posts"}
      "Things I've Written"
      [:h6.subtitle.is-6 "On Technology and Ideas"]]]
    [:li.subtitle
     [:a {:href "/files/vincent-raerek-resume.pdf" :download true}
      "Resume"
      [:h6.subtitle.is-6 "Things I've Built and Places I've Worked"]]]    
    ]])

(defn also-on []
  [:nav
   [:h3.title "I'm also on"]
   [:ul
    [:li.subtitle
     [:a {:href "https://github.com/vaer-k"
          :target "tab"}
      "GitHub" [:h6.subtitle.is-6 "If you code"]]]
    [:li.subtitle
     [:a {:href "https://twitter.com/vaer_k"
          :target "tab"}
      "Twitter" [:h6.subtitle.is-6 "If you tweet"]]]
    [:li.subtitle
     [:a {:href "https://www.linkedin.com/in/vincentraerek"
          :target "tab"}
      "LinkedIn" [:h6.subtitle.is-6 "If you wanna work together"]]]
    [:li.subtitle
     [:a {:href "https://instagram.com/vaer_k"
          :target "tab"}
      "Instagram" [:h6.subtitle.is-6 "If you like photos"]]]
    [:li.subtitle
     [:a {:href "https://stackoverflow.com/users/2602816/vaer-k"
          :target "tab"}
      "Stack Overflow" [:h6.subtitle.is-6 "If you like to ask questions"]]]]])


(defn color-demo []
  (let [color (r/atom "blue")]
    (fn []
      (condp = @color
        "blue" [:div.box {:on-click #(reset! color "red")
                          :style {:background "blue"}}]
        "red" [:div.box {:on-click #(reset! color "blue")
                         :style {:background "red"}}]))))

(defn site-nav []
  [:header.column
   [the-goods]
   [:div.strike]
   [also-on]])

(defn display-og []
  [:div.column.is-hidden-mobile
   [:figure.me-img]
   [:legend "Yosemite National Park"]])

(defn display []
  [:div.column.is-hidden-mobile
   [:figure
    [:img {:src "/img/profile.jpg"}]]
   [:legend "Yosemite National Park"]])

(defn about-page []
  [:<>
   [banner]
   [:main.section
    [:div.container
     [:div.columns.is-variable.is-7
      [display]
      [site-nav]]]]])

;;;;
;;;;
;;;;
(defn page []
  (if-let [page @(rf/subscribe [:common/page])]
     [page]))

(defn navigate! [match _]
  (rf/dispatch [:common/navigate match]))

(def router
  (reitit/router
    [["/" {:name        :home
           :view        #'about-page ;; TODO move back to home page
           :controllers [{:start (fn [_] (rf/dispatch [:page/init-home]))}]}]
     ["/about" {:name :about
                :view #'about-page}]]))

(defn start-router! []
  (rfe/start!
    router
    navigate!
    {}))

;; -------------------------
;; Initialize app
(defn ^:dev/after-load mount-components []
  (rf/clear-subscription-cache!)
  (rdom/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (start-router!)
  (ajax/load-interceptors!)
  (mount-components))
