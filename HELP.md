***
### Initial Setup :beginner:
Initial setup for Nikki is required to enable database to save your notes
and user data.
To do that, change to directory `nikki/` and run command:

```shell
java -jar nikki-1.0-SNAPSHOT.jar -init password
```
- where `password` is used for database access

> [!CAUTION]
> initial setup will create _nikki.properties_ file with sensitive information.