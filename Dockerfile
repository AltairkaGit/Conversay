#
# Build stage
#
FROM maven:3-amazoncorretto-17 AS build
WORKDIR /home/convy

COPY pom.xml ./
COPY src ./src

#Install maven deps
RUN mvn -f ./pom.xml clean package

#
# Package stage
#
FROM amazoncorretto:17-alpine
COPY --from=build /home/convy/target/leopold-0.0.1-SNAPSHOT.jar /home/
COPY --from=build /home/convy/src/main/resources /home/
RUN chmod +x /home/leopold-0.0.1-SNAPSHOT.jar
# Expose the port your application will run on
EXPOSE 80
CMD ["java", "-jar", "/home/leopold-0.0.1-SNAPSHOT.jar"]
