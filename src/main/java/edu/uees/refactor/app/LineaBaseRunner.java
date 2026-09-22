package edu.uees.refactor.app;

import edu.uees.refactor.domain.Reserva;
import edu.uees.refactor.service.ServicioReservas;

import java.time.LocalDateTime;

/**
 * NO es parte del codigo heredado. Es solo un arnes de observacion para
 * construir la linea base manual (docs/01_LINEA_BASE.md) de la Actividad 1.
 * No modifica ni refactoriza ServicioReservas ni Reserva: unicamente los
 * invoca con distintas entradas y registra la salida real.
 */
public class LineaBaseRunner {

    public static void main(String[] args) {
        ServicioReservas servicio = new ServicioReservas();
        LocalDateTime ahora = LocalDateTime.now().plusDays(1);

        ejecutar("LB-01 NORMAL valida",
                new Reserva("R-101", "ana@uees.edu.ec", ahora, ahora.plusHours(1), "NORMAL"),
                5, servicio);

        ejecutar("LB-02 VIP valida",
                new Reserva("R-102", "ana@uees.edu.ec", ahora, ahora.plusHours(1), "VIP"),
                5, servicio);

        ejecutar("LB-03 Correo invalido",
                new Reserva("R-103", "incorrecto", ahora, ahora.plusHours(1), "NORMAL"),
                5, servicio);

        ejecutar("LB-04 Periodo invalido (fin <= inicio)",
                new Reserva("R-104", "ana@uees.edu.ec", ahora, ahora, "NORMAL"),
                5, servicio);

        ejecutar("LB-05 Limite valido (2h anticipacion)",
                new Reserva("R-105", "ana@uees.edu.ec", ahora, ahora.plusHours(1), "NORMAL"),
                2, servicio);

        ejecutar("LB-06 Limite invalido (1h anticipacion)",
                new Reserva("R-106", "ana@uees.edu.ec", ahora, ahora.plusHours(1), "NORMAL"),
                1, servicio);
    }

    private static void ejecutar(String escenario, Reserva reserva, int horas, ServicioReservas servicio) {
        System.out.println("=== " + escenario + " ===");
        double total = servicio.procesar(reserva, horas);
        System.out.println("-> estado=" + reserva.getEstado() + " | retorno=" + total);
        System.out.println();
    }
}
