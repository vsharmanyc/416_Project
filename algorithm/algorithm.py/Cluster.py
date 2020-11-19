class Cluster:
    def __init__(self, nodes, id):
        self.id = id
        self.nodes = nodes
        self.unvisited = nodes
        self.neighbors = []

    def reset_unvisited(self):
        self.unvisited = self.nodes
