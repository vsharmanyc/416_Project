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

    # def combine_clusters(self, clust1, clust2):
    #     # collapsed clust 2 into clust 1
    #     # clust2 = self.find_node(clust2)
    #     for clust in self.nodes:
    #         if clust.parent_cluster == clust2.parent_cluster:
    #             clust.parent_cluster = clust1.parent_cluster
    #     clust1.neighbors.remove(clust2.id)
    #     clust2.neighbors.remove(clust1.id)
    #     if (clust1, clust2) in self.edges:
    #         self.edges.remove((clust1, clust2))
    #     if (clust2, clust1) in self.edges:
    #         self.edges.remove((clust2, clust1))
    def combine_clusters(self, clust1, clust2):
        # print("COMBINING", (clust2, clust2))
        neighbors = self.get_neighbors(clust2)
        neigh_1 = self.get_neighbors(clust1)
        # print("Len clust1 neigh", len(neigh_1))
        # print("Len clust2 neigh", len(neighbors))
        if (clust1, clust2) in self.edges:
            # print("REMOVED")
            self.edges.remove((clust1, clust2))
        elif (clust2, clust1) in self.edges:
            # print("REMOVED")
            self.edges.remove((clust2, clust1))
        # print("Neighbors", neighbors)
        # print(len(neighbors))
        # for edge in self.edges:
        #     if edge[0].id == clust2.id:
        #         if edge[1].id != clust1.id:
        #             self.edges.append((clust1, edge[1]))
        #         self.edges.remove(edge)
        #     elif edge[1].id == clust2.id:
        #         if edge[0].id != clust1.id:
        #             self.edges.append((edge[0], clust1))
        #         self.edges.remove(edge)
        while len(neighbors) != 0:
            for neigh in neighbors:
                if (clust2, neigh) in self.edges:
                    # print("Deleting", (clust2, neigh))
                    self.edges.remove((clust2, neigh))
                    # print("Edge len", len(self.edges))
                    if neigh != clust1:
                        self.edges.append((clust1, neigh))
                if (neigh, clust2) in self.edges:
                    # print("Deleting", (neigh, clust2))
                    self.edges.remove((neigh, clust2))
                    # print("Edge len", len(self.edges))
                    if neigh != clust1:
                        self.edges.append((neigh, clust1))
            neighbors = self.get_neighbors(clust2)
        neigh_1 = self.get_neighbors(clust1)
        # print("New len clust1 neigh", len(neigh_1))
        # if (len(neighbors) > 0):
        #     print("ERROR", clust2)
        #     print("New len clust2 neigh", len(neighbors))
        #     print("Unchecked neighbors", neighbors)
        # print("CLust1  LENGTH", len(clust1.nodes))
        # print("CLust2  LENGTH", len(clust2.nodes))
        for node in clust2.nodes:
            if node not in clust1.nodes:
                clust1.nodes.append(node)
            # print("Removed", node)
            # clust2.nodes.remove(node)
        #print("CLust1 LENGTH 2", len(clust1.nodes))
        #print(clust1.nodes)
        #print("CLust2  LENGTH 2", len(clust2.nodes))
        #print(clust2.nodes)
        self.nodes.remove(clust2)
        # print("combination complete")
