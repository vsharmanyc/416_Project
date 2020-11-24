class Graph:
    def __init__(self, nodes):
        # nodes are clusters, basically.
        self.nodes = nodes
        self.edges = []
        self.determine_edges()

    def determine_edges(self):
        edges = []
        for node in self.nodes:
            for neighbor in node.neighbors:
                node_neighbor = self.find_node(neighbor)
                if node_neighbor is not None and (node_neighbor, node) not in edges:
                    edges.append((node, node_neighbor))
        self.edges = edges

    def find_node(self, neighbor):
        for node in self.nodes:
            if node.id == neighbor:
                return node

    def get_neighbors(self, node):
        neighbor_list = []
        for edge in self.edges:
            if edge[0] == node:
                # if edge[0] in self.nodes and edge[1] in self.nodes:
                neighbor_list.append(edge[1])

            if edge[1] == node:
                #if edge[0] in self.nodes and edge[1] in self.nodes:
                neighbor_list.append(edge[0])
        return neighbor_list

    def combine_clusters(self, clust1, clust2):
        neighbors = self.get_neighbors(clust2)
        if (clust1, clust2) in self.edges:
            self.edges.remove((clust1, clust2))
        elif (clust2, clust1) in self.edges:
            self.edges.remove((clust2, clust1))
        while len(neighbors) != 0:
            for neigh in neighbors:
                if (clust2, neigh) in self.edges:
                    self.edges.remove((clust2, neigh))
                    if neigh != clust1:
                        self.edges.append((clust1, neigh))
                if (neigh, clust2) in self.edges:
                    self.edges.remove((neigh, clust2))
                    if neigh != clust1:
                        self.edges.append((neigh, clust1))
            neighbors = self.get_neighbors(clust2)
        for node in clust2.nodes:
            if node not in clust1.nodes:
                clust1.nodes.append(node)
        self.nodes.remove(clust2)

    def clean_up_edges(self):
        edges = []
        for edge in self.edges:
            if edge not in edges and (edge[1], edge[0]) not in edges:
                edges.append(edge)

        self.edges = edges
