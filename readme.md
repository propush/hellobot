# Hello Bot

### Description

Hello Bot is a simple telegram bot that allows its admins to communicate
with their users indirectly, by sending messages to the bot.

So the user adds a bot, begins to send text messages, the bot resends them to
admins. Any of the admins can reply the bot, the replies would be sent back 
to the user.

So the user does not see the admin contact.

### Common use cases

Site admins, project owners, shop workers and so on willing to communicate
anonymously with their customers.

### Building and running

To build run<br>
`./gradlew bootJar`<br>
and the artifact (fat jar) will be located in the `build/libs` folder. It
can be run with `java -jar build/libs/hello-bot-x.x.x-SNAPSHOT.jar`.

There are environment variables available that can be set to configure the
bot.

| Variable | Description |
| -------- | ----------- |
| `BOT_TOKEN` | Your bot token. Please contact the official Telegram https://t.me/BotFather to register your own bot and get the token. |
| `PERSIST_PATH` | Directory to store admin settings. Please redefine for your own. |
| `BOT_ADMINS_0`, `BOT_ADMINS_1` and so on | A list of telegram usernames (without `@`) each of which defines the bot admin. |

After starting the bot for the first time it needs to get acquainted with 
all the admins. Every admin who wants to interact, should send a `/start`
message to the bot. If the bot replies with `Hello, admin!`, then
congratulations!

### Limitations

 * Bot admin ids are stored in file system, the path is provided via
`PERSIST_PATH` environment variable. The default is `/tmp/hello-bot` so
it is recommended to change it.
 * Only text messages are supported.

### Author

Telegram: [@pushkin_kukushkin](https://t.me/pushkin_kukushkin)
<br>
Please feel free to contact me. You can also submit a PR :)
