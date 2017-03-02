@echo off

cmd /c gradlew clean build uploadArchives -i
pause