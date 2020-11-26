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
    i = 1
    one = 0
    two = 0
    no_edge = 0
    while i <= 50:
        print("================================================ITERATION", i, "========================================"
                                                                              "========")
        # print_graph(graph, ("Iteration " + str(i) + " Graph"))
        clusts = combine_random_clusters(graph)
        if len(clusts[1]) == 1 and len(clusts[2]) == 1:
            two += 1
        elif len(clusts[1]) == 1 or len(clusts[2]) == 1:
            one += 1
        #print_graph(graph, "Plot: Combined graph of clusters " + str(clusts[0]) + " " + str(clusts[1]))
        generate_spanning_tree(clusts[0], graph)
        # if len(clusts[0].spanning_tree_edges) < 50:
        #     print_spanning_tree(clusts[0].nodes, clusts[0].spanning_tree_edges, "Spanning tree")
        edge_list = find_acceptable_edges(clusts[0], [clusts[1], clusts[2]], 0.005, ideal_population_per_district)
        if len(edge_list) == 0:
            print("No edges found. Reverting to original two clusters...")
            no_edge += 1
            clust1 = create_cluster(clusts[1], clusts[3][0])
            clust2 = create_cluster(clusts[2], clusts[3][1])
            graph.split_cluster(clusts[0], clust1, clust2)
            graph.determine_edges()
            continue
        cut_from_acceptable_edges(edge_list, clusts[0], clusts[3], graph)
        graph.determine_edges()
        i += 1

    for cluster in graph.nodes:
        print_cluster(cluster.nodes, "Graph of final cluster")
    print("Number of times chosen two one nodes", two)
    print("Number of times chosen one one nodes", one)
    print("Number of times unable to find an edge", no_edge)


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
    for clust in graph.nodes:
        graph.recalculate_cluster_neighbors(clust, False)
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
    #print_cluster(clust1.nodes, "Graphed: Cluster 1 original")
    clust1_id = clust1.id
    prev_clust1 = clust1.nodes.copy()
    neighbors = graph.new_get_neighbors(clust1)
    print(neighbors)
    print(clust1.neighbors)
    clust2 = neighbors[random.randrange(0, len(neighbors))]
    #print_cluster(clust2.nodes, "Graphed: Cluster 2 original")
    clust2_id = clust2.id
    prev_clust2 = clust2.nodes.copy()
    print("Randomly chose clusters", clust1, clust2, "to be combined.")
    # if len(clust1.nodes) < 50:
    #     print_cluster(clust1.nodes, "Clust")
    # if len(clust2.nodes) < 50:
    #     print_cluster(clust2.nodes, ":Clust2")
    # combine these two in our graph.
    graph.combine_clusters(clust1, clust2)
    return [clust1, prev_clust1, prev_clust2, (clust1_id, clust2_id)]


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
    # clust.determine_edges()
    while queue:
        node = queue.pop()
        visited.append(node)
        neighbors = clust.new_get_neighbors(node)
        for neighbor in neighbors:
            if neighbor not in visited:
                queue.append(neighbor)
                visited.append(neighbor)
                if (neighbor, node) not in edges:
                    edges.append((node, neighbor))
    clust.set_spanning_tree(edges)
    # print_spanning_tree(clust.nodes, edges, "Plot: Spanning tree for " + str(clust))


def find_acceptable_edges(clust, prev_clusts, target_diff, ideal_pop):
    print("Looking for accepted or improved edges...")
    # calculating the clusters' pop differences.
    prev_clusts_diff = calculate_previous_clusters_pop_diff(prev_clusts, ideal_pop)
    # find start node to conduct bfs from
    start = clust.find_start_node()
    # for node in clust.nodes:
    #     if len(node.NEIGHBORS) == 1:
    #         start = node
    #         break
    # conduct BFS from this node.
    visited_pop = 0
    unvisited_pop = 0
    for node in clust.nodes:
        unvisited_pop += node.TOTAL
    # bfs
    queue = []
    visited = []
    # accepted_pop_edges = []
    # improved_pop_edges = []
    edge_list = []
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
            if neighbor not in visited and neighbor is not None:
                queue.append(neighbor)
                visited.append(neighbor)
                # check if such an edge is better/acceptable.
                if check_new_pop(unvisited_pop, visited_pop, ideal_pop, target_diff, prev_clusts_diff):
                    edge_list.append((node, neighbor))
    # print("Edges found:", edge_list)
    return edge_list


def check_new_pop(unvisited_pop, visited_pop, ideal_pop, target_diff, prev_clusts_diff):
    print("Checking cut population")
    pop1_diff = abs(unvisited_pop - ideal_pop) / ideal_pop
    pop2_diff = abs(visited_pop - ideal_pop) / ideal_pop
    print("New populations determined to be", pop1_diff, pop2_diff)
    if pop1_diff < target_diff or pop2_diff < target_diff:
        # if acceptable
        print("ACCEPTED: Edge splits into", pop1_diff, pop2_diff)
        return True
    elif (pop1_diff + pop2_diff) <= (prev_clusts_diff[0] + prev_clusts_diff[1]):
        # if improvement
        print("IMPROVED: Edge splits into", pop1_diff, pop2_diff)
        return True
    return False


def check_new_compactness(clust, visited, prev_clusts_diff):
    print("Checking cut compactness...")
    # calculate interior edges. Then exterior edges. Then take ratio.
    vis_num_exterior_edges = 0
    vis_edges = []
    vis_num_edges = 0
    unvis_num_edges = 0
    unvis_num_exterior_edges = 0
    # find nodes in each portion.
    unvisited = []
    unvis_edges = []
    unvisited_ids = []
    visited_ids = []
    for node in clust.nodes:
        if node not in visited:
            unvisited.append(node)
            unvisited_ids.append(node.GEOID10)
        else:
            visited_ids.append(node.GEOID10)
    # first find interior edges, then exterior.
    for node in clust.nodes:
        if node in visited:
            for neighbor in node.NEIGHBORS:
                if (neighbor, node) not in vis_edges and (node, neighbor) not in vis_edges:
                    vis_edges.append((node, neighbor))
                    if neighbor not in visited_ids:
                        vis_num_exterior_edges += 1
                    vis_num_edges += 1
        else:
            for neighbor in node.NEIGHBORS:
                if (neighbor, node) not in unvis_edges and (node, neighbor) not in unvis_edges:
                    unvis_edges.append((node, neighbor))
                    if neighbor not in unvisited_ids:
                        unvis_num_exterior_edges += 1
                    unvis_num_edges += 1

    unvis_ratio = unvis_num_edges / unvis_num_exterior_edges
    vis_ratio = vis_num_edges / vis_num_exterior_edges

    print("Unvisited: ", unvis_num_edges, unvis_num_exterior_edges)
    print("Visited: ", vis_num_edges, vis_num_exterior_edges)
    print("New edge ratios", unvis_ratio, vis_ratio)

    return True


def calculate_previous_clusters_pop_diff(prev_clusts, ideal_pop):
    prev_clusts_diff = []
    pop = 0
    for node in prev_clusts[0]:
        pop += node.TOTAL
    diff = abs(pop - ideal_pop) / ideal_pop
    prev_clusts_diff.append(diff)
    pop = 0
    for node in prev_clusts[1]:
        pop += node.TOTAL
    diff = abs(pop - ideal_pop) / ideal_pop
    prev_clusts_diff.append(diff)
    print("Prev clusters differance was", prev_clusts_diff)
    return prev_clusts_diff


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


def cut_from_acceptable_edges(edge_list, clust, ids, graph):
    print("Randomly choosing an edge to cut from the gathered list...")
    edge = edge_list[random.randrange(0, len(edge_list))]
    print("Randomly chose edge", edge, "to cut. Paritioning into two separate clusters now...")
    # remove the edge.
    if edge in clust.spanning_tree_edges:
        clust.spanning_tree_edges.remove(edge)
    else:
        clust.spanning_tree_edges.remove((edge[1], edge[0]))
    # 2 bfs' to get both of the node lists.
    nodes_1 = accumulate_bfs(clust, edge[0])
    nodes_2 = accumulate_bfs(clust, edge[1])

    clust_1 = create_cluster(nodes_1, ids[0])
    clust_2 = create_cluster(nodes_2, ids[1])
    graph.split_cluster(clust, clust_1, clust_2)
    print("Split into clusters", clust_1, clust_2)
    #print_cluster(clust_1.nodes, "Graphed: Cluster 1")
    #print_cluster(clust_2.nodes, "Graphed: Cluster 2")
    print(clust_1.neighbors)
    print(clust_2.neighbors)


def create_cluster(nodes, id):
    clust = Cluster(nodes, id)
    for node in clust.nodes:
        node.cluster_id = clust.id
    return clust

def accumulate_bfs(clust, start):
    queue = []
    visited = []
    queue.append(start)
    visited.append(start)
    while queue:
        node = queue.pop()
        visited.append(node)
        neighbors = clust.get_mst_neighbors(node)
        for neighbor in neighbors:
            if neighbor not in visited:
                queue.append(neighbor)
                visited.append(neighbor)
    return visited

algorithm()
