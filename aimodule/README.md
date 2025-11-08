# What it is

# What database table it has

* For what copied from main database, only support for reading 
- Accounts: no authencation support, just for indexing to determine what role is
accounts id, role
- Customers: full with account foreign key
- Vendors: full with account foreign key
- Products: full product with vendor flatten (to skip the vendor product), with proudct id, vendor id, porudct-vendor id
- Categories: same
- Product category: same
- Order: same, but also save a list of bought vendor-product id

* Full CRUD for ai chat support
- Accounts: use the same table as above
- Chat session: account id, session id, session name
- Chat history: session id, message, role: [user, system]

# What bussiness logic does it have?
Write things
- Register account, customer, vendor
- Create udpate delete products.
- Create update delete caegories and their products
- Write and save orders
- Save counts of bought products, vendors
- Save counts of read products, vendors 
- Save counts of category a user boughts

Read stuffs
- Read product vendors most popular
- Read wht inside a order (usually cart)
- 