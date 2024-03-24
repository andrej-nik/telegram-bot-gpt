## Telegram bot gpt

### Запуск
1. Указать идентификаторы пользователей telegram в свойстве `application.users-white-list`
2. Указать токен telegram в свойстве `application.telegram.bot-token`
3. Указать токен gpt в свойстве `application.gpt.token`

При локальной разработке для доступа к gpt можно указать параметры JVM proxy
```
-Dhttps.proxyHost=...
-Dhttps.proxyPort=...
```
