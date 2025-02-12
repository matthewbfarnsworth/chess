# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Server Design
[![Server Design](server-design.png)](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIdTUWjXZfF5vD6rGAHYsgoU+Ylj0r7lqOH5PF+Swfn+mDAYBRRqig5QIAePKwvuh6ouisTYgmhgumGbpkhSBq0uBowmkS4YWhyMDcryBqCsKMBvmIrrSkml7IeUzHaI6zoEsRLLlJ63pBgGQYhpx5qRvR0YwLGwYCQRnYIeUWE8tmubwecBbcUhJRXGBI7vp8NZBrOzafHBAGdtkPYwP2g69KZtwQRZU5WY2Nm-kunCrt4fiBF4KDoHuB6+Mwx7pJkmCOReRnXtIACiu6pfUqXNC0D6qE+3TTtASAAF7xIk5QADzTtZaD5O2iEaTARVQKV5VoFVNW+XVelAh2sq8TAaH2NFmFRb6OEYvhcqEcJNEkTA5JgMpUl1t11FMm6dHlIxMbSdoQphF1c6hvNXEDU68pKft8iCYmc2baJHAoNwmQrdOMkiXJhSWjIL0UoYynxjN6n6SWWHRTpCB5mDIIXSBdlXnDhSJWAfYDi+AUrp4wUbpCtq7tCMAAOKjqysWngl57MPDaYVMTmU5fYo6FUGxVlQQFUwNVPlzvV9nJrD5QtW1nMddzx3oPVCFsgRqHQqToyqJhCtk5NeF3bNMhfe6i0Uu9vPoBtZoRj99G7ddcYHaxktoKdj1yRd4KWypt1qURZ2kkNqtK7Cxu0fJO08rawDKiTo4OrJEZO1d9MR9ojSa6DfVpoTsSK2oUMwynnYASZfTM0r4yVP0hcoAAktIxcAIy9gAzAALE8J6ZAaFYWd0fQ6AgoANm3nnVlMZcAHKjv8zT-kjDnU+jrmgQXZPFxUpejpXNf103Uwt-qlH3NWnfd73-fmYPC+jKPozj3By5BeugTYD4UDYNw8C6pk4ejCkcVnjkNM8cZlRagNCZizYIbNWocyfJ1Q2dUXxnxQBfe4rZJiT2oP1ICQtmrgNFlAiWMD8hwJHp5ZBcEZYxxQjAcSmQM6wjgG-FAGd1ZYiTh7B2usloGzWnOf2W1A4MWDi7FiR0YH2xNrnGa5QgaqRBqwk2Yl6E0KIVRURAczaSJtB-FAwNkLJ1TOUOhXpqGjiYb1VM4i0FpnnmXNe5Ra6N0RhY6ev9Z6ENXlXWxG9r6BRxnfAIlgXpoWSDAAAUhAHkmjAiHxAA2Kmv9ZZI3KNUSkd4Whl1Zn6dm7VoFcKlnA5+wB-FQDgBANCUBi59AAOosHLllFoAAhXcCg4AAGkvjWPcTAOxDcHHHEFinYW2DIFcx5jk2Bblng90KcU0p5Sqk1NSvUxpLS2luPXvYgEsN4mDQAFZhLQDQ0JPJGEoDRFNFhD05F62WpJW2PCuJqP4byKR8hDpYNGSo3h5DJE3WAOc7Wnt2EUkUW4u5312RB15BnIRi03EfPOv-Z2ULpE6NkeGcofgtBGNGLCdpsx2KgtNuCvk2BMWGG3sfQw0AhQ92iRSmAEAABmmjtGXUIn0vRIS9nHJzNDUxBlaYmVQb004qMXFuSxrfEKAQvAFK7F6WAwBsDP0IO1L+lNUZbIARUNKGUso5WMA1NkTU+XoKMs7EA3A8AKCVcgEA7U-Z-KjmJS1UBnnAAdVHbaf1XqGFDggQRyLWUah1s6+V-F5Aep1l66Q-135+rtAKQNQl-lsNDXgJFEaCXRtjb6sOGbfmesMnLFh7LgT6Jdda5VdqxZZxNcjZKlien9RFTPZyGNxVmECkAA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
