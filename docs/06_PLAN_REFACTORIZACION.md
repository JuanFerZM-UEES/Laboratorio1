# Fase L | Plan priorizado de refactorización

No implementes todavía.

| Orden | Cambio | Por qué primero / después | Pruebas requeridas | Dependencias |
|---:|---|---|---|---|
| 1 | Caracterizar el comportamiento actual con pruebas automatizadas (JUnit) que reproduzcan los 6 escenarios de línea base | Va primero porque ninguno de los demás cambios se puede verificar de forma segura sin una red de pruebas que corra sola; hasta ahora la línea base es manual | Las 6 pruebas de caracterización equivalentes a LB-01..LB-06 | Ninguna |
| 2 | Extraer el cálculo de tarifa a un método con nombre (`calcularTotal`) y nombrar los literales `40` y `0.85` como constantes | Es el cambio de menor riesgo del diagnóstico (fila "Separar cálculo VIP" = Bajo en `04_MATRIZ_RIESGO.md`); dejar clara la regla de tarifa facilita distinguir el resto de la lógica en los pasos siguientes | `vipConservaResultadoActual()` | Paso 1 |
| 3 | Extraer la notificación y la persistencia simulada (los dos `println`) a colaboradores separados e inyectables | Reduce el acoplamiento a la consola y prepara la testabilidad; conviene hacerlo con la tarifa ya aislada, para no mezclar dos cambios distintos en el mismo commit | `reservaValidaSeConfirma()` | Paso 2 |
| 4 | Introducir los Value Objects `Correo` y `PeriodoReserva`, y reemplazar el `String tipo` por un enum | Es el cambio de mayor riesgo identificado (fila "Introducir PeriodoReserva" = Alto): puede cambiar **cuándo** se detecta un dato inválido (constructor vs. `procesar()`). Se deja para el final, cuando ya exista una suite de pruebas completa capaz de detectar cualquier cambio de contrato | `correoValidoSeAcepta()` / `correoInvalidoSeRechaza()`, `periodoInvalidoNoProcesa()`, `reservaNulaNoLanzaExcepcion()` | Pasos 1–3 |

## Ejemplo de razonamiento

1. Caracterizar casos actuales.
2. Extraer cálculo a método con intención.
3. Separar notificación / persistencia.
4. Introducir Value Objects.

La evaluación se centra en **la justificación**, no en repetir exactamente este orden — y en este caso el orden elegido coincide con el ejemplo precisamente porque la matriz de riesgo (`04_MATRIZ_RIESGO.md`) respalda esa misma progresión: de menor a mayor probabilidad de alterar el contrato observable.
