from sqlalchemy import *
from sqlalchemy import create_engine
from sqlalchemy import Table, Column, Integer, String, MetaData, ForeignKey
from sqlalchemy.orm import mapper
from sqlalchemy.orm import sessionmaker

db = create_engine('sqlite:///:memory:', echo=True)
# db = create_engine('postgresql://pyr1-user:welcome1@localhost/pyr1-db', echo=True)

metadata = BoundMetaData(db)

metadata = MetaData()

users_table = Table('users', metadata,
                    Column('id', Integer, primary_key=True),
                    Column('name', String),
                    Column('fullname', String),
                    Column('password', String)
)

groups_table = Table('groups', metadata,
                     Column('id',Integer, primary_key=True),
                     Column('name', String),
                     Column('user_id', Integer, ForeignKey('users.id'))

metadata.create_all(engine)

class User(object):
    def __init__(self, name, fullname, password):
        self.name = name
        self.fullname = fullname
        self.password = password
    def __repr__(self):
        return "<User('%s','%s', '%s')>" % (self.name, self.fullname, self.password)

mapper(User, users_table)

ed_user = User('ed', 'Ed Jones', 'edspassword')
ed_user.name # 'ed'
ed_user.password # 'edspassword'
str(ed_user.id) # 'None'

Session = sessionmaker(bind=engine)

session = Session()

session.add(ed_user)

our_user = session.query(User).filter_by(name='ed').first()

ed_user is our_user # True

session.add_all([
    User('wendy', 'Wendy Williams', 'foobar'),
    User('mary', 'Mary Contrary', 'xxg527'),
    User('fred', 'Fred Flinstone', 'blah')])

ed_user.password = 'f8s7ccs'

session.dirty # IdentitySet([<User('ed','Ed Jones', 'f8s7ccs')>])

session.new # IdentitySet([<User('wendy','Wendy Williams', 'foobar')>,
            # <User('mary','Mary Contrary', 'xxg527')>,
            # <User('fred','Fred Flinstone', 'blah')>])

session.commit()

ed_user.id # 1

