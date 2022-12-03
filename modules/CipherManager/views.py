import json
import os
from django.shortcuts import render
from modules.CipherManager.models import PropertiesList
from django.shortcuts import render, redirect
from django.http import HttpResponse
from modules.pythonAssets.model import CipherManagerAsset
from modules.pythonAssets.model import EditConfigurationsAsset
from modules.pythonAssets.model import ConfigurationThanksAsset


def index(request):
    uploaded_files_header = ["Id", "Name", "File Name", "Scope", "Type", "Actions"]
    records = PropertiesList.objects.all().values()
    return render(request, "CipherManager/index.html", {
        "uploaded_files_header": uploaded_files_header,
        "records": records,
        "assets": CipherManagerAsset
    })


def editProperties(request, id):
    record = PropertiesList.objects.get(id=id)
    if request.method == "POST":
        filename = record.filename
        file_content = request.POST["content"]
        scope, type = update_file_content(filename, file_content)
        record.scope = scope
        record.type = type
        record.save()
        return redirect(index)
        # edit requested
    property_name = record.name
    property_filename = record.filename
    property_operator = record.type
    content = read_data_from_uploaded_file(property_filename)
    return render(request, "CipherManager/edit.html", {
        "name": property_name,
        "filename": property_filename,
        "type": property_operator,
        "content": content,
        "assets": EditConfigurationsAsset
    })


def read_data_from_uploaded_file(f):
    with open('./modules/static/properties/' + f, 'r') as destination:
        contents = destination.read()
    return contents


def update_file_content(filename, content):
    arr = bytes(content, 'utf-8')
    with open('./modules/static/properties/' + filename, 'wb') as destination:
        destination.write(arr)
    scope, type = read_values_from_file(filename)
    return scope, type

def handle_uploaded_file(f):
    with open('./modules/static/properties/' + f.name, 'wb') as destination:
        for chunk in f.chunks():
            destination.write(chunk)
    return destination.name


def read_values_from_file(f):
    with open('./modules/static/properties/' + f, 'r') as destination:
        item = destination.read().split("\n")
    scope = ""
    type = "unspecified"
    for line in item:
        if 'scope' in line.lower():
            value = line.split("=")[1]
            if 'MAIN' in value:
                scope = "MAIN"
            elif 'SIMILARITY' in value:
                scope = "SIMILARITY"
            elif 'EXHAUSTIVE' in value:
                scope = "EXHAUSTIVE"
        elif 'type' in line.lower() and "=" in line:
            type = line.replace(" ", "").split("=")[1]
    return scope, type


def delete_uploaded_file(f):
    path = './modules/static/properties/' + f
    if os.path.isfile(path):
        os.remove(path)


def uploadPropertyForm(request):
    if request.method == 'POST':
        name = request.POST['name']
        # ptype = request.POST['type']
        filename = request.FILES['file'].name
        path = handle_uploaded_file(request.FILES['file'])  # path from masc core shall be added
        scope, ptype = read_values_from_file(filename)
        data = PropertiesList(name=name, type=ptype, filename=filename, path=path, scope=scope)
        data.save()
        return render(request, 'CipherManager/thanks.html', {
            "assets": ConfigurationThanksAsset
        })
    # list_of_operators = ["StringOperator", "ByteOperator", "InterprocOperator", "Flexible", "IntOperator"]
    return render(request, "CipherManager/uploadProperty.html", {
        # "list_of_operators": list_of_operators,
        "assets": CipherManagerAsset
    })


# Create your views here.


def deleteProperties(request, id):
    record = PropertiesList.objects.get(id=id)
    delete_uploaded_file(record.filename)
    PropertiesList.objects.get(id=id).delete()
    return redirect(index)
