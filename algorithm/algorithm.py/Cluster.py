class Cluster:
    def __init__(self, nodes, id):
        self.id = id
        self.parent_cluster = id
        self.nodes = nodes
        self.neighbors = []
        self.edges = []
        self.spanning_tree_edges = []

    def set_neighbors(self, neighbors):
        self.neighbors = neighbors

    def determine_edges(self):
        edges = []
        for node in self.nodes:
            for neighbor in node.NEIGHBORS:
                node_neighbor = self.find_node(neighbor)
                if node_neighbor is not None and (node_neighbor, node) not in edges:
                    edges.append((node, node_neighbor))

        self.edges = edges

    def find_node(self, neighbor):
        for node in self.nodes:
            if node.GEOID10 == int(neighbor):
                return node

    def get_neighbors(self, node):
        neighbor_list = []
        for edge in self.edges:
            if edge[0] == node:
                neighbor_list.append(edge[1])
            if edge[1] == node:
                neighbor_list.append(edge[0])
        return neighbor_list

    def set_spanning_tree(self, edges):
        self.spanning_tree_edges = edges

    def __repr__(self):
        return "<Cluster " + str(self.id) + ">"
