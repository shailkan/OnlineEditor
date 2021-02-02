CREATE TABLE IF NOT EXISTS image(
                                    imgId SERIAL PRIMARY KEY,
                                    contentId INT REFERENCES data(contentId),
                                    name TEXT,
                                    description TEXT,
                                    img bytea
);