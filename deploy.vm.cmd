@echo off

docker load < telegram-bot.tar

docker run -d --name telegram-bot --env-file bot.env -v /artibotapp/logs:/logs telegram-bot
