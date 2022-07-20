/*TODO's: 
 * - refactor constructor?
 * - machine states, posible mejora - actualmente con strings
 * - Hacer docs
 * - crear clase de error perzonalizada.
*/

package T3JUnitCafetera;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Cafetera {

	// Creating a logger
	private static Logger logger = LogManager.getLogger(Cafetera.class);
		
	// === Constants ===
	private final static Integer MAX_RECETAS = 3;
	//Inventory = max_recipesx3)
	private final static Integer MAX_UN_CAFE_INVENTARIO = 30;
	private final static Integer MAX_UN_CHOCOLATE_INVENTARIO = 30;
	private final static Integer MAX_UN_LECHE_INVENTARIO = 240;
	private final static Integer MAX_UN_AZUCAR_INVENTARIO = 120;
	
	/*private enum estados {
	    SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
	    THURSDAY, FRIDAY, SATURDAY
	}*/
	
	// === variables ===
	private Integer invCafe;
	private Integer invLeche;
	private Integer invChocolate;
	private Integer invAzucar; 
	private String estado;
	private ArrayList<Receta> recetario = new ArrayList<Receta>(MAX_RECETAS);
	//private Integer caja; // no utilizado, se supone vuelto sin restricciones
	
	// ==== Default constructor ====
	public Cafetera() {
		super();
		this.invCafe = 0;
		this.invLeche = 0;
		this.invChocolate = 0;
		this.invAzucar = 0;
		this.estado = "En espera";
		this.recetario.add(new Receta());
		this.recetario.add(new Receta());
		this.recetario.add(new Receta());
		//this.caja = 0; // TODO no utilizada, si se implementa caja a futuro si
	}

	// ==== Getters & Setters ====
	public Integer getInvCafe() {
		return invCafe;
	}
	
	public void setInvCafe(Integer invCafe) throws Exception {
		if(invCafe < 0 || invCafe > MAX_UN_CAFE_INVENTARIO){
			throw new Exception("Unidades de café '" + invCafe + "' fuera de límites [0," + MAX_UN_CAFE_INVENTARIO + "].");
		}
		this.invCafe = invCafe;
	}
	
	public Integer getInvLeche() {
		return invLeche;
	}
	
	public void setInvLeche(Integer invLeche) throws Exception {
		if(invLeche < 0 || invLeche > MAX_UN_LECHE_INVENTARIO){
			throw new Exception("Unidades de leche '" + invLeche + "' fuera de límites [0," + MAX_UN_LECHE_INVENTARIO + "].");
		}
		this.invLeche = invLeche;
	}
	
	public Integer getInvChocolate() {
		return invChocolate;
	}
	
	public void setInvChocolate(Integer invChocolate) throws Exception {
		if(invChocolate < 0 || invChocolate > MAX_UN_CHOCOLATE_INVENTARIO){
			throw new Exception("Unidades de chocolate '" + invChocolate + "' fuera de límites [0," + MAX_UN_CHOCOLATE_INVENTARIO + "].");
		}
		this.invChocolate = invChocolate;
	}
	
	public Integer getInvAzucar() {
		return invAzucar;
	}
	
	public void setInvAzucar(Integer invAzucar) throws Exception {
		if(invAzucar < 0 || invAzucar > MAX_UN_AZUCAR_INVENTARIO){
			throw new Exception("Unidades de azucar " + invChocolate + "' fuera de límites [0," + MAX_UN_AZUCAR_INVENTARIO + "].");
		}
		this.invAzucar = invAzucar;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		//TODO: Validar número, o menu? - actual en hilo principal
		this.estado = estado;
	}
	
	public ArrayList<Receta> getRecetario() {
		return recetario;
	}
	
	public void setRecetario(ArrayList<Receta> recetario) {
		this.recetario = recetario;
	}
	
	/*Caja set y get (no utilizada actualmente)
	public Integer getCaja() {
		return caja;
	}
	
	public void setCaja(Integer caja) {
		this.caja = caja;
	}*/
	
	
	// ==== Other machine logic ====
	
	/**
	* Imprime el menú principal de la maquina de café, con las 4 opciones disponibles.
	*/
	public void menuEspera() {
		System.out.println("\n=== Modo espera - Menú principal ===");
		System.out.println("1 Agregar inventario");
		System.out.println("2 Verificar inventario");
		System.out.println("3 Comprar Bebida");
		System.out.println("4 Apagar");
	}

	/**
	* Realiza la secuencia de configuración inicial de una nueva máquina (constructor+),
	* escribe las <code>MAX_RECETAS</code> recetas iniciales. Maneja errores de entradas usuarias internamente.
	* <br>
	* TODO: possible refactor con Main (?)
	* 
	* @return      nueva Cafetera con 3 recetas añadidas por usuario
	*/
	public static Cafetera confInicial() throws Exception {
		// machine creation		
		Cafetera nuevaCafetera = new Cafetera();
		Integer recetaN = 0;
		
		// edit default recipes with user input
		while(recetaN < MAX_RECETAS) {
			System.out.println("\nConfiguración receta " + (recetaN+1));
			System.out.println("Ingrese receta con el siguiente formato: "
					+ "'(Nombre, precio, unidades café, unidades chocolate, unidades leche, unidades azúcar)'.");
			
			Scanner sc = new Scanner(System.in); // closed in Main otherwise not work in later inputs
			String tuplaReceta = "";
			
			try {
				boolean exito = false; //TODO NOW - revisar este bloque modificado 03:16 (por futuro test)
				while (!exito) {
					// input + parse & edit handle by Receta
					tuplaReceta = sc.nextLine();
					exito = nuevaCafetera.getRecetario().get(recetaN).parseReceta(tuplaReceta);//TODO (improvement):verificar que tenga nombre único antes o suponer que se escoge primero
					recetaN++;
				}
			} catch(Exception e) {
				logger.warn("Error manejado. Configuración de cafetera: " + e.getMessage());
	    	}
		}
		
		return nuevaCafetera;
	}

	/**
	* Añade a la máquina la unidades de inventario enviadas.
	* <p>
	* Las unidades deben ser enteras, no negativas con el límite establecido, si no lanza error descrito más abajo. 
	*
	* @param  tuplaCafeChocoLecheAzucar  <code>String</code> que corresponde a las cantidades ordenadas de los 
	* 								inventarios a añadir separadas por coma.
	* @return      <code>true</code> si la escritura fue realizada
	* @throws Exception si el parametro no cumple con el formato o alguna cantidad no puede ser transformada,
	*/
	public boolean AgregarInventario(String tuplaCafeChocoLecheAzucar) {
		
		String[] listaInv = tuplaCafeChocoLecheAzucar.replace("(","").replace(")","").split(",");
		try {
			Integer unCafe = Integer.parseInt(listaInv[0].trim());
			Integer unChocolate = Integer.parseInt(listaInv[1].trim());
			Integer unLeche = Integer.parseInt(listaInv[2].trim());
			Integer unAzucar = Integer.parseInt(listaInv[3].trim());
			
			Integer cafeTemp = this.getInvCafe();
			Integer chocoTemp = this.getInvChocolate();
			Integer lecheTemp = this.getInvLeche();
			Integer azucarTemp = this.getInvAzucar();
			
			//TODO mejorable función validadora en vez de escribir 2 veces		
			try {
				//first validation
				this.setInvCafe(unCafe);
				this.setInvChocolate(unChocolate);
				this.setInvLeche(unLeche);
				this.setInvAzucar(unAzucar);
				
				//real
				this.setInvCafe(cafeTemp + unCafe);
				this.setInvChocolate(chocoTemp + unChocolate);
				this.setInvLeche(lecheTemp + unLeche);
				this.setInvAzucar(azucarTemp + unAzucar);
				
			}catch (Exception e) {
				//Rollback & throw
				this.setInvCafe(cafeTemp);
				this.setInvChocolate(chocoTemp);
				this.setInvLeche(lecheTemp);
				this.setInvAzucar(azucarTemp);
				throw(e);
			}
			
		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("Inventario invalido, faltan datos para completar. ej: (10, 5, 4, 7).");
			logger.warn("Error manejado, Formato receta '" + tuplaCafeChocoLecheAzucar + "' invalido (faltan datos).");
			return false;
			
		}catch(Exception e) { // 1:NumberFormatException + 2:limits, TODO: add custom limit
			System.out.println("Número invalido, para precios y unidades ingrese un número entero no negativo.\n>Detalle: " + e.getMessage());	
			logger.warn("Error manejado, Formato receta '" + tuplaCafeChocoLecheAzucar + "' invalido, " + e);
			return false;
		}
		
		return true;
	}
	
	/**
	* Retorna el inventario actual presente en la maquina como un mapeo de Strings:Integer 
	* <p>
	* Las llaves del mapeo corresponden a el elemento el inventario en mayuscula, 
	* cada uno con sus respectivas cantidades. Para la obtención usar método . get("llave"); 
	* con llave correspondiente a al inventario a obtener 
	*
	* @return	Mapeo del inventario con llaves: <i>CAFE</i>, <i>CHOCOLATE</i>, <i>LECHE</i> y <i>AZUCAR</i>
	*/
	public Map<String, Integer> getInventario(){
		Map<String, Integer> inv = new HashMap<String, Integer>();
		inv.put("CAFE", this.getInvCafe());
		inv.put("CHOCOLATE", this.getInvChocolate());
		inv.put("LECHE", this.getInvLeche());
		inv.put("AZUCAR", this.getInvAzucar());
		
		return inv;
	}

	/**
	* Realiza la transacción de la compra de una bebida seleccionada acompaña del dinero, 
	* retorna un mapeo con información referente a esta.  
	* <p>
	* Esta funcion retorna un mapeo de elementos que se obtienen con .get("..."):<br>
	* - <code>boolean</code> <b>Error</b>: si existieron errores en la ejecución es true, 
	* 					no debería ya que son lanzados. TODO:refactor <br>
	* - <code>boolean</code> <b>POSIBLE</b>: si la transacción es posible es true, es decir existe 
	* 					inventario suficiente para la receta y el dinero ingresado es suficiente.<br>
	* - <code>String</code> <b>MSJE</b>: detalle de resultado, ya sea error de formato, producto, 
	* 					inventario, dinero o en caso de completarse la transacción bebida realizada.<br>
	* - <code>Integer</code> <b>VUELTO</b>: monto a devolver tanto si la transacción es realizada o no. 
	*
	* @param  tuplaBebidaDinero  String que contiene la bebida y el dinero(Integer) separados por coma. Ej: (bebida, dinero)
	* @return Un mapeo de llaves string: ERROR->boolean, POSIBLE->boolean, MSJE->String y VUELTO->integer
	* @throws Exception  Si el string no cumple con el formato, ya sea por falta de datos o el dinero no es esta entre [0, 2147483647]. 
	* 					Para conocer motivo .getMessage()
	*/
	public Map<String, Object> realizarOrden(String tuplaBebidaDinero) throws Exception {
		
		String[] listatransaccion = tuplaBebidaDinero.replace("(","").replace(")","").split(",");
		Map<String, Object> transaccion = new HashMap<String, Object>(); // almost new class Transaccion, but no (¿TODO refactor new class?)
		logger.info("Entrada", tuplaBebidaDinero);
		
		//default values
		transaccion.put("ERROR", true); // flag to verify recipe exist 
		transaccion.put("POSIBLE", false);
		transaccion.put("MSJE", "No se encuentra producto, intente nuevamente");
		transaccion.put("VUELTO", 0);
		
		try {
			String bebidaElegida = listatransaccion[0].trim();
			Integer dinero = Integer.parseInt(listatransaccion[1].trim());
			transaccion.put("VUELTO", dinero);
			
			//revision existencia de bebida, inventario y precio, no se revisa vuelto debido a supocición de caja infinita
			ArrayList<Receta> recetas = this.getRecetario();
			for (int bebida = 0; bebida < recetas.size(); bebida++) {
				
        		if (recetas.get(bebida).getNombre().equals(bebidaElegida)){
        			transaccion.put("ERROR", false);
        			
        			if (recetas.get(bebida).getUnCafe() <= this.getInvCafe() &&
        				recetas.get(bebida).getUnChocolate() <= this.getInvChocolate() &&
        				recetas.get(bebida).getUnLeche() <= this.getInvLeche() &&
        				recetas.get(bebida).getUnAzucar() <= this.getInvAzucar()){
        				
	        			if(recetas.get(bebida).getPrecio() <= dinero){
		        			transaccion.put("POSIBLE", true);
		        			transaccion.put("VUELTO", dinero - recetas.get(bebida).getPrecio());
		        			transaccion.put("MSJE", "Su bebida " + recetas.get(bebida).getNombre() + " esta lista.");
		        			//set new inventory
		        			this.setInvCafe(this.getInvCafe()-recetas.get(bebida).getUnCafe());
		        			this.setInvChocolate(this.getInvChocolate()-recetas.get(bebida).getUnChocolate());
		        			this.setInvLeche(this.getInvLeche()-recetas.get(bebida).getUnLeche());
		        			this.setInvAzucar(this.getInvAzucar()-recetas.get(bebida).getUnAzucar());
		        			return transaccion;
		        		}else{
		        			transaccion.put("MSJE", "Dinero insuficiente.");
		        			return transaccion;
		        		}
        			}else{
        				transaccion.put("MSJE", "Inventario insuficiente.");
        				return transaccion;
        			}
        		}
        	}
			
			//Si se llega aquí, no se encuentra receta seleccionada
			throw new Exception("Error manejado, Receta no encontrada '" + tuplaBebidaDinero + "'.");
			
		} catch (ArrayIndexOutOfBoundsException e) {
			transaccion.put("ERROR", true); // unused now, just formality TODO: refactor
			transaccion.put("MSJE", "Error de formato.");
			System.out.println("Datos incompletos de receta y/o dinero.");
			throw new Exception("Error manejado, Formato compra '" + tuplaBebidaDinero + "' invalido (faltan datos).");
			
		}catch(NumberFormatException e) {
			System.out.println("Dinero invalido, ingrese un número entero no negativo.");	
			throw new Exception("Error manejado, Formato receta '" + tuplaBebidaDinero + "' invalido, " + e);
		}
	}
	
}
