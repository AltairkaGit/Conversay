FROM amazoncorretto:17-alpine
COPY target /convy-app
WORKDIR /convy-app
