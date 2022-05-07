SELECT Staff.Name_Forename, Staff.Name_Surname
FROM Course NATURAL JOIN Course_TaughtBy_Lecturer NATURAL JOIN Lecturer NATURAL JOIN Staff
WHERE Course.Title = 'CS-1Q' AND Lecturer.StaffID = Staff.StaffNo AND JobTitle = 'professor' AND Course.CourseID = Course_TaughtBy_Lecturer.Course_fk AND Course_TaughtBy_Lecturer.Lecturer_fk = Lecturer.StaffID