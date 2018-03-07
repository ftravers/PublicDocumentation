# Install - Debian Flavors

Ubuntu, Mint

[Reference](http://ryanbigg.com/2010/12/ubuntu-ruby-rvm-rails-and-you/)

```bash
$ sudo apt-get -y install build-essential git-core curl
$ bash -s stable < <(curl -s https://raw.github.com/wayneeseguin/rvm/master/binscripts/rvm-installer)
echo 'source .rvm/scripts/rvm' >> ~/.bashrc; source ~/.bashrc
$ sudo apt-get install -y --force-yes build-essential openssl libreadline6 libreadline6-dev curl git-core zlib1g zlib1g-dev libssl-dev libyaml-dev libsqlite3-0 libsqlite3-dev sqlite3 libxml2-dev libxslt-dev autoconf libc6-dev ncurses-dev automake libtool bison subversion
$ rvm install 1.9.2; rvm use 1.9.2; rvm --default use 1.9.2
```

# RVM on redhat / oracle linux

```bash
# adduser ng
# passwd ng
```

install curl, sudo

```
# yum install -y sudo curl
```

Edit sudoers file with: `visudo`, put in: 

```
docs ALL=(ALL) ALL
```

append `CURL_CA_BUNDLE`, to line `Defaults env_keep`, as an
environment variable to keep after running `sudo`

```bash
# su - ng
$ wget http://curl.haxx.se/ca/cacert.pem
$ export CURL_CA_BUNDLE=~/cacert.pem
$ sudo ./script stable
$ rvm requirements
$ exit
# vi /etc/group
```

adding `ng` to `rvm` group, put a line like:

```
rvm:x:501:ng
```

now install the requirements for ruby/rvm:

```
# wget -c http://apt.sw.be/redhat/el5/en/i386/rpmforge/RPMS/rpmforge-release-0.3.6-1.el5.rf.i386.rpm
# rpm -ivh rpmforge-release-0.3.6-1.el5.rf.i386.rpm
```

The `rvm requirements` line should have listed out a yum statement
with the pre-requisits like the following.  Use whatever is in that
output.

```
# yum install -y git gcc-c++ patch readline readline-devel zlib zlib-devel libyaml-devel libffi-devel openssl-devel make bzip2 autoconf automake libtool bison iconv-devel
# su - ng
$ type rvm | head -1
rvm is a function
$ sudo rvm install 1.9.3
$ rvm use 1.9.3 --default
$ ruby --version
```

### RVM install Gems

Figure out which version of ruby you want to install your gem into
with:

    rvm info
    
Say I want to install `git` gem into ruby version 1.9.2p290 I'd do:

    rvm 1.9.2 do gem install git

## Debugger on Emacs

[ref](http://pivotallabs.com/users/chad/blog/articles/366-ruby-debug-in-30-seconds-we-don-t-need-no-stinkin-gui-)

For ruby 1.9 do:

    $ gem install ruby-debug19 ruby-debug-ide
    
get latest ruby-debug-extra from:

* http://rubyforge.org/projects/ruby-debug/ > files

this needs texlive do (it takes a looooong time to install), also get
the rdebug cheat sheet:

```bash
$ sudo apt-get install texlive texinfo
$ cd ~/Downloads; wget http://rubyforge.org/frs/download.php/73086/ruby-debug-extra-0.10.4.tar.gz; tar xvfz ruby-debug-extra-0.10.4.tar.gz; cd ruby-debug-extra-0.10.4/; ./configure; make; sudo make install
$ cp ~/Downloads/ruby-debug-extra-0.10.4/emacs/rdebug.el ~/.emacs.d/
$ gem install cheat; cheat rdebug
```

create a file ~/.rdebugrc with this content:

```
set autolist
set autoeval
set autoreload
```

* autolist       - execute 'list' command on every breakpoint
* autoeval       - evaluate every unrecognized command
* autoreload     - enables automatic source code reloading

**IMPORTANT**: open emacs from shell to inherit all environment from shell!!!

Open your ruby file and type: `M-x rdebug`

Key                                    | Effect
-------------------------------------- | ---------------------
r                                      | Reload
n                                      | Next Line (Step Over)
s                                      | Step Into
c                                      | Continue
c[ont][ nnn]                           | run until program ends or hits breakpoint or reaches line nnn
where                                  | Display Frame / Call Stack
b my.rb:10                             | set breakpoint at my.rb line 10
b[reak] file:line [if expr]            | breakpoints 
b[reak] class(.|#)method [if expr]     | breakpoints
h                                      | help
@myvar = 'foo'                         | Modify a variable
myvar.class                            | Evaluate any var/expression
disp[lay] <expression>                 | add expression into display expression list
down [count]                           | move to lower frame
e[val]                                 | expression evaluate expression and print its value, alias for p
q[uit], exit                           |
fin[ish]                               | return to outer frame
m[ethod] i[nstance] <obj>              | show methods of object
m[ethod] <class|module>                | show instance methods of class or module
n[ext][+][ nnn]                        | step over once or nnn times, '+' forces to move to another line
up[count]                              | move to higher frame
restart                                | restart the debugger (say if in (rdb:ctrl) state.

## ctags ruby

```
    ctags -e -a --Ruby-kinds=-fF -o TAGS -R .
```

# Language Reference

## Function Parameters

```ruby
def func(my_closure)
  my_closure.call 2,3
end
adder = lambda { |x,y| puts x + y }
func adder
```

prints `5`

## Define a method

```ruby
irb(main):019:0> def h(name = "World")
irb(main):020:1> puts "Hello #{name.capitalize}!"
irb(main):021:1> end
=> nil
irb(main):022:0> h "chris"
Hello Chris!
=> nil
irb(main):023:0> h
Hello World!
=> nil
```

Highlights: 

* default parameter value: "World", if method called without a param.

* `#{}` is string interpolation

## Modules

define a module file: `fenton.rb`

```ruby
module MyModule
  def func1
    puts "Hello"
  end
end
```

### Mixin

If you want to include the same code in many classes you can use
mixins.

```ruby
module Debug
  def whoAmI?
    "#{self.type.name} (\##{self.id}): #{self.to_s}"
  end
end
class Phonograph
  include Debug
  # ...
end
class EightTrack
  include Debug
  # ...
end
ph = Phonograph.new("West End Blues")
et = EightTrack.new("Surrealistic Pillow")
ph.whoAmI?	»	"Phonograph (#537766170): West End Blues"
et.whoAmI?	»	"EightTrack (#537765860): Surrealistic Pillow"
```

Modules cannot be instantiated, only included into a class.  This is
called a `mixin`. In file `travers.rb`.  Modules can include other
modules.

```ruby
require 'fenton'
class ClassOne
  include MyModule
  def func2
    func1
  end
end
```

### Sharing Code

required statement should be following by path and filenames

I may define the following in file: `lib/myTools/fenton.rb`

```ruby
module ItTools
  class VpnTools
    def on_vpn
      puts "hello."
    end
  end
end
```

Assuming lib is one of the number of pre-defined directories on your
system.  These paths are defined in a variable called `$LOAD_PATH`

To use this in another file I'd do:

```ruby
require 'myTools/fenton'
foo = ItTools::VpnTools.new
foo.on_vpn
```

## Exception Handling

```ruby
begin
  1/0
  p 'I should never get executed'
rescue
  p 'I am rescuing an exception and can do what I want!'
end
```

throwing exceptions

```ruby
raise "Here is the error message"
```

## System Commands

```ruby
system "command"
```

surround by backticks to get output of command:

```ruby
output = `ls`
p output
```
## Inheritance

[ref](http://rubylearning.com/satishtalim/ruby_inheritance.html)

* A class can only inherit from a *SINGLE* other class.


```ruby
class Mammal  
  def breathe  
    puts "inhale and exhale"  
  end  
end  
class Cat < Mammal  
  def speak  
    puts "Meow"  
  end  
end  
rani = Cat.new  
rani.breathe  
rani.speak 
```

## Command Line Arguments

are stored in the array `ARGV`

```ruby
#!/usr/bin/env ruby
ARGV.each do|a|
  puts "Argument: #{a}"
end
```

# Cook Book

## Creating Gems

[Ref](http://blog.thepete.net/2010/11/creating-and-publishing-your-first-ruby.html)

In this example we create a gem called: `fentonGem1`

```bash
$ rvm gemset create fentonGem1
$ rvm gemset use fentonGem1
$ gem install bundler
$ bundle gem fentonGem1
```

Look at `fentonGem1.gemspec` update summary/description lines:

```ruby
  s.summary     = %q{Gem basically does nothing}
  s.description = %q{Gem really doesnt do anything}
```

### Create our library function

in file: `lib/fentonGem1/do_little.rb

```ruby
module FentonGem1
  class DoLittle
    def simpleFunc
      time = Time.now
      minute = time.min
      hour = time.hour % 12
      meridian_indicator = time.hour < 12 ? 'AM' : 'PM'
      "#{minute} minutes past #{hour} o'clock, #{meridian_indicator}"
    end
  end
end
```

### Make it scriptable

in file: `bin/getTimeNow` 

```ruby
#!/usr/bin/env ruby
require 'fentonGem1/do_little'
do_little = FentonGem1::DoLittle.new
puts do_little.simpleFunc
```

### Build Gem and Test

Add items into git repository

```bash
$ git add bin/ lib/*; git commit -am'.'
```

```bash
~/tmp/fentonGem1$ rake install
~/tmp/fentonGem1$ getTimeNow
```

### Publish

Signup at rubygems.org:

[Instructions at rubygems.org](https://rubygems.org/pages/gem_docs)

If you have already published the current version, update the version
in the file: 

    lib/<package>/version.rb
    
build it with:

    rake install

publish with:

```bash
$ gem push pkg/fentonGem1-0.0.1.gem
```

## Daemon, Sinatra, Thin

[ref](http://labs.headlondon.com/2010/07/skinny-daemons/)

This example uses a project called `fenton_daemon`.

```bash
$ bundle gem fenton_daemon
      create  fenton_daemon/Gemfile
      create  fenton_daemon/Rakefile
      create  fenton_daemon/.gitignore
      create  fenton_daemon/fenton_daemon.gemspec
      create  fenton_daemon/lib/fenton_daemon.rb
      create  fenton_daemon/lib/fenton_daemon/version.rb
Initializating git repo in /home/fenton/tmp2/fenton_daemon
$ cd fenton_daemon/
$ tree
.
|-- fenton_daemon.gemspec
|-- Gemfile
|-- lib
|   |-- fenton_daemon
|   |   |-- config.ru
|   |   `-- version.rb
|   `-- fenton_daemon.rb
`-- Rakefile
$ touch lib/fenton_daemon/config.ru
```

Edit `lib/fenton_daemon/config.ru` in a text editor so it has the
contents outlined below.

```ruby
require File.dirname(__FILE__) + '/../fenton_daemon'
FentonSinatraDaemon.run! :port => 2003
```

```bash
$ mkdir bin
$ touch bin/fenton_daemon
```

Edit `bin/fenton_daemon`, inserting the contents outlined below:

```ruby
#!/usr/bin/env ruby

require File.dirname(__FILE__) + '/../lib/fenton_daemon'
require 'thin'

rackup_file = "#{File.dirname(__FILE__)}/../lib/fenton_daemon/config.ru"

argv = ARGV
argv << ["-R", rackup_file] unless ARGV.include?("-R")
argv << ["-p", "2003"] unless ARGV.include?("-p")
argv << ["-e", "production"] unless ARGV.include?("-e")
Thin::Runner.new(argv.flatten).run!
```

Insert into file `lib/fenton_daemon.rb` the contents shown below

```ruby
require "fenton_daemon/version"
require 'rubygems'
require 'sinatra/base'
class FentonSinatraDaemon < Sinatra::Base
  get "/" do
    "Your skinny daemon is up and running."
  end
end
module FentonDaemon
end
```

Remove `TODO: ` from `fenton_daemon.gemspec`

```bash
$ sed -i 's/TODO: //' fenton_daemon.gemspec
```

Install

```bash
$ git add .
$ git commit -am'.'
$ rake install
```

## Test

```bash
$ fenton_daemon start
```

Goto: `http://localhost:2003/`

To Stop:

```bash
$ fenton_daemon stop
```

To daemonize, start like so:

```bash
$ fenton_daemon start -d
```

Set to run at ubuntu startup, create file:
`/etc/init.d/documentation`, with the following contents:

```
documentation-daemon start -d
```

```bash
$ sudo update-rc.d documentation defaults

## Clean Up

* Shutdown your sinatra/thin daemon

```bash
$ gem list | grep fenton
fenton_daemon (0.0.1)
$ netstat -na | grep 2003
$ which fenton_daemon
/home/fenton/.rvm/gems/ruby-1.9.2-p290/bin/fenton_daemon
$ gem uninstall fenton_daemon
Remove executables:
	fenton_daemon
in addition to the gem? [Yn]  Y
Removing fenton_daemon
Successfully uninstalled fenton_daemon-0.0.1
$ gem list | grep fenton
$ which fenton_daemon
$ 
```

# Install - RedHat variants

As root

```bash
# yum install -y curl openssl-devel zlib-devel gcc gcc-c++ make autoconf readline-devel curl-devel expat-devel gettext-devel
```

As the user who will be using ruby:

```bash
$ wget https://raw.github.com/wayneeseguin/rvm/master/binscripts/rvm-installer
$ chmod a+x rvm-installer 
$ echo insecure >> ~/.curlrc
$ ./rvm-installer 
$ source /home/fenton/.bash_profile
$ rvm install 1.9.3
$ echo -e "\nrvm use 1.9.3" >> ~/.bash_profile
```

Log out completely (so .bash_profile gets executed) and log back in.

```bash
$ ruby --version
ruby 1.9.3p0 (2011-10-30 revision 33570) [i686-linux]
```

# Logging

[ref][rl intro]

```ruby
require 'logger'
@log = Logger.new('log.txt')
@log.level = Logger::DEBUG
@log.debug "This is a debug message"
@log.fatal "This is a nasty fatal message"
```

levels:

```
debug, info, warn, error, and fatal
```

## rotation

To enable log rotation, pass 'monthly', 'weekly', or 'daily' to the
Logger constructor. Optionally, you can pass a maximum file size and
number of files to to keep in rotation to the constructor.

```ruby
log = Logger.new( 'log.txt', 'daily' )
```

Once the log becomes at least one day old, it will be renamed and a
new log.txt file will be created.

## format

Adjusting log format:

[ref][rlf]

[ref2][rlf2]

```ruby
#!/usr/bin/env ruby -w

require "logger"

# Build a Logger::Formatter subclass.
class PrettyErrors < Logger::Formatter
  # Provide a call() method that returns the formatted message.
  def call(severity, time, program_name, message)
    if severity == "ERROR"
      datetime      = time.strftime("%Y-%m-%d %H:%M")
      print_message = "!!! #{String(message)} (#{datetime}) !!!"
      border        = "!" * print_message.length
      [border, print_message, border].join("\n") + "\n"
    else
      super
    end
  end
end

def expensive_error_report
  sleep 3
  "YOU BROKE IT!"
end

log           = Logger.new(STDOUT)
log.level     = Logger::INFO
log.formatter = PrettyErrors.new  # Install custom formatter!

log.debug("We're not in the verbose debug mode.")
log.info("We do see informative logs though.")
if log.error?
  log.error(expensive_error_report)
end
```

# Templating

```ruby
require 'erb'
class templater
  def do_template
    @template_data = {:email => 'fenton.travers@gmail.com' }
    # template = File.open("create_user1.erb", "r").read
    template = "<ns4:Address><%= @template_data[:email] %></ns4:Address>\n "
    renderer = ERB.new template
    data = renderer.result binding
  end
end
```
  
Often you'll want to include one erb inside another erb.  Here's how.

In real applications (such as CGI), I always write the method 
which build sub part in Ruby script. 

in body.erb:

```
I'm body.erb
<%= inc 'footer.erb' %>
```

in your ruby file:  `my.rb`

```ruby
def inc file
  erb = File.open(file, 'r').read
  erb.result binding
end

erb = import_erb 'body.erb'
erb.result binding
```

# Read a File

## All at once

```ruby
contents = File.open(File.join(read_dir, "style.css"), "rb"){|f| f.read}
```

## Line by line

```ruby
File.open("readfile.rb", "r") do |infile|
    while (line = infile.gets)
      puts line
    end
end
```

# Write File

```ruby
File.open('out.html', 'w'){|f| f.write data}
```

# Data Structures

## Hashes

```ruby
hash1 = {}
hash1['a'] = 2
hash2 = { 'a' => 2 }
p hash1['a'] == hash2['a']
```
Iterate through a hash

```ruby
map = {}
hash.each_pair do |k,v|
  map[k.upcase] = v
end
```

## Arrays

Can hold mixed types of values.

    array = [1, 2, 'three']

Iterate through an array

```ruby
array = [1, 2, 3]
p array[1] # => 2
array.each { |x| puts x }
```

# regex

```ruby
regex = /^\[group (.*)\]/
match = "[group fenton]"
if regex =~ candidate
 puts $1 # prints: fenton
end
```

# Option Parsing

[ref](http://ruby.about.com/od/advancedruby/a/optionparser.htm)

```ruby
require 'optparse'

options = {}
optparse = OptionParser.new do |opts|
  opts.on( '-h', '--help', 'Display this screen' ) do
    puts opts
    exit
  end
  opts.on( '-i', '--input_file FILE', 'The input file' ) do |file|
    options[:input_file] = file
  end
end
optparse.parse!
```

# Unit Testing 

## Assertions

```
assert
assert_block
assert_equal
assert_no_match
assert_not_equal
assert_not_nil
assert_not_same
assert_nothing_raised
assert_nothing_thrown
assert_raise
assert_respond_to
```

[ref](http://www.moseshohman.com/blog/2004/10/13/building-testunit-testsuites-in-ruby/)

## Test Case

Folder layout:

```
it_tools/
├── Gemfile
├── it_tools.gemspec
├── lib
│   └── it_tools
│       ├── deploy.rb
│       ├── it_tools.rb
│       ├── mail.rb
│       └── version.rb
├── Rakefile
└── test
    └── it_tools
        └── test_deploy.rb
```


Sample test: `test/it_tools/test_deploy.rb`

```ruby
require "test/unit"
require 'pathname'
libpath = Pathname.new(
    File.join(File.dirname(__FILE__), [".."]*2, "lib")
  ).cleanpath.to_s
$:.unshift(libpath) unless $:.include?(libpath)

require "it_tools/deploy"

class TestEnvironment < Test::Unit::TestCase
  def test_get_deploy_dir
    env = Deployment::Environment.new
    assert_equal('/scratch/ngsp/hrmsToCrmod', env.get_deploy_dir('dev'))
  end
end
```

This stuff:

```ruby
require 'pathname'
libpath = Pathname.new(
    File.join(File.dirname(__FILE__), [".."]*2, "lib")
  ).cleanpath.to_s
$:.unshift(libpath) unless $:.include?(libpath)
```

ensures that `lib` is on your load path, so you can test now with:

```bash
~/projects/it_tools $ ruby test/it_tools/test_deploy.rb 
```

## Test Suite

Layout:

```
it_tools/
├── Gemfile
├── it_tools.gemspec
├── lib
│   └── it_tools
│       ├── deploy.rb
│       ├── it_tools.rb
│       ├── mail.rb
│       └── version.rb
├── Rakefile
└── test
    └── it_tools
        ├── suite_it_tools.rb
        └── test_deploy.rb
```

file: `test/it_tools/suite_it_tools.rb

```ruby
require 'pathname'
testpath = Pathname.new(
    File.join(File.dirname(__FILE__), ".."*1)
  ).cleanpath.to_s
$:.unshift(testpath) unless $:.include?(testpath)

require 'it_tools/test_deploy'

te = TestDeployment::TestEnvironment.new("TestEnvironment")
te.test_get_deploy_dir
```

test with:

```bash
~/projects/it_tools $ ruby test/it_tools/suite_it_tools.rb
```

# rspec

# cucumber

[ref](http://ghouston.blogspot.com/2009/04/using-cucumber-testing-framework.html)

Cucumber is a framework for Ruby that allows you to do behaviour
driven development (BDD).  This lets the business interact very
closely with the developers.

## formatters

$ cucumber --format progress

## how to

```bash
$ gem install cucumber
$ mkdir -p features/{step_definitions,support}
$ cat features/addition.feature
Feature: Addition
  In order to avoid silly mistakes
  As a math idiot
  I want to be told the sum of two numbers
  
  Scenario: Add two numbers
    Given I have entered 50 into the calculator
    And I have entered 70 into the calculator
    When I press add
    Then the result should be 120 on the screen
$ cucumber
```

You'll see a lot of crud but copy the stuff after:

```
You can implement step definitions for undefined steps with these snippets:
```

into the file: features/step_definitions.rb, and update it so it looks
like: 

```
$ cat features/step_definitions/addition_steps.rb 
Before do
  @calculator = Calculator.new
end
Given /^I have entered (\d+) into the calculator$/ do |value|
  @calculator.enter value.to_i 
end
When /^I press add$/ do
  @calculator.add
end
Then /^the result should be (\d+) on the screen$/ do |value|
  @calculator.screen.should equal( value.to_i )
end
$ cucumber
```

You'll see the output has changed.  Now lets implement the
calculator. 

```
$ cat lib/it_tools/calc.rb
class Calculator
  def initialize
    @stack = []
  end
  def enter( value ) 
    @stack.push value
  end
  def screen
    @stack[0]
  end
  def add
    @stack.push( @stack.pop + @stack.pop )
  end
end
$ cucumber
```

With your final invocation of `cucumber` the features/steps should
have all passed.


[rlf]: http://rubydoc.info/github/TwP/logging/master/Logging/Layouts/Pattern
[rl intro]: http://ruby.about.com/od/tasks/a/logger.htm
[rlf2]: http://blog.grayproductions.net/articles/the_books_are_wrong_about_logger

# Sinatra, Rack, Capistrano, Nginx

Here are the values I'm using for each parameter, you should change
yours according to your needs:

Parameter                |  Example Value
------------------------ | ----------------------
nginx host               | arch
application name         | fentonGem1
git url                  | ft_git3@spicevan.com:fentonGem1.git
unix deployment user     | docs
unix deployment dir      | /data/apps/production

## Create App

Create app:

    bundle gem fentonGem1

## Git Repo

Setup a remote git repository.  Here my value will be:
`ft_git3@spicevan.com:fentonGem1.git`.  

You need this because you'll be pushing from your local machine to the
GIT server, and then when you deploy, the Nginx server will pull from
this GIT server.

## Sinatra App: lib/fentonGem1.rb

Add a dependency for sinatra into your `fentonGem1.gemspec` file,
like: 

```ruby
Gem::Specification.new do |gem|
...
  gem.add_runtime_dependency "sinatra"
...
end
```

Create a small sinatra app in file: `lib/fentonGem1.rb` like:

```ruby
...
require 'sinatra/base'
module FentonGem1
  class FirstSinatraApp < Sinatra::Base
    get "/" do
      "Hello World."
    end
  end
end
```

## Rack: config.ru

Create a rackup file: `config.ru`, with contents:

```ruby
require File.dirname(__FILE__) + '/lib/fentonGem1'
run Rack::URLMap.new "/" => FentonGem1::FirstSinatraApp
```

The first line sucks in the Sinatra file we edited above.  The second
line indicates what class represents the web root.  I think?

## Capistrano configuration: config/deploy.rb

Install capistrano (if required):

    gem install capistrano

Create a `config/deploy.rb` file, with the following contents:

```ruby
set :application, "fentonGem1"
set :scm, :git
set :git_enable_submodules, 1
set :repository, "ft_git3@spicevan.com:fentonGem1.git"
set :branch, "master"
set :ssh_options, { :forward_agent => true }
set :stage, :production
set :user, "docs"
set :use_sudo, false
set :runner, "docs"
set :deploy_to, "/data/apps/#{stage}/#{application}"
set :app_server, :passenger
set :domain, "arch"
role :app, domain
role :web, domain
role :db, domain, :primary => true
namespace :deploy do
  task :start, :roles => :app do
    run "touch #{current_release}/tmp/restart.txt"
  end
  task :stop, :roles => :app do
    # Do nothing.
  end
  desc "Restart Application"
  task :restart, :roles => :app do
    run "touch #{current_release}/tmp/restart.txt"
  end
end
```

Param          | Meaning
-------------- | ------------------------------------
application    | The name of your application
repository     | The git repository of this application
stage          | Is this development, staging, or production
user           | The user with which to log in to the deployment server with
deploy_to      | Where your application will get copied to
domain         | Should be the hostname of the server you are deploying to


## Capistrano wiring: Capfile

To enable capistrano commands have a `Capfile` in the project root,
with the following contents:

```
load 'deploy' if respond_to?(:namespace) # cap2 differentiator
load 'config/deploy'
```

## Capistrano deployment

Do the following once initially

    cap deploy:setup
    
Now each time you wish to deploy your app, commit/push everything to the
remote git repository and execute:

    cap deploy

## Gems on server

In order to ensure that all the gems are properly installed on the
server, log into the server and do the following:

```
# su - docs
$ cd /data/apps/production/fentonGem1/current
$ bundle
```
 
You'll only need to do this once, and it will download/compile all the
gems you may need for your application.

## Nginx: nginx.conf

In: `/opt/nginx/conf/nginx.conf`, have the following config:

```
user  docs users;
...
http {
...
     server {
        listen            80;
        server_name       arch;
        root              /data/apps/production/fentonGem1/current/public;
        passenger_enabled on;
    }
}
```

For each application you want to deploy to the server has a separate
`server` section.  So the following has two applications: `fenton6`
and `fentonGem2`:

```
    server {
        listen       80;
        server_name  fenton6;
        root /data/apps/production/fenton6/current/public;        
        passenger_enabled on;        
    }               
    server {
        listen 80;
        server_name arch;
        root /data/apps/production/fentonGem2/current/public;
        passenger_enabled on;
    }
```

Since these are on the same server you need the names: `fenton6` and
`fentonGem2` to both resolve to the IP address of the server.  Do this
in DNS for production scenarios, or for testing just update
`/etc/hosts` of the client you are testing from, eg: 

```
192.168.1.31     arch fenton6
```

## Test

Goto: `http://arch/`, or `http://fenton6/`.

# Rdoc - Comments

The following is a sample of how you might want to document your
functions in ruby.

```ruby
# 
# * *Args*    :
#   - +first_name+ -> The users first name
#   - +age+ -> The users age
# * *Returns* :
#   - +user_record+ -> A record of the user
# * *Raises* :
#   - ++ ->
#
def method_name

end
```

Generate ctags:

    ctags -e -a --Ruby-kinds=-fF -o TAGS -R .

keys             | Meaning
---------------- | ---------------------
A-.              | go to definition
A-*              | return from definition
M-. <tag> <RET>  | Search for a particular tag

# Slim

Render a partial:

file: `_thing.slim`

```
h2 = title
```

file `test.slim`

```
== render :slim, :'_thing', :locals => {:title => "This is a title"}
```

