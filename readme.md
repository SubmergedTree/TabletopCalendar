# Tabletop Calendar 

To plan tabletop sessions.
This app consists out of two parts:
- Webservice (Spring Boot, Kotlin)
- Webapp (Elm)


## Webservice
...

## Webapp
The webapp is served by the spring service.
Therefore, the compiled webapp must be copied to resources folder.
The process is automated by executing a shell script on mvn compile step.
 
Command to compile and copy:
```
elm make src/Main.elm --output=../src/main/resources/static/built/main.js
```

Dev Server:
```
elm-live src/Main.elm --open --pushstate --start-page=target/index.html -- --output=main.j
```