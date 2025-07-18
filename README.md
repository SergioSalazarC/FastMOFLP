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

#### Oscar Cordón
<table>
<tr>
<td>

**Institution:** [Andalusian Research Institute in Data Science and Computational Intelligence (DaSCI) and Dept. of Computer Science and Artificial Intelligence, University of Granada](https://www.ugr.es/personal/oscar-cordon-garcia)  
**Email:** [ocordon@decsai.ugr.es](mailto:ocordon@decsai.ugr.es)  
**ORCID:** [0000-0001-5112-5629](https://orcid.org/0000-0001-5112-5629)  

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

Benchmarks information could be obtained in https://doi.org/10.1016/j.omega.2022.102639.


## Code

Final experiment can be locate in this  java [CODE](https://github.com/SergioSalazarC/pMedianVariableProblem/blob/master/src/Experimentos/MainMemetico_modified.java) file. This shows a general framework to solve any of the SOTA benchmark instances, in this case is setted to solve n=100 and p=2 instance.

### Requirements and Dependencies
- Java 11
- Gurobi 11.0.3 ([Java Package](https://docs.gurobi.com/projects/optimizer/en/current/reference/java.html))


[//]: <> (## Executing)



## Cite

Consider citing our paper if used in your own work:
(Fill with the references to your own published work)


### DOI
[https://doi.org/j.asoc.2025.113401](https://doi.org/10.1016/j.asoc.2025.113401)
### Zenodo
[https://doi.org/10.5281/zenodo.15689089](https://doi.org/10.5281/zenodo.15689089)
### Bibtex
```bibtex
@article{salazar2025113401,
title = {Efficient heuristics for the obnoxious planar p-median problem with variable sizes},
journal = {Applied Soft Computing},
volume = {181},
pages = {113401},
year = {2025},
issn = {1568-4946},
doi = {https://doi.org/10.1016/j.asoc.2025.113401},
url = {https://www.sciencedirect.com/science/article/pii/S1568494625007124},
author = {Sergio Salazar and Oscar Cordón and J. Manuel Colmenar}
}
```

