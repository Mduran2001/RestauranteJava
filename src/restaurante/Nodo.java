package restaurante;

// Nodo generico reutilizable para estructuras enlazadas y arboles.
public class Nodo<T> {
	private T dato;
	private Nodo<T> izquierdo;
	private Nodo<T> derecho;

	// Constructor con el dato principal del nodo.
	public Nodo(T dato) {
		this.dato = dato;
	}

	public T getDato() {
		return dato;
	}

	public void setDato(T dato) {
		this.dato = dato;
	}

	public Nodo<T> getIzquierdo() {
		return izquierdo;
	}

	public void setIzquierdo(Nodo<T> izquierdo) {
		this.izquierdo = izquierdo;
	}

	public Nodo<T> getDerecho() {
		return derecho;
	}

	public void setDerecho(Nodo<T> derecho) {
		this.derecho = derecho;
	}
}
