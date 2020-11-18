class Request:
    def __init__(self, job):
        for attr in job:
            setattr(self, attr, job[attr])