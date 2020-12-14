#!/usr/bin/expect

spawn scp swData_job14.txt panthersm.slurm ../../../../../algorithm/algorithm.py ../../../../../algorithm/Cluster.py ../../../../../algorithm/Graph.py ../../../../../algorithm/Node.py ../../../../../algorithm/Subgraph.py ../../../../../algorithm/Precinct.py ../../../../../algorithm/Request.py jlungu@login.seawulf.stonybrook.edu:/gpfs/scratch/jlungu/PA_job#14
expect "jlungu@login.seawulf.stonybrook.edu's password:"
send "Ifyougiveapigapancake12112!"
interact
