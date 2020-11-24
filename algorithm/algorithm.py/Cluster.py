class Cluster:
    def __init__(self, nodes, id):
        self.id = id
        self.parent_cluster = id
        self.nodes = nodes
        self.neighbors = []

    def set_neighbors(self, neighbors):
        self.neighbors = neighbors

    def merge_cluster(self, clust):
        for node in clust.nodes:
            if node not in self.nodes:
                self.nodes.append(node)
