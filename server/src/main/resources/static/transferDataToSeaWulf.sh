#!/usr/bin/expect

spawn scp swData_job1.txt vvsharma@login.seawulf.stonybrook.edu:/gpfs/scratch/vvsharma
expect "vvsharma@login.seawulf.stonybrook.edu's password:"
send "As4676as741852654"
interact
