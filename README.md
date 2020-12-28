# A Deeper Look into Gerrymandering in the United States
### Team Members
* Harsh Vig - Front End, Server-Front End Communication
* James Lungu - Server-Side, Algorithm, Supercomputer Connectivity
* Travis Li - Preprocessing of Data, Post-Processing of Districting Plans
* Vasu Sharma - Front End, Server-Front End Communication

## Quick Information
This project was part of our Stony Brook Computer Science Capstone Course, CSE 416. We created a Full-Stack Web Application that generated completely random congressional districtings for three states; New York, Pennsylvania, and Maryland. These random distractions were compared to the enacted plans, in order for us to determine whether or not the state seemed to have traces of Gerrymandering in their congressional districting plans. 
Front-End: React.js
Back-End: Spring Boot
Pre-Processing & Algorithm: Python
Supercomputer Communication: SLURM

## Introduction
Gerrymandering is the process of drawing congressional district boundaries with the intent of having one group have a political advantage in a state. Although Gerrymandering has been outlawed in any state, it can still be seen today in quite a few states. Typically, in a state where Gerrymandering is present, the congressional district boundaries will look wacky and oddly shaped, displaying the effect of ‘cracking’ and ‘packing’ (cracking refers to the idea of spreading out the opposing parties voters across many districts to lessen the voting power they would have, packing refers to the opposite tactic of packing together opposing voters into just one district so they do not have a say elsewhere). Furthermore, if the Gerrymandered state’s minority voting-age population (MVAP) was to be graphed in a scatter plot for each district in the state, one may see a gap of MVAP between the percentages of 40-60% in a Gerrymandered state. This is due to the ‘cracking’ and ‘packing’ mentioned before; some districts have high minority voting age populations, while others have drastically low comparatively. 

## Methods
Our project was based off of the Virginia Report Research Study done by a research group at Tufts University, located here: https://mggg.org/VA-report.pdf. Please take a few moments to read through it; not only does it contain incredibly interesting methods and ideas, but it is very informative on the matter of Gerrymandering. Our Web Application starts off by allowing the user to input the number of random districtings they want to analyze, the population equality threshold they would like in the districting plans (difference between highest/lowest populated districts in state), the compactness they would like to see in the districts, and the minority groups they would like to analyze for the state. Compactness was what we referred to before when speaking of those wacky and oddly shaped districts in a districting plan; a highly compact district would contain such shapes, and a not-very-compacted district would contain much more regular and manageable border shapes. The application would then send off the user requested parameters all the way to the supercomputer made available to us by the Stony Brook Computer Science Department, where the main algorithm of generating the districting plans would run. After generating the random districting plans, the server would then generate a box plot from all the districting plans generated, and generate a summary file for the user to inspect. The box plot would help the user determine if there are any inklings of gerrymandering in the state, for the requested minority populations. 

As previously stated, the algorithm ran on the University’s Supercomputer. The algorithm starts off by generating a ‘seed districting’, the initial starting point of the individual districting plan. After seed generation, the algorithm balances the districts in the plan according to the user inputted values of compactness and population. In short, the algorithm does this by randomly combining two neighboring districts, generates a spanning tree of the combined subgraph, and randomly cuts an edge from the spanning tree that is considered ‘acceptable’ (meets compactness and population metrics). If none are acceptable, it randomly selects an edge bringing the two districts closer to acceptable. The algorithm does this sequence of steps 10,000 times, ensuring randomness and giving the algorithm enough time to find a districting plan within the user-given metrics.

# Running the Application

### Start Server
- You'll need to install Maven first (https://maven.apache.org). Easy on MacOS/Linux, not sure about Windows
- Go to wherever you enter commands, be in cmd or terminal.
- Navigate to project directory, then to the server folder.
- To run server, simply type mvn spring-boot:run
- Should pop up, and run fine. Then, you can start making calls to the backend.
- NOTE: Please edit the absolute path for your machine for grabbing JSON files. You need to edit the 'MapHandler.class' file, specifically the method 'requestDistricts'. Modify the 'path' variable to reflect the absolute path of your system, up until the 'server' folder.

### Config Properties
We are using system config files to load static variables.
To fix the server crashing...
- Create a file named 'config.properties' in server/src/main/resources
Fill with this info:

serverStaticWd = server/src/main/resources/static
seaWulfDistrictingThreshold = 5000
transferDataBash = /transferDataToSeaWulf.sh
swDataPrefix = /swData_job
precinctDataSuffix = _Precinct_data.json
bashScript = #!/usr/bin/expect\n\nspawn scp swData_job%d.txt {netid}@login.seawulf.stonybrook.edu:/gpfs/scratch/{netid}\nexpect \"{netid}@login.seawulf.stonybrook.edu's password:\"\nsend \"{netid_passcode}\\r\"\ninteract\n

- Replace {netid} and {netid_passcode} with your credentials



**Run Project:**
* Install Node.js and NPM
* ```git clone https://github.com/vsharmanyc/416_Project.git```
* ```cd 416_Project```
* ```npm install```
* ```npm start```

___


**Course:** CSE 416

**Team Name:** Panthers


