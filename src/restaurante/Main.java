package restaurante;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.swing.JOptionPane;

// Clase principal del sistema de restaurante con interfaz basada en JOptionPane.
public class Main {
	// Nodo simple para enlazar pedidos registrados durante la ejecucion.
	private static class NodoOrden {
		private final Producto dato;
		private NodoOrden siguiente;

		private NodoOrden(Producto dato) {
			this.dato = dato;
		}
	}

	// Registro enlazado de pedidos sin usar colecciones de Java.
	private static class RegistroOrdenes {
		private NodoOrden cabeza;
		private NodoOrden cola;

		// Inserta un pedido al final de la lista.
		private void agregar(Producto orden) {
			NodoOrden nuevo = new NodoOrden(orden);
			if (cabeza == null) {
				cabeza = nuevo;
				cola = nuevo;
				return;
			}
			cola.siguiente = nuevo;
			cola = nuevo;
		}

		// Busca un pedido por su numero de orden.
		private Producto buscarPorNumero(int numeroOrden) {
			NodoOrden actual = cabeza;
			while (actual != null) {
				if (actual.dato.getNumeroOrden() == numeroOrden) {
					return actual.dato;
				}
				actual = actual.siguiente;
			}
			return null;
		}

		// Genera un texto con todos los pedidos registrados.
		private String mostrar() {
			if (cabeza == null) {
				return "No hay pedidos registrados.";
			}
			StringBuilder texto = new StringBuilder();
			NodoOrden actual = cabeza;
			while (actual != null) {
				texto.append(actual.dato).append("\n\n");
				actual = actual.siguiente;
			}
			return texto.toString();
		}
	}

	// Punto de entrada de la aplicacion.
	public static void main(String[] args) {
		Reserva reserva = new Reserva();

		Clientes clientes = new Clientes();
		Empleado empleado = new Empleado();
		DetallePedido detallePedido = new DetallePedido();
		RegistroOrdenes ordenes = new RegistroOrdenes();

		int siguienteNumeroOrden = 1;
		int siguienteNumeroFactura = 1;

		// Bucle principal del menu.
		boolean continuar = true;
		while (continuar) {
			// Menu principal mostrado al usuario.
			String menu = """
					===== SISTEMA RESTAURANTE =====
					1. Registrar empleado en turno
					2. Registrar mesa
					3. Ver mesas registradas
					4. Registrar reserva
					5. Registrar cliente
					6. Ver menu
					7. Registrar pedido
					8. Ver pedidos
					9. Registrar devolucion/cancelacion
					10. Ver incidencias
					11. Imprimir factura por pedido
					12. Ver reservas
					13. Ver clientes
					0. Salir

					Seleccione una opcion:
					""";

			// Captura de opcion del usuario.
			String entrada = JOptionPane.showInputDialog(null, menu, "Menu principal", JOptionPane.QUESTION_MESSAGE);
			if (entrada == null) {
				break;
			}

			try {
				int opcion = Integer.parseInt(entrada.trim());
				// Despacha la accion segun opcion del menu.
					switch (opcion) {
					case 1 -> registrarEmpleado(empleado);
						case 2 -> registrarMesa(reserva);
						case 3 -> mostrarTexto(
								"Mesas registradas",
								reserva.mostrarMesasEnOrden() + "\n" + reserva.resumenEstadoMesas());
					case 4 -> registrarReserva(reserva);
					case 5 -> registrarCliente(clientes, reserva);
					case 6 -> mostrarTexto("Menu", Producto.mostrarMenu());
					case 7 -> {
						Producto orden = registrarPedido(reserva, siguienteNumeroOrden);
						siguienteNumeroOrden++;
						ordenes.agregar(orden);
						Mesa mesa = reserva.buscarMesa(orden.getNumeroMesa());
						if (mesa != null) {
							mesa.setEstado(Mesa.ESTADO_OCUPADA);
							mesa.setPedidoActual("Orden #" + orden.getNumeroOrden());
						}
						mostrarTexto("Pedido", "Pedido registrado correctamente.\n" + orden);
					}
					case 8 -> mostrarTexto("Pedidos", ordenes.mostrar());
					case 9 -> registrarIncidencia(detallePedido, empleado);
					case 10 -> mostrarTexto("Incidencias", detallePedido.mostrarIncidencias());
					case 11 -> {
						int numeroPedido = pedirEntero("Ingrese numero de pedido para facturar:");
						Producto pedido = ordenes.buscarPorNumero(numeroPedido);
						if (pedido == null) {
							throw new IllegalArgumentException("No existe un pedido con ese numero.");
						}
						Factura factura = new Factura(siguienteNumeroFactura++, pedido);
						mostrarTexto("Factura", factura.generarDetalleFactura());
					}
					case 12 -> mostrarTexto(
							"Reservas",
							"Inicio a fin:\n" + reserva.mostrarReservasDesdeInicio()
									+ "\nFin a inicio:\n" + reserva.mostrarReservasDesdeFinal());
					case 13 -> mostrarTexto("Clientes", clientes.mostrarClientes());
					case 0 -> continuar = false;
					default -> throw new IllegalArgumentException("Opcion no valida.");
				}
			} catch (RuntimeException e) {
				// Manejo unificado de errores para entradas invalidas o reglas de negocio.
				mostrarTexto("Error", e.getMessage());
			}
		}
	}

	// Registra una mesa solicitando sus datos basicos.
	private static void registrarMesa(Reserva reserva) {
		int numeroMesa = pedirEntero("Ingrese numero de mesa:");
		String estadoMesa = pedirTexto("Ingrese estado de la mesa: Libre, Ocupada o Reservada");
		String pedidoActual = pedirTexto("Ingrese pedido actual (si no tiene, escriba Sin pedido)");

		reserva.registrarMesa(new Mesa(numeroMesa, estadoMesa, pedidoActual));
		mostrarTexto("Mesa", "Mesa registrada correctamente.");
	}

	// Registra el empleado que atiende en turno.
	private static void registrarEmpleado(Empleado empleado) {
		String nombre = pedirTexto("Ingrese el nombre del empleado que va a trabajar:");
		empleado.setNombre(nombre);
		mostrarTexto("Empleado", "Empleado registrado: " + empleado.getNombre());
	}

	// Registra una reserva validando mesa disponible y formato de fecha.
	private static void registrarReserva(Reserva reserva) {
		String nombreCliente = pedirTexto("Ingrese nombre del cliente:");
		String fechaTexto = pedirTexto("Ingrese fecha y hora de reserva (AAAA-MM-DDTHH:mm):");
		LocalDateTime fechaHora = LocalDateTime.parse(fechaTexto);
		int numeroMesa = pedirEntero("Ingrese numero de mesa para reservar:");

		Reserva.ReservaRegistro registro = reserva.registrarReserva(nombreCliente, fechaHora, numeroMesa);
		mostrarTexto("Reserva", "Reserva registrada:\n" + registro);
	}

	// Registra un cliente con hora de ingreso, estado y mesa asignada.
	private static void registrarCliente(Clientes clientes, Reserva reserva) {
		String nombre = pedirTexto("Ingrese nombre del cliente:");
		int cantidad = pedirEntero("Ingrese cantidad de personas (maximo 4):");
		int numeroMesa = pedirEntero("Ingrese numero de mesa:");
		String horaTexto = pedirTexto("Ingrese hora de ingreso (HH:mm):");
		LocalTime horaIngreso = LocalTime.parse(horaTexto);
		String estado = pedirTexto("Ingrese estado del cliente: Esperando, Atendido o Pagado");

		Clientes.ClienteRegistro registro = clientes.registrarCliente(
				nombre,
				cantidad,
				numeroMesa,
				horaIngreso,
				estado,
				reserva);
		mostrarTexto("Cliente", "Cliente registrado:\n" + registro);
	}

	// Registra un pedido para una mesa y agrega productos del menu hasta recibir FIN.
	private static Producto registrarPedido(Reserva reserva, int numeroOrden) {
		int numeroMesa = pedirEntero("Ingrese numero de mesa:");
		Mesa mesa = reserva.buscarMesa(numeroMesa);
		if (mesa == null) {
			throw new IllegalArgumentException("No existe la mesa indicada.");
		}

		String estado = pedirTexto("Ingrese estado del pedido: Pendiente, En preparacion o Entregado");
		Producto orden = new Producto(numeroOrden, numeroMesa, estado);

		mostrarTexto("Menu disponible", Producto.mostrarMenu());
		while (true) {
			String nombreItem = pedirTexto("Ingrese un producto del menu (o FIN para terminar):");
			if ("FIN".equalsIgnoreCase(nombreItem)) {
				break;
			}
			orden.agregarItemAlPedido(nombreItem);
		}

		if (!orden.tieneItems()) {
			throw new IllegalArgumentException("Debe agregar al menos un producto al pedido.");
		}

		return orden;
	}

	// Registra una incidencia en la pila de detalle de pedidos.
	private static void registrarIncidencia(DetallePedido detallePedido, Empleado empleado) {
		String pedido = pedirTexto("Ingrese pedido afectado:");
		String motivo = pedirTexto("Ingrese motivo (devolucion o cancelacion):");
		String fechaTexto = pedirTexto("Ingrese fecha y hora (AAAA-MM-DDTHH:mm):");
		LocalDateTime fecha = LocalDateTime.parse(fechaTexto);

		String responsable;
		if (empleado.getNombre() == null || empleado.getNombre().isBlank()) {
			responsable = pedirTexto("Ingrese responsable:");
		} else {
			responsable = empleado.getNombre();
		}

		detallePedido.ingresarIncidencia(pedido, motivo, fecha, responsable);
		mostrarTexto("Incidencia", "Incidencia registrada correctamente.");
	}

	// Solicita un numero entero desde un cuadro de texto.
	private static int pedirEntero(String mensaje) {
		String valor = pedirTexto(mensaje);
		return Integer.parseInt(valor.trim());
	}

	// Solicita texto general al usuario.
	private static String pedirTexto(String mensaje) {
		String valor = JOptionPane.showInputDialog(null, mensaje, "Sistema restaurante", JOptionPane.QUESTION_MESSAGE);
		if (valor == null) {
			throw new IllegalArgumentException("Operacion cancelada por el usuario.");
		}
		return valor.trim();
	}

	// Muestra mensajes informativos en cuadro de dialogo.
	private static void mostrarTexto(String titulo, String contenido) {
		JOptionPane.showMessageDialog(null, contenido, titulo, JOptionPane.INFORMATION_MESSAGE);
	}
}
