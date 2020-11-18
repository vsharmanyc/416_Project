"""Node in our cluster of graphs."""


class Node:
    def __init__(self, geoid, state, stateid, county, precinct_id, total, totvap, htot, hvap, wtot, wvap, btot, bvap, aiantot, aianvap, atot,
                 avap, nhoptot, nhopvap, othertot, othervap, district_id, neighbors):
        self.geoid = geoid
        self.state = state
        self.stateid = stateid
        self.county = county
        self.precinct_id = precinct_id
        self.total = total
        self.totvap = totvap
        self.htot = htot
        self.hvap = hvap
        self.wtot = wtot
        self.wvap = wvap
        self.btot = btot
        self.bvap = bvap
        self.aiantot = aiantot
        self.aianvap = aianvap
        self.atot = atot
        self.avap = avap
        self.nhoptot = nhoptot
        self.nhopvap = nhopvap
        self.othertot = othertot
        self.othervap = othervap
        self.district_id = district_id
        self.neighbors = neighbors
