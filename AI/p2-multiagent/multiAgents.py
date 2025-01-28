# multiAgents.py
# --------------
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


from util import manhattanDistance
from game import Directions
import random, util

from game import Agent


class ReflexAgent(Agent):
    """
    A reflex agent chooses an action at each choice point by examining
    its alternatives via a state evaluation function.

    The code below is provided as a guide.  You are welcome to change
    it in any way you see fit, so long as you don't touch our method
    headers.
    """
    def ghostEvaluationFunction(self, newPos, newGhostStates, newScaredTimes):
        cost = 0
        closestGhost = manhattanDistance(newPos, newGhostStates[0].getPosition())

        if newScaredTimes[0] == 0 and closestGhost <= 4:
            cost += (8 - closestGhost) * 200

        return cost
    

    def foodEvaluationFunction(self, foodGrid, pos):
        closestDistance = 1e18

        for i, row in enumerate(foodGrid):
            for j, isFood in enumerate(row):
                if isFood:
                    currDistance = manhattanDistance(pos, (i, j))
                    if currDistance < closestDistance:
                        closestDistance = currDistance

        return 1e3 / max(closestDistance, 1)



    def getAction(self, gameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {NORTH, SOUTH, WEST, EAST, STOP}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()

        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices) # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]

        
        value = self.foodEvaluationFunction(currentGameState.getFood(), newPos) 
        value -= self.ghostEvaluationFunction(newPos, newGhostStates, newScaredTimes)
        
        return value

def scoreEvaluationFunction(currentGameState):
    """
    This default evaluation function just returns the score of the state.
    The score is the same one displayed in the Pacman GUI.

    This evaluation function is meant for use with adversarial search agents
    (not reflex agents).
    """
    return currentGameState.getScore()

class MultiAgentSearchAgent(Agent):
    """
    This class provides some common elements to all of your
    multi-agent searchers.  Any methods defined here will be available
    to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

    You *do not* need to make any changes here, but you can if you want to
    add functionality to all your adversarial search agents.  Please do not
    remove anything, however.

    Note: this is an abstract class: one that should not be instantiated.  It's
    only partially specified, and designed to be extended.  Agent (game.py)
    is another abstract class.
    """

    def __init__(self, evalFn = 'scoreEvaluationFunction', depth = '2'):
        self.index = 0 # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)

class MinimaxAgent(MultiAgentSearchAgent):
    """
    Your minimax agent (question 2)
    """
    def maxValue(self, gameState, counter, agent):
        val = -1e18
        actions = gameState.getLegalActions(agent)
        bestAction = actions[0]

        nextAgent = (agent + 1) % gameState.getNumAgents()

        for action in actions:
            nextState = gameState.generateSuccessor(agent, action)
            nextStateValue = self.value(nextState, counter, nextAgent)
            if nextStateValue > val:
                bestAction = action
                val = nextStateValue
        
        return val, bestAction

    def minValue(self, gameState, counter, agent):
        val = 1e18

        if agent == gameState.getNumAgents() - 1:
            counter += 1

        nextAgent = (agent + 1) % gameState.getNumAgents()
        
        actions = gameState.getLegalActions(agent)
        
        for action in actions:
            nextState = gameState.generateSuccessor(agent, action)
            nextStateValue = self.value(nextState, counter, nextAgent)
            val = min(val, nextStateValue)

        return val

    def value(self, gameState, counter, agent):
        if gameState.isWin() or gameState.isLose() or counter == self.depth:
            return self.evaluationFunction(gameState)
        
        if agent == 0:
            return self.maxValue(gameState, counter, agent)[0]
        
        return self.minValue(gameState, counter, agent)



    def getAction(self, gameState):
        """
        Returns the minimax action from the current gameState using self.depth
        and self.evaluationFunction.

        Here are some method calls that might be useful when implementing minimax.

        gameState.getLegalActions(agentIndex):
        Returns a list of legal actions for an agent
        agentIndex=0 means Pacman, ghosts are >= 1

        gameState.generateSuccessor(agentIndex, action):
        Returns the successor game state after an agent takes an action

        gameState.getNumAgents():
        Returns the total number of agents in the game

        gameState.isWin():
        Returns whether or not the game state is a winning state

        gameState.isLose():
        Returns whether or not the game state is a losing state
        """
        _, bestMove = self.maxValue(gameState, 0, 0)
        return bestMove

class AlphaBetaAgent(MultiAgentSearchAgent):
    """
    Your minimax agent with alpha-beta pruning (question 3)
    """



    def maxValue(self, gameState, counter, agent, alpha, beta):
        maxVal = -1e18
        actions = gameState.getLegalActions(agent)
        bestAction = actions[0]

        nextAgent = (agent + 1) % gameState.getNumAgents()

        for action in actions:
            nextState = gameState.generateSuccessor(agent, action)
            nextStateValue = self.value(nextState, counter, nextAgent, alpha, beta)
            if nextStateValue > maxVal:
                bestAction = action
                maxVal = nextStateValue
            if maxVal > beta:
                return maxVal, bestAction
            alpha = max(alpha, maxVal)
        return maxVal, bestAction

    def minValue(self, gameState, counter, agent, alpha, beta):
        minVal = 1e18

        if agent == gameState.getNumAgents() - 1:
            counter += 1

        nextAgent = (agent + 1) % gameState.getNumAgents()
        
        actions = gameState.getLegalActions(agent)
        
        for action in actions:
            nextState = gameState.generateSuccessor(agent, action)
            nextStateValue = self.value(nextState, counter, nextAgent, alpha, beta)
            minVal = min(minVal, nextStateValue)
            if minVal < alpha:
                return minVal
            beta = min(beta, minVal)

        return minVal

    def value(self, gameState, counter, agent, alpha, beta):
        if gameState.isWin() or gameState.isLose() or counter == self.depth:
            return self.evaluationFunction(gameState)
        
        if agent == 0:
            maxScore, _ = self.maxValue(gameState, counter, agent, alpha, beta)
            return maxScore
        
        return self.minValue(gameState, counter, agent, alpha, beta)


    def getAction(self, gameState):
        """
        Returns the minimax action using self.depth and self.evaluationFunction
        """
        _, bestMove = self.maxValue(gameState, 0, 0, -1e18, 1e18)
        return bestMove

class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """

    def maxValue(self, gameState, counter, agent):
        maxVal = -1e18
        actions = gameState.getLegalActions(agent)
        bestAction = actions[0]

        nextAgent = (agent + 1) % gameState.getNumAgents()

        for action in actions:
            nextState = gameState.generateSuccessor(agent, action)
            nextStateValue = self.value(nextState, counter, nextAgent)
            if nextStateValue > maxVal:
                bestAction = action
                maxVal = nextStateValue

        return maxVal, bestAction

    def expectValue(self, gameState, counter, agent):
        expectVal = 0

        if agent == gameState.getNumAgents() - 1:
            counter += 1

        nextAgent = (agent + 1) % gameState.getNumAgents()
        
        actions = gameState.getLegalActions(agent)
        
        prob = 1 / len(actions)

        for action in actions:
            nextState = gameState.generateSuccessor(agent, action)
            nextStateValue = self.value(nextState, counter, nextAgent)
            expectVal += prob * nextStateValue

        return expectVal

    def value(self, gameState, counter, agent):
        if gameState.isWin() or gameState.isLose() or counter == self.depth:
            return self.evaluationFunction(gameState)
        
        if agent == 0:
            maxScore, _ = self.maxValue(gameState, counter, agent)
            return maxScore
        
        return self.expectValue(gameState, counter, agent)


    def getAction(self, gameState):
        """
        Returns the minimax action using self.depth and self.evaluationFunction
        """
        _, bestMove = self.maxValue(gameState, 0, 0)
        return bestMove


def closestFood(pos, food):
    res = 1e18
    
    for i, row in enumerate(food):
        for j, isFood in enumerate(row):
            if isFood:
                res = min(res, manhattanDistance(pos, (i, j)))
    if res == 1e18:
        return 0
    return res


def closestGhost(pos, ghostStates, scaredTimes):
    closestGhost = 1e18
    scared = 0

    if len(ghostStates) == 0:
        return 0

    for index, ghost in enumerate(ghostStates):
        if closestGhost >  manhattanDistance(pos, ghost.getPosition()):
            closestGhost = manhattanDistance(pos, ghost.getPosition())
            scared = scaredTimes[index]

    return closestGhost, scared


def closestCapsules(pos, capsules):
    minDist = 1e18
    if len(capsules) == 0:
        return 0
    
    for caps in capsules:
        minDist = min(minDist, manhattanDistance(pos, caps))
    
    return minDist

def betterEvaluationFunction(currentGameState):
    """
    Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
    evaluation function (question 5).

    DESCRIPTION: <write something here so we know what you did>
    """
    pos = currentGameState.getPacmanPosition()
    foods = currentGameState.getFood()
    capsules = currentGameState.getCapsules()
    ghostStates = currentGameState.getGhostStates()
    scaredTimes = [ghostState.scaredTimer for ghostState in ghostStates]
    v = 30 * currentGameState.getScore()
    
    food = closestFood(pos, foods) 
    capsule = closestCapsules(pos, capsules)
    ghost, timer = closestGhost(pos, ghostStates, scaredTimes)
    
    v += - 16 * food - 10 * capsule

    if capsule < food:
        v -= 6 * capsule
        v += 6 * food

    if timer > ghost:
        v += -22 * ghost
    else:
        v += 2 * ghost
    
    return v

# Abbreviation
better = betterEvaluationFunction
