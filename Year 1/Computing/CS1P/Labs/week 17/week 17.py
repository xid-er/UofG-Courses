# -*- coding: utf-8 -*-
"""
Created on Fri Mar  6 13:05:56 2020

@author: karli
"""
import tkinter as tk

def distance(cur, nex):
    return ((cur[0] - nex[0]) ** 2 + (cur[1] - nex[1]) ** 2) ** 0.5

cities = []

# Read file
with open("Cities.txt",'r') as f:
    for i in f.readlines():
        data = i.strip().split()
        cities.append([data[2], int(data[0]), int(data[1])])
    print(cities)
    
# Draw cities
if len(cities) != 0:
    top = tk.Tk()
    canvas = tk.Canvas(top)
    canvas.grid()
    for i in cities:
        canvas.create_oval(i[1]-1, i[2]-1, i[1]+1, i[2]+1)
        canvas.create_text(i[1]-3, i[2]+5, text=i[0])
        
# Find NN sequence and length
    length = 0
    for i in range(len(cities) - 1):
        cur = [cities[i][1], cities[i][2]]
        nex = [cities[i+1][1], cities[i+1][2]]
        nex_ix = i+1
        min_dis = distance(cur,nex)
        for j in range(i+1, len(cities)):
            nex = [cities[j][1], cities[j][2]]
            temp_dis = distance(cur,nex)
            if temp_dis < min_dis:
                min_dis = temp_dis
                nex_ix = j
        length += min_dis
        cities[i+1], cities[nex_ix] = cities[nex_ix], cities[i+1]
        
# Draw lines
    for i in range(len(cities) - 1):
        canvas.create_line(cities[i][1], cities[i][2], cities[i+1][1], cities[i+1][2])
    n = len(cities)
    canvas.create_line(cities[n-1][1], cities[n-1][2], cities[0][1], cities[0][2])
    length += distance([cities[n-1][1], cities[n-1][2]], [cities[0][1], cities[0][2]])
    canvas.create_text(200,250, text='Tour length = %.2f' % length)
        
    tk.mainloop()