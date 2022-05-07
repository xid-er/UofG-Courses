from collections import Counter, defaultdict

def summarise_report(results):
    """
        Take a list of results, and print out a short
        report showing dwell times for each state, and
        the count of each action taken.
    """
    # compute the total time in each state
    time_in_state = defaultdict(float)
    action_count = Counter()
    action_state = Counter()
    
    
    for time_enter, time_leave, from_state, action, to_state in results:        
        time_in_state[from_state] += time_leave - time_enter
        action_count[action] += 1
        action_state[(from_state, action)]  += 1

    # print out the time spent in each state
    print("State dwell times")
    print("-"*80)
    for state, time in time_in_state.items():        
        print("{state} {time:.1f}".format(state=state.ljust(20), time=time))
    print()
    
    # print the number of times each action was triggered
    print("Action counts")
    print("-"*80)
    for action, count in action_count.items():
          print("{action} {count:d}".format(action=action.ljust(20), count=count))
    
    print()
    
    
    # print a state-action table
    # shows the number of times each action was taken
    # from each state    
    print("State-action count table")
    print("-"*80)
    n_actions = len(action_count)
    print(" "*16, end=" ")
    # print the action header
    for action in action_count.keys(): 
        print(action.ljust(10)[:10], end=" ")
    print()
    # print each row
    for state in time_in_state.keys():
        print(state.ljust(16)[:16], end=" ")
        for action in action_count.keys(): 
            count = action_state.get((state, action), 0)
            print(str(count).center(10), end=" ")
        print()
    
    