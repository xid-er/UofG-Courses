## Implements a simple FSM class, and allows interactive
## simulation using TKInter, as well as automatic fuzz
## testing.

import time
from collections import defaultdict
from fsm_viewer import StateMachineViewer, PhaseMachineViewer
import random
from IPython.kernel.zmq.eventloops import register_integration


@register_integration("xtk")
def loop_tk(kernel):
    """Start a kernel with the Tk event loop."""
    from tkinter import Tk

    # Tk uses milliseconds
    poll_interval = int(1000 * kernel._poll_interval)
    # For Tkinter, we create a Tk object and call its withdraw method.
    class Timer(object):
        def __init__(self, func):
            self.app = Tk()
            self.app.withdraw()
            self.func = func

        def on_timer(self):
            self.func()
            self.app.after(poll_interval, self.on_timer)

        def start(self):
            self.on_timer()  # Call it once to get things going.
            self.app.mainloop()

    kernel.timer = Timer(kernel.do_one_iteration)
    kernel.timer.start()


@loop_tk.exit
def loop_tk_exit(kernel):
    kernel.app.destroy()

class FSM:
    def __init__(self, state_action_table):
        """Create a new FSM object, given a dictionary of the form:
        (state, action) -> new_state

        States and actions should be strings, or in the case of actions
        may also be numbers indicating a timeout.
    
        """
        # get a list of all states and validate the machine
        self.from_states = set(
            [state_action[0] for state_action in state_action_table.keys()]
        )
        self.to_states = set(state_action_table.values())
        self.action_table = defaultdict(dict)
        self.timeout_table = defaultdict(list)
        self.states = self.from_states | self.to_states

        self.actions = set()
        # create the action table
        for ((from_state, action), to_state) in state_action_table.items():
            self.actions.add(action)
            self.action_table[from_state][action] = to_state
            if type(action) == type(5.0) or type(action) == type(5):
                self.timeout_table[from_state].append((action, to_state))

        # maintain a sorted table of timeouts from any given state
        self.timeout_table = {
            from_state: sorted(timeouts)
            for from_state, timeouts in self.timeout_table.items()
        }

        self.validate()
        # default to starting in the first state defined
        self.state = list(state_action_table.keys())[0][0]
        self.enter_time = time.clock()

        # remember state transitions
        self.history = []

        # allow callbacks when we enter/leave/reenter a state
        # or when specific actions occur, or when specific action/state combos occur
        self.entry_hooks = {}
        self.leave_hooks = {}
        self.self_hooks = {}
        self.action_hooks = {}
        self.action_state_hooks = {}

    def hook_enter(self, state, fn):
        if state in self.states:
            self.entry_hooks[state] = fn

    def hook_leave(self, state, fn):
        if state in self.states:
            self.leave_hooks[state] = fn

    def hook_self(self, state, fn):
        if state in self.states:
            self.self_hooks[state] = fn

    def hook_action(self, action, fn):
        self.action_hooks[action] = fn

    def hook_action_state(self, state, action, fn):
        self.action_state_hooks[(state, action)] = fn

    def validate(self):
        """Check if the state machine has any:
            states that have no incoming actions (unreachable) or any
            states that have no outgoing actions (unleavable traps).
        """
        unreachable_states = [
            state for state in self.from_states if state not in self.to_states
        ]
        unleavable_states = [
            state for state in self.to_states if state not in self.from_states
        ]
        if len(unreachable_states) > 0:
            print("WARNING: the following states are never entered by any action.")
            print("\t" + "\n\t".join(unreachable_states))
        if len(unleavable_states) > 0:
            print("WARNING: the following states entered by never left by any action.")
            print("\t" + "\n\t".join(unleavable_states))

    def reset(self):
        """Clear the history."""
        self.history = []

    def remaining_time(self):
        """Return the time remaining until the first timeout expires, or
        None if there is no timeout in this state."""
        dwell_time = time.clock() - self.enter_time
        if self.state in self.timeout_table:
            if len(self.timeout_table[self.state]) > 0:
                t, state = self.timeout_table[self.state][0]
                return t - dwell_time
        return None

    def tick(self):
        """
            Update timing, so that states with timeouts will expire as expected.            
        """
        time_left = self.remaining_time()
        if time_left is not None and time_left <= 0.0:
            state = self.timeout_table[self.state][0][1]
            self._transition(
                state,
                action="timeout:{t:.2f}".format(t=self.timeout_table[self.state][0][0]),
            )

    def _transition(self, state, action):
        """Set the state of the machine to the given state,
            validating that it is in fact a valid state"""
        if state in self.states:
            t = time.clock()            
            # call any callbacks that need calling on the transitions
            if self.state in self.leave_hooks:
                self.leave_hooks[self.state](self.state, state, action)
            if action in self.action_hooks:
                self.action_hooks[action](self.state, state, action)
            if (self.state, action) in self.action_state_hooks:
                self.action_state_hooks[(self.state, action)](self.state, state, action)
            if state in self.entry_hooks:
                self.entry_hooks[state](self.state, state, action)
            if state == self.state and state in self.self_hooks:
                self.self_hooks[action](self.state, state, action)

            # record the history
            if type(action)!=type(""):
                action_name = "timeout:{t:.1f}".format(t=action)
            else:
                action_name = action

            self.history.append([self.enter_time, t, self.state, action_name, state])

            # update the state
            self.state = state
            self.enter_time = t
        else:
            raise ValueError(
                "State '{state}' is not a valid state.".format(state=state)
            )

    def get_next_state(self, action):
        """Return the state that execution action in the given state
        would take the FSM to, or None if that isn't a possible
        transition from this state"""
        if self.state in self.action_table and action in self.action_table[self.state]:
            return self.action_table[self.state][action]

    def available_actions(self):
        """Return the list of possible actions in the current state, excluding timeouts."""
        return list(self.action_table[self.state].keys())

    def action(self, action):
        """Make a transition in the state machine. Action specifies
        the action to take. Will raise ValueError if the action is
        not defined for the current state."""
        
        if action in self.action_table[self.state]:
            self._transition(self.action_table[self.state][action], action=action)



def fsm_simulate(machine, start_state=None, fuzz=None):
    if len(machine)<1:
        raise ValueError("A state machine needs at least one state/action transition.")

    fsm = FSM(machine)
    fsm._transition(start_state, "START")
    # fuzz testing; just do random transitions
    # and return the result
    if fuzz is not None:
        for i in range(fuzz):
            options = fsm.available_actions()
            if len(options) > 0:
                fsm.action(random.choice(options))
            else:
                raise ValueError(
                    "Trapped in state '{state}'; cannot exit.".format(state=fsm.state)
                )
        return fsm.history
    else:
        # start the state machine viewer
        viewer = StateMachineViewer(fsm)
        #viewer.root.mainloop()
        return fsm.history


def phase_simulate(machine, start_state=None, regions=None, **kwargs):
    if len(machine)<1:
        raise ValueError("A state machine needs at least one state/action transition.")

    fsm = FSM(machine)
    fsm._transition(start_state, "START")
    # fuzz testing; just do random transitions
    # and return the result
    # start the state machine viewer
    viewer = PhaseMachineViewer(fsm, regions=regions or {}, **kwargs)
    #if __name__=="__main__":
    #viewer.root.mainloop()
    return fsm.history

def phase_simulate_trial(machine, start_state=None, regions=None, **kwargs):
    if len(machine)<1:
        raise ValueError("A state machine needs at least one state/action transition.")

    fsm = FSM(machine)
    fsm._transition(start_state, "START")
    # fuzz testing; just do random transitions
    # and return the result
    # start the state machine viewer
    viewer = PhaseMachineViewer(fsm, regions=regions or {}, **kwargs)
    #if __name__=="__main__":
    #viewer.root.mainloop()
    return viewer

if __name__ == "__main__":
    on_off_fsm = {
        # (state, action)    -> state
        ("off", "turn_on"): "on",
        ("on", "turn_off"): "off",
        ("on", 1.0): "off",
    }
    results = phase_simulate(on_off_fsm, "off", tasks=[["off", "on"]], 
    params={"noise":0.05, "offset":0.075, "smoothing":0.08})
    
    
