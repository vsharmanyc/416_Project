#!/usr/bin/env bash
#SBATCH --job-name=PA_750
#SBATCH --output=PA_750.log
#SBATCH --ntasks-per-node=40
#SBATCH --nodes=3
#SBATCH --time=2-00:00:00
#SBATCH -p long-40core
#SBATCH --mail-type=BEGIN,END
#SBATCH --mail-user=james.lungu@stonybrook.edu
module load gnu-parallel/6.0
module load mpi4py/3.0.3
rm -rf result.json
rm -rf coordinator.log
rm -rf output1.txt
rm -rf output2.txt
rm -rf output0.txt
rm -rf result0.json
rm -rf result1.json
rm -rf result2.json
touch output0.txt output1.txt output2.txt result0.json result1.json result2.json
touch coordinator.log
echo '{"results": ['>> result.json
echo I am using the following nodes: $SLURM_JOB_NODELIST
echo My job ID is: $SLURM_JOB_ID
cat << EOF > panthers.py
import os
import time
from mpi4py import MPI
start_time = time.time()
comm = MPI.COMM_WORLD
size = comm.Get_size()
rank = comm.Get_rank()
print("This is node {}".format(rank))
job_count = 750
job_count0_sz3 = ceil(job_count / 3)
job_count1_sz3 = job_count0_sz3
job_count2_sz3 = job_count - job_count0_sz3 - job_count1_sz3
job_count0_sz2 = ceil(job_count/2)
job_count1_sc2 = job_count - job_count0_sz2
if rank == 0:
		print("We have {} nodes in our cluster.".format(size))
		data = [x for x in range(size)]
		print('We will be scattering files across nodes:', data)
else:
		data = None
if size == 1:
		jobs = job_count
elif size == 2:
		if rank == 0:
		jobs = job_count0_sz2
else:
		job_count1_sc2
elif size == 3:		
if rank == 0:
jobs = job_count0_sz3
elif rank == 1:
		jobs = job_count1_sz3
else:
		jobs = job_count2_sz3
data = comm.scatter(data, root=0)
print('rank', rank, 'has data:', data)
print("Node {} will be completing {} jobs".format(rank, jobs))
while jobs > 0:
		if jobs > 40:
		s = ""
		fname = "info" + str(rank) + ".txt"
		f = open(fname, "w")
		for i in range(1, 41):
		s += str(rank) + "\n"
		s += str(rank)
		f.write(s)
		f.close()
		command = "cat " + fname + " | parallel -l1 --jobs 40 python3 algorithm.py".format(data)
		print("Running command:", command)
		os.system(command)
		else:
		s = ""
		fname = "info" + str(rank) + ".txt"
		f = open(fname, "w")
		for i in range(1, jobs+1):
		s += str(rank) + "\n"
		s += str(rank)
f.write(s)
		f.close()
		command = "cat " + fname + " | parallel -l1 --jobs " + str(jobs) + " python3 algorithm.py".format(data)
		print("Running command:", command)
		os.system(command)
		jobs -= 40
comm.barrier()
print("Completed in {:.2f}s for rank {}:".format(time.time() - start_time, rank))
EOF
mpirun -np 3 python3 panthers.py
echo ']}'>> result.json