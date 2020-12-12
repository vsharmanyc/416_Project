import json
import random
import sys
from Node import Node
from Request import Request
# import networkx as nx
# import matplotlib.pyplot as plt
from Cluster import Cluster
from Graph import Graph
from Subgraph import Subgraph
import datetime

debug = False
ideal_population_per_district = 0
avoid_clusters = {}  # when we find pairs of clusters where we cant find a suitable edge, we do not use them.


def entry_point():
    # parse data
    data = parse_data_from_file()
    n = data[0]
    nodes = filter_nodes(n)
    request = data[1]
    graph = prepare_seed(nodes)
    # write_to_results_file(graph, request)
    reset_avoid_list(graph)
    algorithm(graph, request)
    write_to_results_file(graph, request)


def enter_after_seed():
    request = None
    g = test_from_seed()
    global ideal_population_per_district
    for clust in g.nodes:
        for node in clust.nodes:
            ideal_population_per_district += node.TOTAL
    ideal_population_per_district = ideal_population_per_district / 19
    if debug:
        print("Ideal population per district:", ideal_population_per_district)
    algorithm(g, request)
    write_to_results_file(g, request)


def filter_nodes(nodes):
    true_nodes = []
    for node in nodes:
        if node.TOTAL != "":
            true_nodes.append(node)
    return true_nodes


def reset_avoid_list(graph):
    global avoid_clusters
    for clust in graph.nodes:
        avoid_clusters[clust.id] = []


def prepare_seed(nodes):
    global ideal_population_per_district
    for node in nodes:
        ideal_population_per_district += node.TOTAL
    ideal_population_per_district = ideal_population_per_district / 19
    if debug:
        print("Ideal population per district:", ideal_population_per_district)
    # parsing nodes into clusters
    clusts = []
    for node in nodes:
        clusts.append(Cluster([node], int(node.GEOID10)))
    # find its neighbor clusters
    for clust in clusts:
        set_neighboring_clusts(clust)

    # graph of clusters.
    graph = Graph(clusts)
    return graph


def test_from_seed():
    f = open("seed.json")
    data = json.load(f)
    subgraphs = data["subgraphs"]

    clusts = []
    for subgraph in subgraphs:
        nodes = subgraph["nodes"]
        precincts = parse_precincts_from_json2(nodes)
        clust_id = precincts[0].cluster_id
        clusts.append(Cluster(precincts, clust_id))
    g = Graph(clusts)
    return g


def parse_data_from_file():
    f = open("swData_job1.json")
    # data = json.load(f)
    data = json.load(f)
    job = data["data"][0]
    precincts = data["data"][1]
    nodes = parse_precincts_from_json(precincts["precincts"])
    nodes = nodes
    request = parse_job_from_json(job)
    return [nodes, request]


def parse_job_from_json(job):
    return Request(job)


def parse_precincts_from_json(precincts):
    nodes = []
    for precinct in precincts:
        # Node class pulls all data from precinct dict.
        node = Node(precinct)
        if node.GEOID10 == "" or node.NEIGHBORS == "":
            continue
        if type(node.NEIGHBORS) == int:
            node.NEIGHBORS = [node.NEIGHBORS]
        else:
            node.NEIGHBORS = node.NEIGHBORS.split(',')
        nodes.append(node)
    return nodes


def parse_precincts_from_json2(precincts):
    nodes = []
    for precinct in precincts:
        # Node class pulls all data from precinct dict.
        node = Node(precinct)
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


def algorithm(graph, request):
    alg_start = datetime.datetime.utcnow()
    if debug:
        print("Determining a Seed Districting...")
    determine_seed_districting(graph)
    # clean up neighboring clusters
    graph.clean_up_edges()
    graph.recalculate_literally_all_neighbors()
    # for cluster in graph.nodes:
    #     print_cluster(cluster.nodes, "Graph of final cluster")
    # algorithm begins. Find neighboring clusters to combine.
    i = 1
    one = 0
    two = 0
    no_edge = 0
    not_found = 0
    pop_diff_target = 0.2
    while i <= 100:
        if i > 80:
            pop_diff_target = 0.1
        if i > 90:
            pop_diff_target = 0.017
        if debug:
            print("================================================ITERATION", i,
                  "================================================")
        res = main_algorithm_loop(graph, pop_diff_target)
        if res == 1:
            i += 1
            not_found = 0
        else:
            no_edge += 1
            not_found += 1
        graph.determine_edges()
        if not_found == 1000:
            break

    output = ""
    output += "====================ALGORITHM RESULTS====================\n"
    output += "Iterations: " + str(i-1) + "\n"
    output += "Total algorithm runtime " + str(datetime.datetime.utcnow() - alg_start) + "\n"
    # print("Number of times alg chose a 1-node graph", one)
    # print("Number of times chosen two one nodes", two)
    # print("Number of times chosen one one nodes", one)
    output += "Number of times unable to find an edge: " + str(no_edge) + "\n"
    output += "Clusters:\n"
    for cluster in graph.nodes:
        pop = 0
        ids = []
        for node in cluster.nodes:
            pop += node.TOTAL
            ids.append(node.GEOID10)
        compact = find_compactness(cluster.nodes, ids)
        compact1 = abs(1 - compact[0])
        compact2 = abs(1 - compact[1])
        if compact2 == 0:
            compact1 = 0
            compact2 = 1
        compactness = compact1/compact2
        output += "- CLUSTER " + str(cluster.id) + " | POPULATION: " + str(pop) + " | COMPACTNESS: " + str(compactness) + "\n"
    fname = "output" + sys.argv[1] + ".txt"
    f = open(fname, "a")
    f.write(output + "\n")
    f.close()


def main_algorithm_loop(graph, pop_diff_target):
    # global avoid_clusters
    # print_graph(graph, ("Iteration " + str(i) + " Graph"))
    clusts = combine_random_clusters(graph)
    generate_spanning_tree(clusts[0], graph)
    # dfs_generate_spanning_tree(clusts[0])
    # NARAYAN - Print spanning tree
    # print_spanning_tree(clusts[0].nodes, clusts[0].spanning_tree_edges, "DFS")
    fae_result = find_acceptable_edge(clusts[0], [clusts[1], clusts[2]], pop_diff_target, ideal_population_per_district,
                                      clusts[4])
    edge = fae_result[0]
    node_lists = [fae_result[1], fae_result[2]]
    if not edge:
        if debug:
            print("No edges found. Reverting to original two clusters...")
        clust1 = create_cluster(clusts[1], clusts[3][0])
        clust2 = create_cluster(clusts[2], clusts[3][1])
        # avoid this pairing in the future
        temp = avoid_clusters[clust1.id]
        temp.append(clust2.id)
        avoid_clusters[clust1.id] = temp
        temp = avoid_clusters[clust2.id]
        temp.append(clust1.id)
        avoid_clusters[clust2.id] = temp

        graph.split_cluster(clusts[0], clust1, clust2)
        graph.determine_edges()
        # i+=1
        return -1
    else:
        # clusts[3] house ids
        reset_avoid_list(graph)
    node_nums = cut_from_acceptable_edges(edge, clusts[0], clusts[3], graph, node_lists)
    return 1


def determine_seed_districting(graph):
    start = datetime.datetime.utcnow()
    # districts = determine_nodes_per_subgraph(graph.nodes)
    # wrap node in cluster
    ext_clusts = []
    # combine clusters randomly until we have target districts
    while len(graph.nodes) != 19:
        clust = graph.nodes[random.randrange(0, len(graph.nodes))]
        neighs = graph.get_neighbors(clust)
        if not neighs:
            graph.nodes.remove(clust)
            ext_clusts.append(clust)
            continue
        neigh_clust = neighs[random.randrange(0, len(neighs))]
        graph.combine_clusters(clust, neigh_clust)
    for clust in graph.nodes:
        graph.recalculate_cluster_neighbors(clust, False)
    #print("Seed Time:", (datetime.datetime.utcnow()-start))
    return graph


def combine_random_clusters(graph):
    global avoid_clusters
    if debug:
        print("Combining random clusters")
    clusters = graph.nodes
    clust1 = clusters[random.randrange(0, len(graph.nodes))]
    # print_cluster(clust1.nodes, "Graphed: Cluster 1 original")
    clust1_pop = 0
    for node in clust1.nodes:
        clust1_pop += node.TOTAL

    clust1_id = clust1.id
    prev_clust1 = clust1.nodes.copy()
    neighbors = graph.new_get_neighbors(clust1)
    # for neighbor in neighbors:
    #     if neighbor.id in avoid_clusters[clust1_id]:
    #         neighbors.remove(neighbor)
    # if len(neighbors) == 0:
    #     clusters.remove(clust1)
    #     continue
    clust2 = neighbors[random.randrange(0, len(neighbors))]

    clust2_pop = 0
    for node in clust2.nodes:
        clust2_pop += node.TOTAL
    if debug:
        print("Original populations:", '{:,}'.format(clust1_pop), '{:,}'.format(clust2_pop))
    # print_cluster(clust2.nodes, "Graphed: Cluster 2 original")
    clust2_id = clust2.id
    prev_clust2 = clust2.nodes.copy()
    if debug:
        print("Randomly chose clusters", clust1, clust2, "to be combined.")
    # combine these two in our graph.
    # calculating these graphs
    clust1_compactness = calculate_previous_clusters_compactness(clust1)
    clust2_compactness = calculate_previous_clusters_compactness(clust2)
    if debug:
        print("Prev clust compactness", clust1_compactness, clust2_compactness)
    graph.combine_clusters(clust1, clust2)
    return [clust1, prev_clust1, prev_clust2, (clust1_id, clust2_id), (clust1_compactness, clust2_compactness)]


def generate_spanning_tree(clust, graph):
    if debug:
        print("Generating a spanning tree of cluster", clust)
    # print_cluster(clust.nodes, "Plot: Combined cluster " + str(clust))
    # simply doing a bfs on the cluster.
    queue = []
    visited = set()
    edges = set()
    first_node = clust.nodes[random.randrange(0, len(clust.nodes))]
    queue.append(first_node)
    visited.add(first_node)
    clust.determine_node_neighbor_dict()
    start2 = datetime.datetime.utcnow()
    while queue:
        node = queue.pop()
        visited.add(node)
        neighbors = clust.node_neighbor_dict[node.GEOID10]
        for neighbor in neighbors:
            if neighbor not in visited:
                queue.append(neighbor)
                visited.add(neighbor)
                if (neighbor, node) not in edges:
                    edges.add((node, neighbor))
    clust.set_spanning_tree(list(edges))
    # print_spanning_tree(clust.nodes, edges, "Plot: Spanning tree for " + str(clust))
    clust.determine_mst_neighbors_dict()


def dfs_generate_spanning_tree(clust):
    clust.determine_node_neighbor_dict()
    visited = set()
    edges = []
    first_node = clust.nodes[random.randrange(0, len(clust.nodes))]
    dfs_spanning_tree_util(clust, first_node, visited, edges)
    clust.set_spanning_tree(edges)
    clust.determine_mst_neighbors_dict()


def dfs_spanning_tree_util(clust, n, visited, edges):
    visited.add(n)
    neighbors = clust.node_neighbor_dict[n.GEOID10]
    for neighbor in neighbors:
        if neighbor not in visited:
            if (neighbor, n) not in edges and (n, neighbor) not in edges:
                edges.append((n, neighbor))
            dfs_spanning_tree_util(clust, neighbor, visited, edges)


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

        num_vis += 1
        num_unvis -= 1

        if clust.node_status[node.GEOID10]:
            num_unvis_ext -= 1
            num_vis_ext += 1
        neighbors = clust.mst_neighbors_dict[node.GEOID10]
        for neighbor in neighbors:
            if neighbor not in visited:
                queue.append(neighbor)
                visited.append(neighbor)
                # check if such an edge is better/acceptable.
                # if check_new_pop(unvisited_pop, visited_pop, ideal_pop, target_diff, prev_clusts_diff) and \
                #         check_new_compactness([num_vis_ext, num_vis], [num_unvis_ext, num_unvis], prev_compact):
                if check_new_pop(unvisited_pop, visited_pop, ideal_pop, target_diff, prev_clusts_diff):
                    print("FOR EDGE", (node, neighbor))
                    edge_list.append((node, neighbor))
    # print("Edges found:", edge_list)
    return [edge_list, start]


def find_acceptable_edge(clust, prev_clusts, target_diff, ideal_pop, prev_compact):
    edges = clust.spanning_tree_edges.copy()
    st_dict = clust.mst_neighbors_dict.copy()
    prev_clusts_diff = calculate_previous_clusters_pop_diff(prev_clusts, ideal_pop)
    edge = None
    overall_pop = calculate_clusters_population(clust)
    improved = {}
    while len(edges) > 0:
        edge = edges[random.randrange(0, len(edges))]
        edges.remove(edge)
        # print("Observing edge", edge)
        # remove edge from spanning tree
        neighbors1 = clust.mst_neighbors_dict[edge[0].GEOID10]
        neighbors2 = clust.mst_neighbors_dict[edge[1].GEOID10]
        neighbors1.remove(edge[1])
        neighbors2.remove(edge[0])
        clust.mst_neighbors_dict[edge[0]] = neighbors1
        clust.mst_neighbors_dict[edge[1]] = neighbors2

        # bfs, get populations after cut
        res1 = bfs(clust, edge[0])
        nodes1 = res1[0]
        pop1 = res1[1]
        pop2 = overall_pop - pop1

        neighbors1 = clust.mst_neighbors_dict[edge[0].GEOID10]
        neighbors2 = clust.mst_neighbors_dict[edge[1].GEOID10]
        neighbors1.append(edge[1])
        neighbors2.append(edge[0])
        clust.mst_neighbors_dict[edge[0]] = neighbors1
        clust.mst_neighbors_dict[edge[1]] = neighbors2

        # print("Populations calculated to be", pop1, pop2)
        # if check_new_pop(pop1, pop2, ideal_pop, target_diff, prev_clusts_diff) and \
        #         check_new_compactness(compact_info1, compact_info2, prev_compact):
        #     print("EDGE ACCEPTED:", edge)
        #     return (edge, nodes1, nodes2)
        check = check_new_pop_range(pop1, pop2, ideal_pop, target_diff, prev_clusts_diff)
        if check == "Accepted":
            # print("AN ACCEPTED EDGE WAS FOUND")
            compact_info1 = find_compactness(res1[2][0], res1[2][1])
            other_nodes = get_other_side_nodes(clust.nodes, nodes1)
            compact_info2 = find_compactness(other_nodes[0], other_nodes[1])
            if check_new_compactness(compact_info1, compact_info2, prev_compact):
                if debug:
                    print("EDGE ACCEPTED:", edge)
                return edge, list(nodes1), other_nodes[0]
        elif check == "Improved":
            improved[edge] = res1
            continue
        # if check_new_pop(pop1, pop2, ideal_pop, target_diff, prev_clusts_diff):
        #     # check if compactness is OK too
        #     compact_info1 = find_compactness(res1[2][0], res1[2][1])
        #     other_nodes = get_other_side_nodes(clust.nodes, nodes1)
        #     compact_info2 = find_compactness(other_nodes[0], other_nodes[1])
        #     if check_new_compactness(compact_info1, compact_info2, prev_compact):
        #         if debug:
        #             print("EDGE ACCEPTED:", edge)
        #         return edge, list(nodes1), other_nodes[0]
    while len(improved.keys()) > 0:
        edge = list(improved.keys())[random.randrange(0, len(improved.keys()))]
        res1 = improved[edge]
        nodes1 = res1[0]
        del improved[edge]
        compact_info1 = find_compactness(res1[2][0], res1[2][1])
        other_nodes = get_other_side_nodes(clust.nodes, nodes1)
        compact_info2 = find_compactness(other_nodes[0], other_nodes[1])
        if check_new_compactness(compact_info1, compact_info2, prev_compact):
            if debug:
                print("EDGE ACCEPTED:", edge)
            return edge, list(nodes1), other_nodes[0]
    return None, None, None


def calculate_clusters_population(cluster):
    pop = 0
    for node in cluster.nodes:
        pop += node.TOTAL
    return pop


def get_other_side_nodes(nodes, nodes1):
    nodes2 = []
    nodes2_ids = set()
    for node in nodes:
        if node not in nodes1:
            nodes2.append(node)
            nodes2_ids.add(node.GEOID10)
    return [nodes2, nodes2_ids]


def bfs(clust, start):
    queue = []
    visited = set()
    nodes = set()
    nodes_ids = set()
    queue.append(start)
    visited.add(start)
    total_pop = 0
    while queue:
        node = queue.pop()
        total_pop += node.TOTAL
        visited.add(node)
        nodes.add(node)
        nodes_ids.add(node.GEOID10)
        neighbors = clust.mst_neighbors_dict[node.GEOID10]
        for neighbor in neighbors:
            if neighbor not in visited:
                queue.append(neighbor)
                visited.add(neighbor)
    # compactness_info = find_compactness(nodes, nodes_ids)
    compactness_info = [nodes, nodes_ids]
    return nodes, total_pop, compactness_info


def check_external_node(node, ids):
    for neighbor in node.NEIGHBORS:
        if neighbor not in ids:
            return True
    return False


def find_compactness(nodes, ids):
    ext_nodes = set()
    for node in nodes:
        for neighbor in node.NEIGHBORS:
            if int(neighbor) not in ids and node.GEOID10 not in ext_nodes:
                ext_nodes.add(node.GEOID10)
    # NARAYAN - Compactness check
    # print("Ext edge and total edge:", ext_edge_count, len(edges)/2+ext_edge_count)
    return len(ext_nodes), len(nodes)


def check_new_pop(unvisited_pop, visited_pop, ideal_pop, target_diff, prev_clusts_diff):
    # if debug:
    #     print("Checking cut population")
    pop1_diff = abs(unvisited_pop - ideal_pop) / ideal_pop
    pop2_diff = abs(visited_pop - ideal_pop) / ideal_pop
    acceptable = False
    # NARAYAN - Population check
    # if debug:
    #     print("New populations determined to be", visited_pop, unvisited_pop)
    if pop1_diff <= target_diff and pop2_diff <= target_diff:
        if debug:
            print("ACCEPTED: Edge splits into", pop1_diff, pop2_diff)
        return True
    # elif abs(pop1_diff - target_diff) <= abs(prev_clusts_diff[0] - target_diff) and abs(pop2_diff - target_diff) <= abs(
    #         prev_clusts_diff[1] - target_diff):
    #     if debug:
    #         print("1) IMPROVED: Edge splits into", pop1_diff, pop2_diff, unvisited_pop, visited_pop)
    #     return True
    # elif abs(pop1_diff - target_diff) <= abs(prev_clusts_diff[1] - target_diff) and abs(pop2_diff - target_diff) <= abs(
    #         prev_clusts_diff[0] - target_diff):
    #     if debug:
    #         print("2) IMPROVED: Edge splits into", pop1_diff, pop2_diff, unvisited_pop, visited_pop)
    #     return True
    # if not acceptable, see if they are closer to being acceptable!
    elif abs((pop1_diff+pop2_diff)-target_diff) <= abs((prev_clusts_diff[1] + prev_clusts_diff[0])-target_diff):
        if debug:
            print("3) IMPROVED: Edge splits into", pop1_diff, pop2_diff, unvisited_pop, visited_pop)
        return True
    # elif (pop1_diff == prev_clusts_diff[0] and pop2_diff == prev_clusts_diff[1]) or (pop1_diff == prev_clusts_diff[1]
    #                                                                             and pop2_diff == prev_clusts_diff[0]):
    #     # print("SAME EDGE - TAKEN")
    #     return True
    return False


def check_new_pop_range(unvisited_pop, visited_pop, ideal_pop, target_diff, prev_clusts_diff):
    pop1_diff = abs(unvisited_pop - ideal_pop) / ideal_pop
    pop2_diff = abs(visited_pop - ideal_pop) / ideal_pop
    # if debug:
    #     print("New populations determined to be", '{:,}'.format(visited_pop), '{:,}'.format(unvisited_pop))
    if pop1_diff <= target_diff and pop2_diff <= target_diff:
        if debug:
            print("ACCEPTED: Edge splits into", pop1_diff, pop2_diff)
        return "Accepted"
    elif (abs(pop1_diff - target_diff) + abs(pop2_diff - target_diff)) <= (abs(prev_clusts_diff[0] - target_diff) + abs(prev_clusts_diff[1] - target_diff)):
        if debug:
            print("IMPROVED: Edge splits into", pop1_diff, pop2_diff)
        return "Improved"
    return "Bad"


def alt_check_new_pop(unvisited_pop, visited_pop, ideal_pop, target_diff, prev_clusts_diff):
    # if debug:
    #     print("Checking cut population")
    pop1_diff = abs(unvisited_pop - ideal_pop) / ideal_pop
    pop2_diff = abs(visited_pop - ideal_pop) / ideal_pop
    if debug:
        print("New populations determined to be", '{:,}'.format(visited_pop), '{:,}'.format(unvisited_pop))
    if pop1_diff <= target_diff and pop2_diff <= target_diff:
        if debug:
            print("ACCEPTED: Edge splits into", pop1_diff, pop2_diff)
        return "Accepted"
    elif abs(pop1_diff - target_diff) <= abs(prev_clusts_diff[0] - target_diff) and abs(pop2_diff - target_diff) <= abs(
            prev_clusts_diff[1] - target_diff):
        if debug:
            print("1) IMPROVED: Edge splits into", pop1_diff, pop2_diff, unvisited_pop, visited_pop)
        return "Improved"
    elif abs(pop1_diff - target_diff) <= abs(prev_clusts_diff[1] - target_diff) and abs(pop2_diff - target_diff) <= abs(
            prev_clusts_diff[0] - target_diff):
        if debug:
            print("2) IMPROVED: Edge splits into", pop1_diff, pop2_diff, unvisited_pop, visited_pop)
        return "Improved"
    # if not acceptable, see if they are closer to being acceptable!
    elif abs((pop1_diff+pop2_diff)-target_diff) <= abs((prev_clusts_diff[1] + prev_clusts_diff[0])-target_diff):
        if debug:
            print("3) IMPROVED: Edge splits into", pop1_diff, pop2_diff, unvisited_pop, visited_pop)
        return "Improved"
    return "Bad"


def check_new_compactness(vis, unvis, prev_compact):
    user_compact = "Somewhat Compact"
    #print("Checking compactness")
    # calculate interior edges. Then exterior edges. Then take ratio.
    vis_num_exterior_edges = vis[0]
    vis_num_edges = vis[1]
    unvis_num_edges = unvis[1]
    unvis_num_exterior_edges = unvis[0]

    # if unvis_num_exterior_edges == 0:
    #     unvis_num_exterior_edges = 1
    # if vis_num_exterior_edges == 0:
    #     vis_num_exterior_edges = 1
    unvis_ratio = unvis_num_exterior_edges / unvis_num_edges
    vis_ratio = vis_num_exterior_edges / vis_num_edges
    # need more edges == less compact, so we invert the ratio.
    unvis_ratio = abs(1 - unvis_ratio)
    vis_ratio = abs(1 - vis_ratio)

    # if debug:
    #     print("Unvisited: ", unvis_ratio)
    #     print("Visited: ", vis_ratio)
    if user_compact == "Somewhat Compact":
        if 0.3 <= unvis_ratio <= 0.6 and 0.3 <= vis_ratio <= 0.6:
            if debug:
                print("ACCEPTED: Compactness is", unvis_ratio, vis_ratio)
            return True
        elif abs(unvis_ratio - 0.6) <= abs(prev_compact[0] - 0.6) and abs(vis_ratio - 0.6) <= abs(
                prev_compact[1] - 0.6) or \
                abs(unvis_ratio - 0.99) <= abs(prev_compact[1] - 0.99) and abs(vis_ratio - 0.99) <= abs(
            prev_compact[0] - 0.99) or \
                abs(unvis_ratio - 0.99) <= abs(prev_compact[0] - 0.99) and abs(vis_ratio - 0.99) <= abs(
            prev_compact[1] - 0.99) or \
                abs(unvis_ratio - 0.6) <= abs(prev_compact[1] - 0.6) and abs(vis_ratio - 0.6) <= abs(
            prev_compact[0] - 0.6):
            if debug:
                print("IMPROVED: Compactness is", unvis_ratio, vis_ratio)
            return True
        elif (unvis_ratio+vis_ratio-1.98) <= (prev_compact[0]+prev_compact[1]-1.98) and \
            (unvis_ratio + vis_ratio - 1.2) <= (prev_compact[0] + prev_compact[1] - 1.2):
            if debug:
                print("IMPROVED: Compactness is", unvis_ratio, vis_ratio)
            return True
    # if debug:
    #     print("Compactness is", unvis_ratio, vis_ratio)
    return False


def calculate_previous_clusters_compactness(clust):
    clust.determine_node_neighbor_dict()
    ids = []
    for node in clust.nodes:
        ids.append(node.GEOID10)
    compactness = find_compactness(clust.nodes, ids)
    return abs((compactness[0] / compactness[1])-1)


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


def cut_from_acceptable_edges(edge_list, clust, ids, graph, start_node):
    if debug:
        print("Randomly choosing an edge to cut from the gathered list...")
    edge = edge_list
    if debug:
        print("Randomly chose edge", edge, "to cut. Paritioning into two separate clusters now...")
    # remove the edge.
    # if edge in clust.spanning_tree_edges:
    #     clust.spanning_tree_edges.remove(edge)
    # else:
    #     clust.spanning_tree_edges.remove((edge[1], edge[0]))
    # clust.determine_mst_neighbors_dict()
    # # 2 bfs' to get both of the node lists.
    start = datetime.datetime.utcnow()
    # nodes_1 = accumulate_bfs(clust, edge[0])
    # nodes_2 = accumulate_bfs(clust, edge[1])
    # # node_partitions = accumulate_bfs(clust, start_node, edge)
    # # nodes_1 = node_partitions[0]
    # # nodes_2 = node_partitions[1]
    nodes_1 = start_node[0]
    nodes_2 = start_node[1]
    clust_1 = create_cluster(nodes_1, ids[0])
    clust_2 = create_cluster(nodes_2, ids[1])

    clust1_pop = 0
    for node in clust_1.nodes:
        clust1_pop += node.TOTAL
    clust2_pop = 0
    for node in clust_2.nodes:
        clust2_pop += node.TOTAL
    if debug:
        print("New populations determined to be", '{:,}'.format(clust1_pop), '{:,}'.format(clust2_pop))
    start = datetime.datetime.utcnow()
    graph.split_cluster(clust, clust_1, clust_2)
    if debug:
        print("Split into clusters", clust_1, clust_2)
    # print_cluster(clust_1.nodes, "Graphed: Cluster 1")
    # print_cluster(clust_2.nodes, "Graphed: Cluster 2")
    # if debug:
    #     print(clust_1.neighbors)
    #     print(clust_2.neighbors)
    return [nodes_1, nodes_2]


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

def accumulate_bfs(clust, start):
    print("Start", start)
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
            if neighbor not in visited:
                queue.append(neighbor)
                visited.append(neighbor)
    return nodes


# def accumulate_bfs(clust, start, cut):
#     queue = []
#     visited = []
#     nodes1 = []
#     nodes2 = []
#     first_part = True
#     queue.append(start)
#     while queue:
#         node = queue.pop()
#         visited.append(node)
#         if first_part:
#             nodes1.append(node)
#         else:
#             nodes2.append(node)
#         neighbors = clust.mst_neighbors_dict[node.GEOID10]
#         for neighbor in neighbors:
#             if neighbor not in visited and neighbor is not None:
#                 queue.append(neighbor)
#                 visited.append(neighbor)
#                 if neighbor == cut[0] and node == cut[1] or neighbor == cut[1] and node == cut[0]:
#                     first_part = False
#                     print(neighbor, node)
#                     print(node, neighbor)
#     return [nodes1, nodes2]


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


# def print_graph(graph, title):
#     G = nx.Graph()
#     clusters = graph.nodes
#     for node in clusters:
#         neighbors = graph.new_get_neighbors(node)
#         G.add_node(node)
#         for neighbor in neighbors:
#             if not G.has_edge(neighbor, node):
#                 G.add_edge(node, neighbor)
#
#     nx.draw(G, with_labels=True, font_weight='light')
#     print(len(G.edges))
#     print(len(G.nodes))
#     print(title)
#     plt.show()
#
#
# def print_cluster(graph, title):
#     G = nx.Graph()
#     for node in graph:
#         neighbors = node.NEIGHBORS
#         G.add_node(node)
#         for neighbor in neighbors:
#             index = find_node_index(graph, neighbor)
#             if index != None:
#                 if not G.has_edge(graph[index], node):
#                     G.add_edge(node, graph[index])
#
#     nx.draw(G, with_labels=False, font_weight='light')
#     print(len(G.edges))
#     print(len(G.nodes))
#     print(title)
#     plt.show()


# def print_spanning_tree(nodes, edges, title):
#     G = nx.Graph()
#     for node in nodes:
#         G.add_node(node)
#     for edge in edges:
#         G.add_edge(edge[0], edge[1])
#     nx.draw(G, with_labels=False, font_weight='light')
#     print(len(G.edges))
#     print(len(G.nodes))
#     print(title)
#     plt.show()
#
#
# def new_print_spanning_tree(clust):
#     G = nx.Graph()
#     for node in clust.nodes:
#         G.add_node(node)
#     for node in clust.nodes:
#         for node2 in clust.mst_neighbors_dict[node.GEOID10]:
#             G.add_edge(node, node2)
#     nx.draw(G, with_labels=False, font_weight='light')
#     print(len(G.edges))
#     print(len(G.nodes))
#     plt.show()

def clean_district_numbers(graph):
    graph.recalculate_literally_all_neighbors()
    clust_mappings = {}
    i = 1
    for clust in graph.nodes:
        clust_mappings[clust.id] = i
        i += 1
    for clust in graph.nodes:
        new_neighbors = []
        for neighbor in graph.new_get_neighbors(clust):
            new_neighbors.append(clust_mappings[neighbor.id])
        clust.neighbors = new_neighbors
    i = 1
    for clust in graph.nodes:
        clust.id = i
        i += 1
    return clust_mappings


def write_to_results_file(graph, request):
    district_id_map = clean_district_numbers(graph)
    subgraphs = []
    s = "{\n\"data\": [\n"
    s += request.toJSON() + "\n"
    s += ",{\n\"districting\": [\n"
    for cluster in graph.nodes:
        f = Subgraph(cluster, request, graph, district_id_map)
        subgraphs.append(f)

    s += subgraphs[0].toJSON()
    i = 1
    while i < len(subgraphs):
        s += ",\n" + subgraphs[i].toJSON()
        i += 1
    s += "\n]\n}"
    fname = "result" + sys.argv[1] + ".json"
    results = open(fname, "a")
    results.write(s)
    results.close()


entry_point()
# enter_after_seed()
