import json
import random
from Node import Node
from Request import Request
import networkx as nx
import matplotlib.pyplot as plt
from Cluster import Cluster

def parse_data_from_file():
    f = open("swData_job1.json")
    data = json.load(f)
    job = data["data"][0]
    precincts = data["data"][1]
    nodes = parse_precincts_from_json(precincts["precincts"])
    request = parse_job_from_json(job)
    return [nodes, request]


def algorithm():
    # data aggregation
    data = parse_data_from_file()
    nodes = data[0]
    request = data[1]
    # nodes = parse_neighbors(nodes)

    determine_seed_districting(nodes)



def parse_job_from_json(job):
    return Request(job)


def parse_precincts_from_json(precincts):
    nodes = []
    for precinct in precincts:
        # Node class pulls all data from precinct dict.
        node = Node(precinct)
        if type(node.NEIGHBORS) == int:
            node.NEIGHBORS = [node.NEIGHBORS]
        else:
            node.NEIGHBORS = node.NEIGHBORS.split(',')
        nodes.append(node)
    return nodes


# def parse_neighbors(nodes):
#     for node in nodes:
#         node_neigh = []
#         if type(node.NEIGHBORS) == int:
#             neighbors = str(node.NEIGHBORS)
#         else:
#             neighbors = node.NEIGHBORS.split(",")
#         for neighbor in neighbors:
#             if neighbor == "NULL":
#                 continue
#             neigh = find_node(nodes, neighbor)
#             if neigh is not None:
#                 node_neigh.append(neigh)
#         node.NEIGHBORS = node_neigh
#     return nodes


def determine_seed_districting(nodes):
    districts = determine_nodes_per_subgraph(nodes)
    k = int(len(nodes)/districts)
    # main seed algorithm
    graph = []
    nodes_copy = nodes.copy()
    i = 1
    while i <= districts:
        clust = []
        while len(clust) < k or (k > len(nodes) and len(clust) < k):
            node = nodes[random.randrange(0, len(nodes))]
            generate_cluster(nodes, node,  k, clust)
        graph.append(Cluster(clust, i))
        i += 1
    # set neighbors
    i = 0
    j = 0
    while i < len(graph):
        clust1 = graph[i]
        j = 0
        while j < len(graph):
            clust2 = graph[j]
            if neighboring_cluster(clust1, clust2):
                if clust2.id not in clust1.neighbors:
                    clust1.neighbors.append(clust2.id)
            j += 1
        i += 1
    print_graph(graph)


def neighboring_cluster(clust1, clust2):
    clust1_neigh = []
    clust2_nodes = []
    for node in clust1.nodes:
        for neighbor in node.NEIGHBORS:
            if neighbor not in clust1_neigh:
                if neighbor != "NULL":
                    clust1_neigh.append(neighbor)
    for node in clust2.nodes:
        clust2_nodes.append(node.GEOID10)
    i = 0
    while i < len(clust1_neigh):
        if int(clust1_neigh[i]) in clust2_nodes:
            return True
        i += 1
    return False


def generate_cluster(nodes, node,  k, clust):
    # pick random node
    clust.append(node)
    nodes.remove(node)

    neighbors = node.NEIGHBORS.copy()
    node = neighbors[random.randrange(0, len(neighbors))]
    # find a viable neighbor that isn't visited
    while node not in nodes:
        neighbors.remove(node)
        if len(neighbors) == 0:
            return
        node = neighbors[random.randrange(0, len(neighbors))]

    # recursive call
    if k > 0 and len(nodes) > 0:
        k -= 1
        generate_cluster(nodes, node, k, clust)


def determine_nodes_per_subgraph(nodes):
    k = 0
    for node in nodes:
        if node.DISTRICTID > k:
            k = node.DISTRICTID
    return k


def find_node_index(nodes, id):
    i = 0
    while i < len(nodes):
        if nodes[i].GEOID10 == int(id):
            return i
        i += 1
    return None


def find_cluster_index(clusters, id):
    i = 0
    while i < len(clusters):
        if clusters[i].id == id:
            return i
        i += 1
    return None


def print_graph(clusters):
    G = nx.Graph()
    for node in clusters:
        neighbors = node.neighbors
        G.add_node(node)
        for neighbor in neighbors:
            index = find_cluster_index(clusters, neighbor)
            if not G.has_edge(clusters[index], node):
                G.add_edge(node, clusters[index])

    nx.draw(G, with_labels=False, font_weight='light')
    print(len(G.edges))
    print(len(G.nodes))
    plt.show()


algorithm()
