## JAVA Dependencies:
$ java -version
java version "1.7.0_51"
Java(TM) SE Runtime Environment (build 1.7.0_51-b13)
Java HotSpot(TM) 64-Bit Server VM (build 24.51-b03, mixed mode)

## Maven Dependencies:
Apache Maven 3.0.4 (r1232337; 2012-01-17 03:44:56-0500)
Maven home: /Users/foo/tools/maven-3.0.4
Java version: 1.7.0_51, vendor: Oracle Corporation
Java home: /Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre
Default locale: en_US, platform encoding: UTF-8

## How to run:
- mvn clean install - to build
- mvn exec:java@server -- to run server
- mvn exec:java@CLI -- to run GameTracker(CLI - Chess Layout Interface)
- mvn exec:java@Fen -- to run simulated producer.
- mvn exec:java@GetGame -- to run WebClientGetGame.
- mvn exec:java@GetMoves -- to run WebClientGetMoves.
- mvn exec:java@PostGame -- to run WebClientPostGame.
- mvn exec:java@PostMoves -- to run webClientPostMoves.
- mvn exec:java@PutGame -- to run webClientPutGame.
- java -cp src/local-libs/conductor/chess-core/0.9.1/chess-core-0.9.1.jar chess.CLI -- to run .jar file
- CHESS UI static page : http://localhost:8080/chess
- API to talk to the Chess Engine is available here: http://localhost:8080/chess/api

- GAME STATE ENDPOINT :  http://localhost:8080/api/chess

