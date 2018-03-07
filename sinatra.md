# Basic Sinatra 

In this example we create a site that will respond to:

    http://tmp:8082/
    
So hostname `tmp` and port `8082` are the variables.    

setup nginx:

```
server {
   listen 8082;
   server_name tmp;
   root /home/fenton/projects/proj1/public;
   passenger_enabled on;
}
```

I'm calling my project: `proj1`, you can use another name.

Add `tmp` to `/etc/hosts` file

```
127.0.0.1	localhost.localdomain	localhost ss9 tmp
```

Make sinatra folder layout:

```
mkdir -p proj1/{app,public}
```

In file: `app/application.rb` put:

```
require 'sinatra/base'
module Proj1
  Class MySinatraApp < Sinatra::Base
    get "/" do
      "Hello World"
    end
  end
end
```

In file: `config.ru` put:

```
require './app/application'
run Proj1::MySinatraApp
```

Now test by openning the following URL in a browser:

   http://tmp:8082/

# Add in bundler support

In file: `Gemfile` put:

```
source :rubygems
gem 'rack'
gem 'sinatra'
```

# View Templates

In file: `app/application.rb` CHANGE:

```
get "/" do
  "Hello World"
end
```

to:

```
get "/" do
  @val = "hello"
  erb :home
end
```

Make/use an erb template:

    mkdir app/views
    
In file: `app/views/home.erb` put:

```
This is the <%= @val %>
```

Test: `http://tmp:8082/`
    
# Use Cells    

In file: `Gemfile` add:

```
gem 'cells'
```

run:

    $ bundle install

create required dirs:

```bash
$ mkdir app/views/joke
$ mkdir app/cells
```    
    
In file: `app/cells/joke_cell.rb` put:

```
require 'cell/base'
require "cell/rails/helper_api"

class JokeCell < ::Cell::Base
  include Cell::Rails::HelperAPI
  def get_bad_joke
    @bj="chick cross road"
    render
  end
end
```
    
In file: `application.rb` put:

```

```
    
In file: `` put:

```

```
    
In file: `` put:

```

```
    
In file: `` put:

```

```
    
In file: `` put:

```

```
    
In file: `` put:

```

```
    
In file: `` put:

```

```

