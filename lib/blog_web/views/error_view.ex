defmodule BlogWeb.ErrorView do
  use BlogWeb, :view

  # If you want to customize a particular status code
  # for a certain format, you may uncomment below.
  def render("404.html", assigns) do
    ~H"""
      <div>
        <img src="https://i.giphy.com/QiFoJRaLiMGYg.gif">
        <h4 class="title">Not found :(</h4>
        <p class="subtitle">Sorry, but the page you were trying to view does not exist.</p>
      </div>
    """
  end

  # By default, Phoenix returns the status message from
  # the template name. For example, "404.html" becomes
  # "Not Found".
  def template_not_found(template, _assigns) do
    Phoenix.Controller.status_message_from_template(template)
  end
end
