# Generated by Django 4.0.6 on 2022-12-02 17:26

import datetime
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('MascEngine', '0002_alter_processlog_start_time'),
    ]

    operations = [
        migrations.AddField(
            model_name='processlog',
            name='threadId',
            field=models.CharField(default=-100, max_length=50),
        ),
        migrations.AlterField(
            model_name='processlog',
            name='start_time',
            field=models.DateTimeField(default=datetime.datetime(2022, 12, 2, 23, 26, 33, 409151)),
        ),
    ]
