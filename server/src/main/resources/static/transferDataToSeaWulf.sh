#!/usr/bin/expect

spawn scp swData_job2.json jlungu@login.seawulf.stonybrook.edu:/gpfs/scratch/jlungu
expect "jlungu@login.seawulf.stonybrook.edu's password:"
send "Uranium12*\r"
interact
