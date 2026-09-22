package edu.uees.refactor.service;

import edu.uees.refactor.domain.Correo;
import edu.uees.refactor.domain.PeriodoReserva;
import edu.uees.refactor.domain.Reserva;

/**
 * Código heredado intencional (Laboratorio 1 y 2), en proceso de
 * refactorización controlada para Ae5.
 *
 * Historial:
 * - Laboratorio 2: Extract Method de {@link #calcularTotal}.
 * - Ae5 · Refactorización 1: Extract Class de la notificación hacia
 *   {@link NotificadorReserva}.
 * - Ae5 · Refactorización 2: Value Object {@link Correo} para validar
 *   el correo.
 * - Ae5 · Refactorización 3: Value Object {@link PeriodoReserva} para
 *   agrupar inicio/fin (este cambio).
 *
 * Cada paso está respaldado por la suite de {@code ServicioReservasTest}
 * y sigue el plan priorizado en {@code docs/06_PLAN_REFACTORIZACION.md}.
 */
public class ServicioReservas {

    private final NotificadorReserva notificador;

    public ServicioReservas() {
        this(new NotificadorReserva());
    }

    public ServicioReservas(NotificadorReserva notificador) {
        this.notificador = notificador;
    }

    public double procesar(
            Reserva r,
            int horasAnticipacion) {

        if (r == null) {
            return 0;
        }

        if (!Correo.esValido(r.getCorreo())) {
            return 0;
        }

        PeriodoReserva periodo = new PeriodoReserva(
                r.getInicio(), r.getFin()
        );

        if (!periodo.esValido()) {
            return 0;
        }

        if (horasAnticipacion < 2) {
            return 0;
        }

        double total = calcularTotal(r);

        notificador.notificar(r);

        r.confirmar();

        return total;
    }

    private double calcularTotal(Reserva r) {
        double total = 40;

        if ("VIP".equals(r.getTipo())) {
            return total * 0.85;
        }

        return total;
    }
}
