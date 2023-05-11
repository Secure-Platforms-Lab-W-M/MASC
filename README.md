# MASC Web
The project is intended to provide a user interface for the user in order to easily use MASC.

## Environments
The project is based on django frame work.
```sh
python => 3.10.4
Django => (4, 0, 5, 'final', 0)
```

## Run Project
- Create virtual environment
```sh
python3 -m venv venv
source venv/bin/activate
```
for windows
```sh
python3 -m venv venv
.\venv\Scripts\activate
```

- Install dependecies
```sh
pip install -r requirements.txt
```

- Make Migration
```sh
python manage.py makemigrations
python manage.py migrate
```
- Run server
```sh
python manage.py runserver
```
