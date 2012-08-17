## About ##
The bees algorithm is a population-based search algorithm in the field of swarm intelligence that finds some optimal solution by modeling the foraging bee-havior of honey bees.

Rough psuedocode (taken from [Wikipedia](http://en.wikipedia.org/wiki/Bees_algorithm)):
`1. Initialize population with random solutions.`
`2. Evaluate fitness of the population.`
`3. While (stopping criterion not met) //Forming new population.`
`4.   Select sites for neighborhood search.`
`5.   Recruit bees for selected sites (more bees for best e sites) and evaluate fitnesses.`
`6.   Select the fittest bee from each patch.`
`7.   Assign remaining bees to search randomly and evaluate their fitnesses.`
`8. End While.`

Inspired by [Clever Algorithms: Nature-Inspired Programming Recipes](http://www.cleveralgorithms.com/nature-inspired/swarm/bees_algorithm.html)'s implementation in Ruby.

Clojure

## Usage ##
As long as [Leiningen](https://github.com/technomancy/leiningen/) is installed, you can clone the repository, cd into the directory, and run `lein run`.

## TDL ##
In progress.