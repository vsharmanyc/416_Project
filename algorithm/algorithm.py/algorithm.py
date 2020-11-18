import json
from Node import Node
from Request import Request


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
    nodes = parse_neighbors(nodes)

    print(nodes[0].NEIGHBORS)


def parse_job_from_json(job):
    return Request(job)


def parse_precincts_from_json(precincts):
    nodes = []
    for precinct in precincts:
        # Node class pulls all data from precinct dict.
        nodes.append(Node(precinct))
    return nodes


def parse_neighbors(nodes):
    for node in nodes:
        node_neigh = []
        if type(node.NEIGHBORS) == int:
            neighbors = str(node.NEIGHBORS)
        else:
            neighbors = node.NEIGHBORS.split(",")
        for neighbor in neighbors:
            if neighbor == "NULL":
                continue
            neigh = find_node(nodes, neighbor)
            if neigh is not None:
                node_neigh.append(neigh)
        node.NEIGHBORS = node_neigh
    return nodes

def determine_seed_districting():


def find_node(nodes, id):
    for node in nodes:
        if node.GEOID10 == int(id):
            return node
    return None


algorithm()
