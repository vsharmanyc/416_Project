import geopandas as gpd
import sys
import json

demographicStrings = ["WHITE","AFRICAN_AMERICAN","ASIAN","AM_INDIAN_AK_NATIVE","HISPANIC_LATINO","NH_OR_OPI"]
populationStrings = ["WTOT","BTOT","ATOT","AIANTOT","HTOT","NHOPTOT"]
vapStrings = ["WVAP","BVAP","AVAP","AIANVAP","HVAP","NHOPVAP"]

precinctPath = "{}_neighbors.geojson"
districtPath = "{}_district_with_pop.json"

summaryJSON = {
    "stateID": None,
    "precinctsGeoJSON": None,
    "enactedPlanMVAPs": [],
    "extremeDistricting": None,
    "averageDistricting": None,
    "districtings": []
}

extremeJSON = {
    "districtingID": None,
    "constraints": None,
    "districtsGeoJSON": None
}

averageJSON = {
    "districtingID": None,
    "constraints": None,
    "districtsGeoJSON": None
}

randomJSON = {
    "districtingID": None,
    "constraints": None,
    "districtsGeoJSON": None
}

constraintsJSON = {
    "compactnessLimit": None,
    "populationDifferenceLimit": None,
    "minorityGroups": []
}

def calculateMVAP(districtFilename):
    minorityGroups = constraintsJSON['minorityGroups']
    with open(districtFilename, 'r') as dFile:
        dData = json.load(dFile)
        for district in dData:
            mvap = 0.0
            minorityVAP = 0
            totalVAP = district['TOTVAP']
            for minority in minorityGroups:
                minorityVAP += int(district[vapStrings[demographicStrings.index(minority)]])
            mvap = minorityVAP/totalVAP
            summaryJSON['enactedPlanMVAPs'].append(mvap)
        summaryJSON['enactedPlanMVAPs'].sort()
    dFile.close()

def createDistrictingGEOJSON(districtingData, districtingJSON, precinctFilename, outputFilename):
    districtingJSON['constraints'] = constraintsJSON
    districtingJSON['districtingID'] = districtingData['districtingPlanID']

    precinctsInDistricts = []
    precinctData = []
    precinctDataInDistrict = []
    dTotalPop = []
    dTotalVAP = []
    dMinorityPop = []
    dMinorityVAP = []
    dAdjacentDistricts = []
    dNumCounties = []
    dPercentMVAP = []
    districting = districtingData['districts']

    with open(precinctFilename, 'r') as pFile, open("temp.geojson", 'w') as districtingOut:
        pData = json.load(pFile)
        summaryJSON['precinctsGeoJSON'] = pData
        precinctsFile = pData['features']

        for district in districting:
            districtTotalPop = 0
            districtTotalVAP = 0
            districtMinorityPop = 0
            districtMinorityVAP = 0
            districtPrecincts = []
            districtID = district['districtNum']
            dAdjacentDistricts.append(str(district['neighbors']))
            dNumCounties.append(district['counties'])
            dPercentMVAP.append(district['percentVap'])
            precincts = district['precincts']
            for precinct in precincts:
                pJSON = {
                    "geoID": None,
                    "minorityPop": None,
                    "minorityVAP": None
                }
                geoID = precinct['geoid10']
                pJSON['geoID'] = geoID
                minorityPop = sum(precinct['populationData']['minorityPopulations'].values())
                minorityVAP = sum(precinct['populationData']['minorityVAPopulations'].values())
                totalPop = precinct['populationData']['population']
                totalVAP = precinct['populationData']['votingAgePopulation']
                pJSON['minorityPop'] = minorityPop
                pJSON['minorityVAP'] = minorityVAP
                districtTotalPop += totalPop
                districtTotalVAP += totalVAP
                districtMinorityPop += minorityPop
                districtMinorityVAP += minorityVAP
                precinctDataInDistrict.append(pJSON)
                districtPrecincts.append(geoID)
                for p in precinctsFile:
                    if (p['properties']['GEOID10'] != None) and (int(geoID) == int(p['properties']['GEOID10'])):
                        p['properties']['DISTRICTID'] = districtID
                        break
            dTotalPop.append(districtTotalPop)
            dTotalVAP.append(districtTotalVAP)
            dMinorityPop.append(districtMinorityPop)
            dMinorityVAP.append(districtMinorityVAP)
            precinctsInDistricts.append(str(districtPrecincts))
            precinctData.append(json.dumps(precinctDataInDistrict))
        json.dump(pData, districtingOut)
        districtingOut.close()
    
        newPrecinctsGEOJSON = gpd.read_file("temp.geojson")
        newPrecinctsGEOJSON = newPrecinctsGEOJSON[['STATE','DISTRICTID','geometry']]
        districtingGeo = newPrecinctsGEOJSON.dissolve(by='DISTRICTID')
        districtingGeo['PRECINCTS'] = precinctsInDistricts
        districtingGeo['TOTALPOP'] = dTotalPop
        districtingGeo['MINORITYPOP'] = dMinorityPop
        districtingGeo['TOTALVAP'] = dTotalVAP
        districtingGeo['MINORITYVAP'] = dMinorityVAP
        districtingGeo['DNEIGHBORS'] = dAdjacentDistricts
        districtingGeo['NUMCOUNTIES'] = dNumCounties
        districtingGeo['PRECINCTDATA'] = precinctData
        districtingGeo.to_file(outputFilename, driver='GeoJSON')

    with open(outputFilename, 'r') as dGeoJSON:
        dGeoJSONData = json.load(dGeoJSON)
        districtingJSON['districtsGeoJSON'] = dGeoJSONData
    dGeoJSON.close()
    summaryJSON['districtings'].append(districtingJSON)

def main(resultsFile):
    with open(resultsFile, 'r') as rFile:
        rData = json.load(rFile)
        jobID = rData['job']['jobId']
        state = rData['job']['state']
        precinctFile = precinctPath.format(state)
        districtFile = districtPath.format(state)
        demoGroups = rData['job']['demographicGroups']
        constraintsJSON['compactnessLimit'] = rData['job']['compactness']
        constraintsJSON['populationDifferenceLimit'] = rData['job']['popEqThreshold']
        constraintsJSON['minorityGroups'] = demoGroups
        calculateMVAP(districtFile)
        summaryJSON['stateID'] = state
        extremeDistricting = rData['extremeDistricting']
        averageDistricting = rData['averageDistricting']
        randomDistricting = rData['randomDistricting']

        summaryJSON['averageDistricting'] = averageDistricting['districtingPlanID']
        summaryJSON['extremeDistricting'] = extremeDistricting['districtingPlanID']

        outputExtremeFilename = "jobID{}_{}_extreme_districting.geojson".format(jobID, state)
        outputAverageFilename = "jobID{}_{}_average_districting.geojson".format(jobID, state)
        outputRandomFilename = "jobID{}_{}_random_districting.geojson".format(jobID, state)

        createDistrictingGEOJSON(extremeDistricting, extremeJSON, precinctFile, outputExtremeFilename)
        createDistrictingGEOJSON(averageDistricting, averageJSON, precinctFile, outputAverageFilename)
        createDistrictingGEOJSON(randomDistricting, randomJSON, precinctFile, outputRandomFilename)

    rFile.close()
    outputSummaryFilename = "jobID{}_{}.json".format(jobID, state)
    with open(outputSummaryFilename, 'w') as summaryOut:
        json.dump(summaryJSON, summaryOut, indent=2)
        summaryOut.close()

if __name__ == '__main__':
    if len(sys.argv) != 2:
        print("Usage: createDistricting.py [results json]")
        sys.exit()
    main(sys.argv[1])
    