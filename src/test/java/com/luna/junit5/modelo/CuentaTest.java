package com.luna.junit5.modelo;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.luna.junit5.exceptions.DineroException;

/**
 * Test Junit 5 solo se puede desde photon en adelante
 * @author Hector
 *
 */
public class CuentaTest {
	
	/**
	 * Variable cuenta a nivel de clase
	 */
	 Cuenta cuenta;
	 
	 private TestInfo testInfo;
	 
	 private TestReporter testReporter;
	
	/**
	 * Se ejecuta antes de cada test
	 */
	@BeforeEach
	void initMetodoTest(TestInfo testInfo,TestReporter testReporter) {
		this.cuenta= new Cuenta("Erick",new BigDecimal("12.22"));
		this.testInfo= testInfo;
		this.testReporter= testReporter;
		System.out.println("Iniciando el metodo");
		testReporter.publishEntry("ejecutando: "+testInfo.getDisplayName()+" "+testInfo.getTestMethod().get().getName()+
				" en las etiquetas "+ testInfo.getTags());
	}
	
	/**
	 * Se ejecuta al finlizar un test
	 */
	@AfterEach
	void afterEach() {
		System.out.println("Finalizando metodo de prueba!!");
	}
	
	/**
	 * Se ejecuta antes de que se ejecuten todos los test
	 */
	@BeforeAll
	static void beforeAll() {
		System.out.println("Inicializando el Test");
	}
	
	
	/**
	 * Se ejecuta despues de todos los test
	 */
	@AfterAll
	static void afterAll() {
		System.out.println("Finalizando los test");
	}
	
	@Tag("cuenta")
	@Nested
	@DisplayName("Probando atributos de la cuenta corriente	")
	class CuentaNombreSaldo{
		@Test
		@DisplayName("validar nombre de la cuenta!!")
		void testNombreCuenta() {
			
			System.out.println(testInfo.getTags());
			
			if(testInfo.getTags().contains("cuenta")) {
				System.out.println("Hacer algo con las etiquetas cuenta");
			}
			
			Cuenta cuenta= new Cuenta("Erick",new BigDecimal("12.22"));
//			cuenta.setPersona("Erick");
			String esperado= "Erick";
			String real = cuenta.getPersona();
			
			//Si falla un assertions todo falla, todos o nada, 
			//El mensaje personalizado es para que apareca cuando haya un error
			//la expresion lambda es para que se construya el mensaje solo en caso de error
			assertNotNull(real,()->"La cuenta no puede ser nula");
			assertEquals(esperado, real,()->"El nombre de la cuenta no es la esperado "+esperado +" mas sin embargo fue "+real);
			assertTrue(real.equals("Erick"),()-> "El nombre esperado debe ser igual al real");
		}
		
		
		@Test
		@DisplayName("validar el saldo de la cuenta")
		void testSaldoCuenta() {
			Cuenta cuenta= new Cuenta("Juan",new BigDecimal("789.87"));
			
			assertEquals(789.87, cuenta.getSaldo().doubleValue());
			assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO)<0);
			
			
		}
		
		
		@Test
		@DisplayName("validar instancias que sean iguales")
		void testReferenciaCuenta() {
			
			Cuenta cuenta1= new Cuenta("Doe",new BigDecimal("8999.99"));
			Cuenta cuenta2= new Cuenta("Doe",new BigDecimal("8999.99"));
			
//			assertNotEquals(cuenta2,cuenta1);
			//Son dos instancias distintas por eso falla, puntero en la memoria que es diferente, son dos objetos diferentes a pesar de tener el mismo valor 
			assertEquals(cuenta2,cuenta1);
		}
		
	}
	
	@Nested
	class CuentaOperacionesBanco{
		
		@Tag("cuenta")
		@RepeatedTest(value=5,name="Repetición numero {currentRepetition} de {totalRepetitions}")
		void testDebitoCuentaRepetir(RepetitionInfo info) {
			
			if(info.getCurrentRepetition()==3) {
				System.out.println("Esta es la repeticion 3");
			}
			
			
			Cuenta cuenta= new Cuenta("Andres",new BigDecimal("1000.12345"));
			cuenta.debito(new BigDecimal("100"));
			assertNotNull(cuenta.getSaldo());
			assertEquals(900,cuenta.getSaldo().intValue());
			assertEquals("900.12345",cuenta.getSaldo().toPlainString());
			
			
		}	
		
		@Tag("cuenta")
		@Test
		void testCreditoCuenta() {
			
			Cuenta cuenta= new Cuenta("Andres",new BigDecimal("1000.12345"));
			cuenta.credito(new BigDecimal("100"));
			assertNotNull(cuenta.getSaldo());
			assertEquals(1100,cuenta.getSaldo().intValue());
			assertEquals("1100.12345",cuenta.getSaldo().toPlainString());
				
		}
		
		@Tag("cuenta")
		@Tag("banco")
		@Test
		public void testTransferirDineroCuentas() {
			
			Cuenta origen = new Cuenta("Luis Luna",new BigDecimal("2500"));
			Cuenta destino = new Cuenta("Roberto Luna",new BigDecimal("1500.9999"));
			
			Banco banco= new Banco();
			banco.setNombre("Banco del estado");
			banco.transferir(destino,origen , new BigDecimal(500));
			
			assertEquals("1000.9999",destino.getSaldo().toPlainString());
			assertEquals("3000",origen.getSaldo().toPlainString());
			
		}
		
	}
	
	  @Tag("param")
	  @Nested
	  static
	  class PruebasParametrizadas{
		@ParameterizedTest(name="numero {index} ejecutando con valor {0} - {argumentsWithNames} ")
		@ValueSource(strings= {"100","200","300","500","700","1000"})
		void testDebitoValueSource(String monto) {
			Cuenta cuenta= new Cuenta("Andres",new BigDecimal("1000.12345"));
			cuenta.debito(new BigDecimal(monto));
			assertNotNull(cuenta.getSaldo());
			assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0  );
			//assertEquals("900.12345",cuenta.getSaldo().toPlainString());
			
			
		}
		
		@ParameterizedTest(name="numero {index} ejecutando con valor {0} - {argumentsWithNames} ")
		@CsvSource({"1,100","2,200","3,300","4,500","5,700","6,1000"})
		void testDebitoCsvSource(String index,String monto) {
			Cuenta cuenta= new Cuenta("Andres",new BigDecimal("1000.12345"));
			System.out.println(index +" -> "+ monto);
			cuenta.debito(new BigDecimal(monto));
			assertNotNull(cuenta.getSaldo());
			assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0  );
			//assertEquals("900.12345",cuenta.getSaldo().toPlainString());
		}
		
		@ParameterizedTest(name="numero {index} ejecutando con valor {0} - {argumentsWithNames} ")
		@CsvSource({"200,100,Jose,Andres","250,200,Pepe,Pepe","300,300,Pablo,Pablo","510,500,Lucas,lucas","750,700,Lucas,lucas","1000,1000,Maria,maria"})
		void testDebitoCsvSource2(String saldo,String monto,String esperado,String actual) {
			Cuenta cuenta= new Cuenta("Andres",new BigDecimal("1000.12345"));
			System.out.println(saldo +" -> "+ monto);
			cuenta.setPersona(actual);
			cuenta.setSaldo(new BigDecimal(saldo));
			cuenta.debito(new BigDecimal(monto));
			assertNotNull(cuenta.getSaldo());
			assertNotNull(cuenta.getPersona());
			assertEquals(esperado,actual);
			assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0  );
			//assertEquals("900.12345",cuenta.getSaldo().toPlainString());
			
			
		}
		
		
		@ParameterizedTest(name="numero {index} ejecutando con valor {0} - {argumentsWithNames} ")
		@CsvFileSource(resources="/data.csv")
		void testDebitoCsvFileSource(String monto) {
			Cuenta cuenta= new Cuenta("Andres",new BigDecimal("1000.12345"));
			System.out.println(" -> "+ monto);
			cuenta.debito(new BigDecimal(monto));
			assertNotNull(cuenta.getSaldo());
			assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0  );
			//assertEquals("900.12345",cuenta.getSaldo().toPlainString());
			
			
		}
		
		
		@ParameterizedTest(name="numero {index} ejecutando con valor {0} - {argumentsWithNames} ")
		@CsvFileSource(resources="/data2.csv")
		void testDebitoCsvFileSource2(String saldo,String monto,String esperado,String actual) {
			Cuenta cuenta= new Cuenta("Andres",new BigDecimal("1000.12345"));
			System.out.println(" -> "+ monto);
			cuenta.setPersona(actual);
			cuenta.setSaldo(new BigDecimal(saldo));
			cuenta.debito(new BigDecimal(monto));
			assertNotNull(cuenta.getSaldo());
			assertNotNull(cuenta.getPersona());
			assertEquals(esperado,actual);
			assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0  );
			//assertEquals("900.12345",cuenta.getSaldo().toPlainString());
			
			
		}
		
		
		@ParameterizedTest(name="numero {index} ejecutando con valor {0} - {argumentsWithNames} ")
		@MethodSource("montoLista")
		void testDebitoMethodSource(String monto) {
			Cuenta cuenta= new Cuenta("Andres",new BigDecimal("1000.12345"));
			System.out.println(" -> "+ monto);
			cuenta.debito(new BigDecimal(monto));
			assertNotNull(cuenta.getSaldo());
			assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0  );
			//assertEquals("900.12345",cuenta.getSaldo().toPlainString());
			
			
		}
		
		static List<String> montoLista(){
			return Arrays.asList("100","200","300","400","700","1000","1000.12345");
		}
	
	}
	
	
	/**
	 * Test para ejecutar Manejo de excepciones
	 */
	@Tag("cuenta")
	@Tag("menejo de errores")
	@Test
	void testDineroInsuficienteException() {
		Cuenta cuenta= new Cuenta("Andres",new BigDecimal("1000.12345"));
		
		Exception e=  assertThrows(DineroException.class,()->{
			cuenta.debito(new BigDecimal(1500));
		});
		
		String actual= e.getMessage();
		String esperado= "Dinero Insuficiente";
		assertEquals(esperado,actual);
		
		
	}
	
	
	
	
	
	
	/**
	 * 
	 */
	@Test
	@Tag("cuenta")
	@Tag("banco")
	//@Disabled  para desabilidat el test
	@DisplayName("validar relacionea entre las cuentas")
	public void testRelacionBancoCuentas() {
		//fail(); para forzar que falle, paquete de Assertions
		Cuenta origen = new Cuenta("Luis Luna",new BigDecimal("2500"));
		Cuenta destino = new Cuenta("Roberto",new BigDecimal("1500.9999"));
		
		Banco banco= new Banco();
		banco.agregarCuenta(origen);
		banco.agregarCuenta(destino);
		banco.setNombre("Banco del estado");
		banco.transferir(destino,origen , new BigDecimal(500));
		/*
		assertEquals("1000.9999",destino.getSaldo().toPlainString());
		assertEquals("3000",origen.getSaldo().toPlainString());
		
		assertEquals(2,banco.getCuentas().size());
		assertEquals("Banco del estado", origen.getBanco().getNombre());
		
		//Comprobar que el banco tiene una cuenta con el nombre de andres
		assertEquals("Roberto", banco.getCuentas().stream()
				.filter(c-> c.getPersona().equals("Roberto"))
				.findFirst()
				.get().getPersona()
				);
		
		assertTrue(banco.getCuentas().stream()
				.filter(c-> c.getPersona().equals("Roberto"))
				.findFirst()
				.isPresent()
				);
		
		assertTrue(banco.getCuentas().stream()
				.anyMatch(c-> c.getPersona().equals("Roberto"))
				);*/
		//Poner todos los asserts  en expresiona lambda
		assertAll(()->
		{
			assertEquals("1000.9999",destino.getSaldo().toPlainString());
		},
		()->{
			assertEquals("3000",origen.getSaldo().toPlainString());
		},
		()->{
			assertEquals(2,banco.getCuentas().size());
			assertEquals("Banco del estado", origen.getBanco().getNombre());
		},
		()->{
			assertEquals("Roberto", banco.getCuentas().stream()
					.filter(c-> c.getPersona().equals("Roberto"))
					.findFirst()
					.get().getPersona()
					);
		},
		()->{
			assertTrue(banco.getCuentas().stream()
					.anyMatch(c-> c.getPersona().equals("Roberto"))
					);
			
		}
				
		);
		
	}
	
	/**
	 * Clases anidadas de Test
	 * @author Hector
	 *
	 */
	@Nested
	class SistemaOperativoTest{
		/**
		 * Habilita test solo windows
		 */
		@Test
		@EnabledOnOs(OS.WINDOWS)
		public void soloWindows() {
			
		}
		
		/**
		 * Habilita correr el test solo en MAc y Linux
		 */
		@Test
		@EnabledOnOs({OS.MAC,OS.LINUX})
		public void soloMacLinux() {
			
		}
		
		/**
		 * Desabilita correr el test en windows
		 */
		@Test
		@DisabledOnOs(OS.WINDOWS)
		void testNoWindows() {
			
		} 
		
		
	}
	
	@Nested
	class JavaVersionTest{
		/**
		 * Para especificar que corra en una version de jdk
		 */
		@Test
		@EnabledOnJre(JRE.JAVA_8)
		void soloJdk8() {
			
		}
		
		/**
		 * Para especificar que corra en una version de jdk
		 */
		@Test
		@EnabledOnJre(JRE.JAVA_15)
		void soloJdk15() {
			
		}
		
		
		/**
		 * Para especificar que no corra en una version de jdk
		 */
		@Test
		@DisabledOnJre(JRE.JAVA_15)
		void testNoJdk15() {
			
		}
		
	}
	
	@Nested
	class SystemPropesties{
		@Test
		void imprimirSystemProperties() {
			Properties properties=System.getProperties();
			properties.forEach((k,v)-> System.out.println(k +" : "+v));
			
		}
		
		/**
		 * Se ejecuta solo si se encuentra un properties en especifico
		 */
		@Test
		@EnabledIfSystemProperty(named="java.version", matches="1.8.0_221")
		void testJavaVersion() {
			
		}
		
		/**
		 * Desabilidar
		 */
		@Test
		@DisabledIfSystemProperty(named="os.arch", matches=".*32.*")
		void solo64() {
			
		}
		
		/**
		 * Habilitar
		 */
		@Test
		@EnabledIfSystemProperty(named="os.arch", matches=".*32.*")
		void solo32() {
			
		}
		
		
		/**
		 * Habilitar
		 */
		@Test
		@EnabledIfSystemProperty(named="user.name", matches="Hector")
		void soloUserName() {
			
		}
		
		
		/**
		 * Habilitar
		 */
		@Test
		@EnabledIfSystemProperty(named="ENV", matches="dev")
		void testDev() {
			
		}
		
	}
	
	@Nested
	class VariablesAmbiente{
		@Test
		void imprimirVaribalesAmbiente() {
			Map<String,String>env= System.getenv();
			env.forEach((k,v) -> System.out.println(k+" = "+v ));
		}
		
		@Test
		@EnabledIfEnvironmentVariable(named="JAVA_HOME",matches=".*jdk1.8.0_45.*")
		void testJavaHome() {
			
		}
		
		@Test
		@EnabledIfEnvironmentVariable(named="NUMBER_OF_PROCESSORS",matches="4")
		void testProcesadores() {
			
		}
		
		@Test
		@EnabledIfEnvironmentVariable(named="ENVIROMENT",matches="dev")
		void testEnv() {
			
		}
		
		
		/**
		 * Se desabilita la prueba si el assumeTrue no se cumple
		 */
		@Test
		void testSaldoCuentaDev() {
			Cuenta cuenta= new Cuenta("Juan",new BigDecimal("789.87"));
			
			boolean esDev="dev".equals(System.getProperty("ENV"));
			
			assumeTrue(esDev);
			
			
			
			assertEquals(789.87, cuenta.getSaldo().doubleValue());
			assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO)<0);
			
			
		}
		
		
		/**
		 * Si ejecuta la prueba aunque no sea valido el assumingThat
		 */
		@Test
		void testSaldoCuentaDev2() {
			Cuenta cuenta= new Cuenta("Juan",new BigDecimal("789.87"));
			
			boolean esDev="dev".equals(System.getProperty("ENV"));
			
			assumingThat(esDev, ()->{
				assertEquals(789.87, cuenta.getSaldo().doubleValue());
				assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO)<0);
			});
			
			
			assertEquals(789.87, cuenta.getSaldo().doubleValue());
			assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO)<0);
			
		}
		
	}
	
	
	@Nested
	@Tag("timeout")
	class TimeOutPruebas{
		
		@Test
		@Timeout(1)//Equivale a 5 segundos
		void pruebaTimeOut() throws InterruptedException {
			
			TimeUnit.SECONDS.sleep(1);
			
		}
		
		@Test
		@Timeout(value=1000,unit=TimeUnit.MILLISECONDS)//Equivale a 5 segundos
		void pruebaTimeOut2() throws InterruptedException {
			
			TimeUnit.MILLISECONDS.sleep(900);
			
		}
		
		@Test
		void timeOutAsserts() {
			
			assertTimeout(Duration.ofSeconds(5), ()->{
				TimeUnit.MILLISECONDS.sleep(4000);
			});
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	



}







































