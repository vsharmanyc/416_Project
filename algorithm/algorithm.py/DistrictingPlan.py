import json
class DistrictingPlan:
    def __init__(self, districts):
        self.districting = districts

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__,
                          sort_keys=True, indent=4)