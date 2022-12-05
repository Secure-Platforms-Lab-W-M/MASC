from django.urls import path, re_path
from modules.toolprofile import views

app_name = "ToolProfile"

urlpatterns = [
    path('', views.index, name='toolprofile'),
    path('run', views.runToolProfiling, name='run'),
    path('history', views.history, name='history'),
    path('delete/<int:id>', views.delete, name='delete'),
    path('detail/<int:id>', views.detail, name='detail'),
    path('detail/<str:tool_name>', views.profile, name='profile')
]