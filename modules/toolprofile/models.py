from datetime import datetime

from django.db import models

# Create your models here.
class ToolProfile(models.Model):
    Toolname = models.CharField(max_length=200)
    filename = models.CharField(max_length=100, db_index=True, default="any.properties")
    status = models.CharField(max_length=500)
    start_time = models.DateTimeField(default=datetime.now())
    threadId = models.CharField(max_length=50, default=-100)

class Reports(models.Model):
    tool_profile_id = models.ForeignKey('ToolProfile', on_delete=models.CASCADE)
    Toolname = models.CharField(max_length=200)
    content = models.CharField(max_length=1000)
