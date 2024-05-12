@echo off

docker load < telegram-bot.tar

docker run -d --name telegram-bot --env-file bot.env telegram-bot

