@echo off
SETLOCAL

call mvn clean package
docker build -t telegram-bot .
docker save telegram-bot > telegram-bot.tar

set SERVER_USER=
set SERVER_IP=
set SERVER_PATH=
scp telegram-bot.tar bot.env %SERVER_USER%@%SERVER_IP%:%SERVER_PATH%

ENDLOCAL
