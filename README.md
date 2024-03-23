## Telegram bot gpt

### Запуск
1. Указать идентификаторы пользователей telegram в свойстве `application.users-white-list`
2. Указать токен telegram в свойстве `application.telegram.bot-token`
3. Указать токен gpt в свойстве `application.gpt.token`

Для локальной разработки для доступа к gpt можно указать настойки proxy
```
-Dhttps.proxyHost=189.240.60.164
-Dhttps.proxyPort=9090
```
