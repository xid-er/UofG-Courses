from des import SchedulerDES
from event import Event, EventTypes
from process import Process, ProcessStates

"""
 schedulers.py
 
 made by Kārlis Siders (2467273S) & Maximilian Donar (2483099D)
 for Networks and Operating Systems Essentials
 on 25th November 2020.
"""

"""
note: Process.run_for is implemented to run only
    for the time it needs. Thus, parsing any time
    as its first argument does not inflate the
    real processing time of run_for method.
    Returns the actual time it needed. 
"""


class FCFS(SchedulerDES):

    def scheduler_func(self, cur_event):
        """
             Scheduler function for FCFS algorithm
             @:parameter trigger Event
             @:return first READY Process object in queue
        """
        return self.processes[cur_event.process_id]

    def dispatcher_func(self, cur_process):
        """
            Dispatcher function for FCFS algorithm
            @:parameter Process to be executed
            Changes Process state to RUNNING and runs it for as long
              as necessary.
            @:return Event object confirming the completion of Process,
              updates the current time.
        """
        cur_process.process_state = ProcessStates.RUNNING
        run_time = cur_process.run_for(cur_process.remaining_time, self.time)
        cur_process.process_state = ProcessStates.TERMINATED
        return Event(process_id=cur_process.process_id,
                     event_type=EventTypes.PROC_CPU_DONE,
                     event_time=self.time + run_time)


class SJF(SchedulerDES):

    def scheduler_func(self, cur_event):
        """
            Scheduler function for SJF algorithm
            @:parameter trigger Event
            @:return READY Process object with the shortest Process.service_time
        """
        min_ix = 0
        min_time = 1000000
        for ix in range(len(self.processes)):
            serv_time = self.processes[ix].service_time
            if serv_time < min_time and self.processes[ix].process_state == ProcessStates.READY:
                min_time = serv_time
                min_ix = ix
        return self.processes[min_ix]

    def dispatcher_func(self, cur_process):
        """
            Dispatcher function for SJF algorithm
            @:parameter Process to be executed
            Changes Process state to RUNNING and runs it for as long
              as necessary.
            @:return Event object confirming the completion of Process,
              updates the current time.
        """
        cur_process.process_state = ProcessStates.RUNNING
        run_time = cur_process.run_for(cur_process.remaining_time, self.time)
        cur_process.process_state = ProcessStates.TERMINATED
        return Event(process_id=cur_process.process_id,
                     event_type=EventTypes.PROC_CPU_DONE,
                     event_time=self.time + run_time)


class RR(SchedulerDES):

    def scheduler_func(self, cur_event):
        """
             Scheduler function for RR algorithm
             @:parameter trigger Event
             @:return first READY Process object in queue
        """
        return self.processes[cur_event.process_id]

    def dispatcher_func(self, cur_process):
        """
            Dispatcher function for RR algorithm
            @:parameter Process to be executed
            Changes Process state to RUNNING and runs it for a given
              amount of time - quantum.
            @:return Event object confirming the state of Process,
              updates the current time.
        """
        cur_process.process_state = ProcessStates.RUNNING
        run_time = cur_process.run_for(self.quantum, self.time)
        if cur_process.remaining_time == 0:
            cur_process.process_state = ProcessStates.TERMINATED
            return Event(process_id=cur_process.process_id,
                         event_type=EventTypes.PROC_CPU_DONE,
                         event_time=self.time + run_time)
        else:
            cur_process.process_state = ProcessStates.READY
            return Event(process_id=cur_process.process_id,
                         event_type=EventTypes.PROC_CPU_REQ,
                         event_time=self.time + run_time)


class SRTF(SchedulerDES):

    def scheduler_func(self, cur_event):
        """
            Scheduler function for SRTF algorithm
            @:parameter trigger Event
            @:return READY Process object with the shortest Process.service_time

            note: This implementation of scheduler function for SRTF
                  does not take into the consideration time required
                  for changing the process while selecting the next
                  shortest job process to be run.
                  Thus, the result with context_switch_time != 0.0
                  might be slightly skewed.
        """
        min_ix = 0
        min_time = 1000000
        for ix in range(len(self.processes)):
            serv_time = self.processes[ix].remaining_time
            if serv_time < min_time and self.processes[ix].process_state == ProcessStates.READY:
                min_time = serv_time
                min_ix = ix
        return self.processes[min_ix]

    def dispatcher_func(self, cur_process):
        """
            Dispatcher function for SRTF algorithm
            @:parameter Process to be executed
            Changes Process state to RUNNING and runs it until
              next Event trigger occurs.
            @:return Event object confirming the state of Process,
              updates the current time.
        """
        cur_process.process_state = ProcessStates.RUNNING
        run_time = self.next_event_time() - self.time
        run_time = cur_process.run_for(run_time, self.time)
        if cur_process.remaining_time == 0:
            cur_process.process_state = ProcessStates.TERMINATED
            return Event(process_id=cur_process.process_id,
                         event_type=EventTypes.PROC_CPU_DONE,
                         event_time=self.time + run_time)
        else:
            cur_process.process_state = ProcessStates.READY
            return Event(process_id=cur_process.process_id,
                         event_type=EventTypes.PROC_CPU_REQ,
                         event_time=self.time + run_time)

class non_pre_priority(SchedulerDES):
    
    pass