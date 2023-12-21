import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Platform Channels',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'Platform Channels'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  // Cria uma instância constante de MethodChannel chamada 'platform'
  // para estabelecer um canal de comunicação com o código nativo,
  // usando o identificador 'samples.flutter.dev/battery'.
  static const platform = MethodChannel('samples.flutter.dev/battery');
  // Recupera o nivel de bateria.
  String _batteryLevel = 'Unknown battery level.';
  // Função assíncrona para obter o nível da bateria.
  Future<void> _getBatteryLevel() async {
    String batteryLevel;
    try {
      // Invoca o método 'getBatteryLevel' no canal 'platform' e aguarda o resultado.
      final result = await platform.invokeMethod<int>('getBatteryLevel');
      // Atualiza a string com o nível da bateria obtido.
      batteryLevel = 'Battery level at $result % .';
    } on PlatformException catch (e) {
      // Em caso de falha, registra a mensagem de erro.
      batteryLevel = "Failed to get battery level: '${e.message}'.";
    }
    // Altera estado do text com o resultado
    setState(() {
      _batteryLevel = batteryLevel;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Material(
      child: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            ElevatedButton(
              onPressed: _getBatteryLevel,
              child: const Text('Get Battery Level'),
            ),
            Text(_batteryLevel),
          ],
        ),
      ),
    );
  }
}
