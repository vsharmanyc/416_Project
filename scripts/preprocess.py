import os
import pathlib
#import geopandas as gpd

states = ["AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DC", "DE", "FL", "GA", 
          "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", 
          "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", 
          "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", 
          "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"]

shpDir = pathlib.Path(__file__).parent / 'data'

def findDistrictNeighbors(filename):
    print("test district")
    #find district neighbors

    #empty set

    #for every entry
        #for entry near range
            #if compared entry is a neighbor, put into the set
        #send data from the file and a new column of the set of neighbors to the district table
        #clear the set
    return

def findPrecinctNeighbors(filename):
    print("test precinct")
    #find precinct neighbors

    #empty set

    #for every entry
        #for entry near range
            #if compared entry is a neighbor, put into the set
        #send data from the file and a new column of the set of neighbors to the precinct table
        #clear the set
    return

def createState(filename):
    #filename conventions: [state abbreviation]_[district/precinct].geojson
    state = filename[:2].upper()
    if filename[:2].upper() in states:
        if "district" in filename.lower():
            findDistrictNeighbors(filename)
        elif "precinct" in filename.lower():
            findPrecinctNeighbors(filename)
        else:
            print("Filename not in proper format")
    else:
        print("Filename not in proper format")
        return
    return

if __name__ == "__main__":
    for entry in os.listdir(shpDir):
        if entry.lower().endswith('.shp'):
            createState(entry)
