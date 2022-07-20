/*TODO's: 
 * - Add logs info
*/

package T3JUnitCafetera;

import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main{
	
	// Creating a logger
	private static Logger logger = LogManager.getLogger(Main.class);
	
	
	public static void main(String[] args) {
    	
    	// initialization
		Scanner sc = new Scanner(System.in); //System.in is a standard input stream
		
		try {
			
			logger.info("Iniciando cafetera.");
			Cafetera miCafetera = Cafetera.confInicial(); //TODO do own try and let other out
			logger.info("Cafetera iniciada");
	    	
	    	// principal loop XOR
	    	while(!(miCafetera.getEstado().equals("Apagar") ^ miCafetera.getEstado().equals("4"))) {
	    		
		    	switch (miCafetera.getEstado()) {
			    	case "Agregar inventario":
			    	case "1":
			    		System.out.println("Ha ingresado: 1. Agregar inventario.");
			    		logger.info("Agregar inventario");
			    		boolean exito = false; // TODO improvement exito as (miCafetera.estado != "Error", set when catch inside class or instead return, miCafetera.estado  = "En espera")
			    		while (exito == false) {
			    			System.out.println("Ingrese cantidades a añadir en invantario: '(#cafe, #chocolate, #leche, #azúcar)'");
			    			exito = miCafetera.AgregarInventario(sc.nextLine());
			    		}
			    		miCafetera.setEstado("En espera"); // no es necesario, pero por formalidad (ver si poner dentro de funciones, por tests)
			    		System.out.println("Inventario agregado exitosamente!"); //need verify but technically yes
			    		break;
			            
			        case "Verificar inventario":
			        case "2":
			        	System.out.println("Ha ingresado: 2. Verificar inventario máquina");
			        	logger.info("Verificar inventario");
			        	Map<String, Integer> inventario = miCafetera.getInventario(); //f to test, otherwise more structured verify all unique strings (include for in f to join)
			        	for (Map.Entry<String, Integer> insumo : inventario.entrySet()) {
			        		System.out.println(insumo.getKey() + ": " + insumo.getValue());
			        		logger.info("Inventario actual máquina " + insumo.getKey() + ": " + insumo.getValue());
			        	}
			        	miCafetera.setEstado("En espera"); // no es necesario, pero por formalidad (ver si poner dentro de funciones, por tests)
			        	break;
			            
			        case "Comprar bebida":
			        case "3":
			        	System.out.println("Ha ingresado: 3. Comprar Bebida.");
			        	logger.info("Comprar bebida.");
			        	
			        	ArrayList<Receta> recetas = miCafetera.getRecetario();
			        	exito = false; // TODO improvement(?) exito var as miCafetera.estado
			    		while (exito == false) {
			    			
			    			//listar bebidas estilo: N°| #CAFE .... | precio | nombre
			    			System.out.println("Recetario\nN°| #Café #Chocolate #Leche #Azucar | $Precio      | Nombre");
				        	for (int bebida = 0; bebida < recetas.size(); bebida++) {
				        		System.out.printf("%d | %3s %8s %8s %6s    | $%-11d | %s\n",
				        						(bebida+1), recetas.get(bebida).getUnCafe(), 
				        						recetas.get(bebida).getUnChocolate(),
				        						recetas.get(bebida).getUnLeche(),
				        						recetas.get(bebida).getUnAzucar(), 
				        						recetas.get(bebida).getPrecio(),
				        						recetas.get(bebida).getNombre()
				        						);
				        	}
				        	
			    			System.out.println("Seleccione una e ingrese su dinero separado por coma. Ej: (" + miCafetera.getRecetario().get(0).getNombre() + "," + miCafetera.getRecetario().get(0).getPrecio() + ").");
			    			
			    			try {
			    				//intentar seleccionar y comprar bebida + dinero (input separados por "(,)" por facilidad)
			    				Map<String, Object> transaccion = miCafetera.realizarOrden(sc.nextLine()); //.get("ERROR"->bool||"POSIBLE"->bool||"MSJE"->str||"VUELTO"->int)
			    				exito = !(boolean) transaccion.get("ERROR"); //format & number caught but existence of recipe catch here
			    				System.out.println("> Transaccion " + ((boolean)transaccion.get("POSIBLE")? "completa" : "rechazada") + ":"); //TODO: refactor, tal vez es innecesaria variable "POSIBLE", only for test
			    				System.out.println((String) transaccion.get("MSJE"));
		    					System.out.println("Vuelto: " + transaccion.get("VUELTO").toString());
		    					logger.info("Transacción exitosa. Detalles-> Mensaje:" + transaccion.get("MSJE").toString() + "Vuelto: " + transaccion.get("VUELTO").toString());
		    					
			    			}catch(Exception e ){
			    				logger.warn(e);
			    				continue;
			    			}
			    		}
			    		miCafetera.setEstado("En espera");//formalidad
			        	break;
			            
			        default:
			        	logger.warn("Opción inválida de menú: " + miCafetera.getEstado()); //aplica a estado: En espera, revisar.
			            break;
		    	}
		    	
		    	//print menu principal
		    	logger.info("Menu principal, Es espera.");
		    	miCafetera.menuEspera();
		    	miCafetera.setEstado(sc.nextLine().trim());
	    	}
	    	
		} catch(Exception e) {
			logger.fatal(e);
			System.out.println("Unexpected Fatal error: " + e.getMessage());
    		
    	} finally {
            sc.close();
        }
   	}
}