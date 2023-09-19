#
# Build stage
#
FROM maven:3-amazoncorretto-17 AS build
RUN ls -l /tmp/
WORKDIR /home/convy

COPY pom.xml ./
COPY src ./src

#Install maven deps
RUN mvn -f ./pom.xml clean package

#
# Package stage
#
FROM amazoncorretto:17-alpine
COPY --from=build /home/convy/target/leopold-0.0.1-SNAPSHOT.jar /usr/local/lib/
COPY --from=build application.properties /usr/local/lib/
#COPY --from=build /tmp/env.properties /usr/local/lib/
# Expose the port your application will run on
EXPOSE 80
CMD ["java", "-jar", "/usr/local/lib/leopold-0.0.1-SNAPSHOT.jar"]
