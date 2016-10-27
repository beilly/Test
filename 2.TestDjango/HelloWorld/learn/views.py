#coding:utf-8

from django.shortcuts import render

# Create your views here.
from django.http import HttpResponse
 
def index(request):
    return HttpResponse(u"欢迎光临 自强学堂!" + request.META.get("HTTP_USER_AGENT", "没有HTTP_USER_AGENT")
	+ "\n" + request.GET.get("a", '0'))

def add2(request, a, b):
	c = int(a) + int(b)
	return HttpResponse(str(c))
	
def add(request, a, b):
	c = int(a) + int(b)
	return HttpResponse(str(c))
