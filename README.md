# Juego Sudoku :pencil2:
Implementación del juego Sudoku en Java. Proyecto de la materia Tecnología de Programación de la Universidad Nacional del Sur.

# Consideraciones sobre la implementación :hammer_and_wrench:
Detalle de algunas de las deciciones tomadas.

## Consideraciones sobre la parte gráfica :tv:

### Tableros de mayor tamaño
Como fue sugerido, la GUI puede adaptarse a tableros de un tamaño mayor a 9x9 (como 16x16, 25x25, 36x36, ...).
Para simplificar la implementación, el tamaño del panel del tablero es fijo, es decir, si aumenta la cantidad de celdas contenidas éstas pasarán a ser de menor tamaño, puesto que si se conservara el tamaño, una tablero muy grande no entraría en la pantalla. No obstante, el tamaño de la ventana principal puede variar según la cantidad de botones que deban aparecer en la botonera inferior, admitiendo siempre 10 botones por fila y aumentando la cantidad de filas según sea necesario.

### Tablero de 16x16
Se incluye una solución válida de un tablero de 16x16 cuyo único fin es ver cómo se adapta la interfaz gráfica. Así, por simplicidad, no son incluídas las imágenes correspondientes a cada valor. Luego, los valores entre 1 y 9 serán representados con la imagen correcta, mientras que los valores _v_ entre 10 y 16 se representarán con la imagen correspondiente a _v mod 9_.

## Consideraciones sobre la parte lógica :gear:

### Errores en la carga del juego
Si se produce un error en la carga de la parte lógica del juego, como una solución incorrecta o incompleta, archivos corruptos o inexistentes, o culquier otro error que impida la carga del juego, éste es representado anulando el campo del tablero lógico para que la interfaz gráfica pueda capturar que se produjo un error y abortar el programa.

# Archivos JAR incluídos :cd:
Se incluyen tres archivos JAR creados con la versión de **Java 14.0.2**.

### Sudoku (versión 9x9, general).jar
Es la versión más general, un tablero de 9x9 al que se le eliminan 45 celdas para jugar.

### Sudoku (versión 9x9, victoria rápida).jar
Versión de 9x9 al que se le elimina únicamente una celda para observar el comportamiento ante una victoria.

### Sudoku (versión 16x16, victoria rápida).jar
Versión de 16x16 al que se le elimina únicamente una celda para observar el comportamiento ante una victoria.
