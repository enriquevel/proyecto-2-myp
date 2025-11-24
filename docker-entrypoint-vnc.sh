#!/bin/bash

/usr/bin/Xvfb :1 -screen 0 1920x1080x16 &
export DISPLAY=:1

openbox &

x11vnc -display :1 -nopw -forever -shared &
sleep 2

java \
  --module-path $JAVAFX_HOME/lib \
  --add-modules javafx.controls \
  -cp "bin:lib/json-20250517.jar" \
  myp.proyecto2.Proyecto2 \
  "$@"
