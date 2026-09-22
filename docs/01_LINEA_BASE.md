# Fase C | Línea base manual

Completa los seis escenarios **sin refactorizar el diseño**.

| ID | Escenario | Entrada principal | Estado | Retorno | Mensajes / excepción |
|---|---|---|---|---|---|
| LB-01 | NORMAL válida | NORMAL, correo válido, 5h | CONFIRMADA | 40.0 | "Guardando reserva R-101" / "Correo enviado a ana@uees.edu.ec" |
| LB-02 | VIP válida | VIP, correo válido, 5h | CONFIRMADA | 34.0 | "Guardando reserva R-102" / "Correo enviado a ana@uees.edu.ec" |
| LB-03 | Correo inválido | "incorrecto" | PENDIENTE | 0.0 | (sin salida) |
| LB-04 | Periodo inválido | fin <= inicio | PENDIENTE | 0.0 | (sin salida) |
| LB-05 | Límite válido | 2h anticipación | CONFIRMADA | 40.0 | "Guardando reserva R-105" / "Correo enviado a ana@uees.edu.ec" |
| LB-06 | Límite inválido | 1h anticipación | PENDIENTE | 0.0 | (sin salida) |

Evidencia real (capturada ejecutando `LineaBaseRunner`, sin modificar `ServicioReservas` ni `Reserva`):

```
=== LB-01 NORMAL valida ===
Guardando reserva R-101
Correo enviado a ana@uees.edu.ec
-> estado=CONFIRMADA | retorno=40.0

=== LB-02 VIP valida ===
Guardando reserva R-102
Correo enviado a ana@uees.edu.ec
-> estado=CONFIRMADA | retorno=34.0

=== LB-03 Correo invalido ===
-> estado=PENDIENTE | retorno=0.0

=== LB-04 Periodo invalido (fin <= inicio) ===
-> estado=PENDIENTE | retorno=0.0

=== LB-05 Limite valido (2h anticipacion) ===
Guardando reserva R-105
Correo enviado a ana@uees.edu.ec
-> estado=CONFIRMADA | retorno=40.0

=== LB-06 Limite invalido (1h anticipacion) ===
-> estado=PENDIENTE | retorno=0.0
```

## Preguntas

1. **¿Qué valores cambian entre NORMAL y VIP?** Solo el total retornado: VIP aplica un descuento del 15% (`total = 40 * 0.85 = 34.0`) a través del literal `"VIP".equals(r.getTipo())`. El resto del comportamiento —validaciones, mensajes impresos, estado final— es idéntico entre ambos tipos.

2. **¿Qué casos dejan la reserva en PENDIENTE?** Todos los que no llegan a `confirmar()`: correo inválido (LB-03), periodo inválido (LB-04) y anticipación menor a 2 horas (LB-06). Por lectura del código, `reserva == null` también deja el flujo sin confirmar, aunque no aplica como escenario propio porque no hay una `Reserva` sobre la cual consultar el estado.

3. **¿Qué devuelve `procesar()` cuando una entrada no es procesable?** Siempre `0.0`, sin distinguir la causa: correo inválido, periodo inválido, anticipación insuficiente y reserva nula producen exactamente el mismo valor de retorno.

4. **¿Existe alguna excepción visible en el flujo actual?** No. El método nunca lanza una excepción; incluso con `reserva == null` responde con un `return 0` temprano. Todos los casos inválidos se resuelven en silencio con el mismo valor.

5. **¿Qué mensajes aparecen solo cuando la reserva se confirma?** `"Guardando reserva <id>"` y `"Correo enviado a <correo>"`. Ningún mensaje se imprime en los tres casos rechazados (LB-03, LB-04, LB-06).
