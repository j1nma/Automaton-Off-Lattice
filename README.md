# Automaton Off-Lattice

## Compilation

```
mvn clean package
```

## Execution
### Generation of dynamic file
To generate a file for a specific L and N.
```
python scripts/generate.py 40 20.0
```

To generate multiple files for N from 100 to 1000.
```
python scripts/generate_multiple.py
```
### Running simulation

```
java -jar ./target/tp2-1.0-SNAPSHOT.jar --dynamicFile="./scripts/Dynamic-N=300.txt" -n 0.1 -t 1000 -l 5.0 -M 4 -r 1
```

Options:

* **-h, --help**: Prints usage infp.
* **-M, --matrix &lt;size>**: Size of the squared matrix.
* **-r, --radius &lt;double>**: Interaction radius.
* **--pbc**: Enable periodic boundary conditions.
* **--bf**: Enable brute force algorithm.
* **--dynamicFile &lt;path>**: Path to dynamic file.

The simulation's results (execution time and list of neighbours for each particle)
are printed to `ovito_file.txt`.

### Run script to generate order parameter (Va) means for a particular set of parameters

```
python3 scripts/va.py
```

### (Octave) Run script to graph order parameter versus noise

```
run("order-parameter-noise.m")
```
