# search.py
# ---------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


"""
In search.py, you will implement generic search algorithms which are called by
Pacman agents (in searchAgents.py).
"""

import util
from util import Stack
from util import Queue
from util import PriorityQueue

class SearchProblem:
    """
    This class outlines the structure of a search problem, but doesn't implement
    any of the methods (in object-oriented terminology: an abstract class).

    You do not need to change anything in this class, ever.
    """

    def getStartState(self):
        """
        Returns the start state for the search problem.
        """
        util.raiseNotDefined()

    def isGoalState(self, state):
        """
          state: Search state

        Returns True if and only if the state is a valid goal state.
        """
        util.raiseNotDefined()

    def getSuccessors(self, state):
        """
          state: Search state

        For a given state, this should return a list of triples, (successor,
        action, stepCost), where 'successor' is a successor to the current
        state, 'action' is the action required to get there, and 'stepCost' is
        the incremental cost of expanding to that successor.
        """
        util.raiseNotDefined()

    def getCostOfActions(self, actions):
        """
         actions: A list of actions to take

        This method returns the total cost of a particular sequence of actions.
        The sequence must be composed of legal moves.
        """
        util.raiseNotDefined()


def tinyMazeSearch(problem):
    """
    Returns a sequence of moves that solves tinyMaze.  For any other maze, the
    sequence of moves will be incorrect, so only use this for tinyMaze.
    """
    from game import Directions
    s = Directions.SOUTH
    w = Directions.WEST
    return  [s, s, w, s, w, w, s, w]

# adds child nodes to stack
def haruForDFS(s, pathBefore, children, used):
    for child in children:
        if child[0] in used:
            continue
        s.push((child[0], pathBefore + [child[1]]))


def depthFirstSearch(problem):
    s = Stack()
    used = set()
    startingState = problem.getStartState()
    s.push((startingState, []))
    used.add(startingState)
    while not s.isEmpty():
        curr = s.pop()
        if problem.isGoalState(curr[0]):
            return curr[1]
        used.add(curr[0])
        haruForDFS(s, curr[1], problem.getSuccessors(curr[0]), used)
    return []


# adds child nodes to queue
def haruForBFS(q, pathBefore, children, used):
    for child in children:
        if child[0] in used:
            continue
        used.add(child[0])
        q.push((child[0], pathBefore + [child[1]]))

def breadthFirstSearch(problem):
    """Search the shallowest nodes in the search tree first."""
    q = Queue()
    used = set()
    startingState = problem.getStartState()
    q.push((startingState, []))
    used.add(startingState)
    while not q.isEmpty():
        curr = q.pop()
        if problem.isGoalState(curr[0]):
            return curr[1]
        haruForBFS(q, curr[1], problem.getSuccessors(curr[0]), used)
    return []



# adds child nodes to priority queue
def haruForUCS(pq, children, parent, data):
    for child in children:
        if child[0] in data and data[parent][0] + child[2] >= data[child[0]][0]:
            continue
        data[child[0]] = (data[parent][0] + child[2], data[parent][1] + [child[1]])
        pq.update(child[0], data[child[0]][0])

def uniformCostSearch(problem):
    pq = PriorityQueue()
    startingState = problem.getStartState()
    data = {startingState : (0, [])}
    pq.push(startingState, 0)
    while not pq.isEmpty():
        curr = pq.pop()
        if problem.isGoalState(curr):
            return data[curr][1]
        haruForUCS(pq, problem.getSuccessors(curr), curr, data)
    return []

def nullHeuristic(state, problem=None):
    """
    A heuristic function estimates the cost from the current state to the nearest
    goal in the provided SearchProblem.  This heuristic is trivial.
    """
    return 0


# adds child nodes to priority queue
def haruForASS(pq, children, parent, data, heuristic, problem):
    for child in children:
        if child[0] in data and data[parent][0] + child[2] >= data[child[0]][0]:
            continue
        data[child[0]] = (data[parent][0] + child[2], data[parent][1] + [child[1]])
        pq.update(child[0], data[child[0]][0] + heuristic(child[0], problem))

def aStarSearch(problem, heuristic=nullHeuristic):
    pq = PriorityQueue()
    startingState = problem.getStartState()
    data = {startingState : (0, [])}
    pq.push(startingState, heuristic(startingState, problem))
    while not pq.isEmpty():
        curr = pq.pop()
        if problem.isGoalState(curr):
            return data[curr][1]
        haruForASS(pq, problem.getSuccessors(curr), curr, data, heuristic, problem)
    return []


# Abbreviations
bfs = breadthFirstSearch
dfs = depthFirstSearch
astar = aStarSearch
ucs = uniformCostSearch
