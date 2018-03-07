http://ruby.railstutorial.org/chapters/

Creating project called `adsnet`

```bash
$ rails new adsnet
$ cd adsnet
```

in file: `Gemfile` change:

```ruby
gem 'sqlite3'
```

to:

```ruby
group :development do
  gem 'sqlite3', '1.3.5'
end
```

    $ bundle install

Test with:

  Start Server
  
    $ rails server

  Open browser:
  
    http://0.0.0.0:3000

# Heroku

in file: `Gemfile` add:

```ruby
group :production do
  gem 'pg', '0.12.2'
end
```

login/setup app for heroku

```bash
$ heroku login
$ heroku create
$ heroku rename adsnet
```

push latest up to heroku and view the page:

```
$ git push heroku master
$ heroku open
```

# Modelling

```
$ rails generate scaffold User name:string email:string
$ bundle exec rake db:migrate
```

startup rails server and look at the users:

```
$ rails s

```

    http://0.0.0.0:3000/users


