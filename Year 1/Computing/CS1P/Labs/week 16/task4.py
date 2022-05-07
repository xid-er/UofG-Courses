# -*- coding: utf-8 -*-
"""
Created on Fri Feb 28 18:29:44 2020

@author: karli
"""

import tkinter

def convert():
    amount = textVar.get()
    if amount.isnumeric():
        amount = float(amount)
        frCr = fromCur.get()
        rates = {'£-£': 1, '£-€': 1.16, '£-$': 1.28, '£-RM': 5.39}
        btnVars = {'£': pndVar.get(), 
                   '€': euroVar.get(), 
                   '$': dollaVar.get(), 
                   'RM': rngtVar.get()}
        msg = "%.2f is: \n" % amount
        if frCr == 1:
            for key,val in btnVars.items():
                if val == 1:
                    msg += key + ': %.2f \n' % (amount * rates['£-'+key])
        elif frCr == 2:
            for key,val in btnVars.items():
                if val == 1:
                    msg += key + ': %.2f \n' % (amount / rates['£-€'] * rates['£-'+key])
        elif frCr == 3:
            for key,val in btnVars.items():
                if val == 1:
                    msg += key + ': %.2f \n' % (amount / rates['£-$'] * rates['£-'+key])
        elif frCr == 4:
            for key,val in btnVars.items():
                if val == 1:
                    msg += key + ': %.2f \n' % (amount / rates['£-RM'] * rates['£-'+key])
        else:
            msg = 'Please choose a currency to convert'
    else:
        msg = "Sorry, that is not a valid number"
    
    curLbl.configure(text=msg)

top = tkinter.Tk()
top.title("Currency converter")
top.geometry("500x500+350+50")

textVar = tkinter.StringVar("")
textEntry = tkinter.Entry(top,textvariable=textVar,width=12)
textEntry.grid(row=0,column=0)

curLbl = tkinter.Label(top,text="",width=30)
curLbl.grid(row=5,column=0)

fromCur = tkinter.IntVar(0)

fromMenuButton = tkinter.Menubutton(top,text="From:")
fromMenuButton.grid(row=0,column=1)
fromMenu = tkinter.Menu(fromMenuButton,tearoff=0)
fromMenuButton.configure(menu=fromMenu)
fromMenu.add_radiobutton(label="£",variable=fromCur,value=1)
fromMenu.add_radiobutton(label="€",variable=fromCur,value=2)
fromMenu.add_radiobutton(label="$",variable=fromCur,value=3)
fromMenu.add_radiobutton(label="RM",variable=fromCur,value=4)

pndVar = tkinter.IntVar()
euroVar = tkinter.IntVar()
dollaVar = tkinter.IntVar()
rngtVar = tkinter.IntVar()

toMenuButton = tkinter.Menubutton(top,text="To:")
toMenuButton.grid(row=0,column=4)
toMenu = tkinter.Menu(toMenuButton,tearoff=0)
toMenuButton.configure(menu=toMenu)
toMenu.add_checkbutton(label="£",variable=pndVar)
toMenu.add_checkbutton(label="€",variable=euroVar)
toMenu.add_checkbutton(label="$",variable=dollaVar)
toMenu.add_checkbutton(label="RM",variable=rngtVar)

cnvtBtn = tkinter.Button(top,text="Convert",command=convert, bg='white')
cnvtBtn.grid(row=1,column=0)

tkinter.mainloop()