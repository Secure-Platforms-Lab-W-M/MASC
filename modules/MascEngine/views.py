import sys
import os
from asgiref.sync import sync_to_async
from django.http import HttpResponse
from django.shortcuts import render
from os import kill
from os import getpid
import signal

from modules.CipherManager.models import PropertiesList
from modules.MascEngine.models import SourceCode
from modules.MascEngine.models import ProcessLog
from zipfile import ZipFile
from datetime import datetime
import asyncio
from shutil import make_archive
from wsgiref.util import FileWrapper
from django.conf import settings
from modules.pythonAssets.model import MASCEngineAsset
from modules.pythonAssets.model import MASCEngineHistoryAsset

import time

# Create your views here.

import threading


class thread(threading.Thread):
    def __init__(self, app_name, build_properties_path, log_id):
        threading.Thread.__init__(self)
        self.app_name = app_name
        self.build_properties_path = build_properties_path
        self.log_id = log_id
        self._stop_event = threading.Event()
        # helper function to execute the threads

    def stop(self):
        self._stop_event.set()

    def run(self):
        jar_path = settings.CORE_DIR + '/modules/static/properties/app-all.jar'
        p, status_code, pid = asyncio.run(
            run('java -jar ' + jar_path + ' ' + self.build_properties_path))
        record = ProcessLog.objects.get(id=self.log_id)
        print('Hello =>' + record.status)
        if status_code == 0:
            record.status = 'completed'
        else:
            record.status = 'failed'
        record.save()
        sys.exit()


async def run(cmd):
    print(cmd)
    proc = await asyncio.create_subprocess_shell(
        cmd,
        stdout=asyncio.subprocess.PIPE,
        stderr=asyncio.subprocess.PIPE)
    stdout, stderr = await proc.communicate()
    print(f'[{cmd!r} exited with {proc.returncode}]')
    utf = 'utf-8'
    status = 'ignore'
    if stdout:
        print(f'[stdout]\n{stdout.decode(utf, status)}')
        return stdout.decode(utf, status), proc.returncode, proc.pid
    if stderr:
        print(f'[stderr]\n{stderr.decode(utf, status)}')
        return stderr.decode(utf, status), proc.returncode, proc.pid


def read_selected_file(f):
    with open('./modules/static/properties/' + f, 'r') as destination:
        item = destination.read().split("\n")
    content = ''
    scope = ''
    for line in item:
        if 'scope' in line.lower() or 'appsrc' in line.lower() or 'outputdir' in line.lower() or 'appname' in line.lower():
            if 'scope' in line.lower():
                scope = line.split("=")[1]
            continue
        else:
            content = content + line + '\n'
    return content, scope


def handle_uploaded_file(f, app_name):
    if not os.path.exists('./modules/static/sourcecodes/'):
        os.makedirs('./modules/static/sourcecodes/')
    if not os.path.exists('./modules/static/unzippedCodes/'):
        os.makedirs('./modules/static/unzippedCodes/')

    with open('./modules/static/sourcecodes/' + f.name, 'wb') as destination:
        for chunk in f.chunks():
            destination.write(chunk)
    with ZipFile('./modules/static/sourcecodes/' + f.name, 'r') as zipObj:
        zipObj.extractall('./modules/static/unzippedCodes/' + app_name)
    inputPath = './modules/static/unzippedCodes/' + app_name
    data = SourceCode(zip_file_name=f.name, zip_file_directory='./modules/static/sourcecodes/' + f.name,
                      input_path=inputPath, output_path='', appName=app_name);
    data.save()
    return data.id, inputPath


async def build_properties(app_name, scope, input_path, contents):
    initial = 'appName=' + app_name + '\n' + 'scope=' + scope + '\n' + 'appSrc=' + input_path + '\n' + 'outputDir=./app/outputs/' + app_name + '\n'
    contents = initial + contents
    with open('./modules/static/properties/' + app_name + '.properties', 'w') as destination:
        destination.write(contents)
    return './modules/static/properties/' + app_name + '.properties'


def run_sub_process_masc_engine(build_properties_path, source_code_id, scope):
    source = SourceCode.objects.get(id=source_code_id)
    data = ProcessLog(properties=build_properties_path, scope=scope, status='running', source_code=source,
                      start_time=datetime.now())
    data.save()
    threadMASC = thread(source.appName, build_properties_path, data.id)
    threadMASC.daemon = True
    threadMASC.start()
    process_data = ProcessLog.objects.get(id=data.id)
    process_data.threadId = threading.get_native_id()
    process_data.save()
    # print(threading.get_native_id(), threadMASC.getName(), threading.current_thread())
    # # thread  can be killed using native id


def runMASCEngine(request):
    if sys.platform == 'win32':
        terminate_class = 'btn btn-danger disabled'
    else:
        terminate_class = 'btn btn-danger'
    custome_operator_headers = ["Id", "Scope", "Properties", "App Name", "Status", "Actions"]
    if request.method == 'POST':
        scopes = request.POST['scope']
        properties_file = request.POST['file_name']
        app_name = request.POST['appName']
        contents = request.POST['content']
        source_code_id, input_path = handle_uploaded_file(request.FILES['sourcecode'], app_name)
        build_properties_path = asyncio.run(build_properties(app_name, scopes, input_path, contents))
        run_sub_process_masc_engine(build_properties_path, source_code_id, scopes)
    data = ProcessLog.objects.all().values()
    records = []
    for x in data:
        source = SourceCode.objects.get(id=x['source_code_id'])
        x['source_code'] = source
        arr = x['properties'].split('/')
        x['properties_name'] = arr[len(arr) - 1]
        records.append(x)
    return render(request, "masc-engine/history.html", {
        "custome_operator_headers": custome_operator_headers,
        "records": records,
        "terminate_class": terminate_class,
        "assets": MASCEngineHistoryAsset
    })


def index(request):
    if request.method == 'POST':
        # scope = request.POST['scopes']
        properties = request.POST['properties']
        contents, scope = read_selected_file(properties)
        return render(request, "masc-engine/engine-details.html", {
            "scope": scope,
            "filename": properties,
            "content": contents,
            "assets": MASCEngineAsset
        })
    records = PropertiesList.objects.filter().exclude(scope='MAIN')
    return render(request, "masc-engine/engine.html", {
        "properties_file": records,
        "assets": MASCEngineAsset

    })


def delete_uploaded_file(f):
    path = './app/outputs/' + f
    if os.path.isdir(path):
        os.system("rm -rf " + path)
    if os.path.isfile('./app/outputs/' + f + './zip'):
        os.remove('./app/outputs/' + f + './zip')


def delete_source_code(request, id, name):
    SourceCode.objects.get(id=id).delete()
    delete_uploaded_file(name)
    return runMASCEngine(request)


def download(request, app_name):
    files_path = "./app/outputs/" + app_name
    path_to_zip = make_archive(files_path, "zip", files_path)
    response = HttpResponse(FileWrapper(open(path_to_zip, 'rb')), content_type='application/zip')
    response['Content-Disposition'] = 'attachment; filename="{filename}.zip"'.format(
        filename=app_name.replace(" ", "_")
    )
    return response


def terminate(request, threadId, id):
    record = ProcessLog.objects.get(id=id)
    for thread in threading.enumerate():
        if thread.native_id == threadId:
            signal.pthread_kill(thread.native_id, -9)
            record.status = 'terminated'
            print('process terminated')
            break
    return runMASCEngine(request)

# Create your views here.
