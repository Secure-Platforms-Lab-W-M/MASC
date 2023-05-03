## MASC Web Interface
Upon entering the web interface the user is presented with an welcome screen from where they can enter any features. MASC web interface provide 5 core features.
- [MASC Lab](#masc-lab)
- [MASC Engine](#masc-engine)
- [Configuration Manager](#configuration-manager)
- [Plugin Manager](#plugin-manager)
- [Tool Profiling](#tool-profiling)

![MASC web Interface landing screen](./assets/home.png)

As like MASC core MASC web also utilizes various configurations
in order to come up with various mutations. SO the user has to keep track of this various configurations files in order to take full advantage of MASC.
For, this purpose MASC web has introduced ```Configuration Manager```

### Configuration Manager
First the user needs to upload their existing configuration file. This part can be done using the 
Configuration Manager upload page. 
_**From home > Configurations Manager > Upload Configuration_** 
The user then has to provide a name for this configuration and select a file.
Once the file is uploaded it will be available for further usage in MASC. From this configuration manager the user can also update any configuration. 

User can update any configuration file content and the changes are saved. Upon uploading the configurations the user is ready to move forward.

### MASC Lab
From the home page or the side panel the user can easily enter in MASC Lab. 
- First the user will have to select a previously uploaded configuration file.
Users can also upload new one by clicking **create configurations** 
- Nest upon submitting the configuration the user will be taken to the set up page of MASC Lab. Here the user can make changes to the configuration as well.
- In this page the user hae the ability to opt out any operators from this process. It will help them only inspect the operators they are interested about.
- Finallyy **Run Masc Lab** will start the process.
#### Output
The output page for MASC Lab has 3 windows. 
![Masc Lab output](./assets/mlout.png)

- The left window of the top row provides user the configuration upon which the mutation has taken place.
- The right window is the main window as it shows the mutations to the user.
- The bottom window is the console output of the whole process.

The user can also download this mutated codes as a zip file.

### MASC Engine
So far users have explored MASC through MASC Lab and before that they  had uploaded their configurations. MASC Engine provides user with an inter face wher the users can upload their source code and mutate them.
- First given the option to select the configuration file of their interest.Upon clicking the submit button MASC will read the file, get necessary informations and show it to the user.
- Next Here the user has to provide the name of the project which they want to mutate and the compressed source code of the project. 

Once the process is completed the user will be able to download the mutated source code  from MASC Engine. The process may take some time, the user can refresh the dashboard to see if the processs has been completed.

### Plugin Manager
Users can easily upload the java code containing his/her plugin and build that using plugin manager.
- The user will have to give a name and a java code file of the plugin.
- Once uploaded the user can build the code directlyy from plugin managers in order to use it for mutation.

![Plugin Manager](./assets/pmd.png)

From the actions column the user easily builds the operator by clicking the blue build button. At the same time if the user want to change the activation status of the plugin they can also do it from the actions columns. **_`Inactive`_** status means the operator won’t take part in mutation. The user has to **_`activate`_** the plugin plugin in order to take part in mutation

### Tool Profiling
This provides the user with an interface to completely run the whole process of MASC with just button clicks. That is
mutation, running crypto detector upon that mutation and analyzing the output of the crypto detectors.

- The user has to select a configuration file with all configuration for running the targeted tool.
- Upon submitting the user will be taken to the next page. Where the user can inspect the configurations and make any last minute changes if necessary.
- Once the user is happy they can hit **`Run Tool Profiling`** button and process will start. User will be taken to the history page and once the process is completed the can see details of the process.

Output shall be a generated table from the results of the crypto detectors. Which mutations have been detected and which haven't. **_Killed_** stands for _'The mutation has been detected'_. **_Unkilled_** stands for _'The tool failed to detect this mutation'_