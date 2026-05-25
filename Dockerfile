FROM maven:3.9.9-eclipse-temurin-11 AS build
WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn -q -DskipTests package

FROM eclipse-temurin:11-jre
WORKDIR /app

COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8087

# Startup script – profile is driven by SPRING_PROFILES_ACTIVE env var.
# Spring Boot auto-loads application.yml + application-{profile}.yml so no
# hard-coded config location is needed.
RUN echo '#!/bin/sh' > /app/startup.sh && \
    echo 'echo "[INFO] ============================================"' >> /app/startup.sh && \
    echo 'echo "[INFO] Sales Deep Dive ERP Backend Starting..."' >> /app/startup.sh && \
    echo 'echo "[INFO] Startup Time: $(date)"' >> /app/startup.sh && \
    echo 'echo "[INFO] Spring Profile: ${SPRING_PROFILES_ACTIVE:-docker}"' >> /app/startup.sh && \
    echo 'echo "[INFO] Database URL: ${ERP_DB_URL}"' >> /app/startup.sh && \
    echo 'echo "[INFO] ============================================"' >> /app/startup.sh && \
    echo 'exec java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-docker} -jar /app/app.jar' >> /app/startup.sh && \
    chmod +x /app/startup.sh

ENTRYPOINT ["/app/startup.sh"]
