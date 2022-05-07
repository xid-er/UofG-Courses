import sqlite3
import time
import os
import json
from fsm_simulator import phase_simulate_trial
import pandas as pd

# the list of tasks, as sequences of *flashlight* states
# that must be entered. Note that the starting state will
# be off, and should be entered automatically anyway
tasks = [

    ["off", "on", "off"],
    ["off", "strobe", "off"],
    ["off", "strobe", "on", "high", "on"],
    ["off", "locked", "on", "high", "strobe", "off", "locked"],
    ["off", "on", "high", "on", "off", "on"],
]

# specification for each condition
conditions = {
    "test": {        
    },
    "a" : {        
    },
    "b" : {
        "noise":0.05, "offset":0.075
    },
    "c" : {
        "noise":0.05, "offset":0.075, "smoothing":0.08
    },
}

import random

def phase_trial(machine, start_state, regions, params, repetitions, tasks, condition):
    # create n repetitions of the tasks
    # and randomly order them
    task_list = [t for i in range(repetitions) for t in tasks]
    random.shuffle(task_list)

    fname = time.strftime("%Y-%m-%d-%H-%M-%S")

    full_path = "json/{condition}/trial_{fname}.json".format(condition=condition, fname=fname)
    phase_simulate_trial(machine=machine, start_state=start_state, regions=regions, params=params, tasks=task_list,
    fname=full_path, condition=condition)
    

def run_trial(machine, start_state, regions,  condition="test"):
    # create the directories
    try:
        os.makedirs(os.path.join("json", condition))
    except OSError:
        # directory already exists        
        pass

    if condition=="test":
        # no writing in this case
        # 1 repetition, and just the first task    
        phase_trial(machine, start_state, regions, conditions[condition], repetitions=1, tasks=[
            ["off", "on", "high", "strobe", "off", "locked", "off"]], condition=condition)            
    else:
        phase_trial(machine, start_state, regions, conditions[condition], repetitions=5, tasks=tasks, condition=condition)        
  

def trial_dataframes(condition):        
    # Read all of the json logs for one condition
    # return as a Pandas DataFrame, one record per entry
    json_files = [fname for fname in os.listdir("json/{condition}".format(condition=condition)) if fname.endswith(".json")]    
    assert(len(json_files)>0)
    fname = sorted(json_files)[-1]
    with open(os.path.join("json", condition, fname)) as f:    
        return pd.DataFrame(json.load(f))



if __name__=="__main__":
    on_off_fsm = {
        # (state, action)    -> state
        ("off", "turn_on"): "on",
        ("on", "turn_off"): "off",
        ("on", 1.0): "off",
    }

    run_trial(on_off_fsm, "off", {})