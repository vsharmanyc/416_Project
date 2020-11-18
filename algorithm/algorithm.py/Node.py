class Node:
    def __init__(self, precinct):
        for attr in precinct:
            setattr(self, attr, precinct[attr])

    def __str__(self):
        return self.PRECINCTID