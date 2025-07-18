# [Efficient heuristics for the obnoxious planar p-median problem with variable sizes](https://doi.org/XXXXX)
[![DOI]()
## Abstract
The Multiple Obnoxious Facility Location Problem (MOFLP) is one of the most studied problems in the literature of the obnoxious facility location problems family. In this work, we propose an alternative algorithmic approach for this problem, based on an efficient metaheuristic procedure over a discretization of the plane based on Voronoi diagrams, helped by a local search able to traverse the continuous space. To this aim, our algorithmic proposal first begins with the implementation of a Greedy Randomized Adaptive Search Procedure (GRASP) method whose improvement phase is implemented by a General Variable Neighborhood Search (GVNS) procedure. The GVNS is supported by two local search methods that allow the algorithm to escape the initial discretization and find good solutions in short execution times. The results show that the proposed algorithm achieves the best results regarding the objective function value, deviation, and number of best results in relation to the state of the art. These results are further confirmed by conducting non-parametric statistical tests.

## Authors

#### Sergio Salazar Cárdenas
<table>
<tr>
<td>

**Institution:** [Dept. of Computer Science and Statistics, Universidad Rey Juan Carlos](https://servicios.urjc.es/pdi/ver/sergio.salazar)  
**Email:** [sergio.salazar@urjc.es](mailto:sergio.salazar@urjc.es)  
**ORCID:** [0009-0007-3492-2221](https://orcid.org/0009-0007-3492-2221)  

</td>
</tr>
</table>

#### Abraham Duarte
<table>
<tr>
<td>

**Institution:** [Dept. of Computer Science and Statistics, Universidad Rey Juan Carlos](https://servicios.urjc.es/pdi/ver/abraham.duarte)  
**Email:** [abraham.duarte@urjc.es](mailto:abraham.duarte@urjc.es)  
**ORCID:** [0000-0002-4532-3124](https://orcid.org/0000-0002-4532-3124)  

</td>
</tr>
</table>

#### José Manuel Colmenar
<table>
<tr>
<td>

**Institution:** [Dept. of Computer Science and Statistics, Universidad Rey Juan Carlos](https://servicios.urjc.es/pdi/ver/josemanuel.colmenar)  
**Email:** [josemanuel.colmenar@urjc.es](mailto:josemanuel.colmenar@urjc.es)  
**ORCID:** [0000-0001-7490-9450](https://orcid.org/0000-0001-7490-9450)  

</td>
</tr>
</table>
  
## Benchmarks

The coordinates of the benchmark instances can be found in the following file, by selecting the first 100 or 1000 entries: [CHOORDS](https://github.com/SergioSalazarC/FastMOFLP/blob/main/Voronoi/Instances/FacilityChoords)


## Code

Final experiment can be locate in this  java [CODE](https://github.com/SergioSalazarC/FastMOFLP/blob/main/Voronoi/src/MOFLP/Main.java) file. This shows a general framework to solve any instance selecting n and D parameters with p values from 2 to 20.

### Requirements and Dependencies
- Java 21


[//]: <> (## Executing)



## Cite

Consider citing our paper if used in your own work:


### DOI

### Zenodo
[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.16095883.svg)](https://doi.org/10.5281/zenodo.16095883)

### Bibtex
```bibtex
@article{}
```

