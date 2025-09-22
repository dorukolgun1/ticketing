# ---------- build stage ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /src

# Maven cache'i katmanlamak için mount kullan (BuildKit varsayılan)
# 1) önce pom kopyala ve plugin/dep çöz
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -e -DskipTests dependency:resolve dependency:resolve-plugins

# 2) kaynak kodu kopyala ve paketle
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -e -DskipTests clean package

# ---------- runtime stage ----------
FROM eclipse-temurin:21-jre
ENV JAVA_TOOL_OPTIONS="-XX:+UseZGC -XX:MaxRAMPercentage=75"
WORKDIR /app
COPY --from=build /src/target/ticketing.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]