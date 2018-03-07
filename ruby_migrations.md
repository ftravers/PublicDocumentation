Kind of mimicking rails.

Migration version level is kept in a table:

    mysql> select * from schema_migrations;

in file: `config/database.yml` put:

```yml
adapter: mysql2
database: ad_net
username: ad
password:
host: localhost
```

here, of course, use your own username, password and database.

in file: `db/migrate/001_books.rb` put:

```ruby
class Books < ActiveRecord::Migration
  def self.up
     create_table :books do |t|
	t.column :title, :string, :limit => 32, :null => false
	t.column :price, :float
	t.column :subject_id, :integer
	t.column :description, :text
	t.column :created_at, :timestamp
     end
  end

  def self.down
    drop_table :books
  end
end
```

in file: `Rakefile` put:

```
require 'active_record'
require 'yaml'
require 'logger'
task :default => :migrate
desc "Migrate the database through scripts in db/migrate. Target specific version with VERSION=x"
task :migrate => :environment do
  ActiveRecord::Migrator.migrate('db/migrate', ENV["VERSION"] ? ENV["VERSION"].to_i : nil )
end
task :environment do
  ActiveRecord::Base.establish_connection(YAML::load(File.open('config/database.yml')))
  ActiveRecord::Base.logger = Logger.new(File.open('database.log', 'a'))
end
```


Moving Between Versions

At this stage we are at at version 1 of the database. But how do we go
down to say version 0, before everything existed?

    $ rake VERSION=0

What? You only want to go to version 1?

    $ rake VERSION=1
