#!/usr/bin/expect

spawn scp swData_job1.txt jlungu@login.seawulf.stonybrook.edu:/gpfs/scratch/jlungu
expect "jlungu@login.seawulf.stonybrook.edu's password:"
send "Uranium12*\r"
interact
