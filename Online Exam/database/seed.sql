USE online_exam;

INSERT INTO users (name, email, password_hash, role) VALUES
('System Admin', 'admin@exam.com', 'admin123-demo-only', 'ADMIN'),
('Demo Student', 'student@exam.com', 'student123-demo-only', 'STUDENT');

INSERT INTO exams (title, duration_minutes, status, created_by) VALUES
('Java Fundamentals', 10, 'ACTIVE', 1),
('Web Technology Basics', 8, 'ACTIVE', 1);

INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_option) VALUES
('Which keyword is used to inherit a class in Java?', 'implements', 'extends', 'inherits', 'super', 'B'),
('Which method is the entry point of a Java program?', 'start()', 'main()', 'run()', 'init()', 'B'),
('Which language is used for styling web pages?', 'HTML', 'CSS', 'SQL', 'Java', 'B');

INSERT INTO exam_questions (exam_id, question_id) VALUES
(1, 1),
(1, 2),
(2, 3);

