class Node:
    def __init__(self, precinct):
        for attr in precinct:
            setattr(self, attr, precinct[attr])
        self.visited = False
        self.cluster_id = self.GEOID10

    def __repr__(self):
        return str(self.GEOID10)