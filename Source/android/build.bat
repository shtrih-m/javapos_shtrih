@echo off

cmd /c gradlew clean utilVersionClass uploadArchives dist
pause