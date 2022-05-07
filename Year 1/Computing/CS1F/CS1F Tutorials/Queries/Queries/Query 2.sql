SELECT ExamMark
FROM Student_Takes_Course, Course
WHERE Title = 'CS-1Q' AND Student_Takes_Course.Course_fk = Course.CourseID