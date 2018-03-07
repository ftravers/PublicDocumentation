`~/.bash_profile`

```
export PATH="$HOME/bin:$HOME/.gems/bin:$PATH"
export RUBYLIB="$HOME/lib:$RUBYLIB"
```

I created a sub-domain called: `sinatra.spicevan.com`.  Ensure `Passenger
(Ruby/Python apps only):` is selected when you create the domain.

`~/sinatra.spicevan.com/config.ru`

```
ENV['GEM_HOME'] = '/home/fen_annie/.gems'
ENV['GEM_PATH'] = '$GEM_HOME:/usr/lib/ruby/gems/1.8'  
require 'rubygems'
Gem.clear_paths

require 'test'

run Sinatra::Application
```

`~/sinatra.spicevan.com/test.rb`

```
require 'sinatra'
get '/' do
  'Hello world!'
end
```

To restart application touch file: `~/sinatra.spicevan.com/tmp/restart.txt`

```bash
$ cd sinatra.spicevan.com; mkdir tmp; cd tmp; touch restart.txt
```

# Just Sinatra

parse parameters received from a post:

