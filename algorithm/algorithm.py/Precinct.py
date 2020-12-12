class Precinct:
    def __init__(self, GEOID10, PRECINCTID, TOTAL, TOTVAP, COUNTYID, COUNTY):
        self.GEOID10 = GEOID10
        self.PRECINCTID = PRECINCTID
        self.TOTAL = TOTAL
        self.TOTVAP = TOTVAP
        self.COUNTYID = COUNTYID
        self.COUNTY = COUNTY
        self.AIANTOT = -1
        self.AIANVAP = -1
        self.BTOT = -1
        self.BVAP = -1
        self.HTOT = -1
        self.HVAP = -1
        self.NHOPTOT = -1
        self.NHOPVAP = -1
        self.ATOT = -1
        self.AVAP = -1
        neighbors = []

    def set_asian(self, aiantot, aianvap):
        self.AIANTOT = aiantot
        self.AIANVAP = aianvap

    def set_aa(self, aatot, aavap):
        self.BTOT = aatot
        self.BVAP = aavap

    def set_h(self, htot, hvap):
        self.HTOT = htot
        self.HVAP = hvap

    def set_nhop(self, nhoptot, nhopvap):
        self.NHOPTOT = nhoptot
        self.NHOPVAP = nhopvap

    def set_a(self, atot, avap):
        self.ATOT = atot
        self.AVAP = avap

    def determine_mvap(self, request):
        total = self.TOTVAP
        mvap = 0
        minorities = request.demographicGroups
        for minority in minorities:
            if minority == "AFRICAN_AMERICAN":
                mvap += self.BVAP
            elif minority == "ASIAN":
                mvap += self.AIANVAP
            elif minority == "HISPANIC_LATINO":
                mvap += self.HVAP
            elif minority == "AM_INDIAN_AK_NATIVE":
                mvap += self.AVAP
            elif minority == "NH_OR_OPI":
                mvap += self.NHOPVAP
        if self.TOTVAP != 0:
            self.MVAP = mvap / total
        else:
            self.MVAP = 0

