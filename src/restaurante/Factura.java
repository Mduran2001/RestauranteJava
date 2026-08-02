package restaurante;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Genera el comprobante de una orden con su detalle y total.
public class Factura {
	private final int numeroFactura;
	private final Producto orden;
	private final LocalDateTime fechaEmision;

	// Crea la factura con validaciones de datos minimos.
	public Factura(int numeroFactura, Producto orden) {
		if (numeroFactura <= 0) {
			throw new IllegalArgumentException("El numero de factura debe ser mayor a 0.");
		}
		if (orden == null) {
			throw new IllegalArgumentException("La orden no puede ser nula.");
		}

		this.numeroFactura = numeroFactura;
		this.orden = orden;
		this.fechaEmision = LocalDateTime.now();
	}

	// Getters de datos de factura.
	public int getNumeroFactura() {
		return numeroFactura;
	}

	public Producto getOrden() {
		return orden;
	}

	public LocalDateTime getFechaEmision() {
		return fechaEmision;
	}

	// Reutiliza el total calculado por la orden.
	public double calcularTotal() {
		return orden.calcularTotal();
	}

	// Construye una vista completa de la factura lista para mostrar.
	public String generarDetalleFactura() {
		StringBuilder salida = new StringBuilder();
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		salida.append("FACTURA\n");
		salida.append("Numero factura: ").append(numeroFactura).append("\n");
		salida.append("Fecha emision: ").append(fechaEmision.format(formato)).append("\n");
		salida.append("Numero orden: ").append(orden.getNumeroOrden()).append("\n");
		salida.append("Mesa: ").append(orden.getNumeroMesa()).append("\n");
		salida.append("Estado orden: ").append(orden.getEstado()).append("\n");
		salida.append("--------------------------------\n");
		salida.append("DESGLOSE DEL PEDIDO\n");
		salida.append(orden.mostrarItemsPedido());

		salida.append("--------------------------------\n");
		salida.append("TOTAL: S/").append(String.format("%.2f", calcularTotal())).append("\n");

		return salida.toString();
	}
}
