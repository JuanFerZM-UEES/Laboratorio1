# Reflexión técnica — Laboratorio 2 (JUnit 5 y red de seguridad)

Extensión sugerida: **250–350 palabras**.

La prueba que más confianza me dio fue `vipActualmenteRetornaTreintaYCuatro()`, no por ser la más compleja, sino porque fue la única que reaccionó cuando provoqué la regresión intencional (cambiar `0.85` por `0.80`, sección 19): el mensaje real `expected: <34.0> but was: <32.0>` (ver `docs/evidencia/03_regresion_intencional_detectada.txt`) demostró con evidencia ejecutable, y no con una suposición, que la red de pruebas detecta exactamente el tipo de error que un refactor descuidado podría introducir.

Un cambio de diseño que podría alterar el contrato de `correoInvalidoNoProcesaReserva()` es introducir un Value Object `Correo` que valide en su propio constructor: hoy `new Reserva(..., "correo-invalido", ...)` se construye sin problema y es `procesar()` quien detecta el error y retorna `0` en silencio; si `Correo` lanzara una excepción al construirse, el mismo dato pasaría de "reserva construida, total 0, estado PENDIENTE" a "excepción antes de que la reserva exista", un cambio de comportamiento observable, no una refactorización pura.

Los casos de 1h y 2h son más útiles que probar solo 5h porque 5h está lejos del límite y no distingue `<` de `<=`: un condicional roto justo en esa frontera seguiría pasando una prueba con 5h sin que nadie lo note. Los casos límite ejercitan exactamente la línea donde suelen aparecer los errores al refactorizar condicionales.

La diferencia entre una prueba de caracterización y una de nueva funcionalidad es la intención: las siete pruebas de este laboratorio describen "esto es lo que el sistema ya hace hoy", sin opinar si esa regla (el descuento VIP del 15 %) es la correcta; una prueba de nueva funcionalidad fijaría, en cambio, un comportamiento que todavía no existe.

La evidencia concreta de que Extract Method preservó el comportamiento es doble: `docs/evidencia/01_suite_antes_del_refactor.txt` y `02_suite_despues_del_refactor.txt` muestran las mismas 7 pruebas en verde antes y después de mover el cálculo a `calcularTotal()`, y `03_regresion_intencional_detectada.txt` muestra que, ante un cambio real, la misma suite lo detectó de inmediato. Verde antes, verde después y roja ante un cambio real: esa combinación demuestra preservación, no solo apariencia de seguridad.

## Checklist de evidencias (sección 23 del laboratorio)

- [x] `pom.xml` con JUnit 5.10.2 y Surefire 3.2.5.
- [x] `ServicioReservasTest.java` con 7 pruebas.
- [x] AAA reconocible y nombres expresivos.
- [x] Evidencia real de la suite en verde (`docs/evidencia/01_...` y `02_...`).
- [x] Micro-refactorización Extract Method (`calcularTotal`) protegida por la suite.
- [x] Regresión intencional detectada y revertida (`docs/evidencia/03_...`).
- [x] Reflexión técnica de 250–350 palabras.
- [ ] Commit dedicado a pruebas *(pendiente: lo ejecuta el usuario, ver instrucciones de entrega)*.
- [ ] Commit dedicado a la micro-refactorización *(pendiente: lo ejecuta el usuario, ver instrucciones de entrega)*.
