import json


class Request:
    def __init__(self, job):
        for attr in job:
            setattr(self, attr, job[attr])

    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__,
                          sort_keys=True, indent=4)