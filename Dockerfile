# Build stage
#FROM openjdk:8-jdk-alpine AS builder
FROM openjdk:19-jdk-alpine3.16 AS builder

# Set the working directory to /app
WORKDIR /app

# Install curl and other dependencies
RUN apk add --no-cache curl bash

# Install sbt
RUN echo "Installing sbt" && \
    curl -L -o sbt-1.5.5.tgz https://github.com/sbt/sbt/releases/download/v1.5.5/sbt-1.5.5.tgz && \
    tar -xzf sbt-1.5.5.tgz -C /usr/local && \
    rm sbt-1.5.5.tgz && \
    ln -s /usr/local/sbt/bin/sbt /usr/local/bin/

# Copy the build.sbt and project folder into the container
COPY build.sbt ./
COPY project ./project

# Copy the privateKey and publicKey files
COPY src/main/resources/security/asymmetric_encryption/keys/privateKey.txt ./src/main/resources/security/asymmetric_encryption/keys/
COPY src/main/resources/security/asymmetric_encryption/keys/publicKey.txt ./src/main/resources/security/asymmetric_encryption/keys/

# Copy the rest of the application code
COPY . .

# Build the application
RUN sbt clean update compile stage

# Runtime stage
FROM openjdk:8u212-jre-alpine3.9

# Install bash
RUN apk add --no-cache bash

# Set the PostgreSQL host and port
#ENV POSTGRES_HOST=twinkler_db
#ENV POSTGRES_PORT=5432

# Copy the built application from the build stage
COPY --from=builder /app/target/universal/stage /app

# Copy the key files from the build stage
COPY --from=builder /app/src/main/resources/security/asymmetric_encryption/keys/ /app/src/main/resources/security/asymmetric_encryption/keys/

# Set the working directory to /app
WORKDIR /app

# Expose the application's port
#EXPOSE 8090

# Define the entrypoint script
RUN chmod +x /app/bin/twinkler

ENTRYPOINT ["/app/bin/twinkler"]

# Provide default arguments to the entrypoint script
CMD []
