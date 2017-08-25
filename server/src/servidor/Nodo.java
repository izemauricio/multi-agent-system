package servidor;

import java.io.Serializable;

public class Nodo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	Usuario usuario;
	Nodo proximo;
	Nodo anterior;
	
}
