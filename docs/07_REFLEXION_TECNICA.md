# Reflexión técnica final

Extensión sugerida: **250–350 palabras**.

El mayor riesgo lo genera introducir `PeriodoReserva`: hoy `fin <= inicio` se detecta dentro de `procesar()` y responde en silencio con `0.0`; si el nuevo Value Object valida en su propio constructor, esa misma situación podría pasar de "devolver 0" a "lanzar una excepción al construir la reserva", y ese es un cambio de contrato observable, no solo de estructura interna. En cambio, el problema que parece más fácil de corregir pero esconde el mismo peligro es "simplificar las validaciones": los cuatro `if` ya son guard clauses, así que da la impresión de que reordenarlos es inofensivo, pero si dos condiciones fallan a la vez, el orden actual decide cuál "gana"; cambiarlo sin darse cuenta podría alterar cuál causa de rechazo se evalúa primero, aunque el retorno visible siga siendo `0.0` en ambos casos.

Antes de tocar el código, las pruebas indispensables son las que fijan los seis escenarios de la línea base (`01_LINEA_BASE.md`): no alcanza con verificar que el total sea correcto, también hay que verificar el estado final (`PENDIENTE` vs `CONFIRMADA`), porque un refactor podría preservar el número devuelto y aun así dejar de confirmar la reserva.

La primera responsabilidad que movería es la notificación/persistencia simulada (los dos `println`): es la de menor riesgo según la matriz (`04_MATRIZ_RIESGO.md`), no tiene lógica de negocio propia, y extraerla no obliga a decidir todavía nada sobre el contrato de `Reserva`. La evidencia para defender ese orden es justamente esa matriz: cada cambio candidato quedó clasificado por probabilidad e impacto, y el plan prioriza de menor a mayor riesgo en vez de un orden arbitrario.

La diferencia entre refactorizar y hacer un cambio funcional es el resultado observable: refactorizar mejora la estructura interna —nombres, responsabilidades, tipos— sin que cambie ningún escenario de la línea base; un cambio funcional, en cambio, modifica intencionalmente qué hace el sistema (una regla nueva, un valor de retorno distinto). Por eso este laboratorio termina en un diagnóstico y un plan, y no en código "más limpio": todavía no existe la red de pruebas que permita distinguir, con evidencia, cuál de los dos se está haciendo en cada commit.

## Checklist

- [x] Proyecto base compila y ejecuta.
- [x] Seis escenarios de línea base.
- [x] Mapa de responsabilidades.
- [x] Mínimo cinco problemas diagnosticados.
- [x] Matriz de riesgo.
- [x] Pruebas propuestas.
- [x] Plan priorizado.
- [ ] Commit Git del estado inicial. *(pendiente: se registra al final, ver README de este repositorio)*
- [x] Reflexión técnica.
