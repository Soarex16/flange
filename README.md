# FLANGE (F LANGuagE)

Super tiny and simple language with JS backend

## Constructions
Flange supports only 3 kinds of statements:
- assignment
```javascript
x = 127 - 11
```

- if/then/else
```javascript
x = input()
if (x < 100) then {
    x = 17
} else {
    x = 0
}
print(x)
```

- while loop
```javascript
while (42) {
    x = input()
    print(y + 4)
}
```

## Some useful notes
- only integer data type supported
- anything but zero is treated as a **true** value
- in default implementation there is two built-in functions:
  - `input()` reads next number from input
  - `print(expression)` prints expression into console
  - Built-in functions does not limited by language spec  