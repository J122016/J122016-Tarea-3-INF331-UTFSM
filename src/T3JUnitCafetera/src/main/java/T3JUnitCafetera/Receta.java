/*TODO's: 
 * - make custom exceptions class. 
 *   - NumberBoundException for unidades de X
 *   - ParseRecetaException length + number format (?)
 * - Add maximum to handlers of setters
 * - hacer docs
*/

package T3JUnitCafetera;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Receta {
	
	// Creating a logger
	private static Logger logger = LogManager.getLogger(Receta.class);
	
	//Constants, temporal  possible improvement later
	private final static Integer MAX_UN_CAFE_RECETA = 10;
	private final static Integer MAX_UN_CHOCOLATE_RECETA = 10;
	private final static Integer MAX_UN_LECHE_RECETA = 80;
	private final static Integer MAX_UN_AZUCAR_RECETA = 40;
	
	//Attributes
	private String nombre;
	private Integer precio;
	private Integer unCafe;
	private Integer unChocolate;
	private Integer unLeche;
	private Integer unAzucar;

	// Default constructor, only initialize
	public Receta() {
		super();
		this.nombre = "Default recipe";
		this.precio = 0;
		this.unCafe = 0;
		this.unChocolate = 0;
		this.unLeche = 0;
		this.unAzucar = 0;
	}
	
	// Post "constructor", parse string & edit fields
	/**
	* Crea una receta a partir de un string con los elementos
	* <p>
	* Los elementos indicados en el string deben estas separados por coma en el siguiente orden
	* Nombre receta, precio café, unidades de café necesarias, unidades de chocolate necesarias,
	* unidades de leche necesarias y unidades de azucar necesarias para realizar la bebida.
	*
	* @param  tuplaReceta  String con los parametros necesarios para definir una bebida
	* @return      <code>true</code> si se realizo almaceno correctamente la bebida en la receta
	* @throws ArrayIndexOutOfBoundsException cuando el formato del string es invalido 
	* @throws Exception cuando algún item o precio para realizar el la receta esta fuera de los márgenes establecidos. 
	*/
	public boolean parseReceta(String tuplaReceta) throws Exception{

		//processing tuple values split & clean ( 0 , 1 , 2 , 3 , 4 , 5 )
		String[] listaReceta = tuplaReceta.replace("(","").replace(")","").split(",");
		try {
			String nombreReceta = listaReceta[0].trim();
			Integer precioReceta = Integer.parseInt(listaReceta[1].trim());
			Integer unCafeReceta = Integer.parseInt(listaReceta[2].trim());
			Integer unChocolateReceta = Integer.parseInt(listaReceta[3].trim());
			Integer unLecheReceta = Integer.parseInt(listaReceta[4].trim());
			Integer unAzucarReceta = Integer.parseInt(listaReceta[5].trim());
			
			setNombre(nombreReceta);
			setPrecio(precioReceta); 
			setUnCafe(unCafeReceta);
			setUnChocolate(unChocolateReceta);
			setUnLeche(unLecheReceta);
			setUnAzucar(unAzucarReceta);
			
		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("Receta invalida, faltan datos para completar. ej: (Expresso, 1000, 10, 0, 0, 5).");
			throw new Exception("Formato receta '" + tuplaReceta + "' invalido (faltan datos).");
			//return false;
			
		}catch(Exception e) { // 1:NumberFormatException + 2:limits, TODO: add custom limit
			System.out.println("Número invalido, para precios y unidades ingrese un número entero no negativo.\n>Detalle: " + e.getMessage());	
			throw new Exception("Formato receta '" + tuplaReceta + "' invalido, " + e);
			//return false;
		}
		
		return true;
	}
	


	//Getters & Setters
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public Integer getPrecio() {
		return precio;
	}
	public void setPrecio(Integer precio) throws Exception {
		if(precio < 0 ){
			throw new Exception("Precio de bebida '" + precio + "' negativo, límites [0,2147483647].");
		}
		this.precio = precio;
	}


	public Integer getUnCafe() {
		return unCafe;
	}
	public void setUnCafe(Integer unCafe) throws Exception {
		if(unCafe < 0 || unCafe > MAX_UN_CAFE_RECETA){
			throw new Exception("Unidades de café '" + unCafe + "' fuera de límites [0," + MAX_UN_CAFE_RECETA + "].");
		}
		this.unCafe = unCafe;
	}

	
	public Integer getUnChocolate() {
		return unChocolate;
	}
	public void setUnChocolate(Integer unChocolate) throws Exception {
		if(unChocolate < 0 || unChocolate > MAX_UN_CHOCOLATE_RECETA){
			throw new Exception("Unidades de chocolate '" + unChocolate + "' fuera de límites [0," + MAX_UN_CHOCOLATE_RECETA + "].");
		}
		this.unChocolate = unChocolate;
	}


	public Integer getUnLeche() {
		return unLeche;
	}
	public void setUnLeche(Integer unLeche) throws Exception {
		if(unLeche < 0 || unLeche > MAX_UN_LECHE_RECETA){
			throw new Exception("Unidades de leche '" + unLeche + "' fuera de límites [0," + MAX_UN_LECHE_RECETA + "].");
		}
		this.unLeche = unLeche;
	}


	public Integer getUnAzucar() {
		return unAzucar;
	}
	public void setUnAzucar(Integer unAzucar) throws Exception {
		if(unAzucar < 0 || unAzucar > MAX_UN_AZUCAR_RECETA){
			throw new Exception("Unidades de azúcar '" + unAzucar + "' fuera de límites [0," + MAX_UN_AZUCAR_RECETA + "].");
		}
		this.unAzucar = unAzucar;
	}
	
}
