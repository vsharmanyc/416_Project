import json
import random
from Node import Node
from Request import Request
import networkx as nx
import matplotlib.pyplot as plt
from Cluster import Cluster
from Graph import Graph


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
    ideal_population_per_district = 0
    for node in nodes:
        ideal_population_per_district += node.TOTAL
    ideal_population_per_district = ideal_population_per_district / 8
    # parsing nodes into clusters
    clusts = []
    for node in nodes:
        clusts.append(Cluster([node], int(node.GEOID10)))
    # find its neighbor clusters
    for clust in clusts:
        set_neighboring_clusts(clust)

    # graph of clusters.
    graph = Graph(clusts)
    print("Determining a Seed Districting...")
    seed = determine_seed_districting(graph)
    # clean up neighboring clusters
    graph.clean_up_edges()
    # for clust in graph.nodes:
    #     print_cluster(clust.nodes)
    # print_graph(graph)

    # algorithm begins. Find neighboring clusters to combine.
    print("Beginning algorithm loop")
    clusts = combine_random_clusters(graph)
    #print_graph(graph, "Plot: Combined graph of clusters " + str(clusts[0]) + " " + str(clusts[1]))
    generate_spanning_tree(clusts[0], graph)
    find_acceptable_edges(clusts[0], [clusts[1], clusts[2]], 0.005, ideal_population_per_district)


def set_neighboring_clusts(clust):
    node = clust.nodes[0]
    clust_neigh = []
    for neighbor in node.NEIGHBORS:
        if neighbor == "000000000":
            continue
        clust_neigh.append(int(neighbor))
    clust.set_neighbors(clust_neigh)


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


def determine_seed_districting(graph):
    # districts = determine_nodes_per_subgraph(graph.nodes)
    # wrap node in cluster
    ext_clusts = []
    # combine clusters randomly until we have target districts
    while len(graph.nodes) != 8:
        clust = graph.nodes[random.randrange(0, len(graph.nodes))]
        neighs = graph.get_neighbors(clust)
        if neighs == []:
            graph.nodes.remove(clust)
            ext_clusts.append(clust)
            continue
        neigh_clust = neighs[random.randrange(0, len(neighs))]
        graph.combine_clusters(clust, neigh_clust)
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


# def contains_node(nodes, id):
#     for node in nodes:
#         if node.GEOID10 == id:
#             return node
#     return None


# def generate_cluster(nodes, node, k, clust):
#     # pick random node
#     print("Removed Node", node, " from unvisited nodes.")
#     clust.append(node)
#     nodes.remove(node)
#     neighbors = node.NEIGHBORS.copy()
#     node_id = neighbors[random.randrange(0, len(neighbors))]
#     print("Chose node", node, "next")
#     # find a viable neighbor that isn't visited
#     node = contains_node(nodes, int(node_id))
#     while node is None:
#         print("Cant use this node as we already visited it.")
#         neighbors.remove(node_id)
#         if len(neighbors) == 0:
#             return
#         print("Found neighbor. Choosing this one as a part of the cluster...")
#         node_id = neighbors[random.randrange(0, len(neighbors))]
#         node = contains_node(nodes, int(node_id))
#
#     # recursive call
#     if k > 0 and len(nodes) > 0:
#         k -= 1
#         print("Recursively visiting node", node)
#         generate_cluster(nodes, node, k, clust)


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


def print_graph(graph, title):
    G = nx.Graph()
    clusters = graph.nodes
    for node in clusters:
        neighbors = graph.get_neighbors(node)
        G.add_node(node)
        for neighbor in neighbors:
            if not G.has_edge(neighbor, node):
                G.add_edge(node, neighbor)

    nx.draw(G, with_labels=True, font_weight='light')
    print(len(G.edges))
    print(len(G.nodes))
    print(title)
    plt.show()


def print_cluster(graph, title):
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
    print(title)
    plt.show()


def print_spanning_tree(nodes, edges, title):
    G = nx.Graph()
    for node in nodes:
        G.add_node(node)
    for edge in edges:
        G.add_edge(edge[0], edge[1])
    nx.draw(G, with_labels=False, font_weight='light')
    print(len(G.edges))
    print(len(G.nodes))
    print(title)
    plt.show()


# def combine_random_clusters(graph):
#     copied_graph = graph.copy()
#     clust1 = copied_graph[random.randrange(0, len(copied_graph)-1)]
#     copied_graph.remove(clust1)
#     clust2 = copied_graph[random.randrange(0, len(copied_graph)-1)]
#
#     graph.remove(clust1)
#     graph.remove(clust2)
#
#     nodes = clust1.nodes
#     nodes.extend(clust2.nodes)
#     combine_clust = Cluster(nodes, 0)
#     combine_clust.neighbors = clust1.neighbors
#     for neighbor in clust2.neighbors:
#         if neighbor not in combine_clust.neighbors:
#             combine_clust.neighbors.append(neighbor)
#     graph.append(combine_clust)
#     graph = calculate_cluster_neighbors(graph)
#
#     return [clust1, clust2]

def combine_random_clusters(graph):
    print("Combining random clusters")
    clust1 = graph.nodes[random.randrange(0, len(graph.nodes))]
    prev_clust1 = clust1.nodes.copy()
    neighbors = graph.get_neighbors(clust1)
    clust2 = neighbors[random.randrange(0, len(neighbors))]
    prev_clust2 = clust2.nodes.copy()
    print("Randomly chose clusters", clust1, clust2, "to be combined.")
    # combine these two in our graph.
    graph.combine_clusters(clust1, clust2)
    return [clust1, prev_clust1, prev_clust2]


def generate_spanning_tree(clust, graph):
    print("Generating a spanning tree of cluster", clust)
    #print_cluster(clust.nodes, "Plot: Combined cluster " + str(clust))
    # simply doing a bfs on the cluster.
    queue = []
    visited = []
    edges = []
    first_node = clust.nodes[random.randrange(0, len(clust.nodes))]
    queue.append(first_node)
    visited.append(first_node)
    clust.determine_edges()
    while queue:
        node = queue.pop()
        visited.append(node)
        neighbors = clust.get_neighbors(node)
        for neighbor in neighbors:
            if neighbor not in visited:
                queue.append(neighbor)
                visited.append(neighbor)
                if (neighbor, node) not in edges:
                    edges.append((node, neighbor))
    clust.set_spanning_tree(edges)
    # print_spanning_tree(clust.nodes, edges, "Plot: Spanning tree for " + str(clust))


def find_acceptable_edges(clust, prev_clusts, pop_diff, ideal_pop):
    # loop through all its edges in spanning tree, and see if they work.
    print("Looking for accepted or improved edges...")
    find_pop_accepted_edges(clust, prev_clusts, pop_diff, ideal_pop)
    # i = 0
    # for edge in clust.spanning_tree_edges:
    #     # creates bfs from each node in edge.
    #     # print("Examining edge", edge)
    #
    #     node_set_1 = modified_bfs(clust, edge[0], edge[1])
    #     node_set_2 = modified_bfs(clust, edge[1], edge[0])
    #     # print(len(clust.nodes))
    #     # print("Nodes 1", len(node_set_1))
    #     # print("Nodes 2", len(node_set_2))
    #     print(i)
    #     i += 1


def find_pop_accepted_edges(clust, prev_clusts, target_diff, ideal_pop):
    # do a bfs, calculating acceptability of two subgraphs at each point.
    # find a starting point (leaf node)
    prev_clusts_diff = []
    # calculating the clusters' pop differences.
    pop = 0
    for node in prev_clusts[0]:
        pop += node.TOTAL
    diff = abs(pop - ideal_pop)/ideal_pop
    prev_clusts_diff.append(diff)
    pop = 0
    for node in prev_clusts[1]:
        pop += node.TOTAL
    diff = abs(pop - ideal_pop) / ideal_pop
    prev_clusts_diff.append(diff)
    print("Prev clusters differance was", prev_clusts_diff)
    start = None
    for node in clust.nodes:
        if len(node.NEIGHBORS) == 1:
            start = node
            break
    # conduct BFS from this node.
    visited_pop = 0
    unvisited_pop = 0
    for node in clust.nodes:
        unvisited_pop += node.TOTAL
    # bfs
    queue = []
    visited = []
    accepted_pop_edges = []
    improved_pop_edges = []
    queue.append(start)
    visited.append(start)
    while queue:
        node = queue.pop()
        visited.append(node)
        # moving along bfs, recalculate both sides of the MST
        unvisited_pop -= node.TOTAL
        visited_pop += node.TOTAL

        neighbors = clust.get_mst_neighbors(node)
        for neighbor in neighbors:
            if neighbor not in visited:
                queue.append(neighbor)
                visited.append(neighbor)
                # check if such an edge is better/acceptable.
                pop1_diff = abs(unvisited_pop-ideal_pop)/ideal_pop
                pop2_diff = abs(visited_pop - ideal_pop) / ideal_pop
                if pop1_diff < target_diff or pop2_diff < target_diff:
                    # if acceptable
                    print("ACCEPTED: Edge splits into", pop1_diff, pop2_diff)
                    accepted_pop_edges.append((node, neighbor))
                elif (pop1_diff + pop2_diff) < (prev_clusts_diff[0] + prev_clusts_diff[1]):
                    # if improvement
                    print("IMPROVED: Edge splits into", pop1_diff, pop2_diff)
                    improved_pop_edges.append((node, neighbor))
    #print("Found some accepted edges", accepted_pop_edges)
    #print("Found some improved edges", improved_pop_edges)
    return [accepted_pop_edges, improved_pop_edges]


def modified_bfs(clust, start, avoid):
    # bfs starting at starting node, avoiding other side of edge we are looking at.
    edges = clust.edges.copy()
    clust.edges = clust.spanning_tree_edges
    queue = []
    visited = []
    nodes = []
    queue.append(start)
    visited.append(start)
    while queue:
        node = queue.pop()
        if node not in nodes:
            nodes.append(node)
        visited.append(node)
        neighbors = clust.get_neighbors(node)
        for neighbor in neighbors:
            if neighbor not in visited and neighbor is not avoid:
                queue.append(neighbor)
                visited.append(neighbor)
    return nodes


algorithm()
