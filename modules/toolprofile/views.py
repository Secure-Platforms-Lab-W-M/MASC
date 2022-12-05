import asyncio
import sys
import threading
import types
import uuid
from datetime import datetime

from django.shortcuts import render

# Create your views here.
from django.shortcuts import render
from modules.pythonAssets.model import ToolProfilingAsset
from modules.pythonAssets.model import ToolProfilingHistoryAsset

from modules.CipherManager.models import PropertiesList

from MascWebCore import settings
from modules.toolprofile.models import ToolProfile
from modules.toolprofile.models import Reports


class thread(threading.Thread):
    def __init__(self, tool_name, build_properties_path, log_id):
        threading.Thread.__init__(self)
        self.tool_name = tool_name
        self.build_properties_path = build_properties_path
        self.log_id = log_id
        self._stop_event = threading.Event()
        # helper function to execute the threads

    def stop(self):
        self._stop_event.set()

    def run(self):
        jar_path = settings.CORE_DIR + '/modules/static/properties/app-all.jar'
        p, status_code, pid, content = asyncio.run(
            run('java -jar ' + jar_path + ' ' + self.build_properties_path))
        record = ToolProfile.objects.get(id=self.log_id)
        data = Reports(Toolname=self.tool_name, tool_profile_id=record, content=content)
        data.save()
        if status_code == 0:
            record.status = 'completed'
        else:
            record.status = 'failed'
        record.save()
        print(record.status)
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
    content = ''
    if stdout:
        print(f'[stdout]\n{stdout.decode(utf, status)}')
        item = stdout.decode(utf, status)
        lines = item.split("\n")
        for line in lines:
            if "[MutationAnalyzer]#" in line:
                content = content + line + "\n"
        return stdout.decode(utf, status), proc.returncode, proc.pid, content
    if stderr:
        print(f'[stderr]\n{stderr.decode(utf, status)}')
        return stderr.decode(utf, status), proc.returncode, proc.pid, content


def read_selected_file(f):
    with open('./modules/static/properties/' + f, 'r') as destination:
        item = destination.read().split("\n")
    content = ''
    toolName = ''
    for line in item:
        if 'outputDir' in line or 'toolName' in line:
            if 'toolName' in line:
                toolName = line.split("=")[1]
                content = content + line + '\n'
            if 'outputDir' in line:
                continue
        else:
            content = content + line + '\n'
    return content, toolName


# Create your views here.
def index(request):
    if request.method == "POST":
        properties = request.POST['properties']
        contents, toolName = read_selected_file(properties)
        return render(request, "tool-profile/details-form.html", {
            "toolName": toolName,
            "content": contents,
            "assets": ToolProfilingAsset,
            "properties": properties,
            "alert": False

        })
    records = PropertiesList.objects.filter(scope='MAIN')
    return render(request, "tool-profile/index.html", {
        "assets": ToolProfilingAsset,
        "records": records,
    })


def check_content(content):
    automatedAnalysis = ''
    item = content.split("\n")
    for line in item:
        if 'automatedAnalysis' in line:
            automatedAnalysis = line.split("=")[1].replace(" ", "")
    return automatedAnalysis


def sanitize_content(file):
    content = ''
    item = file.split("\n")
    for line in item:
        if 'outputDir' in line or 'scope' in line:
            continue
        else:
            content = content + line + '\n'
    return content


def run_sub_process_tool_profiling(build_properties_path, tool_name):
    data = ToolProfile(Toolname=tool_name, filename=build_properties_path, status='running', start_time=datetime.now())
    data.save()
    threadMASC = thread(tool_name, build_properties_path, data.id)
    threadMASC.start()
    process_data = ToolProfile.objects.get(id=data.id)
    process_data.threadId = threading.get_native_id()
    process_data.save()


async def update_properties(content, properties):
    arr = bytes(content, 'utf-8')
    with open('./modules/static/properties/' + properties, 'wb') as destination:
        destination.write(arr)
    return './modules/static/properties/' + properties


def runToolProfiling(request):
    if request.method == "POST":
        content = request.POST['content']
        tool_name = request.POST['toolName']
        properties = request.POST['properties']
        automatedAnalysis = check_content(content)
        if 'false' in automatedAnalysis:
            records = PropertiesList.objects.filter(scope='MAIN')
            return render(request, "tool-profile/details-form.html", {
                "toolName": tool_name,
                "content": content,
                "assets": ToolProfilingAsset,
                "properties": properties,
                "alert": True
            })
        sanitized_content = sanitize_content(content)
        print(sanitized_content)
        main_content = "outputDir=app/tools/" + tool_name + '/' + str(uuid.uuid4()).replace("-","") + "\n" + "scope=MAIN" + "\n" + sanitized_content
        build_properties = asyncio.run(update_properties(main_content, properties))
        run_sub_process_tool_profiling(build_properties, tool_name)
        return history(request)


def history(request):
    if sys.platform == 'win32':
        terminate_class = 'btn btn-danger disabled'
    else:
        terminate_class = 'btn btn-danger'
    records = ToolProfile.objects.all().values()
    custome_operator_headers = ["Id", "Start Time", "Properties", "Tool Name", "Status", "Actions"]
    return render(request, "tool-profile/history.html", {
        "records": records,
        "assets": ToolProfilingHistoryAsset,
        "terminate_class": terminate_class,
        "custome_operator_headers": custome_operator_headers
    })


def delete(request, id):
    ToolProfile.objects.get(id=id).delete()
    return history(request)


def detail(request, id):
    tool = ToolProfile.objects.get(id=id)
    record = Reports.objects.get(tool_profile_id=tool)
    lines = record.content.split("\n")
    arr = []
    for line in lines:
        if "[MutationAnalyzer]" in line:
            temp = types.SimpleNamespace()
            temp.operator = line.split("#")[len(line.split("#"))-2]
            temp.status = line.split("#")[len(line.split("#"))-1]
            if "unkilled" in temp.status.lower():
                temp.result = "❌"
            else:
                temp.result = "✔"
            arr.append(temp)
    custom_operator_headers = ["Operator", "Status", "Result"]
    return render(request, "tool-profile/profile-details.html", {
        "records": arr,
        "tool_name": record.Toolname,
        "start_time": tool.start_time,
        "custome_operator_headers": custom_operator_headers
    })

def profile(request, tool_name):
    records = Reports.objects.filter(Toolname=tool_name)
    print(records)
    for record in records:
        print(record.Toolname)
        record.start_time = record.tool_profile_id.start_time
        lines = record.content.split("\n")
        arr = []
        for line in lines:
            if "[MutationAnalyzer]" in line:
                temp = types.SimpleNamespace()
                temp.operator = line.split("#")[len(line.split("#"))-2]
                temp.status = line.split("#")[len(line.split("#"))-1]
                if "unkilled" in temp.status.lower():
                    temp.result = "❌"
                else:
                    temp.result = "✔"
                arr.append(temp)
        record.arr = arr
    custom_operator_headers = ["Operator", "Status", "Result"]
    return render(request, "tool-profile/tool-profile.html", {
        "records": records,
        "tool_name": tool_name,
        "custome_operator_headers": custom_operator_headers
    })