Dependencies:
$ java -version
java version "1.7.0_51"
Java(TM) SE Runtime Environment (build 1.7.0_51-b13)
Java HotSpot(TM) 64-Bit Server VM (build 24.51-b03, mixed mode)

$ mvn -version
Apache Maven 3.0.4 (r1232337; 2012-01-17 03:44:56-0500)
Maven home: /Users/foo/tools/maven-3.0.4
Java version: 1.7.0_51, vendor: Oracle Corporation
Java home: /Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre
Default locale: en_US, platform encoding: UTF-8

HOW TO RUN
-- mvn clean compile exec:java -- to run server
-- java -cp src/local-libs/conductor/chess-core/0.9.1/chess-core-0.9.1.jar chess.CLI -- to run .jar file
-- CHESS UI static page : http://localhost:8080/chess
-- API to talk to the Chess Engine is available here: http://localhost:8080/chess/api

-- GAME STATE ENDPOINT :  http://localhost:8080/api/chess

####GET http://localhost:8080/api/chess Issuing an HTTP GET request to /api/chess will retrieve the current state of the game as a JSON object.
 
####POST http://localhost:8080/api/chess Issuing a POST to this endpoint will create a brand new game, resetting the pieces back to their original starting positions. Any content provided in the body of the request is ignored. This is the only way to restart the game, apart from restarting the server.

####PUT http://localhost:8080/api/chess Issuing a PUT request to this endpoint will alter the state of the game. The body of the PUT request should match that returned from the GET request. However, only the positionToPieces element will be interpreted; everything else is ignored.


Link git-hub: https://github.com/Conductor/chess-web
