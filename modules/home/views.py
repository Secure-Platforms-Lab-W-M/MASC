from django.shortcuts import render
from modules.pythonAssets.model import MASCLabAsset
from modules.pythonAssets.model import MASCEngineAsset
from modules.pythonAssets.model import CipherManagerAsset
from modules.pythonAssets.model import PluginManagerAssets

# Create your views here.
def index(request):
    return render(request, "home.html", {
        "masclab": MASCLabAsset,
        "mascengine": MASCEngineAsset,
        "configuration": CipherManagerAsset,
        "plugins": PluginManagerAssets
    })


def about(request):
    return render(request, "about.html")
