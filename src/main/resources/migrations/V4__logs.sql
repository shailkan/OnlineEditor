CREATE TABLE IF NOT EXISTS logs(
                                   logId SERIAL PRIMARY KEY,
                                   contentId SERIAL REFERENCES data(contentId),
                                   empId INT REFERENCES employee(empId),
                                   action TEXT,
                                   timeOfAction TIMESTAMP
);