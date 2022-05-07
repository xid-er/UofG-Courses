# -*- coding: utf-8 -*-
"""
Created on Fri Feb 28 18:26:23 2020

@author: karli
"""

import tkinter

def convert():
    amount = textVar.get()
    if amount.isnumeric():
        amount = float(amount)
        frCr = fromCur.get()
        rates = {'£-£': 1, '£-€': 1.16, '£-$': 1.28, '£-RM': 5.39}
        btnVars = {'£': pndVar.get(), '€': euroVar.get(), '$': dollaVar.get(), 'RM': rngtVar.get()}
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
pndVar = tkinter.IntVar()
euroVar = tkinter.IntVar()
dollaVar = tkinter.IntVar()
rngtVar = tkinter.IntVar()

fromLbl = tkinter.Label(top,text="From:",width=10)
fromLbl.grid(row=0,column=1)
toLbl = tkinter.Label(top,text="To:",width=10)
toLbl.grid(row=0,column=4)

pndBtn = tkinter.Radiobutton(top,text="£",
                                  variable=fromCur,value=1)
pndBtn.grid(row=1,column=1)

euroBtn = tkinter.Radiobutton(top,text="€",
                                    variable=fromCur,value=2)
euroBtn.grid(row=2,column=1)

dollaBtn = tkinter.Radiobutton(top,text="$",
                                    variable=fromCur,value=3)
dollaBtn.grid(row=3,column=1)

rngtBtn = tkinter.Radiobutton(top,text="RM",
                                    variable=fromCur,value=4)
rngtBtn.grid(row=4,column=1)

pndBtn2 = tkinter.Checkbutton(top,text="£",variable=pndVar)
pndBtn2.grid(row=1,column=4)
euroBtn2 = tkinter.Checkbutton(top,text="€",variable=euroVar)
euroBtn2.grid(row=2,column=4)
dollaBtn2 = tkinter.Checkbutton(top,text="$",variable=dollaVar)
dollaBtn2.grid(row=3,column=4)
rngtBtn2 = tkinter.Checkbutton(top,text="RM",variable=rngtVar)
rngtBtn2.grid(row=4,column=4)

cnvtBtn = tkinter.Button(top,text="Convert",command=convert, bg='white')
cnvtBtn.grid(row=1,column=0)

tkinter.mainloop()