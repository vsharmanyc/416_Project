class Cluster:
    def __init__(self, nodes, id):
        self.id = id
        self.parent_cluster = id
        self.nodes = nodes
        self.neighbors = set()
        self.edges = []
        self.spanning_tree_edges = []
        self.node_dict = {}
        self.determine_node_dict()
        self.mst_neighbors_dict = {}
        self.node_neighbor_dict = {}
        self.determine_node_neighbor_dict()
        self.node_status = {}

    def set_neighbors(self, neighbors):
        self.neighbors = neighbors

    def determine_node_dict(self):
        for node in self.nodes:
            self.node_dict[node.GEOID10] = node

    def determine_mst_neighbors_dict(self):
        mst_dict = {}
        for node in self.nodes:
            mst_dict[node.GEOID10] = []
        for edge in self.spanning_tree_edges:
            array1 = mst_dict[edge[0].GEOID10]
            array1.append(edge[1])
            array2 = mst_dict[edge[1].GEOID10]
            array2.append(edge[0])
            mst_dict[edge[0].GEOID10] = array1
            mst_dict[edge[1].GEOID10] = array2
        self.mst_neighbors_dict = mst_dict

    def determine_node_neighbor_dict(self):
        node_neighbors = {}
        node_status = {}
        ext_node = False
        for node in self.nodes:
            node_neighbors[node.GEOID10] = []
        for node in self.nodes:
            for neighbor in node.NEIGHBORS:
                if int(neighbor) in self.node_dict.keys():
                    array = node_neighbors[node.GEOID10]
                    array.append(self.node_dict[int(neighbor)])
                    node_neighbors[node.GEOID10] = array
                else:
                    ext_node = True
            if ext_node:
                node_status[node.GEOID10] = True
            else:
                node_status[node.GEOID10] = False
        self.node_neighbor_dict = node_neighbors
        self.node_status = node_status

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

    def new_get_neighbors(self, node):
        neighbor_list = []
        for neighbor in node.NEIGHBORS:
            if int(neighbor) in self.node_dict.keys():
                neighbor_list.append(self.node_dict[int(neighbor)])
        return neighbor_list

    def get_mst_neighbors(self, node):
        neighbor_list = []
        for edge in self.spanning_tree_edges:
            if edge[0] == node:
                neighbor_list.append(edge[1])
            if edge[1] == node:
                neighbor_list.append(edge[0])
        return neighbor_list

    def set_spanning_tree(self, edges):
        self.spanning_tree_edges = edges

    def find_start_node(self):
        # finds a leaf node in mst. Any leaf would do.
        node_dict = {}
        for node in self.nodes:
            node_dict[node.GEOID10] = 0
        for edge in self.spanning_tree_edges:
            node_dict[edge[0].GEOID10] = node_dict[edge[0].GEOID10] + 1
            node_dict[edge[1].GEOID10] = node_dict[edge[1].GEOID10] + 1

        for key in node_dict.keys():
            if node_dict[key] <= 1:
                # print("Found start node", key)
                return self.node_dict[key]

    # def get_compactness(self):
    #     self.determine_node_neighbor_dict()
    #     edge_count = 0
    #     ext_count = 0
    #     for node in self.nodes:
    #         for neighbor in self.node_neighbor_dict[node.GEOID10]:
    #             if neighbor not in self.nodes:
    #                 ext_count+=1
    #             else:
    #                 edge_count +=1
    #     if edge_count == 0 and ext_count == 0:
    #         return 0.0
    #     return ext_count/((edge_count/2)+ext_count)

    def __repr__(self):
        return "<Cluster " + str(self.id) + ">"
