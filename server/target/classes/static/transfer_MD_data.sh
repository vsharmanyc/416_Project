#!/usr/bin/expect

spawn scp swData.txt jlungu@login.seawulf.stonybrook.edu:/gpfs/scratch/jlungu
expect "jlungu@login.seawulf.stonybrook.edu's password:"
send "Uranium12*\r"
interact