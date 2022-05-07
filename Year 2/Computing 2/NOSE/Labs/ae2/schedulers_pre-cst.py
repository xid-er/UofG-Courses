from des import SchedulerDES
from event import Event, EventTypes
from process import Process, ProcessStates

class FCFS(SchedulerDES):
    def scheduler_func(self, cur_event):
        return self.processes[cur_event.process_id]

    def dispatcher_func(self, cur_process):
        cur_process.process_state = ProcessStates.RUNNING
        run_time = cur_process.run_for(cur_process.remaining_time, self.time)
        cur_process.process_state = ProcessStates.TERMINATED
        return Event(process_id=cur_process.process_id, 
                     event_type=EventTypes.PROC_CPU_DONE, 
                     event_time=self.time + run_time)
                     
class SJF(SchedulerDES):
    def scheduler_func(self, cur_event):
        min_ix = 0;
        
        min_time = 1000000;
        for ix in range(len(self.processes)):
            serv_time = self.processes[ix].service_time
            if serv_time < min_time and self.processes[ix].process_state == ProcessStates.READY:
                min_time = serv_time
                min_ix = ix
        return self.processes[min_ix]

    def dispatcher_func(self, cur_process):
        cur_process.process_state = ProcessStates.RUNNING
        run_time = cur_process.run_for(cur_process.remaining_time, self.time)
        cur_process.process_state = ProcessStates.TERMINATED
        return Event(process_id=cur_process.process_id, 
                     event_type=EventTypes.PROC_CPU_DONE, 
                     event_time=self.time + run_time)


class RR(SchedulerDES):
    def scheduler_func(self, cur_event):
        return self.processes[cur_event.process_id]

    def dispatcher_func(self, cur_process):
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
        min_ix = 0;
        min_time = 1000000;
        for ix in range(len(self.processes)):
            serv_time = self.processes[ix].remaining_time
            if serv_time < min_time and self.processes[ix].process_state == ProcessStates.READY:
                min_time = serv_time
                min_ix = ix
        return self.processes[min_ix]

    def dispatcher_func(self, cur_process):
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