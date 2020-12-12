import json
class District_test:
    def __init__(self, precincts, id, neighbors):
        self.DISTRICTID = id
        self.DISTRICTNEIGHBORS = neighbors
        self.precincts = precincts
        self.determineMvap()

    def determineMvap(self):
        mvap = 0
        tvap = 0
        for precinct in self.precincts:
            mvap += precinct.BVAP + precinct.AIANVAP
            tvap += precinct.TOTVAP
        mvap = mvap / tvap
        self.MVAP = mvap

    def set_neighs(self, neighbors):
        self.DISTRICTNEIGHBORS = neighbors

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__,
                          sort_keys=True, indent=4)