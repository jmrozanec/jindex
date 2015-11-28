# JINDEX

This project provides means to extract surf Maven central repository and extract metadata from it. 
Information about 75%+ of projects by available by mid 2015 is published in graphs folder, so that anyone can use it for analysis.
Graph was creating by considering the following algorithm:

- pick artifact from projectqueue
 - if no artifact in queue, pick a random artifact
- check its latest version
- bring immediate dependencies
- for each dependency,
 - register it as citation: origin, citedproject,1
 - if dependencyId was not processed yet, enqueue it in projectqueue

## Measures of available dataset

- Total number of artifacts indexed (GAV): 1,111,966
- Total number of unique artifacts indexed (GA): 121,550

## Useful information

- [Maven central REST API](http://search.maven.org/ajaxsolr/images/MavenCentralAPIGuide.pdf)
- [Maven policies for good citizens](http://stackoverflow.com/questions/2439891/downloading-complete-maven-remote-repository-to-local-repository)
