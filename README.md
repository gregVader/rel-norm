# RelNorm

A simple app for relational model normalization. Normalization can be done by decomposition and synthesis.

## Getting started

```shell
java -jar rel-norm-1.0-SNAPSHOT.jar <task-file-path> <method-name>
```

- `<method-name>` - is either `DECOMPOSITION`, `DECOMPOSITION_I` or `SYNTHESIS`
  - `DECOMPOSITION` - normalizing by decomposition method
  - `DECOMPOSITION_I` - normalizing by decomposition method (interactively selecting functional dependencies)
  - `SYNTHESIS` - normalizing by synthesis

### Save output to file

```shell
java -jar rel-norm-1.0-SNAPSHOT.jar <task-file-path> <method-name> > output.txt
```

## Build it yourself

```shell
mvn package
```

#### Requirements

- Maven 3.8.1
- Java 16

## Task files

### Example 1 - `task1.json`
Relation `N` with labels `A`, `B`, `C`, `D`, `E` and functional dependencies `AB -> CE` and `C->D`:

```json
{
  "name": "N",
  "labels": ["A", "B", "C", "D", "E"],
  "functionalDependencies": [
    {"left": ["A", "B"], "right": ["C", "E"]},
    {"left": ["C"], "right": ["D"]}
  ]
}
```















