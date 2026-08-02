package restaurante;

// Representa al empleado responsable en turno.
public class Empleado {
	private String nombre;

	public Empleado() {
	}

	// Constructor con validacion inmediata de nombre.
	public Empleado(String nombre) {
		this.nombre = normalizarNombre(nombre);
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = normalizarNombre(nombre);
	}

	// Limpia espacios y valida que el nombre no sea vacio.
	private String normalizarNombre(String nombre) {
		if (nombre == null || nombre.isBlank()) {
			throw new IllegalArgumentException("El nombre del empleado es obligatorio.");
		}
		return nombre.trim();
	}

	// Representacion textual del empleado.
	@Override
	public String toString() {
		return "Empleado{" +
				"nombre='" + nombre + '\'' +
				'}';
	}
}
