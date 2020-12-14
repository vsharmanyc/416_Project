#!/usr/bin/bash

ONE=$(grep -o -i 'ALGORITHM RESULTS' output0.txt | wc -l)
TWO=$(grep -o -i 'ALGORITHM RESULTS' output1.txt | wc -l)
THREE=$(grep -o -i 'ALGORITHM RESULTS' output2.txt | wc -l)
total=$(($ONE+$TWO+$THREE))
echo $total
