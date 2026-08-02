package restaurante;

import java.time.LocalTime;

// Administra clientes en una lista enlazada simple.
public class Clientes {
	public static final String ESTADO_ESPERANDO = "Esperando";
	public static final String ESTADO_ATENDIDO = "Atendido";
	public static final String ESTADO_PAGADO = "Pagado";

	private NodoCliente cabeza;

	// Modelo de datos de un cliente en atencion.
	public static class ClienteRegistro {
		private final String nombre;
		private final int cantidadPersonas;
		private final int numeroMesaAsignada;
		private final LocalTime horaIngreso;
		private String estadoAtencion;

		public ClienteRegistro(String nombre, int cantidadPersonas, int numeroMesaAsignada,
				LocalTime horaIngreso, String estadoAtencion) {
			this.nombre = nombre;
			this.cantidadPersonas = cantidadPersonas;
			this.numeroMesaAsignada = numeroMesaAsignada;
			this.horaIngreso = horaIngreso;
			this.estadoAtencion = estadoAtencion;
		}

		public String getNombre() {
			return nombre;
		}

		public int getCantidadPersonas() {
			return cantidadPersonas;
		}

		public int getNumeroMesaAsignada() {
			return numeroMesaAsignada;
		}

		public LocalTime getHoraIngreso() {
			return horaIngreso;
		}

		public String getEstadoAtencion() {
			return estadoAtencion;
		}

		public void setEstadoAtencion(String estadoAtencion) {
			this.estadoAtencion = estadoAtencion;
		}

		@Override
		public String toString() {
			return "ClienteRegistro{" +
					"nombre='" + nombre + '\'' +
					", cantidadPersonas=" + cantidadPersonas +
					", numeroMesaAsignada=" + numeroMesaAsignada +
					", horaIngreso=" + horaIngreso +
					", estadoAtencion='" + estadoAtencion + '\'' +
					'}';
		}
	}

	// Nodo simple para enlazar clientes.
	private static class NodoCliente {
		private final ClienteRegistro dato;
		private NodoCliente siguiente;

		public NodoCliente(ClienteRegistro dato) {
			this.dato = dato;
		}
	}

	// Registra cliente validando capacidad de mesa y estado de atencion.
	public ClienteRegistro registrarCliente(String nombre, int cantidadPersonas, int numeroMesa,
			LocalTime horaIngreso, String estadoAtencion, Reserva reserva) {
		if (nombre == null || nombre.isBlank()) {
			throw new IllegalArgumentException("El nombre es obligatorio.");
		}
		if (cantidadPersonas < 1 || cantidadPersonas > 4) {
			throw new IllegalArgumentException("La cantidad de personas debe estar entre 1 y 4.");
		}

		Mesa mesa = reserva.buscarMesa(numeroMesa);
		if (mesa == null) {
			throw new IllegalArgumentException("No existe la mesa numero " + numeroMesa + ".");
		}
		if (!Mesa.ESTADO_LIBRE.equals(mesa.getEstado())) {
			throw new IllegalArgumentException(
					"La mesa " + numeroMesa + " no esta libre. Estado actual: " + mesa.getEstado());
		}
		if (cantidadPersonas > mesa.getCapacidad()) {
			throw new IllegalArgumentException(
					"La mesa " + numeroMesa + " no soporta esa cantidad de personas.");
		}

		String estadoNormalizado = normalizarEstadoAtencion(estadoAtencion);
		ClienteRegistro cliente = new ClienteRegistro(
				nombre.trim(),
				cantidadPersonas,
				numeroMesa,
				horaIngreso,
				estadoNormalizado);

		agregarAlFinal(cliente);

		// Al asignar un cliente, la mesa deja de estar libre.
		if (!ESTADO_ESPERANDO.equals(estadoNormalizado)) {
			mesa.setEstado(Mesa.ESTADO_OCUPADA);
		} else {
			mesa.setEstado(Mesa.ESTADO_RESERVADA);
		}

		return cliente;
	}

	// Actualiza el estado de un cliente buscado por nombre.
	public boolean actualizarEstadoCliente(String nombre, String nuevoEstado) {
		NodoCliente actual = cabeza;
		String estadoNormalizado = normalizarEstadoAtencion(nuevoEstado);

		while (actual != null) {
			if (actual.dato.getNombre().equalsIgnoreCase(nombre)) {
				actual.dato.setEstadoAtencion(estadoNormalizado);
				return true;
			}
			actual = actual.siguiente;
		}

		return false;
	}

	// Devuelve todos los clientes en formato de texto.
	public String mostrarClientes() {
		if (cabeza == null) {
			return "No hay clientes registrados.";
		}

		StringBuilder resultado = new StringBuilder();
		NodoCliente actual = cabeza;
		while (actual != null) {
			resultado.append(actual.dato).append("\n");
			actual = actual.siguiente;
		}
		return resultado.toString();
	}

	// Inserta un cliente al final de la lista enlazada.
	private void agregarAlFinal(ClienteRegistro cliente) {
		NodoCliente nuevo = new NodoCliente(cliente);

		if (cabeza == null) {
			cabeza = nuevo;
			return;
		}

		NodoCliente actual = cabeza;
		while (actual.siguiente != null) {
			actual = actual.siguiente;
		}
		actual.siguiente = nuevo;
	}

	// Normaliza y valida estados permitidos de cliente.
	private String normalizarEstadoAtencion(String estado) {
		if (ESTADO_ESPERANDO.equalsIgnoreCase(estado)) {
			return ESTADO_ESPERANDO;
		}
		if (ESTADO_ATENDIDO.equalsIgnoreCase(estado)) {
			return ESTADO_ATENDIDO;
		}
		if (ESTADO_PAGADO.equalsIgnoreCase(estado)) {
			return ESTADO_PAGADO;
		}
		throw new IllegalArgumentException("Estado de cliente invalido: " + estado);
	}
}
