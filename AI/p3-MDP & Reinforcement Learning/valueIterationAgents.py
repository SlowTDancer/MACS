# valueIterationAgents.py
# -----------------------
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


# valueIterationAgents.py
# -----------------------
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


import mdp, util

from learningAgents import ValueEstimationAgent
import collections

class ValueIterationAgent(ValueEstimationAgent):
    """
        * Please read learningAgents.py before reading this.*

        A ValueIterationAgent takes a Markov decision process
        (see mdp.py) on initialization and runs value iteration
        for a given number of iterations using the supplied
        discount factor.
    """
    def __init__(self, mdp, discount = 0.9, iterations = 100):
        """
          Your value iteration agent should take an mdp on
          construction, run the indicated number of iterations
          and then act according to the resulting policy.

          Some useful mdp methods you will use:
              mdp.getStates()
              mdp.getPossibleActions(state)
              mdp.getTransitionStatesAndProbs(state, action)
              mdp.getReward(state, action, nextState)
              mdp.isTerminal(state)
        """
        self.mdp = mdp
        self.discount = discount
        self.iterations = iterations
        self.values = util.Counter() # A Counter is a dict with default 0
        self.runValueIteration()

    def runValueIteration(self):
        num_iterations = self.iterations
        mdp = self.mdp
        for i in range(0, num_iterations):
            states = mdp.getStates()
            values = util.Counter()
            for state in states:
                values[state] = self.highestQValue(state, mdp)
            self.values = values

    def highestQValue(self, state, mdp):
        if mdp.isTerminal(state):
            return 0
        actions = mdp.getPossibleActions(state)
        res = -1e18
        for action in actions:
            currQValue = self.computeQValueFromValues(state, action)
            res = max(res, currQValue)
        return res  


    def getValue(self, state):
        """
          Return the value of the state (computed in __init__).
        """
        return self.values[state]


    def computeQValueFromValues(self, state, action):
        """
          Compute the Q-value of action in state from the
          value function stored in self.values.
        """
        if action is None:
            return 0
        res = 0
        ts = self.mdp.getTransitionStatesAndProbs(state, action)
        for t in ts:
            nextState, p = t
            res += p * (self.mdp.getReward(state, action, nextState) + self.discount*self.values[nextState])
        return res


    def computeActionFromValues(self, state):
        """
          The policy is the best action in the given state
          according to the values currently stored in self.values.

          You may break ties any way you see fit.  Note that if
          there are no legal actions, which is the case at the
          terminal state, you should return None.
        """
        actions = self.mdp.getPossibleActions(state)
        bestAction, bestValue = None, -1e18
        for action in actions:
            currValue = self.computeQValueFromValues(state, action)
            if currValue > bestValue:
                bestValue = currValue
                bestAction = action
        return bestAction

    def getPolicy(self, state):
        return self.computeActionFromValues(state)

    def getAction(self, state):
        "Returns the policy at the state (no exploration)."
        return self.computeActionFromValues(state)

    def getQValue(self, state, action):
        return self.computeQValueFromValues(state, action)

class AsynchronousValueIterationAgent(ValueIterationAgent):
    """
        * Please read learningAgents.py before reading this.*

        An AsynchronousValueIterationAgent takes a Markov decision process
        (see mdp.py) on initialization and runs cyclic value iteration
        for a given number of iterations using the supplied
        discount factor.
    """
    def __init__(self, mdp, discount = 0.9, iterations = 1000):
        """
          Your cyclic value iteration agent should take an mdp on
          construction, run the indicated number of iterations,
          and then act according to the resulting policy. Each iteration
          updates the value of only one state, which cycles through
          the states list. If the chosen state is terminal, nothing
          happens in that iteration.

          Some useful mdp methods you will use:
              mdp.getStates()
              mdp.getPossibleActions(state)
              mdp.getTransitionStatesAndProbs(state, action)
              mdp.getReward(state)
              mdp.isTerminal(state)
        """
        ValueIterationAgent.__init__(self, mdp, discount, iterations)

    def runValueIteration(self):
        num_iterations = self.iterations
        mdp = self.mdp
        states = mdp.getStates()
        for i in range(0, num_iterations):
            state = states[i % len(states)]
            self.values[state] = self.highestQValue(state, mdp)

class PrioritizedSweepingValueIterationAgent(AsynchronousValueIterationAgent):
    """
        * Please read learningAgents.py before reading this.*

        A PrioritizedSweepingValueIterationAgent takes a Markov decision process
        (see mdp.py) on initialization and runs prioritized sweeping value iteration
        for a given number of iterations using the supplied parameters.
    """
    def __init__(self, mdp, discount = 0.9, iterations = 100, theta = 1e-5):
        """
          Your prioritized sweeping value iteration agent should take an mdp on
          construction, run the indicated number of iterations,
          and then act according to the resulting policy.
        """
        self.theta = theta
        ValueIterationAgent.__init__(self, mdp, discount, iterations)

    def runValueIteration(self):
        num_iterations = self.iterations
        theta = self.theta
        preds = self.getPredecessors()
        queue = util.PriorityQueue()
        mdp = self.mdp
        states = mdp.getStates()

        for state in states:
            if mdp.isTerminal(state):
                continue
            diff = abs(self.values[state] - self.highestQValue(state, mdp))
            queue.push(state, -diff)
            
        for i in range(0, num_iterations):
            if queue.isEmpty():
                break
            currState = queue.pop()
            self.values[currState] = self.highestQValue(currState, mdp)
            for pred in preds[currState]:
                diff = abs(self.values[pred] - self.highestQValue(pred, mdp))
                if diff > theta:
                    queue.update(pred, -diff)


    def getPredecessors(self):
        res = {}
        mdp = self.mdp
        states = mdp.getStates()

        for state in states:
            actions = mdp.getPossibleActions(state)
            for action in actions:
                ts = mdp.getTransitionStatesAndProbs(state, action)
                successorStates = [t[0] for t in ts]
                for successorState in successorStates:
                    res.setdefault(successorState, set()).add(state)

        return res

