FROM gradle:8.12-jdk21 AS build
WORKDIR /workspace
COPY . .
RUN gradle :application:installDist --no-daemon -x test

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /workspace/application/build/install/application /app
EXPOSE 8080
ENTRYPOINT ["/app/bin/application"]
