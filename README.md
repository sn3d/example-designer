# example-designer

This is small example of Designed based on event sourcing. It's just a prototype
that cannot solve all problems, just illustrate how Designed backend should look
like.

## How to build and run

```
  git clone https://github.com/sn3d/example-designer.git
  cd example-designer
  ./gradlew build run
```


## How to use it

The application should run on `http://localhost:8080`. You can create first
theme:

    curl -X POST http://localhost:8080/theme?name=myfirsttheme

You can see the current theme:

    curl http://localhost:8080/theme/myfirsttheme

You can modify theme:

    curl -X POST -H "Content-Type: application/json;event=LogoPositionChanged" -d '{"left":10.0,"top":10.0}' http://localhost:8080/theme/myfirsttheme

or:

    curl -X POST -H "Content-Type: application/json;event=HeaderColorChanged" -d '{"color":{"red":1.0,"green":0.0,"blue":0.0,"apha":0.0}}' http://localhost:8080/myfirsttheme

You can also perform commands like 'activation':

    curl -X POST -H "Content-Type: application/json;command=Activate” http://localhost:8080/theme/myfirsttheme


And also you can render CSS for theme:

    curl http://localhost:8080/theme/myfirsttheme/css


Or you can see all events of theme

    curl http://localhost:8080/theme/myfirsttheme/events
