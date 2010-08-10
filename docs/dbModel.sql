CREATE TABLE deepthought.users (
       id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT
     , nick TEXT NOT NULL
     , usermask TEXT
     , lastmessage TEXT
     , lastseen INTEGER DEFAULT 0
     , karma INTEGER DEFAULT 0
);

CREATE TABLE deepthought.inventory (
       id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT
     , item TEXT NOT NULL
     , channel TEXT NOT NULL
);

CREATE TABLE deepthought.factoids (
       id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT
     , `trigger` TEXT NOT NULL
     , `response` TEXT NOT NULL
     , `submitted_by` INTEGER NOT NULL
     , `action` TEXT
);

CREATE TABLE deepthought.ignored_users (
       id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT
     , user INTEGER NOT NULL
     
);

