#!/usr/bin/expect\n\nspawn scp swData_job1.txt jlungu@login.seawulf.stonybrook.edu:/gpfs/scratch/jlungu\nexpect "jlungu@login.seawulf.stonybrook.edu's password:"\nsend "Uranium12*\r"\ninteract\n