Lets look at where we have a user model and we have three (or more)
different user types: Advertisers, Publishers, and Administrators.







# Business Domain

I don't like this example.  Although technically correct, it's very
confusing... the `belongs_to` should maybe be `has_one`, and the
images shouldn't have any reference to the ads probably.

Ad's have an image.  But one image may belong to many ads.

```ruby
class Ad < ActiveRecord::Base
  belongs_to :image
end  
class Image < ActiveRecord::Base
  has_many :ads
end
```

here is the right create sql for it:

```sql
CREATE TABLE ad_net.ads (
  id int not null auto_increment PRIMARY KEY, 
  text VARCHAR(255),
  image_id INT,
  adv_url VARCHAR(255)
);

CREATE TABLE ad_net.images (
  id int not null auto_increment PRIMARY KEY, 
  orig_name VARCHAR(255),
  unique_name VARCHAR(40)
);
```

some ruby that makes use of that:

```ruby
image = Image.create( :image_id => 2, :orig_name => "girl.jpg", :unique_name => "0000001.jpg" )
ad = Ad.create(:ad_id => 1, :text => "buy some stuff", :adv_url => "http://yahoo.com")

ad.image = image
ad.save

puts ad.inspect
```

# Business Domain

## Example 1

Now we have Advertisers who may have many ads.  So each ad, should
have an Advertiser id.

```sql
CREATE TABLE ad_net.ads (
  ...
  advertiser_id INT,
  ...
);
```

So ruby should be:

```ruby
class Ad < ActiveRecord::Base
  belongs_to :advertiser
end  
class Advertiser < ActiveRecord::Base
  has_many :ads
end
```

## Example 2

I often have a user table where I keep email(login), password, etc.  I
then have other tables for the type of person, like administrator,
manager, etc.

In this case I have an Advertiser, who is also, of course, a user of
the sytem.  I also have Publishers.  In this case we need to use
polymorphic relationship.

First lets get three migrations, one for each table:

```
$ rails g migration create_users_table
$ rails g migration create_advertisers_table
$ rails g migration create_publishers_table
```

This generates three blank migrations in: `db/migrate/`.

Lets fill the users migration out:

```ruby
class CreateUsers < ActiveRecord::Migration
  def change
    create_table :users do |t|
      t.string :name
      t.string :email
      t.timestamps
    end
  end
end
```


So we say, and Advertiser `has_one` User.

```ruby
class Advertiser < ActiveRecord::Base



--------------

working example:

    app/model/user.rb

```ruby
class User < ActiveRecord::Base
  belongs_to :role, :polymorphic => true
  attr_accessible :email
end
```

    app/model/publisher.rb

```ruby
class Publisher < ActiveRecord::Base
  has_one :user, :as => :role
  attr_accessible :company_name
end
```

    app/model/advertiser.rb

```ruby
class Advertiser < ActiveRecord::Base
  has_one :user, :as => :role
  attr_accessible :account_balance
end
```

    db/migrate/[random_int]_create_users_table.rb

```ruby
class CreateUsersTable < ActiveRecord::Migration
  def change
    create_table :users do |t|
      t.string :email
      t.timestamps
      t.integer :role_id
      t.string :role_type
    end
  end
end
```

    db/migrate/[random_int]_create_advertisers_table.rb

```ruby
class CreateAdvertisersTable < ActiveRecord::Migration
  def change
    create_table :advertisers do |t|
      t.decimal :account_balance
      t.timestamps
    end
  end
end
```

    db/migrate/[random_int]_create_publishers_table.rb

```ruby
class CreatePublishersTable < ActiveRecord::Migration
  def change
    create_table :publishers do |t|
      t.string :company_name
      t.timestamps
    end
  end
end
```
