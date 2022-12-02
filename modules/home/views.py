from django.shortcuts import render
from modules.pythonAssets.model import MASCLabAsset
from modules.pythonAssets.model import MASCEngineAsset

# Create your views here.
def index(request):
    return render(request, "home.html", {
        "masclab": MASCLabAsset,
        "mascengine": MASCEngineAsset
    })


def about(request):
    return render(request, "about.html")
