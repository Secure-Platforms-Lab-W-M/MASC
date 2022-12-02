from django.shortcuts import render
from modules.pythonAssets.model import MASCLabAsset


# Create your views here.
def index(request):
    return render(request, "home.html", {
        "masclab": MASCLabAsset
    })


def about(request):
    return render(request, "about.html")
