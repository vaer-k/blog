(ns blog.core
  (:require [baking-soda.core :as b]
            [day8.re-frame.http-fx]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [blog.ajax :as ajax]
            [blog.events]
            [secretary.core :as secretary])
  (:import goog.History))

; the navbar components are implemented via baking-soda [1]
; library that provides a ClojureScript interface for Reactstrap [2]
; Bootstrap 4 components.
; [1] https://github.com/gadfly361/baking-soda
; [2] http://reactstrap.github.io/

(defn nav-link [uri title page]
  [b/NavItem
   [b/NavLink
    {:href   uri
     :active (when (= page @(rf/subscribe [:page])) "active")}
    title]])

(defn navbar []
  (r/with-let [expanded? (r/atom true)]
    [b/Navbar {:light true
               :class-name "navbar-dark bg-primary"
               :expand "md"}
     [b/NavbarBrand {:href "/"} "blog"]
     [b/NavbarToggler {:on-click #(swap! expanded? not)}]
     [b/Collapse {:is-open @expanded? :navbar true}
      [b/Nav {:class-name "mr-auto" :navbar true}
       [nav-link "#/" "Home" :home]
       [nav-link "#/about" "About" :about]]]]))

(defn about-page-old []
  [:div.container
   [:div.row
    [:div.col-md-12
     [:img {:src "/img/warning_clojure.png"}]]]])

(defn home-page []
  [:div.container
   (when-let [docs @(rf/subscribe [:docs])]
     [:div.row>div.col-sm-12
      [:div {:dangerouslySetInnerHTML
             {:__html (md->html docs)}}]])])


;; -------------------------
;; My stuff

(defn banner []
  [:section.hero.is-dark
   [:div.hero-body.container
    [:h1.title.is-size-1-desktop [:a {:href "/"} "Hello world"] [:div.cursor]]
    [:p.subtitle.appear "I'm Vincent Raerek"]]])

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
   [:main.container.section
    [:div.columns.is-variable.is-7
     [display]
     [site-nav]]]])

(def pages
  {:home #'about-page
   :about #'about-page})

(defn page []
  [(pages @(rf/subscribe [:page]))])

(defn old-page []
  [:div
   [navbar]
   [(pages @(rf/subscribe [:page]))]])

;; -------------------------
;; Routes

(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:navigate :home]))

(secretary/defroute "/about" []
  (rf/dispatch [:navigate :about]))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:navigate :home])
  (ajax/load-interceptors!)
  (rf/dispatch [:fetch-docs])
  (hook-browser-navigation!)
  (mount-components))
