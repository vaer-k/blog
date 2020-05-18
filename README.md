# blog

generated using Luminus version "3.70"

FIXME

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein run 
    
For development with cider cljs repl, run:

`lein shadow watch app` and in another tab `lein run`. Then run `cider-connect-clj` in emacs; specify `localhost` and port `7002` at cider prompts;
then run `(shadow/repl :app)` from repl

## License

Copyright © 2020 FIXME
