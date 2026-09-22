package edu.uees.refactor.service;

import edu.uees.refactor.domain.Reserva;

/**
 * Colaborador responsable de "notificar" una reserva confirmada.
 *
 * Ae5 · Refactorización 1 · Extract Class.
 *
 * Extraído de {@code ServicioReservas.procesar()}: antes, esa
 * responsabilidad (avisar que la reserva se guardó y que se envió un
 * correo) vivía mezclada con las reglas de negocio (validaciones,
 * cálculo de tarifa). Esta clase no cambia QUÉ se imprime — sigue
 * siendo exactamente la misma simulación con {@code System.out} — solo
 * separa esa razón de cambio de la de {@code ServicioReservas}, que
 * hoy tenía al menos cinco motivos distintos para cambiar (ver
 * {@code docs/02_MAPA_RESPONSABILIDADES.md}).
 */
public class NotificadorReserva {

    public void notificar(Reserva reserva) {
        System.out.println("Guardando reserva " + reserva.getId());
        System.out.println("Correo enviado a " + reserva.getCorreo());
    }
}
