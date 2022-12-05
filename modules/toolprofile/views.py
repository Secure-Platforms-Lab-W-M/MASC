from django.shortcuts import render

# Create your views here.
from django.shortcuts import render
from modules.pythonAssets.model import ToolProfilingAsset

from modules.CipherManager.models import PropertiesList

def read_selected_file(f):
    with open('./modules/static/properties/' + f, 'r') as destination:
        item = destination.read().split("\n")
    content = ''
    toolName = ''
    automatedAnalysis = ''
    for line in item:
        if 'outputdir' in line.lower() or 'toolName' in line or 'automatedAnalysis' in line:
            if 'toolName' in line.lower():
                toolName = line.split("=")[1]
                content = content + line + '\n'
            if 'automatedAnalysis' in line:
                print(line)
                automatedAnalysis = line.split("=")[1].replace(" ", "")
                content = content + line + '\n'
        else:
            content = content + line + '\n'
    return content, toolName, automatedAnalysis

# Create your views here.
def index(request):
    if request.method == "POST":
        # scope = request.POST['scopes']
        properties = request.POST['properties']
        contents, toolName, automatedAnalysis = read_selected_file(properties)
        print(automatedAnalysis)
        if 'false' in automatedAnalysis:
            records = PropertiesList.objects.filter(scope='MAIN')
            return render(request, "tool-profile/index.html", {
                "assets": ToolProfilingAsset,
                "records": records,
                "alert": True
            })
        return render(request, "masc-engine/engine-details.html", {
            "tool_name": properties,
            "content": contents,
            "assets": ToolProfilingAsset
        })
    # operator
    # make any.properties file
    records = PropertiesList.objects.filter(scope='MAIN')
    return render(request, "tool-profile/index.html",{
        "assets": ToolProfilingAsset,
        "records": records,
        "alert": False
    })