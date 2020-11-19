**Description:** 

Full Stack web application displaying congressional voting data for different voting districts.

***Start Server***
- You'll need to install Maven first (https://maven.apache.org). Easy on MacOS/Linux, not sure about Windows
- Go to wherever you enter commands, be in cmd or terminal.
- Navigate to project directory, then to the server folder.
- To run server, simply type mvn spring-boot:run
- Should pop up, and run fine. Then, you can start making calls to the backend.
- NOTE: Please edit the absolute path for your machine for grabbing JSON files. You need to edit the 'MapHandler.class' file, specifically the method 'requestDistricts'. Modify the 'path' variable to reflect the absolute path of your system, up until the 'server' folder.

**Config Properties**
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

**Team Members:**

* Harsh Vig 
* James Lungu 
* Travis Li 
* Vasu Sharma
