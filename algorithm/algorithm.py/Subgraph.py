import json
from Precinct import Precinct


class Subgraph:
    def __init__(self, clust, request, graph, clust_id_map):
        self.DISTRICTID = clust.id
        self.DISTRICTNEIGHBORS = clust.neighbors
        precincts = self.shorten_nodes(clust, request, clust_id_map)
        self.MVAP = self.determine_mvap(precincts, request)
        self.precincts = precincts

    def shorten_nodes(self, clust, request, clust_id_map):
        precincts = []
        minorities = request.demographicGroups
        for node in clust.nodes:
            p = Precinct(node.GEOID10, node.PRECINCTID, node.TOTAL, node.TOTVAP, node.COUNTYID,
                         node.COUNTY)
            if "AFRICAN_AMERICAN" in minorities:
                p.set_aa(node.BTOT, node.BVAP)
            if "ASIAN" in minorities:
                p.set_asian(node.AIANTOT, node.AIANVAP)
            if "HISPANIC_LATINO" in minorities:
                p.set_h(node.HTOT, node.HVAP)
            if "AM_INDIAN_AK_NATIVE" in minorities:
                p.set_aa(node.ATOT, node.AVAP)
            if "NH_OR_OPI" in minorities:
                p.set_nhop(node.NHOPTOT, node.NHOPVAP)
            precincts.append(p)
        return precincts

    def determine_mvap(self, precincts, request):
        minorities = request.demographicGroups
        mvap = 0
        tvap = 0
        for precinct in precincts:
            for minority in minorities:
                if minority == "AFRICAN_AMERICAN":
                    mvap += precinct.BVAP
                if minority == "ASIAN":
                    mvap += precinct.AIANVAP
                if minority == "HISPANIC_LATINO":
                    mvap += precinct.HVAP
                if minority == "AM_INDIAN_AK_NATIVE":
                    mvap += precinct.AVAP
                if minority == "NH_OR_OPI":
                    mvap += precinct.NHOPVAP
            tvap += precinct.TOTVAP
        if tvap != 0:
            return mvap / tvap
        else:
            return 0


    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__,
                          sort_keys=True, indent=4)