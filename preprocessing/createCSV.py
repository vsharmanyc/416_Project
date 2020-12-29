import csv
import random
import math
import os
import sys
import configparser

config = "./createCSV_config.txt"

configParser = configparser.ConfigParser()
configParser.read(config)
configHeader = configParser['config']['header'].split(',')
filename = configParser['config']['file']

DEMOGRAPHICS = ['W','B','I','A','H','O']
INDEXES = []

demographicsInLabel = []
prevCountyID = ""
precinctIDNum = 0

def findLabelStart(header):
    index = 0
    for label in header:
        if label in DEMOGRAPHICS:
            index = header.index(label)
            break
    for demo in DEMOGRAPHICS:
        INDEXES.append(header.index(demo))
        index += 1
    return index

def addSplit(row, label):
    for letter in label:
        row[INDEXES[DEMOGRAPHICS.index(letter)]] = str(int(row[INDEXES[DEMOGRAPHICS.index(letter)]]) + split)
        demographicsInLabel.append(letter)
    return row

def addRemainder(row, remainder):
    while remainder > 0:
        randDemographic = random.choice(demographicsInLabel)
        row[INDEXES[DEMOGRAPHICS.index(randDemographic)]] = str(int(row[INDEXES[DEMOGRAPHICS.index(randDemographic)]]) + 1)
        demographicsInLabel.remove(randDemographic)
        remainder -= 1
    return row

def normalizeID(header, row):
    global prevCountyID
    global precinctIDNum
    stateID = row[2]
    countyID = row[4].zfill(3)
    if countyID != prevCountyID:
        precinctIDNum = 1
    prevCountyID = countyID
    precinctIDStr = str(precinctIDNum).zfill(4)
    geoID = stateID + countyID + precinctIDStr
    row[header.index("PRECINCTID")] = precinctIDNum
    row.append(geoID)
    precinctIDNum += 1
    return row

outputFilename = os.path.splitext(filename)[0] + "_output.csv"

with open(filename) as csv_in, open(outputFilename, 'w', newline='') as csv_out:
    readCSV = csv.reader(csv_in, delimiter=',')
    writeCSV = csv.writer(csv_out, delimiter=',')
    header = next(readCSV)
    if header != configHeader:
        print(f"csv not in correct format: {configHeader}")
        sys.exit(0)
    header.append("NEWGEOID")
    writeCSV.writerow(header)
    startIndex = findLabelStart(header)
    for row in readCSV:
        index = startIndex
        while index < len(header) - 1:
            demographicsInLabel = []
            count = int(row[index])
            split = math.floor(count/len(header[index]))
            remainder = count%len(header[index])
            row = addSplit(row, header[index])
            if remainder > 0:
                row = addRemainder(row, remainder)
            index += 1
        row = normalizeID(header, row)
        writeCSV.writerow(row)
    print("Processing complete")
csv_in.close()
csv_out.close()
            
