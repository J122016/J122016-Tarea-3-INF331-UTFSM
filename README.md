# Tarea 3 INF331 UTFSM CC - Javier Torres
Tarea 3 para la asignatura INF331 (pruebas de software) dictada por la UTFSM el 2022-1. Simulador de máquina de café por comandos + pruebas en JUnit 	||	 **Versión : Inicial 1.0** 

## Descripción

La idea de este programa es la emulación de una máquina de café por línea de comandos, en grandes rasgos los elementos clave a conocer son:

- Cafetera desarrollada en `src/main/Cafetera.java` (a excepción de la lógica del menú principal que por claridad fue desarrollado en `src/main/Main.java`), que posee:
  - Inventario de café, chocolate, leche y azúcar inicialmente vacíos (con límites)
  -  Un recetario de 3 Recetas configuradas por usuario, que poseen precio y cantidades del inventario para su realización | `src/main/Receta.java`
- Acciones disponibles en el menú de la cafetera:
  - Agregar inventario
  - Verificar inventario
  - Comprar bebida
  - Apagar máquina

### Detalles extras

#### Enunciado

Los detalles del problema, tanto como requerimientos y reglas de negocio seguidas se encuentran en el archivo PDF `TCafetera.pdf` en la raíz del repositorio. Sin embargo se añadieron los siguientes supuestos, los cuales pueden ser modificados a futuro:

1. La máquina tiene caja infinita para entregar vueltos //igualmente se comenta una variable `caja` en `Cafetera.java` para añadir en caso de que esto cambie.
2. Si bien el documento indica los límites de la máquina no se indica que sean para las recetas o el inventario, así se supuso que son referentes a las recetas y que el inventario máximo serán 3 veces la de las recetas.

#### Pruebas

Junto a la simulación se realizo una suite de test en JUnit5 para probar las funciones y comprobar el cumplimiento de los requisitos, este se desarrolla en `src/test/CafeteraTest.java` -  *Code Coverage 68,7%* sin fallas actualmente.

Se incluye un log de una *prueba manual* (`Main.java`) donde fue aplicada *Code Coverage llegando a un 68,8%*, el log el mencionado encuentra en: `src/logs/EjemploFullManualMain_68.8_coverage.log`

#### Logs y manejo de errores

Todos los logs son realizados en las distintas clases, desde los de `INFO` hasta los errores internos manejados `WARN` y un `FATAL` en caso de un indeterminado*. Probablemente necesiten un gran refactor para estandarizarlos. 

*Actualmente los limites de máximos personalizados lanzan una excepción anónima que es manejada, a futuro en un refractor puede añadirse una clase extendida para no ser confundida con otros tipos y así no omitir otros errores.

## Instalación

Vía eclipse, instalar requerimientos con `Maven install`

### Para desarrollo

- Eclipse IDE u otro editor compatible.
- Maven para instalación de entorno compuesto por java [Java SE 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html),  [JUnit 5](https://junit.org/junit5/docs/current/user-guide/#writing-tests-assertions) y [Log4j2](https://logging.apache.org/log4j/2.x/index.html)
- (opcional) [EclEmma](https://www.eclemma.org/index.html) en Eclipse IDE si se desea realizar un Code Coverage

## Cómo usar

Compilar todos los archivos antes (por eclipse automático al correr un archivo).

- Para usar pruebas ejecutar `CafeteraTest.java`
- Para usar la cafetera manualmente ejecutar `Main.java`, se recomienda ver logs luego de utilizar esta opción.

## Cómo contribuir

Existen muchas mejoras e ideas para añadir si se desea continuar con el desarrollo, se agradece si lo considera generar:

1. Una [*issues*](https://github.com/J122016/J122016-Tarea-3-INF331-UTFSM/issues) para dar conocimiento de un problema/incidencia detectado o añadir mejoras.

2. Un [*pull request*](https://github.com/J122016/J122016-Tarea-3-INF331-UTFSM/pulls) desde su [*fork*](https://github.com/J122016/J122016-Tarea-3-INF331-UTFSM/fork) modificado para arreglar errores, typos, traducciones hasta optimizaciones o mejoras.

   PD: para aceptar este último, antes revisar que pase todos los test de `CafeteraTest.java` o sugerir un cambio a estos de ser necesario.

- Para consultas generales relacionadas no dude en contactar, mail: javier.torresr@sansano.usm.cl

## TODO

- Refactorizar código

## Licencia
> Construido por Javier Torres el 2022 como tarea 2 de INF233 (Pruebas de software) y lanzado para uso libre mediante MIT license, pudiendo utilizar el material con casi cualquier propósito (incluyendo el uso comercial), como condición se aprecia el crédito del material utilizado.
