# Kotlin Ecosystem LMS course task

To run the server and postgres database using docker run `docker-compose up`.
Server will run at localhost:8080, postgres at localhost:5432.

You can populate DB initially using [BasicData](/server/src/main/kotlin/BasicData.kt) as the starting point.

## Task

Create an <u>Android app</u> client (25%) and <u>Desktop or Web</u> client (25%) to view courses and corresponding 
assignments / tasks; submit solutions / answers. They have to use data classes from the `shared` module, so go with 
either full Compose or Kotlin/JS.

Implement option to add new courses, assignments, tasks and course materials. (25%)

Implement authorization (Google OAUTH) to differentiate between teachers and students. (25%)
Students should be able to see all courses, enroll into them and submit solutions / answers.
Teachers should be able to add / edit courses, view enrollments and see submissions.