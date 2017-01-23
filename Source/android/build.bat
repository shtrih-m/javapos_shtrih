@echo off

cmd /c gradlew clean utilVersionClass assemble test uploadArchives dist
pause