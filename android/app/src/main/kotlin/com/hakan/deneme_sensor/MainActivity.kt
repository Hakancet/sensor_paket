package com.hakan.deneme_sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.EventSink

class MainActivity : FlutterActivity() {
    private lateinit var sensorManager: SensorManager
    private val sensorDataMap: MutableMap<Int, EventSink?> = mutableMapOf()
    private val sensorListenerMap: MutableMap<Int, SensorEventListener> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        startListeningToSensors()
    }

    private fun startListeningToSensors() {
        val sensorList: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)

        for (sensor in sensorList) {
            val eventChannelName = "sensor_channel_${sensor.type}"
            val eventChannel = EventChannel(flutterEngine?.dartExecutor?.binaryMessenger, eventChannelName)
            eventChannel.setStreamHandler(object : EventChannel.StreamHandler {
                override fun onListen(arguments: Any?, events: EventSink) {
                    sensorDataMap[sensor.type] = events
                    if (sensorListenerMap.containsKey(sensor.type)) {
                        sensorManager.unregisterListener(sensorListenerMap[sensor.type])
                    }

                    val sensorListener = createSensorEventListener(sensor.type)
                    sensorListenerMap[sensor.type] = sensorListener

                    sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
                }

                override fun onCancel(arguments: Any?) {
                    sensorDataMap.remove(sensor.type)
                    if (sensorDataMap.isEmpty()) {
                        stopListeningToSensors()
                    }
                }
            })
        }
    }

    private fun stopListeningToSensors() {
        for (listener in sensorListenerMap.values) {
            sensorManager.unregisterListener(listener)
        }
        sensorListenerMap.clear()
    }

    private fun createSensorEventListener(sensorType: Int): SensorEventListener {
        return object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val eventSink = sensorDataMap[sensorType]

                eventSink?.let {
                    val sensorData = when (sensorType) {
                        Sensor.TYPE_ACCELEROMETER -> {
                            val x = event.values[0]
                            val y = event.values[1]
                            val z = event.values[2]
                            "İvmeölçer: X: $x, Y: $y, Z: $z"
                        }

                        Sensor.TYPE_GYROSCOPE -> {
                            val x = event.values[0]
                            val y = event.values[1]
                            val z = event.values[2]
                            "Jiroskop: X: $x, Y: $y, Z: $z"
                        }

                        Sensor.TYPE_LIGHT -> {
                            val light = event.values[0]
                            "Işık sensörü: $light"
                        }

                        Sensor.TYPE_MAGNETIC_FIELD -> {
                            val x = event.values[0]
                            val y = event.values[1]
                            val z = event.values[2]
                            "Manyetik Alan sensörü: X: $x, Y: $y, Z: $z"
                        }

                        Sensor.TYPE_PROXIMITY -> {
                            val distance = event.values[0]
                            "Yakınlık Sensörü: $distance"
                        }

                        Sensor.TYPE_PRESSURE -> {
                            val pressure = event.values[0]
                            "Basınç Sensörü: $pressure"
                        }

                        Sensor.TYPE_GRAVITY -> {
                            val x = event.values[0]
                            val y = event.values[1]
                            val z = event.values[2]
                            "Yerçekimi Sensörü: X: $x, Y: $y, Z: $z"
                        }

                        Sensor.TYPE_ROTATION_VECTOR -> {
                            val x = event.values[0]
                            val y = event.values[1]
                            val z = event.values[2]
                            "Döndürme Vektörü Sensörü: X: $x, Y: $y, Z: $z"
                        }

                        // Diğer sensör türlerini buraya ekleyin

                        else -> {
                            "Bilinmeyen Sensör Türü"
                        }
                    }

                    it.success(sensorData)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Sensor accuracy değiştiğinde yapılacak işlemler (Opsiyonel)
            }
        }
    }
}
