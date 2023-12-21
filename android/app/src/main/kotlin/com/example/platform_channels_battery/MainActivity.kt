package com.example.platform_channels_battery

import androidx.annotation.NonNull // Importe esta linha
import io.flutter.embedding.engine.FlutterEngine // Importe esta linha
import io.flutter.embedding.android.FlutterActivity

import io.flutter.plugin.common.MethodChannel // Importe esta linha

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES

class MainActivity: FlutterActivity() {
    private val CHANNEL = "samples.flutter.dev/battery"
    /**
    * Configura o mecanismo FlutterEngine para suportar a comunicação entre o código Dart e o código nativo (Kotlin/Java).
    * Um canal de comunicação é criado com o nome "samples.flutter.dev/battery".
    * O método setMethodCallHandler é utilizado para definir um manipulador de chamadas de método, onde o código nativo
    * responde às mensagens enviadas pelo Dart. Neste caso, o código nativo responde à chamada do método "getBatteryLevel".
    * Se a mensagem for reconhecida, o nível da bateria é retornado com sucesso para o Dart; caso contrário, um erro é enviado de volta.
    */
    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
         // Cria um canal de comunicação entre o código Dart e o código nativo (Kotlin/Java).
        // O nome do canal é "samples.flutter.dev/battery".
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
             // Este bloco de código é chamado quando o Dart envia uma mensagem para o canal.
            // Verifica se a mensagem do Dart é para obter o nível da bateria.
            if (call.method == "getBatteryLevel") {
                val batteryLevel = getBatteryLevel()

                if (batteryLevel != -1) {
                    result.success(batteryLevel)
                } else {
                    // Se não for bem-sucedido, envia um erro de volta para o Dart.
                    result.error("UNAVAILABLE", "Battery level not available.", null)
                }
            } else {
              // Se a mensagem do Dart não for reconhecida, informa que não foi implementada.
                result.notImplemented()
            }
        }
    }

    // Função para obter o nível da bateria.
    private fun getBatteryLevel(): Int {
        // Declara uma variável para armazenar o nível da bateria.
        val batteryLevel: Int
        // Verifica a versão do Android para escolher o método 
        // adequado para obter o nível da bateria.
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            // Se a versão for igual ou superior ao Android Lollipop, usa a API BatteryManager.
            val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            // Se a versão for inferior ao Android Lollipop, usa a Intent ACTION_BATTERY_CHANGED.
            val intent = ContextWrapper(applicationContext).registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            batteryLevel = intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100 / intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        }
        // Retorna o nível da bateria.
        return batteryLevel
    }
}
