# Fase D | Mapa actual de responsabilidades

| Fragmento | Responsabilidad observada | Clase actual |
|---|---|---|
| Validar null/correo/periodo/anticipación | Validación | ServicioReservas |
| Calcular total y descuento VIP | Cálculo de tarifa | ServicioReservas |
| Imprimir "Guardando reserva" | Persistencia simulada | ServicioReservas |
| Imprimir "Correo enviado" | Notificación simulada | ServicioReservas |
| Cambiar estado a CONFIRMADA | Cambio de estado de dominio | Reserva |

## Mapa conceptual

```text
ServicioReservas
├── valida entrada
├── interpreta correo
├── interpreta periodo
├── decide anticipación
├── calcula precio
├── conoce descuento VIP
├── simula persistencia
├── simula notificación
└── ordena confirmar Reserva

Reserva
└── mantiene estado
```

**Pregunta clave:** ¿cuántas razones diferentes podría tener `ServicioReservas` para cambiar?

## Respuesta

Al menos cinco razones distintas e independientes:

1. **Cambia una regla de validación** (por ejemplo, aceptar correos sin "@" pero con otro formato, o cambiar el mínimo de anticipación de 2 horas a otro valor).
2. **Cambia la regla de tarifación** (el precio base de 40, o el porcentaje de descuento VIP de 0.85, o se agrega un tercer tipo de tarifa).
3. **Cambia el mecanismo de persistencia** (dejar de "simular" con un `println` y guardar realmente en una base de datos o un repositorio).
4. **Cambia el mecanismo de notificación** (dejar de imprimir en consola y enviar un correo real, un SMS, etc.).
5. **Cambia el flujo de confirmación del dominio** (por ejemplo, si confirmar una reserva alguna vez requiriera pasos adicionales antes de invocar `reserva.confirmar()`).

Que una sola clase pueda cambiar por cinco motivos tan distintos —reglas de negocio, infraestructura de datos y canal de comunicación— es la evidencia concreta de que `ServicioReservas` viola el Principio de Responsabilidad Única (SRP).
