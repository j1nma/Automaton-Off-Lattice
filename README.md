# Automaton Off-Lattice

## Compilation

```
mvn package
```

## Execution
### Generation of dynamic file
To generate a file for a specific L, N and particle radius.
```
python generate.py
```

To generate multiple files for N from 100 to 1000.
```
python generate_multiple.py
```
### Running simulation

```
java -jar tp2-1.0-SNAPSHOT.jar
```

Options:

* **-h, --help**: Prints usage infp.
* **-M, --matrix &lt;size>**: Size of the squared matrix.
* **-r, --radius &lt;double>**: Interaction radius.
* **--pbc**: Enable periodic boundary conditions.
* **--bf**: Enable brute force algorithm.
* **--dynamicFile &lt;path>**: Path to dynamic file.

The simulation's results (execution time and list of neighbours for each particle)
are printed to `/ovito_file.txt`.

```
java -jar target/tp2-1.0-SNAPSHOT.jar --dynamicFile ./random/Dynamic-N=300.txt -n 0.1 -t 1000 -l 5.0 -M 4 -r 1 

```

### Va Mean && standard deviation

```
python3 va.py
```

The calculation of the Va mean and the standart deviation based on repeated tests.

