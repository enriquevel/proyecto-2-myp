#!/bin/bash

# Start Xvfb (virtual display)
/usr/bin/Xvfb :1 -screen 0 1920x1080x16 &
export DISPLAY=:1

# Start a lightweight window manager
openbox &

# Start VNC server
x11vnc -display :1 -nopw -forever -shared &
sleep 2

# Now run JavaFX app with your CLI parameters
java \
  --module-path $JAVAFX_HOME/lib \
  --add-modules javafx.controls \
  -cp "bin:lib/json-20250517.jar" \
  myp.proyecto2.Proyecto2 \
  "$@"
