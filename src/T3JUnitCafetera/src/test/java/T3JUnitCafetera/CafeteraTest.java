package T3JUnitCafetera;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CafeteraTest {
	
	private Cafetera miCafetera;

	@BeforeEach
	void setUpMaquina() throws Exception {
		miCafetera = new Cafetera();
		
		//creación de recetas (correctas - se testea otras para REQ5 en: testParseRecetas)
		miCafetera.getRecetario().get(0).parseReceta("(Expresso, 2000, 10, 0, 0, 2)");
		miCafetera.getRecetario().get(1).parseReceta("(Café con leche, 1000, 5, 0, 5, 1)");
		miCafetera.getRecetario().get(2).parseReceta("(Chocolate caliente, 700, 0, 8, 5, 0)");
	}
	
	// === requerimientos principales ===
	
	// REQ 1
	@Test
	void testInicioEspera() {
		//Dada una cafetera
		//Cuando recien este creada (en @BeforeEach)
		//Entonces, verificar:
		assertEquals("En espera", miCafetera.getEstado());
		assertEquals(0, miCafetera.getInvAzucar());
		assertEquals(0, miCafetera.getInvLeche());
		assertEquals(0, miCafetera.getInvCafe());
		assertEquals(0, miCafetera.getInvChocolate());
		
		//además posterior a cuando se de ejecuta un submenu, entonces debe volver al estado de espera
		try {miCafetera.AgregarInventario("1,-3,0,36");} catch (Exception e) {}
		assertEquals("En espera", miCafetera.getEstado());
		
		try {miCafetera.realizarOrden("10,10,10,10");} catch(Exception e){}
		assertEquals("En espera", miCafetera.getEstado());
		
		miCafetera.getInventario();
		assertEquals("En espera", miCafetera.getEstado());
		
		//mostrar menú no se testea debido a que es solo una impresión por pantalla
	}
	
	// REQ 2: Agregar inventario y 3: Verificar inventario
	@Test
	void testAnnadirInventario() {
		// Dada una maquina cualquiera, (desde @beforeEach recien iniciada)
		
		// cuando se agrega al inventario actual nuevas unidades
		assertTrue(miCafetera.AgregarInventario("(10,10,10,1)")); 		  // maximos:30, 30, 240, 120
		assertFalse(miCafetera.AgregarInventario("(10,-4,3,9)")); 		  //negativo
		assertFalse(miCafetera.AgregarInventario("(10,4,7,2147483648)")); // Integer fuera de transformación
		assertTrue(miCafetera.AgregarInventario("(20,20,230,119)")); 	  //restante para llegar a máximos
		assertTrue(miCafetera.AgregarInventario("(0,0,0,0)")); 			  //suma cero
		
		// entonces estas se debén escribir y actualizar correctamente
		assertEquals(30, miCafetera.getInvCafe());
		assertEquals(30, miCafetera.getInvChocolate());
		assertEquals(240, miCafetera.getInvLeche());
		assertEquals(120, miCafetera.getInvAzucar());
	}
	
	
	//REQ 4: comprar bebidas
	@Nested
    @DisplayName("Tests para la compra de bebida")
    class CompraDeBebida {
		
		@Nested
        @DisplayName("Flujo normal")
        class CuandoEsNormal {
    		@Test
            void testRealizarOrdenCorrecta() throws Exception {
    			//Dada una cafetera (iniciada en beforeEach) con inventario suficiente
    			assertTrue(miCafetera.AgregarInventario("20,20,20,4")); //suponiendo que pasa su test
    			
    			//Cuando se compra una bebida existente con dinero de sobra e inventario normal 
    			Map<String, Object> resultado = miCafetera.realizarOrden("Expresso,5000");
    			
    			//Entonces verificar
    			 assertEquals(3000, resultado.get("VUELTO")); //5000-2000
    			 assertEquals("Su bebida Expresso esta lista.", resultado.get("MSJE")); //formato estricto por requerimiento
    			 
    			 Map<String, Integer> inventario = miCafetera.getInventario();
    			 assertEquals(10, inventario.get("CAFE")); 		//20-10
    			 assertEquals(20, inventario.get("CHOCOLATE")); //se debe mantener
    			 assertEquals(20, inventario.get("LECHE")); 	//se debe mantener
    			 assertEquals(2, inventario.get("AZUCAR")); 	//4-2
    			 
    			 //Cuando se compra con el dinero e inventario justo (caso borde)
    			 resultado = miCafetera.realizarOrden("Expresso,2000");
    			 
    			//Entonces verificar
    			 assertEquals(0, resultado.get("VUELTO"));
    			 assertEquals("Su bebida Expresso esta lista.", resultado.get("MSJE"));
    			 
    			 inventario = miCafetera.getInventario();
    			 assertEquals(0, inventario.get("CAFE")); 		//10-10
    			 assertEquals(20, inventario.get("CHOCOLATE")); //se debe mantener
    			 assertEquals(20, inventario.get("LECHE")); 	//se debe mantener
    			 assertEquals(0, inventario.get("AZUCAR")); 	//2-2
    		}
        }
    	
    	@Nested
        @DisplayName("cuando existe insuficiencia de elementos")
        class CuandoErroresInternos {
    		@Test
            void testRealizarOrdenSinInventario() throws Exception {
    			//Dada una cafetera (iniciada en beforeEach) con inventario NO suficiente
    			assertTrue(miCafetera.AgregarInventario("9,20,20,0")); //requerido min: 10 cafe, 2 azucar
    			
    			//Cuando se compra una bebida existente con dinero de sobra e inventario normal 
    			Map<String, Object> resultado = miCafetera.realizarOrden("Expresso,5000");
    			
    			//Entonces verificar
    			 assertEquals(resultado.get("VUELTO"), 5000); //se devuelve todo
    			 assertNotEquals(resultado.get("MSJE"), "Su bebida Expresso esta lista"); //NOT equals
    			 
    			 Map<String, Integer> inventario = miCafetera.getInventario(); //se debe mantener
    			 assertEquals(inventario.get("CAFE"), 9);
    			 assertEquals(inventario.get("CHOCOLATE"), 20); 
    			 assertEquals(inventario.get("LECHE"), 20);	
    			 assertEquals(inventario.get("AZUCAR"), 0); 
    		}
    		
    		@Test
            void testRealizarOrdenSinDinero() throws Exception {
    			//Dada una cafetera (iniciada en beforeEach) con inventario NO suficiente
    			assertTrue(miCafetera.AgregarInventario("9,20,20,0")); //requerido min: 10 cafe, 2 azucar
    			
    			//Cuando se compra una bebida existente con dinero de sobra e inventario normal 
    			Map<String, Object> resultado = miCafetera.realizarOrden("Chocolate caliente,70");
    			
    			//Entonces verificar
    			 assertEquals(resultado.get("VUELTO"), 70); //300
    			 assertNotEquals(resultado.get("MSJE"), "Su bebida Chocolate caliente esta lista");
    			 
    			 Map<String, Integer> inventario = miCafetera.getInventario(); //se debe mantener
    			 assertEquals(inventario.get("CAFE"), 9);
    			 assertEquals(inventario.get("CHOCOLATE"), 20); 
    			 assertEquals(inventario.get("LECHE"), 20);	
    			 assertEquals(inventario.get("AZUCAR"), 0); 
    		}
        }
    	
    	// TODO: Extras
    	@Disabled("test extras omitidos por ahora, debido a enfoque en test principales")
    	@Nested
        @DisplayName("Errores de formato")
        class CuandoIngresaMalUsuario {
    		@Test
            void findWith_when_X() throws Exception {
    			fail("Not yet implemented");
    		}
        }
    }
	
	
	// REQ 5: configuración inicial, similar a BeforeEach
	@Test
	void testParseReceta() {
		//conf inicial interactua con usuario, simulando interior, 
		//TODO refactorizar .confInicial() ante imposibilidad de uso de input en pruebas
		
		//Dada una cafetera cualquiera
		miCafetera = new Cafetera();
		
		//cuando se realiza su configuración inicial (configuración de recetas)
		try {assertTrue(miCafetera.getRecetario().get(0).parseReceta("(Expresso, 2000, 10, 0, 8, 2)"));} catch (Exception e) {} //correcto
		
		Exception exception = assertThrows(Exception.class, () -> //formato mal realizado - invalido
		miCafetera.getRecetario().get(1).parseReceta("(piña colada, 1000 10, 0, 0, 2)"));
		assertTrue(exception.getMessage().contains("invalido")); //formato mal realizado - invalido
		
		exception = assertThrows(Exception.class, () -> //precio negativo - invalido
		miCafetera.getRecetario().get(1).parseReceta("(Café con leche, -1000, 5, 0, 5, 1)"));
		assertTrue(exception.getMessage().contains("invalido")); //precio negativo - invalido
		
		exception = assertThrows(Exception.class, () -> //ingrediente fuera de límite -invalido
		miCafetera.getRecetario().get(1).parseReceta("(Chocolate caliente, 700, 11, 8, 5, 0)"));
		assertTrue(exception.getMessage().contains("invalido")); //ingrediente fuera de límite -invalido
		
		
		//entonces estan serán guardadas correctamente
		assertEquals("Expresso",miCafetera.getRecetario().get(0).getNombre());
		assertEquals(2000,miCafetera.getRecetario().get(0).getPrecio());
		assertEquals(10,miCafetera.getRecetario().get(0).getUnCafe());
		assertEquals(0,miCafetera.getRecetario().get(0).getUnChocolate());
		assertEquals(8,miCafetera.getRecetario().get(0).getUnLeche());
		assertEquals(2,miCafetera.getRecetario().get(0).getUnAzucar());
		
		//entonces no guardará valores invalidos
		assertNotEquals("piña colada",miCafetera.getRecetario().get(1).getNombre()); //no cambia (esta bien)
		assertNotEquals(1000,miCafetera.getRecetario().get(1).getPrecio());			 //no cambia
		assertNotEquals(10,miCafetera.getRecetario().get(1).getUnCafe());			 //no cambia
		assertEquals(0,miCafetera.getRecetario().get(1).getUnChocolate());			 //Valor por defecto, no cambia (esta bien)
		assertEquals(0,miCafetera.getRecetario().get(1).getUnLeche());				 //Valor por defecto, no cambia
		assertNotEquals(2,miCafetera.getRecetario().get(1).getUnAzucar());			 //no cambia
	}
}
