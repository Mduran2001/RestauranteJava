package restaurante;

import java.time.LocalDateTime;

// Gestiona mesas en un arbol binario y reservas en una lista doble enlazada.
public class Reserva {
	// Raiz del arbol de mesas, ordenado por numero.
	private Nodo<Mesa> raiz;
	// Extremos de la lista doble de reservas.
	private NodoReserva cabezaReservas;
	private NodoReserva colaReservas;

	// Estructura auxiliar para resumir estados de mesas.
	private static class ConteoMesas {
		private int libres;
		private int ocupadas;
		private int reservadas;
	}

	// Modelo de una reserva individual.
	public static class ReservaRegistro {
		private final String nombreCliente;
		private final LocalDateTime fechaHora;
		private final int numeroMesa;

		public ReservaRegistro(String nombreCliente, LocalDateTime fechaHora, int numeroMesa) {
			this.nombreCliente = nombreCliente;
			this.fechaHora = fechaHora;
			this.numeroMesa = numeroMesa;
		}

		public String getNombreCliente() {
			return nombreCliente;
		}

		public LocalDateTime getFechaHora() {
			return fechaHora;
		}

		public int getNumeroMesa() {
			return numeroMesa;
		}

		@Override
		public String toString() {
			return "ReservaRegistro{" +
					"nombreCliente='" + nombreCliente + '\'' +
					", fechaHora=" + fechaHora +
					", numeroMesa=" + numeroMesa +
					'}';
		}
	}

	// Nodo doble para enlazar reservas.
	private static class NodoReserva {
		private final ReservaRegistro dato;
		private NodoReserva anterior;
		private NodoReserva siguiente;

		private NodoReserva(ReservaRegistro dato) {
			this.dato = dato;
		}
	}

	// Devuelve la raiz del arbol de mesas.
	public Nodo<Mesa> getRaiz() {
		return raiz;
	}

	// Inserta una mesa en el arbol binario segun su numero.
	public void registrarMesa(Mesa mesa) {
		raiz = insertarRecursivo(raiz, mesa);
	}

	// Carga de ejemplo de cinco mesas (opcional para pruebas manuales).
	public void registrarCincoMesas() {
		registrarMesa(new Mesa(3, Mesa.ESTADO_LIBRE, "Sin pedido"));
		registrarMesa(new Mesa(1, Mesa.ESTADO_OCUPADA, "Lomo saltado y 2 limonadas"));
		registrarMesa(new Mesa(5, Mesa.ESTADO_RESERVADA, "Sin pedido"));
		registrarMesa(new Mesa(2, Mesa.ESTADO_LIBRE, "Sin pedido"));
		registrarMesa(new Mesa(4, Mesa.ESTADO_OCUPADA, "Pizza familiar y gaseosa"));
	}

	// Insercion recursiva BST por numero de mesa.
	private Nodo<Mesa> insertarRecursivo(Nodo<Mesa> actual, Mesa mesa) {
		if (actual == null) {
			return new Nodo<>(mesa);
		}

		if (mesa.getNumero() < actual.getDato().getNumero()) {
			actual.setIzquierdo(insertarRecursivo(actual.getIzquierdo(), mesa));
		} else if (mesa.getNumero() > actual.getDato().getNumero()) {
			actual.setDerecho(insertarRecursivo(actual.getDerecho(), mesa));
		}

		return actual;
	}

	// Busca una mesa por numero usando recorrido iterativo en el arbol.
	public Mesa buscarMesa(int numeroMesa) {
		Nodo<Mesa> actual = raiz;

		while (actual != null) {
			if (numeroMesa == actual.getDato().getNumero()) {
				return actual.getDato();
			}

			if (numeroMesa < actual.getDato().getNumero()) {
				actual = actual.getIzquierdo();
			} else {
				actual = actual.getDerecho();
			}
		}

		return null;
	}

	// Muestra todas las mesas en orden ascendente por numero.
	public String mostrarMesasEnOrden() {
		if (raiz == null) {
			return "No hay mesas registradas.";
		}

		StringBuilder resultado = new StringBuilder();
		recorrerEnOrden(raiz, resultado);
		return resultado.toString();
	}

	// Devuelve un resumen de cuantas mesas hay por estado.
	public String resumenEstadoMesas() {
		if (raiz == null) {
			return "No hay mesas registradas.";
		}

		ConteoMesas conteo = new ConteoMesas();
		contarEstados(raiz, conteo);

		return """
				Resumen de mesas
				Libres: %d
				Ocupadas: %d
				Reservadas: %d
				""".formatted(conteo.libres, conteo.ocupadas, conteo.reservadas);
	}

	// Registra una reserva validando cliente, fecha y disponibilidad de mesa.
	public ReservaRegistro registrarReserva(String nombreCliente, LocalDateTime fechaHora, int numeroMesa) {
		if (nombreCliente == null || nombreCliente.isBlank()) {
			throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
		}
		if (fechaHora == null) {
			throw new IllegalArgumentException("La fecha y hora son obligatorias.");
		}
		if (numeroMesa <= 0) {
			throw new IllegalArgumentException("El numero de mesa debe ser mayor a 0.");
		}

		Mesa mesa = buscarMesa(numeroMesa);
		if (mesa == null) {
			throw new IllegalArgumentException("No existe la mesa numero " + numeroMesa + ".");
		}
		if (!Mesa.ESTADO_LIBRE.equals(mesa.getEstado())) {
			throw new IllegalArgumentException(
					"La mesa " + numeroMesa + " no esta libre. Estado actual: " + mesa.getEstado());
		}

		ReservaRegistro reserva = new ReservaRegistro(nombreCliente.trim(), fechaHora, numeroMesa);
		agregarReservaAlFinal(reserva);
		mesa.setEstado(Mesa.ESTADO_RESERVADA);

		return reserva;
	}

	// Muestra reservas desde el inicio de la lista doble.
	public String mostrarReservasDesdeInicio() {
		if (cabezaReservas == null) {
			return "No hay reservas registradas.";
		}

		StringBuilder resultado = new StringBuilder();
		NodoReserva actual = cabezaReservas;
		while (actual != null) {
			resultado.append(actual.dato).append("\n");
			actual = actual.siguiente;
		}
		return resultado.toString();
	}

	// Muestra reservas desde el final de la lista doble.
	public String mostrarReservasDesdeFinal() {
		if (colaReservas == null) {
			return "No hay reservas registradas.";
		}

		StringBuilder resultado = new StringBuilder();
		NodoReserva actual = colaReservas;
		while (actual != null) {
			resultado.append(actual.dato).append("\n");
			actual = actual.anterior;
		}
		return resultado.toString();
	}

	// Inserta una reserva al final de la lista doble.
	private void agregarReservaAlFinal(ReservaRegistro reserva) {
		NodoReserva nuevo = new NodoReserva(reserva);

		if (cabezaReservas == null) {
			cabezaReservas = nuevo;
			colaReservas = nuevo;
			return;
		}

		nuevo.anterior = colaReservas;
		colaReservas.siguiente = nuevo;
		colaReservas = nuevo;
	}

	// Recorrido in-order del arbol de mesas.
	private void recorrerEnOrden(Nodo<Mesa> nodo, StringBuilder resultado) {
		if (nodo == null) {
			return;
		}

		recorrerEnOrden(nodo.getIzquierdo(), resultado);
		resultado.append(nodo.getDato()).append("\n");
		recorrerEnOrden(nodo.getDerecho(), resultado);
	}

	// Recorre el arbol y acumula conteo por estado.
	private void contarEstados(Nodo<Mesa> nodo, ConteoMesas conteo) {
		if (nodo == null) {
			return;
		}

		contarEstados(nodo.getIzquierdo(), conteo);

		String estado = nodo.getDato().getEstado();
		switch (estado) {
			case Mesa.ESTADO_LIBRE -> conteo.libres++;
			case Mesa.ESTADO_OCUPADA -> conteo.ocupadas++;
			case Mesa.ESTADO_RESERVADA -> conteo.reservadas++;
			default -> {
			}
		}

		contarEstados(nodo.getDerecho(), conteo);
	}
}
