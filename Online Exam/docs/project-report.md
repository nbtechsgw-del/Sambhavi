# Online Examination System Project Report

## Introduction

The Online Examination System is a web-based application designed to automate examination activities for educational institutions. It helps admins create exams, manage questions, set time limits, and review student performance. Students can register, log in, attempt exams, and receive instant results.

## Problem Statement

Traditional examination systems require manual question paper preparation, invigilation records, answer checking, result calculation, and paperwork. This process is time-consuming, error-prone, and difficult to scale. The proposed system reduces manual effort and improves result accuracy by digitizing the complete examination workflow.

## Objectives

- Automate exam creation and assessment.
- Reduce paperwork and manual result calculation.
- Provide instant score and feedback after submission.
- Improve security using authentication and controlled exam timing.
- Maintain student, exam, question, and result records efficiently.

## Modules

## Admin Module

The admin module allows authorized staff to manage the examination process.

- Add, update, and delete questions.
- Create exams and assign questions.
- Set exam duration.
- View student scores and submission details.

## Student Module

The student module provides exam access to registered users.

- Register and log in.
- View available exams.
- Attempt questions within the time limit.
- Submit answers and view instant results.

## Technology Stack

- HTML, CSS, and JavaScript for the user interface.
- Java Servlets for backend request handling.
- MySQL for persistent data storage.
- Web browser as the platform.

## Database Design

Main entities:

- users
- exams
- questions
- exam_questions
- attempts
- attempt_answers

## Security Features

- Login-based access control.
- Separate admin and student roles.
- Time-limited exams.
- One attempt record per submission.
- Server-side validation in the full backend implementation.

## Conclusion

The Online Examination System provides an efficient and scalable way to conduct exams online. It reduces administrative workload, improves result accuracy, and creates a smoother experience for students and admins.

