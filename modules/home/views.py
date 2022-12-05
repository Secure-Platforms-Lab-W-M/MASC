from django.shortcuts import render
from modules.pythonAssets.model import MASCLabAsset
from modules.pythonAssets.model import MASCEngineAsset
from modules.pythonAssets.model import CipherManagerAsset
from modules.pythonAssets.model import PluginManagerAssets
from modules.pythonAssets.model import ToolProfilingAsset

# Create your views here.
def index(request):
    return render(request, "home.html", {
        "masclab": MASCLabAsset,
        "mascengine": MASCEngineAsset,
        "configuration": CipherManagerAsset,
        "plugins": PluginManagerAssets,
        "toolProfile": ToolProfilingAsset
    })


def about(request):
    return render(request, "about.html")
