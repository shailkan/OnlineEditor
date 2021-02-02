CREATE TABLE IF NOT EXISTS data(
                                   contentId SERIAL PRIMARY KEY,
                                   dateCreated DATE,
                                   catId SERIAL REFERENCES category(catId),
                                   title TEXT,
                                   content TEXT,
                                   source TEXT,
                                   status INT,
                                   sessionId TEXT
);