# MindScriptApp
 Компилирует python-like код в mindusrty mlog

## Перечень математических операций:

Логические операции:
- "!"
- ">"
- ">="
- "<"
- "<="
- "=="
- "!="
- "&&"
- "||"

Арифметика:
- "-!" (умножение на -1)
- "*"
- "/"
- "%"
- "+"
- "-"
- "="
- "+="
- "-="
- "*="
- "/="
- "%="  

Ф-ции:
- "rand()"
--------------

## Примеры математических выражений

Далее 
- `var|1, 2, 3, ...|` - некоторая переменная
- `m(...)` - ф-ция с возвращаемым значением
- `[op]` - любая операция

### Пример 1 (выражения без методов и скобок)

`var|1| = var|2| [op] var|3|`

(любое кол-во операций подряд)

`var|1| = var|1| [op] var|2| [op] ... [op] var|n|`

### Пример 3 (Скобки и порядок операций)

(правильный порядок операций для скобок)

`var|1| = var|1| [op] var|2| [op] (var|3| [op] var|4|)`

для

`var|2| * (var|3| + var|4|) ` 

порядок выполнения: 
1. `var|3| + var|4|`
2. `var|2| * (...)`

### Пример 4 (Вызов функций в выражении)

`var|1| = var|2| [op] m(...)`

--------------

## Ключевые слова:

- ifConstructionName = "if"
- elseConstructionName = "else"
- forCycleName = "for"
- whileCycleName = "while"
- loopCycleName = "loop"
- newMethodKeyWord = "func"
- methodReturnValueKeyWord = "return"
--------------

## Встроенные методы (из mlog)

- "read", 
- "write", 
- "draw", 
- "print", (ввод / вывод)
- "drawflush", 
- "printflush", 
- "getlink", 
- "control", 
- "radar", 
- "sensor", (управление блоками)
- "lookup", 
- "packcolor", (операции)
- "wait", 
- "stop", (управление последовательностью)
- "ubind", 
- "ucontrol", 
- "uradar", 
- "ulocate" (управление юнитами)
--------------

Далее все как в mlog (это некоторые enum, которые передаются в встроенные методы как аргументы)**

## DrawType
- clear = "clear"
- color = "color"
- col = "col"
- stroke = "stroke"
- line = "line"
- rect = "rect"
- lineRect = "lineRect"
- poly = "poly"
- linePoly = "linePoly"
- triangle = "triangle"
- image = "image"
--------------

## ControlType
- enabled = "enabled"
- shoot = "shoot"
- shootp = "shootp"
- config = "config"
- color = "color"
---------------

## RadarFilterType
- any = "any"
- enemy = "enemy"
- ally = "ally"
- player = "player"
- attacker = "attacker"
- flying = "flying"
- boss = "boss"
- ground = "ground"
--------------

## RadarSortType
- distance = "distance"
- health = "health"
- shield = "shield"
- armor = "armor"
- maxHealth = "maxHealth"
--------------

## UnitControlType
- idle = "idle"
- stop = "stop"
- move = "move"
- approach = "approach"
- pathfind = "pathfind"
- autoPathfind = "autoPathfind"
- boost = "boost"
- target = "target"
- targetp = "targetp"
- itemDrop = "itemDrop"
- itemTake = "itemTake"
- payDrop = "payDrop"
- payTake = "payTake"
- payEnter = "payEnter"
- mine = "mine"
- flag = "flag"
- build = "build"
- getBlock = "getBlock"
- within = "within"
- unbind = "unbind"
--------------

## ObjectToFindType
- building = "building"
- ore = "ore"
- spawn = "spawn"
- damaged = "damaged"
--------------

## GroupType
- core = "core"
- storage = "storage"
- generator = "generator"
- turret = "turret"
- factory = "factory"
- repair = "repair"
- battery = "battery"
- reactor = "reactor"
--------------

## LookupType
- block = "block"
- unit = "unit"
- item = "item"
- liquid = "liquid"
--------------

## URadarFilterType
- any = "any"
- enemy = "enemy"
- ally = "ally"
- player = "player"
- attacker = "attacker"
- flying = "flying"
- boss = "boss"
- ground = "ground"
--------------

## URadarSortType
- distance = "distance"
- health = "health"
- shield = "shield"
- armor = "armor"
- maxHealth = "maxHealth"
--------------