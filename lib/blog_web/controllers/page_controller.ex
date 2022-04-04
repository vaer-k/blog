defmodule BlogWeb.PageController do
  use BlogWeb, :controller

  def index(conn, _params) do
    conn =
      conn
      |> assign(:page_title, "Vincent Raerek on the Internet")
      |> assign(:suffix, "")

    render(conn, "index.html")
  end
end
