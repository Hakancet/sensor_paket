import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: SensorDataScreen(),
    );
  }
}

class SensorDataScreen extends StatefulWidget {
  @override
  _SensorDataScreenState createState() => _SensorDataScreenState();
}

class _SensorDataScreenState extends State<SensorDataScreen> {
  String _accelerometerData = "Bekleniyor...";
  String _gyroscopeData = "Bekleniyor...";
  String _lightData = "Işık Sensörü Bekleniyor...";
  String _manyeticData = "Manyetik Sensör Bekleniyor...";
  String _rotationData = "Döndürme Sensörü Bekleniyor...";
  late StreamSubscription<dynamic> _accelerometerSubscription;
  late StreamSubscription<dynamic> _gyroscopeSubscription;
  late StreamSubscription<dynamic> _lightSubscription;
  late StreamSubscription<dynamic> _manyeticSubscription;
  late StreamSubscription<dynamic> _rotationSubscription;

  @override
  void initState() {
    super.initState();
    _startListening();
  }

  @override
  void dispose() {
    _stopListening();
    super.dispose();
  }

  void _startListening() {
    _lightSubscription = EventChannel('sensor_chanel_5')
        .receiveBroadcastStream()
        .listen((event) {
      if (event is String) {
        setState(() {
          _lightData = event;
        });
      } else {}
    }, onError: (error) {});

    _rotationSubscription = EventChannel('sensor_chanel_11')
        .receiveBroadcastStream()
        .listen((event) {
      if (event is String) {
        setState(() {
          _rotationData = event;
        });
      } else {}
    }, onError: (error) {});

    _manyeticSubscription = EventChannel('sensor_chanel_2')
        .receiveBroadcastStream()
        .listen((event) {
      if (event is String) {
        setState(() {
          _manyeticData = event;
        });
      } else {}
    }, onError: (error) {});

    _accelerometerSubscription = EventChannel('sensor_channel_1')
        .receiveBroadcastStream()
        .listen((event) {
      if (event is String) {
        setState(() {
          _accelerometerData = event;
        });
      } else {
      }
    }, onError: (error) {
    });

    _gyroscopeSubscription = EventChannel('sensor_channel_4')
        .receiveBroadcastStream()
        .listen((event) {
      if (event is String) {
        setState(() {
          _gyroscopeData = event;
        });
      } else {
      }
    }, onError: (error) {
    });
  }

  void _stopListening() {
    _accelerometerSubscription.cancel();
    _gyroscopeSubscription.cancel();
    _lightSubscription.cancel();
    _manyeticSubscription.cancel();
    _rotationSubscription.cancel();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Sensör Verileri'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Text("$_accelerometerData"),
            SizedBox(
              height: 20,
            ),
            Text("$_gyroscopeData"),
            SizedBox(
              height: 20,
            ),
            Text("$_lightData"),
            SizedBox(
              height: 20,
            ),
            Text("$_manyeticData"),
            SizedBox(
              height: 20,
            ),
            Text("$_rotationData"),
            ElevatedButton(
              onPressed: _stopListening,
              child: Text('Dinlemeyi Durdur'),
            ),
          ],
        ),
      ),
    );
  }
}
