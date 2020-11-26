class Graph:
    def __init__(self, nodes):
        # nodes are clusters, basically.
        self.nodes = nodes
        self.edges = []
        self.determine_edges()
        self.cluster_dict = {}
        self.set_cluster_dict()

    def determine_edges(self):
        edges = []
        for node in self.nodes:
            for neighbor in node.neighbors:
                node_neighbor = self.find_node(neighbor)
                if node_neighbor is not None and (node_neighbor, node) not in edges:
                    edges.append((node, node_neighbor))
        self.edges = edges

    def set_cluster_dict(self):
        cluster_dict = {}
        for node in self.nodes:
            cluster_dict[node.id] = node
        self.cluster_dict = cluster_dict

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

    def new_get_neighbors(self, clust):
        neighbor_list = []
        for neighbor in clust.neighbors:
            neighbor_list.append(self.cluster_dict[int(neighbor)])
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
                node.cluster_id = clust1.id
                clust1.node_dict[node.GEOID10] = node
                clust1.nodes.append(node)
        self.nodes.remove(clust2)
        self.cluster_dict.pop(clust2.id)

    def clean_up_edges(self):
        edges = []
        for edge in self.edges:
            if edge not in edges and (edge[1], edge[0]) not in edges:
                edges.append(edge)

        self.edges = edges

    def recalculate_cluster_neighbors(self, clust, debug):
        if debug:
            print("Recalculating neighbors for", clust)
        neighbors = []
        clust_neighbors = []
        # find nodes that lie outside the cluster.
        for node in clust.nodes:
            for neighbor in node.NEIGHBORS:
                if int(neighbor) not in clust.node_dict.keys():
                    if neighbor not in neighbors:
                        neighbors.append(neighbor)
        if debug:
            print("Exterior neighbors in this clust:", neighbors)
        # find them in the overall graph.
        for cluster in self.nodes:
            for node in cluster.nodes:
                for id in neighbors:
                    if node.GEOID10 == int(id):
                        if node.cluster_id not in clust_neighbors:
                            clust_neighbors.append(node.cluster_id)
        if debug:
            print("Neighbors determined to be", clust_neighbors)
        clust.neighbors = clust_neighbors

    def split_cluster(self, clust, clust1, clust2):
        self.nodes.remove(clust)
        self.nodes.append(clust1)
        self.nodes.append(clust2)
        # self.recalculate_cluster_neighbors(clust1, True)
        # # self.nodes.append(clust2)
        # self.recalculate_cluster_neighbors(clust2, True)
        self.recalculate_all_neighbors(clust1.id, clust2.id)
        self.set_cluster_dict()

    def recalculate_all_neighbors(self, id1, id2):
        for node in self.nodes:
            if id1 in node.neighbors or id2 in node.neighbors:
                self.recalculate_cluster_neighbors(node, False)
            elif id2 == node.id or id1 == node.id:
                self.recalculate_cluster_neighbors(node, False)



