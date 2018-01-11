@echo off

cmd /c gradlew --info clean utilVersionClass build release
pause