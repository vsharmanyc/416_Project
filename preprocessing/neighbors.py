import libpysal as lps
import geopandas as gpd
import csv
import math
import configparser

config = "./neighbors_config.txt"

cp = configparser.ConfigParser()
cp.read(config)

state = cp['config']['state']
shp_path = cp['config']['file']
shp = gpd.read_file(shp_path)

rW = lps.weights.Rook.from_dataframe(shp, idVariable='GEOID10')

header = ['GEOID10','NEIGHBORS']

outputName = state + "_neighbors_output.csv"

with open(outputName, 'w', newline='') as csv_out:
    writeCSV = csv.writer(csv_out, delimiter=',')
    writeCSV.writerow(header)
    for row in rW:
        neighborString = ""
        geoID = row[0]
        if not (math.isnan(geoID)):
            neighbors = row[1]
            neighborIDs = list(neighbors.keys())
            for id in neighborIDs:
                if not (math.isnan(id)):
                    neighborString = neighborString + str(int(id)) + ','
            neighborString = neighborString[:len(neighborString)-1]
            row = [int(geoID), neighborString]
            writeCSV.writerow(row)
    print("Processing complete")
csv_out.close()