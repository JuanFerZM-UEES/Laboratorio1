package edu.uees.refactor.service;

import edu.uees.refactor.domain.Reserva;

/**
 * Código heredado intencional (Laboratorio 1 y 2).
 *
 * IMPORTANTE:
 * Solo se aplicó la micro-refactorización protegida y autorizada
 * por el Laboratorio 2 (Extract Method de {@link #calcularTotal},
 * sección 18 de la guía), respaldada por la suite de
 * {@code ServicioReservasTest}. El resto de la estructura se deja
 * intacta a propósito para Ae5, según el plan priorizado en
 * {@code docs/06_PLAN_REFACTORIZACION.md}.
 */
public class ServicioReservas {

    public double procesar(
            Reserva r,
            int horasAnticipacion) {

        if (r == null) {
            return 0;
        }

        if (r.getCorreo() == null
                || !r.getCorreo().contains("@")) {
            return 0;
        }

        if (r.getInicio() == null
                || r.getFin() == null
                || !r.getFin().isAfter(r.getInicio())) {
            return 0;
        }

        if (horasAnticipacion < 2) {
            return 0;
        }

        double total = calcularTotal(r);

        System.out.println(
                "Guardando reserva " + r.getId()
        );

        System.out.println(
                "Correo enviado a " + r.getCorreo()
        );

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
