# Fase J | Matriz de riesgo

| Cambio candidato | Probabilidad de romper | Impacto si rompe | Riesgo | Cómo reducirlo |
|---|---|---|---|---|
| Extraer clase de notificación | Baja — el código movido son solo dos `System.out.println`, sin lógica de negocio | Bajo — solo afecta cómo se emite el mensaje, no el resultado de negocio (estado/total) | Bajo | Prueba `reservaValidaSeConfirma()` que verifique estado y total sin depender de la salida de consola |
| Introducir Correo (Value Object) | Media — cambia el tipo del campo y de un parámetro; toca el constructor de `Reserva` y el punto de validación en `ServicioReservas` | Medio — si la validación del Value Object no replica exactamente "contiene @", cambiaría qué correos se aceptan | Medio | `correoValidoSeAcepta()` / `correoInvalidoSeRechaza()` con los mismos casos límite actuales, antes de introducirlo |
| Introducir PeriodoReserva | Media — mueve la validación `fin > inicio` desde `ServicioReservas` hacia el constructor del Value Object, cambiando **cuándo** se detecta el error | Alto — si antes `procesar()` devolvía `0` en silencio y ahora el constructor de `Reserva` lanza una excepción al construirla, el contrato observable cambia para quien construye una `Reserva` con datos inválidos | Alto | Decidir explícitamente si el nuevo Value Object debe lanzar excepción o permitir construirse "inválido pero detectable", y proteger ambos comportamientos con una prueba antes del cambio |
| Simplificar validaciones | Baja — los 4 `if` ya son guard clauses; darles nombre o extraerlos no cambia la lógica booleana | Bajo si se preserva el orden de evaluación exacto; Medio si se reordenan (con varias condiciones inválidas a la vez, cambiar el orden cambia cuál "gana" primero) | Bajo-Medio | `unaHoraNoPermiteProcesar()` más una prueba con varias condiciones inválidas simultáneamente, para fijar qué ruta se ejecuta primero |
| Separar cálculo VIP | Baja — aislar `total = 40; if (VIP) total *= 0.85;` en un método propio es mecánico | Bajo si el resultado numérico (34.0 / 40.0) se preserva exactamente | Bajo | `vipConservaResultadoActual()` fijando 34.0 (VIP) y 40.0 (NORMAL) como resultados esperados antes de mover el cálculo |

## Escala

- **Bajo:** cambio local, comportamiento bien entendido y prueba fácil de crear.
- **Medio:** afecta varias decisiones o requiere adaptar construcción de objetos.
- **Alto:** puede alterar contrato observable, flujos de error o efectos externos.

## Lectura del resultado

El cambio de mayor riesgo (**Introducir PeriodoReserva**) es también el único que puede alterar el contrato observable actual (de "retorna 0 en silencio" a "lanza excepción al construir"). Por eso el plan de refactorización (`06_PLAN_REFACTORIZACION.md`) lo deja para el final, cuando ya exista una suite de pruebas completa capaz de detectar ese cambio de contrato si llegara a ocurrir sin decidirlo explícitamente.
