import mysql.connector
import csv
import configparser

config = "./insertIntoDB_config.txt"

cp = configparser.ConfigParser()
cp.read(config)

dbHost = cp['database']['host']
dbUser = cp['database']['user']
dbPassword = cp['database']['password']
dbDatabase = cp['database']['db']
csvState = cp['files']['states']
csvDistrict = cp['files']['districts']
csvPrecinct = cp['files']['precincts']


def populateStates(csvFile):
    SQL_STRING = "REPLACE INTO states (state_id, state_name) VALUES (%s, %s)"
    with open(csvFile) as csv_in:
        readCSV = csv.reader(csv_in, delimiter=',')
        header = next(readCSV)
        for row in readCSV:
            stateID = row[header.index("STATEID")]
            stateName = row[header.index("STATENAME")]
            cursor.execute(SQL_STRING, (stateID, stateName))
            cse416db.commit()
    csv_in.close()

def populateDistricts(csvFile):
    SQL_STRING = "REPLACE INTO districts (district_id, state_id, total_pop, total_vap, \
                hispanic_tot, hispanic_vap, white_tot, white_vap, black_tot, black_vap, \
                aian_tot, aian_vap, asian_tot, asian_vap, nhop_tot, nhop_vap, other_tot, other_vap) \
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"
    with open(csvFile) as csv_in:
        readCSV = csv.reader(csv_in, delimiter=',')
        header = next(readCSV)
        for row in readCSV:
            districtID = int(row[header.index("DISTRICTID")])
            stateID = row[header.index("STATE")]
            totalPop = row[header.index("TOTAL")]
            totalVAP = row[header.index("TOTVAP")]
            hTotal = row[header.index("HTOT")]
            hVAP = row[header.index("HVAP")]
            wTotal = row[header.index("WTOT")]
            wVAP = row[header.index("WVAP")]
            bTotal = row[header.index("BTOT")]
            bVAP = row[header.index("BVAP")]
            aianTotal = row[header.index("AIANTOT")]
            aianVAP = row[header.index("AIANVAP")]
            aTotal = row[header.index("ATOT")]
            aVAP = row[header.index("AVAP")]
            nhopTotal = row[header.index("NHOPTOT")]
            nhopVAP = row[header.index("NHOPVAP")]
            otherTotal = row[header.index("OTHERTOT")]
            otherVAP = row[header.index("OTHERVAP")]
            cursor.execute(SQL_STRING, (districtID, stateID, totalPop, totalVAP, hTotal, hVAP, wTotal, wVAP, bTotal, bVAP, aianTotal, aianVAP, aTotal, aVAP, nhopTotal, nhopVAP, otherTotal, otherVAP))
            cse416db.commit()
    csv_in.close()

def populatePopulationData(header, row, geoID):
    SQL_STRING = "REPLACE INTO population_data (geo_id, total_pop, total_vap, \
                hispanic_tot, hispanic_vap, white_tot, white_vap, black_tot, black_vap, \
                aian_tot, aian_vap, asian_tot, asian_vap, nhop_tot, nhop_vap, other_tot, other_vap) \
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"
    totalPop = row[header.index("TOTAL")]
    totalVAP = row[header.index("TOTVAP")]
    hTotal = row[header.index("HTOT")]
    hVAP = row[header.index("HVAP")]
    wTotal = row[header.index("WTOT")]
    wVAP = row[header.index("WVAP")]
    bTotal = row[header.index("BTOT")]
    bVAP = row[header.index("BVAP")]
    aianTotal = row[header.index("AIANTOT")]
    aianVAP = row[header.index("AIANVAP")]
    aTotal = row[header.index("ATOT")]
    aVAP = row[header.index("AVAP")]
    nhopTotal = row[header.index("NHOPTOT")]
    nhopVAP = row[header.index("NHOPVAP")]
    otherTotal = row[header.index("OTHERTOT")]
    otherVAP = row[header.index("OTHERVAP")]
    cursor.execute(SQL_STRING, (geoID, totalPop, totalVAP, hTotal, hVAP, wTotal, wVAP, bTotal, bVAP, aianTotal, aianVAP, aTotal, aVAP, nhopTotal, nhopVAP, otherTotal, otherVAP))
    cse416db.commit()

def populatePrecincts(csvFile):
    SQL_STRING = "REPLACE INTO precincts \
                (geo_id, district_id, state_id, county_name, \
                precinct_name, precinct_num, neighbors) VALUES (%s, %s, %s, %s, %s, %s, %s)"
    with open(csvFile) as csv_in:
        readCSV = csv.reader(csv_in, delimiter=',')
        header = next(readCSV)
        print(header)
        for row in readCSV:
            geoID = int(row[header.index("NEWGEOID")])
            print(geoID)
            districtID = int(row[header.index("DISTRICTID")])
            stateID = row[header.index("STATEID")]
            countyName = row[header.index("COUNTY")]
            precinctName = row[header.index("PRECINCT")]
            precinctNum = int(row[header.index("PRECINCTID")])
            precinctNeighbors = row[header.index("NEIGHBORS")]
            cursor.execute(SQL_STRING, (geoID, districtID, stateID, countyName, precinctName, precinctNum, precinctNeighbors))
            cse416db.commit()
            populatePopulationData(header, row, geoID)
    csv_in.close()

cse416db = mysql.connector.connect(
    host=dbHost,
    user=dbUser,
    password=dbPassword,
    db=dbDatabase
)
cursor = cse416db.cursor()
#populateStates(csvState)
populateDistricts(csvDistrict)
#populatePrecincts(csvPrecinct)