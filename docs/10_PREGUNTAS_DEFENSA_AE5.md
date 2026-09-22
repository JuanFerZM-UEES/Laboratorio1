# Preparación para la defensa · Ae5 (sección 14 de la guía)

Respuestas de apoyo para la sustentación oral. Practícalas con tus propias palabras — no las leas textual, conócelas.

**1. ¿Qué comportamiento protegiste antes de la primera refactorización?**
Las 7 pruebas de caracterización del Laboratorio 2 (`ServicioReservasTest`): reserva normal y VIP válidas, correo inválido, periodo inválido, los límites de 1 h y 2 h de anticipación, y `procesar(null, ...)`. Antes de tocar código confirmé que las 7 estaban verdes (`docs/evidencia_ae5/00_estado_inicial_verde.txt`).

**2. ¿Por qué seleccionaste esas tres refactorizaciones?**
Seguí el orden de la matriz de riesgo del Laboratorio 1 (`docs/04_MATRIZ_RIESGO.md`): de menor a mayor probabilidad de romper algo. Extraer la notificación era "Bajo" riesgo (solo movía dos `println` sin lógica de negocio). Introducir `Correo` era "Medio" pero decidí explícitamente que no cambiara el punto de validación. Agrupar `inicio`/`fin` en `PeriodoReserva` toca el problema de mayor riesgo ("Alto"), pero de forma deliberadamente acotada: sin tocar el constructor de `Reserva` ni lanzar excepciones, para no combinar ese riesgo con el resto en la misma entrega.

**3. ¿Qué prueba habría detectado una regresión concreta?**
Si al extraer `calcularTotal` (Laboratorio 2) alguien hubiera cambiado el descuento VIP, `vipActualmenteRetornaTreintaYCuatro()` lo habría detectado — de hecho lo demostré con la regresión intencional 0.85→0.80 (`docs/evidencia/03_regresion_intencional_detectada.txt`). En esta actividad, si `Correo.esValido()` hubiera cambiado su regla de validación, `correoInvalidoNoProcesaReserva()` y las 4 pruebas de `CorreoTest` lo habrían detectado de inmediato.

**4. ¿Qué cambió en el diseño y qué permaneció igual funcionalmente?**
Cambió la estructura interna: la notificación ya no está inline, el correo y el periodo ya no son datos sueltos sino tipos con su propia regla de validez. Lo que permaneció igual: la firma pública de `ServicioReservas.procesar(Reserva, int)`, el resultado de las 7 pruebas originales sin modificarlas, y la salida exacta de `Main.java` (`docs/evidencia_ae5/04_main_end_to_end_sin_cambios.txt`).

**5. ¿Qué evidencia proporciona tu historial Git?**
Un commit por refactorización, cada uno precedido de una corrida verde de la suite — la separación entre commits permite ver, uno por uno, qué cambió y confirmar que en ningún punto se acumularon varios cambios sin volver a probar.

**6. ¿Qué costo o riesgo introdujo alguna de tus decisiones?**
Dejar `PeriodoReserva` sin validar en el constructor es un costo deliberado: cualquiera que use `PeriodoReserva` directamente (fuera de `ServicioReservas`) puede construir un periodo inválido sin que nada se lo impida — la validez solo se puede consultar, no se garantiza. Fue una decisión consciente para no cambiar el contrato observable actual, pero es deuda técnica explícita que quedó documentada en el javadoc de la clase para la siguiente iteración.
