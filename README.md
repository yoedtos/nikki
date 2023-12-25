# Nikki :blue_book:
A simple diary note with access control made with JavaFX and Nitrite NoSql.

This experiment use Clean Architecture principles
and were inspired by [theWisePad](https://github.com/otaviolemos/thewisepad-core)

> [!IMPORTANT]
> To make nikki's database persistent, initial setup is required.

[Initial setup instruction](HELP.md) :point_left:
***

This application require java 11. To easily set it, use:
[sdkman](https://sdkman.io/)

#### Update project
`mvn dependency:resolve`

#### Unit Test
`mvn test`

#### Integration Test
`mvn integration-test`

#### Run the project
`mvn javafx:run`

#### Check and create jar package
`mvn verify`

to run test without gui just add `-Dheadless`

***

#### Screenshots

Entry

![gui main](images/entry.png?raw=true)

Create

![gui create](images/create.png?raw=true)

List and View

![gui list](images/list.png?raw=true)