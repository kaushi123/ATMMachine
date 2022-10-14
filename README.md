## General info:
    This is an API system which used to get the details of ATM Machine functionalities. This project contains springboot, swagger ui and H2 database, etc. 

## Requirement: 
    -	should initialize with the following accounts:
            Account Number	PIN	Opening Balance	Overdraft
            123456789		1234	800			200
            987654321		4321	1230		150
    -	should initialize with €1500 made up of 10 x €50s, 30 x €20s, 30 x €10s and 20 x €5s
    -	should not dispense funds if the pin is incorrect,
    -	cannot dispense more money than it holds,
    -	cannot dispense more funds than customer have access to
    -	should not expose the customer balance if the pin is incorrect,
    -	should only dispense the exact amounts requested,
    -	should dispense the minimum number of notes per withdrawal

## Technologies:
    -Spring Boot
    -Spring JPA
    -Spring Security
    -H2 Database
    -JUnit
    -Mokito
    
## Database Configurations -
    - http://localhost:8081/h2-console/
    - Database Name - bankdb
    - Username - sa
    
## Swagger API documentation:
    - http://localhost:8081/swagger-ui.html

## Running locally:
Open in your favorite IDE, import dependencies with Maven, build, and run
