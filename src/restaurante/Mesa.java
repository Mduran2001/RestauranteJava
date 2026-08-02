package restaurante;

// Representa una mesa del restaurante con estado y pedido actual.
public class Mesa {
	public static final String ESTADO_LIBRE = "Libre";
	public static final String ESTADO_OCUPADA = "Ocupada";
	public static final String ESTADO_RESERVADA = "Reservada";

	private int numero;
	private final int capacidad;
	private String estado;
	private String pedidoActual;

	// Cada mesa se crea con capacidad fija de 4 personas.
	public Mesa(int numero, String estado, String pedidoActual) {
		this.numero = numero;
		this.capacidad = 4;
		this.estado = validarEstado(estado);
		this.pedidoActual = pedidoActual;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public int getCapacidad() {
		return capacidad;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = validarEstado(estado);
	}

	public String getPedidoActual() {
		return pedidoActual;
	}

	public void setPedidoActual(String pedidoActual) {
		this.pedidoActual = pedidoActual;
	}

	// Valida y normaliza el estado permitido de la mesa.
	private String validarEstado(String estado) {
		if (!ESTADO_LIBRE.equalsIgnoreCase(estado)
				&& !ESTADO_OCUPADA.equalsIgnoreCase(estado)
				&& !ESTADO_RESERVADA.equalsIgnoreCase(estado)) {
			throw new IllegalArgumentException("Estado de mesa invalido: " + estado);
		}

		if (ESTADO_LIBRE.equalsIgnoreCase(estado)) {
			return ESTADO_LIBRE;
		}
		if (ESTADO_OCUPADA.equalsIgnoreCase(estado)) {
			return ESTADO_OCUPADA;
		}
		return ESTADO_RESERVADA;
	}

	// Representacion textual de la mesa.
	@Override
	public String toString() {
		return "Mesa{" +
				"numero=" + numero +
				", capacidad=" + capacidad +
				", estado='" + estado + '\'' +
				", pedidoActual='" + pedidoActual + '\'' +
				'}';
	}
}
