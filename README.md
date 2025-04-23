A Spring Boot application for renting movies, backed by a MySQL database.  
Clone this repo, import the schema & data, then run the app.

To import the database we'll need to use the database dump file under the src/main/db folder

To run it, change the directory to your local moviesapp directory. 
From inside the folder, please run: mysql -u root -p < src/main/resources/db/main_dump.sql
If you have a password for MySQL, it will prompt you for the password that you have set up.
The dump includes CREATE DATABASE main; and USE main; so you don’t need to create it manually.

Alternatively if you are using MySQL Workbench you can import the DB by going into server-> Data Import. 

In import options choose import from self-contained-file

Click the "..." button and browse to: <path-to-your-clone>/moviesapp/src/main/resources/db/main_dump.sql
Select the “main” schema under Default Target Schema (or let it create it if you included the CREATE SCHEMA).

Click Start Import at the bottom right.

Workbench will run the entire dump (CREATE DATABASE, USE, CREATE TABLE, INSERTs) for you.

Once the database import finishes, start the Spring Boot service.
We used InteliJ to run the project files with Java   23.0.2 2025-01-21


