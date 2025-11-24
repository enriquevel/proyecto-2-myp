FROM eclipse-temurin:24-jdk

# Install VNC + JavaFX dependencies
RUN apt-get update && apt-get install -y \
    wget unzip x11vnc xvfb openbox \
    libgtk-3-0 libgl1 libxrender1 \
    libxtst6 libxi6 tigervnc-standalone-server \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Install JavaFX SDK
RUN wget https://download2.gluonhq.com/openjfx/24/openjfx-24_linux-x64_bin-sdk.zip \
    && unzip openjfx-24_linux-x64_bin-sdk.zip \
    && rm openjfx-24_linux-x64_bin-sdk.zip \
    && mv javafx-sdk-24 /opt/javafx-sdk-24

ENV JAVAFX_HOME=/opt/javafx-sdk-24

# Copy project
COPY src/ /app/src/
COPY lib/ /app/lib/
COPY data/ /app/data/

# Create bin dir
RUN mkdir -p /app/bin

# COMPILE your Java code exactly like your local commands
RUN javac \
    --module-path $JAVAFX_HOME/lib \
    --add-modules javafx.controls \
    -cp "bin:lib/json-20250517.jar" \
    -d bin \
    $(find src/main/java -name "*.java")

# VNC password
RUN mkdir -p ~/.vnc && \
    echo "password" | vncpasswd -f > ~/.vnc/passwd && \
    chmod 600 ~/.vnc/passwd

# Entrypoint
COPY docker-entrypoint-vnc.sh /app/docker-entrypoint-vnc.sh
RUN chmod +x /app/docker-entrypoint-vnc.sh

EXPOSE 5900

# DEFAULT arguments (can be overridden later)
ENTRYPOINT ["/app/docker-entrypoint-vnc.sh"]
CMD ["--provider=google", "--apikey=YOUR_KEY_HERE"]
