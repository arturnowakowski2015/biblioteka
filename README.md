# Biblioteka — Library Server

This README explains how to clone, build and run the Library server locally on Windows (cmd.exe), and how to import the project into common Java IDEs (Eclipse / IntelliJ).

Repository

Clone URL:
https://github.com/arturnowakowski2015/biblioteka.git

Prerequisites
- Java JDK installed (check required version in `pom.xml`, commonly Java 11 or 17). Set `JAVA_HOME` to your JDK installation.
- Maven (or use your IDE's built-in Maven). Verify with `mvn -v`.
- Git

Quick start (Windows, cmd.exe)

1. Clone the repository and change to the server folder:

    git clone https://github.com/arturnowakowski2015/biblioteka.git
    cd biblioteka\server

2. Build the project:

    mvn clean package

3. Run the application (if an executable JAR is produced):

    java -jar target\*.jar

Alternatively, if the project uses Spring Boot:

    mvn spring-boot:run

Run tests:

    mvn test

Import into IDEs

Eclipse
- File → Import → Existing Maven Projects → select the `server` folder (the one containing `pom.xml`).
- After import: right-click the project → Maven → Update Project...
- Locate `pl.biblioteka.Main` in `src/main/java` and run it as Java Application (or Spring Boot App if you have Spring Tools installed).

IntelliJ IDEA
- File → Open → select the `server` folder (IntelliJ will detect `pom.xml` and import it as a Maven project).
- Ensure Project SDK is set (File → Project Structure).
- Run `pl.biblioteka.Main` as an Application or use the Maven runner (`spring-boot:run`).

Configuration
- Application settings are in `src/main/resources/application.properties`.
- To change the server port without editing the file:

    java -jar target\app.jar --server.port=8081

or

    mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"

Troubleshooting
- Wrong Java version: check `pom.xml` for `maven.compiler.source` or `<java.version>`. Install matching JDK and set `JAVA_HOME`.
- Missing dependencies in IDE: run Maven Update (Eclipse) or Reload Maven Projects (IntelliJ).
- Port already in use: change `server.port` or stop the process using the port.
- If GitHub blocks a push due to secret scanning, remove the secret from history or use the repository's unblock flow and rotate any exposed tokens immediately.

Useful commands (Windows cmd.exe)

    java -version
    mvn -v
    git status
    mvn clean package
    mvn spring-boot:run
    java -jar target\*.jar
    mvn test

Contributing
- Create a branch per feature/bugfix and open a pull request against `main`.

License
- See the repository `LICENSE` file for details.
