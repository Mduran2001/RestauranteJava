package restaurante;

import java.time.LocalDateTime;

// Gestiona incidencias de pedidos usando una pila (LIFO).
public class DetallePedido {
	// Datos de una incidencia: devolucion o cancelacion.
	public static class IncidenciaPedido {
		private final String pedido;
		private final String motivo;
		private final LocalDateTime fecha;
		private final String responsable;

		public IncidenciaPedido(String pedido, String motivo, LocalDateTime fecha, String responsable) {
			this.pedido = pedido;
			this.motivo = motivo;
			this.fecha = fecha;
			this.responsable = responsable;
		}

		public String getPedido() {
			return pedido;
		}

		public String getMotivo() {
			return motivo;
		}

		public LocalDateTime getFecha() {
			return fecha;
		}

		public String getResponsable() {
			return responsable;
		}

		@Override
		public String toString() {
			return "IncidenciaPedido{" +
					"pedido='" + pedido + '\'' +
					", motivo='" + motivo + '\'' +
					", fecha=" + fecha +
					", responsable='" + responsable + '\'' +
					'}';
		}
	}

	// Nodo simple para implementar la pila de incidencias.
	private static class NodoPila {
		private final IncidenciaPedido dato;
		private NodoPila siguiente;

		private NodoPila(IncidenciaPedido dato) {
			this.dato = dato;
		}
	}

	// Cima de la pila.
	private NodoPila cima;

	// Apila una nueva incidencia validando campos obligatorios.
	public void ingresarIncidencia(String pedido, String motivo, LocalDateTime fecha, String responsable) {
		validarCampos(pedido, motivo, fecha, responsable);
		IncidenciaPedido incidencia = new IncidenciaPedido(
				pedido.trim(),
				motivo.trim(),
				fecha,
				responsable.trim());
		apilar(incidencia);
	}

	// Elimina y retorna la incidencia en la cima.
	public IncidenciaPedido desapilarIncidencia() {
		if (cima == null) {
			return null;
		}

		IncidenciaPedido dato = cima.dato;
		cima = cima.siguiente;
		return dato;
	}

	// Consulta la incidencia mas reciente sin retirarla.
	public IncidenciaPedido verUltimaIncidencia() {
		if (cima == null) {
			return null;
		}
		return cima.dato;
	}

	// Indica si la pila esta vacia.
	public boolean estaVacia() {
		return cima == null;
	}

	// Cuenta incidencias recorriendo la pila.
	public int contarIncidencias() {
		int total = 0;
		NodoPila actual = cima;

		while (actual != null) {
			total++;
			actual = actual.siguiente;
		}

		return total;
	}

	// Muestra todas las incidencias desde la mas reciente a la mas antigua.
	public String mostrarIncidencias() {
		if (cima == null) {
			return "No hay incidencias registradas.";
		}

		StringBuilder salida = new StringBuilder();
		NodoPila actual = cima;

		while (actual != null) {
			salida.append(actual.dato).append("\n");
			actual = actual.siguiente;
		}

		return salida.toString();
	}

	// Inserta en cima (operacion push).
	private void apilar(IncidenciaPedido incidencia) {
		NodoPila nuevo = new NodoPila(incidencia);
		nuevo.siguiente = cima;
		cima = nuevo;
	}

	// Valida que los datos de incidencia no esten vacios.
	private void validarCampos(String pedido, String motivo, LocalDateTime fecha, String responsable) {
		if (pedido == null || pedido.isBlank()) {
			throw new IllegalArgumentException("El pedido es obligatorio.");
		}
		if (motivo == null || motivo.isBlank()) {
			throw new IllegalArgumentException("El motivo es obligatorio.");
		}
		if (fecha == null) {
			throw new IllegalArgumentException("La fecha es obligatoria.");
		}
		if (responsable == null || responsable.isBlank()) {
			throw new IllegalArgumentException("El responsable es obligatorio.");
		}
	}
}
