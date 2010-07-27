drop table factoids;
CREATE TABLE factoids (
       id INTEGER NOT NULL CONSTRAINT PK_FACTOIDS PRIMARY KEY AUTOINCREMENT
     , trigger TEXT NOT NULL
     , response TEXT NOT NULL
     , submitted_by TEXT
     , action TEXT
);

