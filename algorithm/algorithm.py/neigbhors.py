import os
import json
from shapely.geometry import Polygon, MultiPolygon

path = os.path.realpath('../precinctsGeoJson/NY_normalized.geojson')
file = open(path)
precincts =  json.load(file);


precinctPolygons = {}

#for each precinct ...
for precinctInfo in precincts['features']:
    #get the precinct name
    precinctName = precinctInfo['properties']['PRECINCT']
    
    #the precinct geography is either a Polygon (some shape) or a MultiPolygon (a collection of polygons)
    precinctGeographyType = precinctInfo['geometry']['type']
    
    #if it's a Polygon, convert the coordinates to a shapely Polygon object
    if precinctGeographyType == 'Polygon':
        precinctGeometry = Polygon(precinctInfo['geometry']['coordinates'][0])
        
    #if its a MultiPolygon, convert each contained polygon into a shapely Polygon object ...
    #and then store the list of Polygons in a shapely MultiPolygon object
    elif precinctGeographyType == 'MultiPolygon':
        polygonsInMultipolygon = [Polygon(p[0]) for p in precinctInfo['geometry']['coordinates']]
        precinctGeometry = MultiPolygon(polygonsInMultipolygon)
    
    #store the precinct geography info in the dictionary
    precinctPolygons[precinctName] = precinctGeometry


neighbors = {}

#for each precinct ...
for k1,v1 in precinctPolygons.items():
    neighbors[k1] = []
    #iterate over each other precinct
    for k2,v2 in precinctPolygons.items():
        #if the precincts touch, then add this precinct to the list of neighboring precincts
        if v1.touches(v2):
            neighbors[k1].append(k2)

#create a new dictionary for number of bordering precincts
precinctBorderCount = {}
for k,v in neighbors.items():
    precinctBorderCount[k] = len(v)

# print neighbors
print(neighbors);