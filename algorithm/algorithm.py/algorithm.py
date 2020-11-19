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

    print("Determining a Seed Districting...")
    graph = determine_seed_districting(nodes)
    print(graph)
    i = 0
    while i < 8:
        print_cluster(graph[i].nodes)
        i += 1
    # while True:
    clusters = combine_random_clusters(graph)
    print("Combining random clusters...")
    print("Clusters", clusters, " were combined into cluster 0")
    print_graph(graph)


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


def determine_seed_districting(nodes):
    districts = determine_nodes_per_subgraph(nodes)
    k = int(len(nodes)/districts)
    # main seed algorithm
    graph = []
    nodes_copy = nodes.copy()
    i = 1
    while i <= districts:
        clust = []
        while ((len(clust) < k) or (k > len(nodes) and len(clust) < k)) and len(nodes) > 0:
            node = nodes[random.randrange(0, len(nodes))]
            print("Top of recursive stack. Chose random node", node, ". Looking into its neighbors...")
            generate_cluster(nodes, node,  k, clust)
        graph.append(Cluster(clust, i))
        i += 1
    # set neighbors
    graph = calculate_cluster_neighbors(graph)
    return graph


def calculate_cluster_neighbors(graph):
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
    return graph


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


def contains_node(nodes, id):
    for node in nodes:
        if node.GEOID10 == id:
            return node
    return None


def generate_cluster(nodes, node, k, clust):
    # pick random node
    print("Removed Node", node, " from unvisited nodes.")
    clust.append(node)
    nodes.remove(node)
    neighbors = node.NEIGHBORS.copy()
    node_id = neighbors[random.randrange(0, len(neighbors))]
    print("Chose node", node, "next")
    # find a viable neighbor that isn't visited
    node = contains_node(nodes, int(node_id))
    while node is None:
        print("Cant use this node as we already visited it.")
        neighbors.remove(node_id)
        if len(neighbors) == 0:
            return
        print("Found neighbor. Choosing this one as a part of the cluster...")
        node_id = neighbors[random.randrange(0, len(neighbors))]
        node = contains_node(nodes, int(node_id))

    # recursive call
    if k > 0 and len(nodes) > 0:
        k -= 1
        print("Recursively visiting node", node)
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
            if index != None:
                if not G.has_edge(clusters[index], node):
                    G.add_edge(node, clusters[index])

    nx.draw(G, with_labels=True, font_weight='light')
    print(len(G.edges))
    print(len(G.nodes))
    plt.show()


def print_cluster(graph):
    G = nx.Graph()
    for node in graph:
        neighbors = node.NEIGHBORS
        G.add_node(node)
        for neighbor in neighbors:
            index = find_node_index(graph, neighbor)
            if index != None:
                if not G.has_edge(graph[index], node):
                    G.add_edge(node, graph[index])

    nx.draw(G, with_labels=False, font_weight='light')
    print(len(G.edges))
    print(len(G.nodes))
    plt.show()


def combine_random_clusters(graph):
    copied_graph = graph.copy()
    clust1 = copied_graph[random.randrange(0, len(copied_graph)-1)]
    copied_graph.remove(clust1)
    clust2 = copied_graph[random.randrange(0, len(copied_graph)-1)]

    graph.remove(clust1)
    graph.remove(clust2)

    nodes = clust1.nodes
    nodes.extend(clust2.nodes)
    combine_clust = Cluster(nodes, 0)
    combine_clust.neighbors = clust1.neighbors
    for neighbor in clust2.neighbors:
        if neighbor not in combine_clust.neighbors:
            combine_clust.neighbors.append(neighbor)
    graph.append(combine_clust)
    graph = calculate_cluster_neighbors(graph)

    return [clust1, clust2]


algorithm()
