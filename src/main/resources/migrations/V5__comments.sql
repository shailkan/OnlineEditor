CREATE TABLE IF NOT EXISTS comments(
                                       commentId SERIAL PRIMARY KEY,
                                       commentText TEXT,
                                       timeOfComment TIMESTAMP,
                                       contentId SERIAL REFERENCES data(contentId),
                                       empId INT REFERENCES employee(empId)
);