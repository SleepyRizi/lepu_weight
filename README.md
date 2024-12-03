# sm_lepu_weight

A new Flutter project.

## Getting Started

This project is a starting point for a Flutter
[plug-in package](https://flutter.dev/to/develop-plugins),
a specialized package that includes platform-specific implementation code for
Android and/or iOS.

For help getting started with Flutter development, view the
[online documentation](https://docs.flutter.dev), which offers tutorials,
samples, guidance on mobile development, and a full API reference.


to add this plugin to your project you can add below tow lines in your project pubspec

      sm_lepu_weight:
        git: https://github.com/SmartMindSYSCoder/lepu_weight.git

Now you can create instance of plugin :

     final smLepuWeight = SmLepuWeight();


To start use this plugin must be call getPermissions() like below:


                smLepuWeight.checkPermissions();



Then call init  method like below:


                     smLepuWeight.init();



                if(!smLepuWeight.isListening) {
                  smLepuWeight.statusStream.listen((data) {


                    if (data is WeightModel) {
                      weightModel = data;
                    }

                    setState(() {
                    });
                  });
                }


Note that :  the stream listen to check the update of state of connection and retrieve data


You can also see the example

I hope this clear 