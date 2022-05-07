import os
import numpy as np
import time
import json
geometry = {"width": 600, "height": 420}

# ugh
from tkinter import *
from PIL import Image, ImageTk

from filter import SavitzkyGolay, ButterFilter

# load the animation images for
# each flashlight state
animations = {
    "off": ["state_off.png"],
    "high": ["state_on.png"],
    "on": ["state_on_low.png"],
    "locked": ["state_locked.png"],
    "strobe": ["state_on_low.png", "state_on.png"],
    "zombie": ["state_on_low.png", "state_on.png", "state_nuke.png"] + [ "state_nuke.png"] * 16 +
              ["state_nuke.png", "state_nuke_2.jpg", "state_nuke_3.jpg", "state_nuke_4.jpg"],
}

IMAGE_SIZE = 400
def load_img(fname):
        img = Image.open(fname)
        img = img.resize(((IMAGE_SIZE*16)//9, IMAGE_SIZE), Image.ANTIALIAS)
        tkimage = ImageTk.PhotoImage(img)
        return tkimage

animation_imgs = {}

def load_anims():
    for state, fnames in animations.items():
        animation_imgs[state] = [load_img(os.path.join("imgs", fname)) for fname in fnames]


class StateMachineViewer:
    def __init__(self, fsm):
        
        self.root = Tk()
        
        self.frame = Frame(
            self.root, width=geometry["width"], height=geometry["height"]
        )
        load_anims()

        self.frame_index = 0
        # a very ugly interface
        self.frame.pack(expand=1, fill="both")
        self.state_frame = Frame(self.frame)
        self.action_frame = Frame(self.frame)
        self.timeout_frame = Frame(self.frame)
        self.torch_frame = Frame(self.frame)        
        self.state_frame.grid(row=0, column=0, padx=20, pady=20)
        # add the image label for the torch        
        self.torch_label= Label(self.torch_frame)        
        self.action_frame.grid(row=2, column=0, padx=20, pady=20)
        self.state_label = Label(self.state_frame, text="---", font=("Arial", 40), width=20)
        self.state_label.pack(expand=1, fill="both")
        self.torch_frame.grid(row=1, column=0, padx=20, pady=20)
        self.torch_label.pack(expand=1, fill="both")                
        self.timeout_label = Label(self.state_frame, text="", font=("Arial", 20))
        self.timeout_label.pack(expand=1, fill="both", pady=20)

    
        self.buttons = {}
        # create the button array for **all**
        # possible actions in the state machine
        row, col = 0, 0
        n_cols = 4
        for action in fsm.actions:
            if type(action) == type(""):
                action_button = Button(self.action_frame, text=action)
                action_button["state"] = "disabled"
                action_button.grid(row=row, column=col)
                self.buttons[action] = action_button
                col += 1
                if col > n_cols:
                    col = 0
                    row += 1

        # set up the window
        self.root.iconify()
        self.root.update()
        self.root.deiconify()
        self.root.wm_title("Press ESC to quit")
        self.root.bind("<Escape>", self.quit)
        self.root.protocol("WM_DELETE_WINDOW", self.quit)
        self.root.update()

        # attach the callbacks for animation
        self.root.after(int(10), self.tick)
        self.frame_rate = 0.15

        # initialise the FSM
        self.fsm = fsm        
        self.torch_state = "off"
        self.update_actions()

    def start(self):
        """Start the TK mainloop"""
        self.root.mainloop()

    def quit(self, _=None):
        """Destroy the TK window and exit the main loop"""
        self.root.destroy()

    def update_actions(self):
        """Update the mapping of possible actions to keys"""
        actions = [
            action
            for action in self.fsm.available_actions()
            if type(action) == type("")
        ]

        self.action_mapping = {
            str(i): (action, self.fsm.get_next_state(action))
            for i, action in enumerate(sorted(actions))
        }

        # update countdown
        delta_t = self.fsm.remaining_time()
        if delta_t is not None:
            self.timeout_label["text"] = "{t:4.1f}".format(t=delta_t)
        else:
            self.timeout_label["text"] = ""

        # enable all the buttons that correspond to valid
        # actions in this state
        for action, button in self.buttons.items():
            if action in actions:
                button["state"] = "normal"
                button["command"] = lambda action=action: self.transition(action)
            else:
                button["state"] = "disabled"

        self.state_label["text"] = self.fsm.state

        # check if we need to set the torch image
        for state in animation_imgs:
            if self.fsm.state.endswith(":"+state):
                self.torch_state = state

    def transition(self, action):
        """Apply the given action to the FSM and
           update the visuals"""
        self.fsm.action(action)
        self.frame_index = 0
        self.update_actions()

    def tick(self):
        """Regularly update the finite state machine so that timeout transitions
        can be taken as required."""
        self.fsm.tick()
        self.update_actions()
        self.root.after(int(10), self.tick)
        self.frame_index += self.frame_rate        

        # update animation frames
        if self.torch_state in animation_imgs:
            frame = int(self.frame_index) % len(animation_imgs[self.torch_state])
            self.torch_label["image"] = animation_imgs[self.torch_state][frame]

### Represents a rectangular region
### of phase space
class Region:
    def __init__(self, pts):
        self.x1, self.x2, self.y1, self.y2 = pts
        self.active = False
        # make sure coords are in correct order
        if self.x2<self.x1:
            self.x1, self.x2 = self.x2, self.x1
        
        if self.y2<self.y1:
            self.y1, self.y2 = self.y2, self.y1
        
    def test(self, x,y):
        # test if x,y is in the rectangle
        # if so, set the active flag
        # return both the previous and current
        # state of that flag
        last_state = self.active
        if x>self.x1 and x<self.x2 and y>self.y1 and y<self.y2:            
            self.active = True
        else:
            self.active = False
        return last_state, self.active

    def reset(self):
        self.active = False

class PhaseMachineViewer:
    def __init__(self, fsm, params={}, regions={}, tasks=[], condition="none", fname=None):
   
        self.noise_offset = params.get("offset", 0.0)
        self.slider_offset = np.random.normal(0, self.noise_offset)
        self.slider_noise = params.get("noise", 0.0) 
        self.cutoff = params.get("smoothing", 1.0)/2.0+0.01
        self.smoothing = ButterFilter(4, self.cutoff)
        self.just_entered = False
        self.deriv_scale = 10.0

        self.full_path = fname
        self.root = Tk()        
        load_anims()

        

        
        self.outer_frame = Frame(self.root)
        self.condition_label = Label(self.outer_frame, text="Condition: {cond}".format(cond=condition), font=("Arial", 30), fg='gray')
        self.condition_label.pack()
        self.task_label = Label(self.outer_frame, text="<no task>", font=("Arial", 20))
        self.task_label.pack()
        

        self.frame = Frame(self.outer_frame)
        
        self.slider_frame = Frame(self.outer_frame)        
        self.phase_canvas = Canvas(self.slider_frame, width=geometry["width"], 
        height=geometry["height"], background='black')
        self.slider_canvas = Canvas(self.slider_frame, width=geometry["width"], 
        height=100, background='gray')
        
        self.phase_canvas.pack(anchor="n")        
        self.slider_canvas.pack(anchor="n", pady=60)
        self.slider_frame.pack(anchor="n", side=RIGHT, padx=20, pady=20)        
        self.frame.pack(anchor='w')
        self.outer_frame.pack()
        self.frame_index = 0

        # bind the events to the slider
        self.slider_canvas.bind( "<Any-Motion>", self.slide)
     
        self.slider_canvas.bind( "<Enter>", self.slide_on)
        self.slider_canvas.bind( "<Leave>", self.slide_off)
     
        # a very ugly interface
        self.frame.pack(expand=1, fill="both")
        self.state_frame = Frame(self.frame)
        self.action_frame = Frame(self.frame)
        self.timeout_frame = Frame(self.frame)
        self.torch_frame = Frame(self.frame)        
        self.state_frame.grid(row=0, column=0, padx=20, pady=20)
        # add the image label for the torch        
        self.torch_label= Label(self.torch_frame)        
        self.action_frame.grid(row=2, column=0, padx=20, pady=20)
        self.state_label = Label(self.state_frame, text="---", font=("Arial", 40), width=20)
        self.state_label.pack(expand=1, fill="both")
        self.torch_frame.grid(row=1, column=0, padx=20, pady=20)
        self.torch_label.pack(expand=1, fill="both")                
        self.timeout_label = Label(self.state_frame, text="", font=("Arial", 20))
        self.timeout_label.pack(expand=1, fill="both", pady=20)

        # set up the window
        self.root.iconify()
        self.root.update()
        self.root.deiconify()
        self.root.wm_title("Experimental trial")
        #self.root.bind("<Escape>", self.quit)
        self.root.protocol("WM_DELETE_WINDOW", self.quit)
        self.root.update()
        self.root.lift()
        self.root.attributes("-topmost", True)

        # attach the callbacks for animation
        self.root.after(int(10), self.tick)
        self.frame_rate = 0.15

        # initialise the FSM
        self.fsm = fsm        
        self.torch_state = "off"
        self.update_actions()
        self.slider_pos = 0.0
        self.raw_filter = SavitzkyGolay(11,0,3)
        self.deriv_filter = SavitzkyGolay(11,1,3)
        self.phase_trace = []
        self.slider_offset = 0
        self.slider_active = False

        self.task_total_actions = 0
        self.current_task = []
        self.task_start_time = 0
        self.task_name = ""
        self.task_total_incorrect = 0
        
       

        # initialise the activation regions
        # maps names -> region
        self.regions = {}
        for region_name, rect in regions.items():
            self.regions[region_name] = Region(rect)        
        
        # update task sequences
        self.tasks = tasks 
        
        self.task_results = []
        self.use_tasks = len(tasks)>0
        self.n_tasks = len(self.tasks)
        self.next_task()
        
        
        

    def next_task(self):
        # no tasks at all, so do nothing
        if not self.use_tasks:
            return 

        if len(self.tasks)==0:
            # no tasks, so exit
            self.quit()
        else:
            self.current_task = self.tasks.pop()
            self.task_name = " - ".join(self.current_task)
            self.update_tasks()

            self.task_start_time = time.clock()
            self.task_total_actions = 0
            self.task_total_incorrect = 0
        
        

    def update_tasks(self):        
        # check if we are in the state we are looking for next
        if len(self.current_task)>0:
            if self.torch_state == self.current_task[0]:            
                # have we finished this task?    
                self.current_task = self.current_task[1:]
            else:            
                self.task_total_incorrect += 1
            
        if len(self.current_task)==0:
                print("Task complete!")
                task_duration = time.clock() - self.task_start_time
                # record:
                # (task_name, duration, actions_required, total_actions)
                self.task_results.append((self.task_name,
                task_duration, self.task_total_actions,
                self.task_total_incorrect))
                self.next_task()

        # set the label showing which task to do next
        self.task_label["text"] = ("Task {task}/{n}: ".format(task=self.n_tasks-len(self.tasks), 
        n=self.n_tasks)) +" ⇒ ".join(self.current_task)
        
        

    def slide_on(self, event):
        """Handle the mouse coming over the slider"""
        self.slider_offset = np.random.normal(0, self.noise_offset)
        self.slider_active = True
        self.just_entered = True

    def slide_off(self, event):
        """Handle the mouse leaving the slider"""
        self.slider_active = False

    def slide(self, event):
        """Process a mouse move event
        over the slider control, updating 
        the mouse coordinate to the normalised
        slider position in the range 0.0->1.0"""        
        self.slider_pos = event.x / self.slider_canvas.winfo_width()

    def start(self):
        """Start the TK mainloop"""
        self.root.iconify()
        self.root.update()
        self.root.deiconify()
        self.root.mainloop()

    def quit(self, _=None):
        """Destroy the TK window and exit the main loop"""
        # write the JSON to disk
        if self.full_path is not None:
            with open(self.full_path, "w") as f:
                json.dump(self.task_results, f)
                print("*"*80)
                print("LOG FILE WRITTEN TO ", self.full_path)
                print("{n_tasks} tasks recorded".format(n_tasks=len(self.task_results)))                
                print("*"*80)
        
        self.root.destroy()

    def update_actions(self):
        """Update the mapping of possible actions to keys"""
        actions = [
            action
            for action in self.fsm.available_actions()
            if type(action) == type("")
        ]

        self.action_mapping = {
            str(i): (action, self.fsm.get_next_state(action))
            for i, action in enumerate(sorted(actions))
        }

        # update countdown
        delta_t = self.fsm.remaining_time()
        if delta_t is not None:
            self.timeout_label["text"] = "{t:4.1f}".format(t=delta_t)
        else:
            self.timeout_label["text"] = ""

       
        self.state_label["text"] = self.fsm.state

        # check if we need to set the torch image
        for state in animation_imgs:
            if self.fsm.state.endswith(":"+state):
                self.torch_state = state

    def transition(self, action):
        """Apply the given action to the FSM and
           update the visuals"""        
 
        self.fsm.action(action)
        self.frame_index = 0
        self.update_actions()
        # print (self)
        self.task_total_actions += 1
        
    def redraw_phase_trace(self):
        """Draw the phase space trace on the canvas block"""
        self.phase_canvas.delete(ALL)
        pts = np.array(self.phase_trace[-100:])
        w, h = self.phase_canvas.winfo_width(), self.phase_canvas.winfo_height()

        def map_to_screen(pts):
            pts = np.array(pts)
            pts[:,0] = pts[:,0] * w
            pts[:,1] = pts[:,1] * (h/2) + (h/2)                
            return pts

        
        pts = map_to_screen(pts)
        self.phase_canvas.create_rectangle(0,0,w,h/2, fill='#113366')
        self.phase_canvas.create_rectangle(0,h/2,w,h, fill='#664411')
        self.phase_canvas.create_text(w/2, 10, text='+ve velocity -1.0', fill='#ffffff')
        self.phase_canvas.create_text(w/2, h-10, text='-ve velocity  1.0', fill='#ffffff')
        self.phase_canvas.create_text(w/2, h/2, text='position', fill='#ffffff')
        self.phase_canvas.create_text(10, h/2, text='0.0', fill='#ffffff')
        self.phase_canvas.create_text(w-10, h/2, text='1.0', fill='#ffffff')

        # draw the regions
        for region_name, region in self.regions.items():            
            rect_pts = map_to_screen(np.array([[region.x1, region.y1], 
                                                [region.x2, region.y2]]))
            if region.active:
                color = "yellow"
            else:
                color = "gray"

            self.phase_canvas.create_rectangle(rect_pts[0,0], rect_pts[0,1], rect_pts[1,0], rect_pts[1,1], outline=color)
            self.phase_canvas.create_text(rect_pts[0,0]+25, rect_pts[0,1]+15, text=region_name, fill=color, justify="left")


        if len(pts)>2:
            self.phase_canvas.create_line(*(pts.ravel()), fill='white')

        

    def redraw_slider(self):
        self.slider_canvas.delete(ALL)
        if self.slider_active:
            self.slider_canvas["background"] = "#202020"
            x, dx = self.phase_trace[-1]
            x_coord = x * self.slider_canvas.winfo_width()            
            w = 20
            self.slider_canvas.create_rectangle(x_coord-w, 0, x_coord+w, self.slider_canvas.winfo_height(), fill='white')
        else:
            self.slider_canvas["background"] = "#505050"
        
        
    def update_slider(self):
        """Update the slider state"""
        # update the phase space filters

        if self.just_entered:
            loops = 32
        else:
            loops = 1

        for i in range(loops):
            processed_x = self.slider_pos + self.slider_offset
            processed_x += np.random.normal(0, self.slider_noise)
            processed_x = self.smoothing.new_sample(processed_x)
            x, dx = self.raw_filter.new_sample(processed_x), self.deriv_filter.new_sample(processed_x)  * self.deriv_scale    

        self.just_entered = False
        
        self.phase_trace.append((x,dx))

        for name, region in self.regions.items():
            prev, current = region.test(x,dx)            
            # state change
            if prev!=current:                
                # leave
                if prev:
                # enter                    
                    self.transition(name+":exit")
                else:
                    self.transition(name+":enter")



        self.redraw_phase_trace()
        self.redraw_slider()

    def tick(self):
        """Regularly update the finite state machine so that timeout transitions
        can be taken as required."""

        old_torch_state = self.torch_state
        self.fsm.tick()
        self.update_actions()
        self.root.after(int(10), self.tick)
        self.frame_index += self.frame_rate  

        self.update_slider()

        # if the torch status changed, see
        # if we need to update the task checking:        
        if old_torch_state!=self.torch_state:            
            self.update_tasks()

        # update animation frames
        if self.torch_state in animation_imgs:
            frame = int(self.frame_index) % len(animation_imgs[self.torch_state])
            self.torch_label["image"] = animation_imgs[self.torch_state][frame]

