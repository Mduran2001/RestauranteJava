package restaurante;

// Representa un pedido realizado por una mesa y el menu disponible.
public class Producto {
	public static final String ESTADO_PENDIENTE = "Pendiente";
	public static final String ESTADO_EN_PREPARACION = "En preparacion";
	public static final String ESTADO_ENTREGADO = "Entregado";

	// Clasificacion basica de items del menu.
	public enum Categoria {
		PLATILLO,
		POSTRE,
		BEBIDA
	}

	// Item del menu enlazado de forma simple.
	public static class ItemMenu {
		private final String nombre;
		private final Categoria categoria;
		private final double precio;
		private ItemMenu siguiente;

		public ItemMenu(String nombre, Categoria categoria, double precio) {
			this.nombre = nombre;
			this.categoria = categoria;
			this.precio = precio;
		}

		public String getNombre() {
			return nombre;
		}

		public Categoria getCategoria() {
			return categoria;
		}

		public double getPrecio() {
			return precio;
		}

		@Override
		public String toString() {
			return nombre + " - S/" + precio;
		}
	}

	// Cabeza de la lista enlazada con todo el menu fijo.
	private static final ItemMenu MENU_CABEZA = construirMenu();

	// Nodo para enlazar items seleccionados en un pedido.
	private static class NodoItemPedido {
		private final ItemMenu dato;
		private NodoItemPedido siguiente;

		private NodoItemPedido(ItemMenu dato) {
			this.dato = dato;
		}
	}

	private final int numeroOrden;
	private final int numeroMesa;
	// Lista enlazada de items solicitados en la orden actual.
	private NodoItemPedido cabezaItemsPedido;
	private NodoItemPedido colaItemsPedido;
	private String estado;

	// Crea una orden vacia de items, lista para recibir productos del menu.
	public Producto(int numeroOrden, int numeroMesa, String estado) {
		if (numeroOrden <= 0) {
			throw new IllegalArgumentException("El numero de orden debe ser mayor a 0.");
		}
		if (numeroMesa <= 0) {
			throw new IllegalArgumentException("El numero de mesa debe ser mayor a 0.");
		}

		this.numeroOrden = numeroOrden;
		this.numeroMesa = numeroMesa;
		this.estado = normalizarEstado(estado);
	}

	// Getters basicos de identificacion de orden.
	public int getNumeroOrden() {
		return numeroOrden;
	}

	public int getNumeroMesa() {
		return numeroMesa;
	}

	// Indica si ya se agrego al menos un item al pedido.
	public boolean tieneItems() {
		return cabezaItemsPedido != null;
	}

	// Agrega al pedido un item existente en el menu por nombre.
	public void agregarItemAlPedido(String nombreItem) {
		ItemMenu itemMenu = buscarEnMenu(nombreItem);
		NodoItemPedido nuevo = new NodoItemPedido(itemMenu);

		if (cabezaItemsPedido == null) {
			cabezaItemsPedido = nuevo;
			colaItemsPedido = nuevo;
			return;
		}

		colaItemsPedido.siguiente = nuevo;
		colaItemsPedido = nuevo;
	}

	// Estado del pedido (pendiente, preparacion o entregado).
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = normalizarEstado(estado);
	}

	// Suma los precios de todos los items registrados en la orden.
	public double calcularTotal() {
		double total = 0;
		NodoItemPedido actual = cabezaItemsPedido;
		while (actual != null) {
			total += actual.dato.getPrecio();
			actual = actual.siguiente;
		}
		return total;
	}

	// Devuelve el menu formateado por categorias.
	public static String mostrarMenu() {
		StringBuilder salida = new StringBuilder();

		agregarCategoriaAlMenu(salida, Categoria.PLATILLO, "PLATILLOS");
		agregarCategoriaAlMenu(salida, Categoria.POSTRE, "POSTRES");
		agregarCategoriaAlMenu(salida, Categoria.BEBIDA, "BEBIDAS");

		return salida.toString();
	}

	// Devuelve el detalle de items del pedido con numeracion y precio.
	public String mostrarItemsPedido() {
		if (cabezaItemsPedido == null) {
			return "Sin productos.";
		}

		StringBuilder salida = new StringBuilder();
		NodoItemPedido actual = cabezaItemsPedido;
		int indice = 1;
		while (actual != null) {
			salida.append(indice)
					.append(". ")
					.append(actual.dato.getNombre())
					.append(" (")
					.append(actual.dato.getCategoria())
					.append(") - S/")
					.append(String.format("%.2f", actual.dato.getPrecio()))
					.append("\n");
			indice++;
			actual = actual.siguiente;
		}
		return salida.toString();
	}

	// Busca un item por nombre dentro del menu enlazado.
	private ItemMenu buscarEnMenu(String nombre) {
		ItemMenu actual = MENU_CABEZA;
		while (actual != null) {
			if (actual.getNombre().equalsIgnoreCase(nombre)) {
				return actual;
			}
			actual = actual.siguiente;
		}
		throw new IllegalArgumentException("El producto no existe en el menu: " + nombre);
	}

	// Recorre el menu y agrega solo la categoria solicitada al texto final.
	private static void agregarCategoriaAlMenu(StringBuilder salida, Categoria categoria, String titulo) {
		salida.append(titulo).append("\n");
		ItemMenu actual = MENU_CABEZA;
		while (actual != null) {
			if (actual.getCategoria() == categoria) {
				salida.append("- ")
						.append(actual.getNombre())
						.append(" - S/")
						.append(String.format("%.2f", actual.getPrecio()))
						.append("\n");
			}
			actual = actual.siguiente;
		}
	}

	// Construye manualmente la lista enlazada del menu fijo.
	private static ItemMenu construirMenu() {
		ItemMenu cabeza = null;
		ItemMenu cola = null;

		cabeza = enlazar(cabeza, cola, new ItemMenu("Lomo saltado", Categoria.PLATILLO, 28.00));
		cola = obtenerUltimo(cabeza);
		cola.siguiente = new ItemMenu("Arroz con pollo", Categoria.PLATILLO, 24.00);
		cola = cola.siguiente;
		cola.siguiente = new ItemMenu("Aji de gallina", Categoria.PLATILLO, 26.00);
		cola = cola.siguiente;
		cola.siguiente = new ItemMenu("Pollo a la plancha", Categoria.PLATILLO, 23.00);
		cola = cola.siguiente;
		cola.siguiente = new ItemMenu("Tacu tacu", Categoria.PLATILLO, 25.00);
		cola = cola.siguiente;
		cola.siguiente = new ItemMenu("Tres leches", Categoria.POSTRE, 12.00);
		cola = cola.siguiente;
		cola.siguiente = new ItemMenu("Suspiro limeno", Categoria.POSTRE, 11.50);
		cola = cola.siguiente;
		cola.siguiente = new ItemMenu("Brownie con helado", Categoria.POSTRE, 14.00);
		cola = cola.siguiente;
		cola.siguiente = new ItemMenu("Cheesecake de fresa", Categoria.POSTRE, 13.50);
		cola = cola.siguiente;
		cola.siguiente = new ItemMenu("Limonada", Categoria.BEBIDA, 8.00);
		cola = cola.siguiente;
		cola.siguiente = new ItemMenu("Chicha morada", Categoria.BEBIDA, 7.50);
		cola = cola.siguiente;
		cola.siguiente = new ItemMenu("Gaseosa", Categoria.BEBIDA, 6.00);

		return cabeza;
	}

	// Enlaza un nodo al final de una lista cuyo inicio ya existe.
	private static ItemMenu enlazar(ItemMenu cabeza, ItemMenu cola, ItemMenu nuevo) {
		if (cabeza == null) {
			return nuevo;
		}
		cola.siguiente = nuevo;
		return cabeza;
	}

	// Obtiene el ultimo elemento de una lista de ItemMenu.
	private static ItemMenu obtenerUltimo(ItemMenu cabeza) {
		ItemMenu actual = cabeza;
		while (actual.siguiente != null) {
			actual = actual.siguiente;
		}
		return actual;
	}

	// Normaliza y valida estados permitidos del pedido.
	private String normalizarEstado(String estado) {
		if (ESTADO_PENDIENTE.equalsIgnoreCase(estado)) {
			return ESTADO_PENDIENTE;
		}
		if (ESTADO_EN_PREPARACION.equalsIgnoreCase(estado)) {
			return ESTADO_EN_PREPARACION;
		}
		if (ESTADO_ENTREGADO.equalsIgnoreCase(estado)) {
			return ESTADO_ENTREGADO;
		}
		throw new IllegalArgumentException("Estado de orden invalido: " + estado);
	}

	// Representacion textual general de la orden.
	@Override
	public String toString() {
		return "Producto{" +
				"numeroOrden=" + numeroOrden +
				", numeroMesa=" + numeroMesa +
				", itemsPedido=" + mostrarItemsPedido() +
				", estado='" + estado + '\'' +
				", total=S/" + calcularTotal() +
				'}';
	}
}
