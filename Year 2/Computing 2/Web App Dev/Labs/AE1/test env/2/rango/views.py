from django.shortcuts import render
from django.http import HttpResponse

def index(request):
    return HttpResponse("Rango says 'Hey there, pardner!'<br>" +
    "Go to <a href='/rango/about/'>About</a>.")
    
def about(request):
    return HttpResponse("Rango says 'Here is the about page, pardner.'<br>" +
    "Go back to <a href='/rango/'>Index</a>.")