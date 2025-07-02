defmodule BlogWeb do
  @moduledoc """
  The entrypoint for defining your web interface, such
  as controllers, views, channels and so on.

  This can be used in your application as:

      use BlogWeb, :controller
      use BlogWeb, :view

  The definitions below will be executed for every view,
  controller, etc, so keep them short and clean, focused
  on imports, uses and aliases.

  Do NOT define functions inside the quoted expressions
  below. Instead, define any helper function in modules
  and import those modules here.
  """

  def controller do
    quote do
      use Phoenix.Controller, namespace: BlogWeb

      import Plug.Conn
      import BlogWeb.Gettext
      import BlogWeb.VerifiedRoutes

      unquote(verified_routes())
    end
  end

  def view do
    quote do
      use Phoenix.View,
        root: "lib/blog_web/templates",
        namespace: BlogWeb

      # Import convenience functions from controllers
      import Phoenix.Controller,
        only: [get_flash: 1, get_flash: 2, view_module: 1, view_template: 1]

      # Include shared imports and aliases for views
      unquote(view_helpers())

      # Use verified routes
      use Phoenix.VerifiedRoutes,
        endpoint: BlogWeb.Endpoint,
        router: BlogWeb.Router,
        statics: BlogWeb.static_paths()
    end
  end

  def live_view do
    quote do
      use Phoenix.LiveView,
        layout: {BlogWeb.LayoutView, "live.html"}

      unquote(view_helpers())
    end
  end

  def live_component do
    quote do
      use Phoenix.LiveComponent

      unquote(view_helpers())
    end
  end

  def router do
    quote do
      use Phoenix.Router

      import Plug.Conn
      import Phoenix.Controller
      import Phoenix.LiveView.Router
    end
  end

  def channel do
    quote do
      use Phoenix.Channel
      import BlogWeb.Gettext
    end
  end

  defp view_helpers do
    quote do
      # Import HTML functionality
      import Phoenix.HTML
      import Phoenix.HTML.Form
      use PhoenixHTMLHelpers

      # Import LiveView and .heex helpers (live_render, live_patch, <.form>, etc)
      import Phoenix.Component
      import Phoenix.LiveView.Helpers

      # Import basic rendering functionality (render, render_layout, etc)
      import Phoenix.View

      import BlogWeb.ErrorHelpers
      import BlogWeb.Gettext
      import BlogWeb.VerifiedRoutes
    end
  end

  def html do
    quote do
      use Phoenix.Component

      # Import convenience functions from controllers
      import Phoenix.Controller,
        only: [get_csrf_token: 0, view_module: 1, view_template: 1]

      # Include general helpers for rendering HTML
      unquote(html_helpers())
    end
  end

  def verified_routes do
    quote do
      use Phoenix.VerifiedRoutes,
        endpoint: BlogWeb.Endpoint,
        router: BlogWeb.Router,
        statics: BlogWeb.static_paths()
    end
  end

  defp html_helpers do
    quote do
      # HTML escaping functionality
      import Phoenix.HTML
      import Phoenix.HTML.Form
      use PhoenixHTMLHelpers

      # Core UI components and translation
      import BlogWeb.ErrorHelpers
      import BlogWeb.Gettext

      # Routes generation with the ~p sigil
      unquote(verified_routes())
    end
  end

  def static_paths do
    ~w(assets fonts images favicon.ico robots.txt)
  end

  @doc """
  When used, dispatch to the appropriate controller/view/etc.
  """
  defmacro __using__(which) when is_atom(which) do
    apply(__MODULE__, which, [])
  end
end
