import json
import random
from Node import Node
from Request import Request
import networkx as nx
import matplotlib.pyplot as plt
from Cluster import Cluster
from Graph import Graph
import datetime

debug = True

seed_timer = 0
combine_cluster_times = []
spanning_tree_times = []
acceptable_edge_times = []
cut_edge_times = []
recalculate_neighbor_timess = []
calc_edge_time = []
acceptable_edge_loop_times = []


def parse_data_from_file():
    f = open("swData_job1.json")
    data = json.load(f)
    job = data["data"][0]
    precincts = data["data"][1]
    nodes = parse_precincts_from_json(precincts["precincts"])
    request = parse_job_from_json(job)
    return [nodes, request]


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


def set_neighboring_clusts(clust):
    node = clust.nodes[0]
    clust_neigh = []
    for neighbor in node.NEIGHBORS:
        if neighbor == "000000000":
            continue
        clust_neigh.append(int(neighbor))
    clust.set_neighbors(clust_neigh)


def algorithm():
    # data aggregation
    alg_start = datetime.datetime.utcnow()
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
    if debug:
        print("Determining a Seed Districting...")
    start = datetime.datetime.utcnow()
    seed = determine_seed_districting(graph)
    seed_timer = datetime.datetime.utcnow() - start
    # clean up neighboring clusters
    graph.clean_up_edges()
    graph.recalculate_literally_all_neighbors()
    # for clust in graph.nodes:
    #     print_cluster(clust.nodes, "CLuster")
    # print_graph(graph)

    # algorithm begins. Find neighboring clusters to combine.
    if debug:
        print("Beginning algorithm loop")
    i = 1
    one = 0
    two = 0
    no_edge = 0
    while i <= 1000:
        if debug:
            print("================================================ITERATION", i,
                  "================================================")
        # print_graph(graph, ("Iteration " + str(i) + " Graph"))
        start = datetime.datetime.utcnow()
        clusts = combine_random_clusters(graph)
        combine_cluster_times.append((datetime.datetime.utcnow() - start).microseconds)
        if len(clusts[1]) == 1 and len(clusts[2]) == 1:
            two += 1
        if len(clusts[1]) == 1:
            one += 1
        if len(clusts[2]) == 1:
            one += 1
        # print_graph(graph, "Plot: Combined graph of clusters " + str(clusts[0]) + " " + str(clusts[1]))

        generate_spanning_tree(clusts[0], graph)
        start = datetime.datetime.utcnow()
        edge_list = find_acceptable_edges(clusts[0], [clusts[1], clusts[2]], 0.005, ideal_population_per_district, clusts[4])
        acceptable_edge_times.append((datetime.datetime.utcnow() - start).microseconds)
        if len(edge_list) == 0:
            if debug:
                print("No edges found. Reverting to original two clusters...")
            no_edge += 1
            clust1 = create_cluster(clusts[1], clusts[3][0])
            clust2 = create_cluster(clusts[2], clusts[3][1])
            graph.split_cluster(clusts[0], clust1, clust2)
            graph.determine_edges()
            continue
        cut_from_acceptable_edges(edge_list, clusts[0], clusts[3], graph)
        start = datetime.datetime.utcnow()
        graph.determine_edges()
        calc_edge_time.append((datetime.datetime.utcnow() - start).microseconds)
        i += 1

    for cluster in graph.nodes:
        print_cluster(cluster.nodes, "Graph of final cluster")
    # print("Number of times chosen two one nodes", two)
    # print("Number of times chosen one one nodes", one)
    # print("Number of times unable to find an edge", no_edge)
    print("====================ALGORITHM RESULTS====================")
    print("Total algorithm runtime", datetime.datetime.utcnow() - alg_start)
    print("Seed time", seed_timer)
    print("Avg combine cluster times", sum(combine_cluster_times) / len(combine_cluster_times))
    print("Avg spanning tree times", sum(spanning_tree_times) / len(spanning_tree_times))
    print("Avg spanning tree loop times", sum(acceptable_edge_loop_times) / len(acceptable_edge_loop_times))
    print("Avg acceptable edge times", sum(acceptable_edge_times) / len(acceptable_edge_times))
    print("Avg cut edge times", sum(cut_edge_times) / len(cut_edge_times))
    print("Avg calculate edge times", sum(calc_edge_time) / len(calc_edge_time))
    print("Avg recalculate neighbor times", sum(recalculate_neighbor_timess) / len(recalculate_neighbor_timess))
    print("Number of times alg chose a 1-node graph", one)


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


def combine_random_clusters(graph):
    if debug:
        print("Combining random clusters")
    clust1 = graph.nodes[random.randrange(0, len(graph.nodes))]
    # print_cluster(clust1.nodes, "Graphed: Cluster 1 original")
    clust1_id = clust1.id
    prev_clust1 = clust1.nodes.copy()
    neighbors = graph.new_get_neighbors(clust1)
    if debug:
        print(neighbors)
        print(clust1.neighbors)
    clust2 = neighbors[random.randrange(0, len(neighbors))]
    # print_cluster(clust2.nodes, "Graphed: Cluster 2 original")
    clust2_id = clust2.id
    prev_clust2 = clust2.nodes.copy()
    if debug:
        print("Randomly chose clusters", clust1, clust2, "to be combined.")
    # if len(clust1.nodes) < 50:
    #     print_cluster(clust1.nodes, "Clust")
    # if len(clust2.nodes) < 50:
    #     print_cluster(clust2.nodes, ":Clust2")
    # combine these two in our graph.
    # calculating these graphs
    clust1_compactness = calculate_previous_clusters_compactness(clust1)
    clust2_compactness = calculate_previous_clusters_compactness(clust2)
    print("Prev clust compactness", clust1_compactness, clust2_compactness)
    graph.combine_clusters(clust1, clust2)
    return [clust1, prev_clust1, prev_clust2, (clust1_id, clust2_id), (clust1_compactness, clust2_compactness)]


def generate_spanning_tree(clust, graph):
    start = datetime.datetime.utcnow()
    if debug:
        print("Generating a spanning tree of cluster", clust)
    # print_cluster(clust.nodes, "Plot: Combined cluster " + str(clust))
    # simply doing a bfs on the cluster.
    queue = []
    visited = []
    edges = []
    first_node = clust.nodes[random.randrange(0, len(clust.nodes))]
    queue.append(first_node)
    visited.append(first_node)
    clust.determine_node_neighbor_dict()
    spanning_tree_times.append((datetime.datetime.utcnow() - start).microseconds)
    start2 = datetime.datetime.utcnow()
    while queue:
        node = queue.pop()
        visited.append(node)
        neighbors = clust.node_neighbor_dict[node.GEOID10]
        for neighbor in neighbors:
            if neighbor not in visited:
                queue.append(neighbor)
                visited.append(neighbor)
                if (neighbor, node) not in edges:
                    edges.append((node, neighbor))
    acceptable_edge_loop_times.append((datetime.datetime.utcnow() - start2).microseconds)
    clust.set_spanning_tree(edges)
    # print_spanning_tree(clust.nodes, edges, "Plot: Spanning tree for " + str(clust))


def find_acceptable_edges(clust, prev_clusts, target_diff, ideal_pop, prev_compact):
    if debug:
        print("Looking for accepted or improved edges...")
    # calculating the clusters' pop differences.
    prev_clusts_diff = calculate_previous_clusters_pop_diff(prev_clusts, ideal_pop)
    # find start node to conduct bfs from
    start = clust.find_start_node()
    visited_pop = 0
    unvisited_pop = 0
    for node in clust.nodes:
        unvisited_pop += node.TOTAL
    # bfs
    queue = []
    visited = []
    edge_list = []
    queue.append(start)
    visited.append(start)
    clust.determine_mst_neighbors_dict()

    num_unvis_ext = 0
    for key in clust.node_status:
        if clust.node_status[key]:
            num_unvis_ext += 1
    num_unvis = len(clust.nodes)
    num_vis_ext = 0
    num_vis = 0
    sum_times = 0
    while queue:
        node = queue.pop()
        visited.append(node)
        # moving along bfs, recalculate both sides of the MST
        unvisited_pop -= node.TOTAL
        visited_pop += node.TOTAL
        if clust.node_status[node.GEOID10]:
            num_unvis_ext -= 1
            num_vis_ext += 1
        neighbors = clust.mst_neighbors_dict[node.GEOID10]
        for neighbor in neighbors:
            if neighbor not in visited and neighbor is not None:
                queue.append(neighbor)
                visited.append(neighbor)
                num_vis += 1
                num_unvis -= 1
                # check if such an edge is better/acceptable.
                if check_new_pop(unvisited_pop, visited_pop, ideal_pop, target_diff, prev_clusts_diff) and \
                        check_new_compactness([num_vis_ext, num_vis], [num_unvis_ext, num_unvis], prev_compact):
                    edge_list.append((node, neighbor))
    # print("Edges found:", edge_list)
    acceptable_edge_loop_times.append(sum_times)
    return edge_list


def check_new_pop(unvisited_pop, visited_pop, ideal_pop, target_diff, prev_clusts_diff):
    if debug:
        print("Checking cut population")
    pop1_diff = abs(unvisited_pop - ideal_pop) / ideal_pop
    pop2_diff = abs(visited_pop - ideal_pop) / ideal_pop
    if debug:
        print("New populations determined to be", pop1_diff, pop2_diff)
    if pop1_diff <= target_diff and pop2_diff <= target_diff:
        if debug:
            print("ACCEPTED: Edge splits into", pop1_diff, pop2_diff)
        return True
    # elif (pop1_diff + pop2_diff) <= (prev_clusts_diff[0] + prev_clusts_diff[1]):
    #     # if improvement
    #     print("IMPROVED: Edge splits into", pop1_diff, pop2_diff)
    #     return True
    elif abs(pop1_diff - target_diff) <= abs(prev_clusts_diff[0] - target_diff) and abs(pop2_diff - target_diff) <= \
            abs(prev_clusts_diff[1] - target_diff) or \
            abs(pop1_diff - target_diff) <= abs(prev_clusts_diff[1] - target_diff) and \
            abs(pop2_diff - target_diff) <= abs(prev_clusts_diff[0] - target_diff):
        if debug:
            print("IMPROVED: Edge splits into", pop1_diff, pop2_diff)
        return True
    return False


def check_new_compactness(vis, unvis, prev_compact):
    user_compact = "Somewhat Compact"
    if debug:
        print("Checking cut compactness...")
    # calculate interior edges. Then exterior edges. Then take ratio.
    vis_num_exterior_edges = vis[0]
    vis_num_edges = vis[1]
    unvis_num_edges = unvis[1]
    unvis_num_exterior_edges = unvis[0]

    if unvis_num_exterior_edges == 0:
        unvis_num_exterior_edges = 1
    if vis_num_exterior_edges == 0:
        vis_num_exterior_edges = 1
    unvis_ratio = unvis_num_edges / unvis_num_exterior_edges
    vis_ratio = vis_num_edges / vis_num_exterior_edges

    if debug:
        print("Unvisited: ", unvis_num_edges, unvis_num_exterior_edges)
        print("Visited: ", vis_num_edges, vis_num_exterior_edges)
    if user_compact == "Somewhat Compact":
        if 0.3 < unvis_ratio < 0.6 and 0.3 < vis_ratio < 0.6:
            if debug:
                print("ACCEPTED: Compactness is", unvis_ratio, vis_ratio)
                return True
        elif abs(unvis_ratio - 0.6) < abs(prev_compact[0] - 0.6) and abs(vis_ratio - 0.6) < abs(prev_compact[1] - 0.6) or \
            abs(unvis_ratio - 0.6) < abs(prev_compact[1] - 0.6) and abs(vis_ratio - 0.6) < abs(prev_compact[0] - 0.6) or \
            abs(unvis_ratio - 0.3) < abs(prev_compact[0] - 0.3) and abs(vis_ratio - 0.3) < abs(prev_compact[1] - 0.3) or \
            abs(unvis_ratio - 0.6) < abs(prev_compact[1] - 0.6) and abs(vis_ratio - 0.6) < abs(prev_compact[0] - 0.6):
                print("IMPROVED: Compactness is", unvis_ratio, vis_ratio)
                return True
    print("Compactness is", unvis_ratio, vis_ratio)
    return True


def calculate_previous_clusters_compactness(clust):
    clust.determine_node_neighbor_dict()
    ext_nodes = 0
    nodes = len(clust.nodes)
    for node in clust.nodes:
        if clust.node_status[node.GEOID10]:
            ext_nodes += 1
    if ext_nodes == 0:
        ext_nodes = 1
    return nodes / ext_nodes


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
    if debug:
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
    if debug:
        print("Randomly choosing an edge to cut from the gathered list...")

    edge = edge_list[random.randrange(0, len(edge_list))]
    if debug:
        print("Randomly chose edge", edge, "to cut. Paritioning into two separate clusters now...")
    # remove the edge.
    if edge in clust.spanning_tree_edges:
        clust.spanning_tree_edges.remove(edge)
    else:
        clust.spanning_tree_edges.remove((edge[1], edge[0]))
    # 2 bfs' to get both of the node lists.
    start = datetime.datetime.utcnow()
    nodes_1 = accumulate_bfs(clust, edge[0], edge[1])
    nodes_2 = accumulate_bfs(clust, edge[1], edge[0])
    cut_edge_times.append((datetime.datetime.utcnow() - start).microseconds)
    clust_1 = create_cluster(nodes_1, ids[0])
    clust_2 = create_cluster(nodes_2, ids[1])
    start = datetime.datetime.utcnow()
    graph.split_cluster(clust, clust_1, clust_2)
    if debug:
        print("Split into clusters", clust_1, clust_2)
    # print_cluster(clust_1.nodes, "Graphed: Cluster 1")
    # print_cluster(clust_2.nodes, "Graphed: Cluster 2")
    if debug:
        print(clust_1.neighbors)
        print(clust_2.neighbors)
    recalculate_neighbor_timess.append((datetime.datetime.utcnow() - start).microseconds)


def create_cluster(nodes, id):
    clust = Cluster(nodes, id)
    for node in clust.nodes:
        node.cluster_id = clust.id
    return clust


# def accumulate_bfs(clust, start):
#     queue = []
#     visited = []
#     queue.append(start)
#     visited.append(start)
#     while queue:
#         node = queue.pop()
#         visited.append(node)
#         neighbors = clust.get_mst_neighbors(node)
#         for neighbor in neighbors:
#             if neighbor not in visited:
#                 queue.append(neighbor)
#                 visited.append(neighbor)
#     return visited

def accumulate_bfs(clust, start, avoid):
    clust.edges = clust.spanning_tree_edges
    queue = []
    visited = []
    nodes = []
    queue.append(start)
    visited.append(start)
    while queue:
        node = queue.pop()
        visited.append(node)
        nodes.append(node)
        neighbors = clust.mst_neighbors_dict[node.GEOID10]
        for neighbor in neighbors:
            if neighbor not in visited and neighbor != avoid:
                queue.append(neighbor)
                visited.append(neighbor)
    return nodes


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
        neighbors = graph.new_get_neighbors(node)
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


algorithm()
