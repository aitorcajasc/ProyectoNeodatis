package principal;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.ObjectValues;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Values;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;
import org.neodatis.odb.impl.core.query.values.ValuesCriteriaQuery;

import clases.*;

public class Principal {

	private static ODB bd;

	public static void main(String[] args) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conexion = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "PROYECTOS",
					"proyectos");
			// CASA
//			Connection conexion = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "C##PROYECTOS",
//					"proyectos");

			bd = ODBFactory.open("proyectos.dat");

			Scanner sc = new Scanner(System.in);
			int opcion = 0;
			do {
				menu();
				opcion = sc.nextInt();
				switch (opcion) {
				case 1:
					cargarBD(conexion);
					break;
				case 2:
					listarProyecto(2);
					listarProyecto(100);
					listarProyecto(5);
					break;
				case 3:
					insertarParticipa(1, 1, "Aportación Prueba", 0);
					insertarParticipa(28, 99, "Aportación Prueba", 0);
					break;
				case 0:
					System.out.println("Programa finalizado");
					break;
				}
			} while (opcion != 0);

			conexion.close();
			bd.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void insertarParticipa(int codEst, int codPro, String aport, int nAport) {
		boolean insertar = true;
		
		Estudiantes est=new Estudiantes();
		try {
			IQuery consulta = new CriteriaQuery(Estudiantes.class, Where.equal("codestudiante", codEst));
			est = (Estudiantes) bd.getObjects(consulta).getFirst();
		} catch (IndexOutOfBoundsException e) {
			System.out.println("El estudiante: " + codEst + " NO EXISTE.");
			insertar = false;
		}

		Proyectos pro=new Proyectos();
		try {
			IQuery consulta = new CriteriaQuery(Proyectos.class, Where.equal("codigoproyecto", codPro));
			pro = (Proyectos) bd.getObjects(consulta).getFirst();
		} catch (IndexOutOfBoundsException e) {
			System.out.println("El proyecto: " + codPro + " NO EXISTE.");
			insertar = false;
		}

		if (insertar) {
			Values val = bd.getValues(new ValuesCriteriaQuery(Participa.class).max("codparticipacion", "cod_max"));
			ObjectValues ov = val.nextValues();
			BigDecimal max = (BigDecimal) ov.getByAlias("cod_max");
			
			int cod = max.intValue() + 1;
			Participa par=new Participa(cod, est, pro, aport, nAport);
			bd.store(par);
			
			System.out.println("Participa "+cod+" CREADO.");
			pro.getParticipantes().add(par);
			bd.store(pro);
			
			System.out.println("Participa "+cod+" AÑADIDO A PRODUCTO: "+codPro);
			est.getParticipaen().add(par);
			bd.store(est);
			
			System.out.println("Participa "+cod+" AÑADIDO A ESTUDIANTE: "+codEst);
			bd.commit();
		} else {
			System.out.println("NO SE INSERTARÁ NINGÚN PARTICIPA.");
		}
		System.out.println();
	}

	private static void listarProyecto(int codpro) throws SQLException {
		try {
			IQuery query = new CriteriaQuery(Proyectos.class, Where.equal("codigoproyecto", codpro));
			Proyectos pro = (Proyectos) bd.getObjects(query).getFirst();
			System.out.println("-----------------------------------------------------------------\n"
					+ "Código proyecto: " + pro.getCodigoproyecto() + "     Nombre: " + pro.getNombre() + "\n"
					+ "Fecha inicio: " + pro.getFechainicio() + "     Fecha fin: " + pro.getFechafin() + "\n"
					+ "Presupuesto: " + pro.getPresupuesto() + "     Extraaportación: " + pro.getExtraaportacion()
					+ "\n" + "-----------------------------------------------------------------");
			ArrayList<Participa> participantes = pro.getParticipantes();
			if(participantes.size()>0) {
				System.out.println("Participantes del proyecto:\n" + "---------------------------");
				System.out.printf("%20s %15s %25s %18s %15s %7s %n", "CODPARTICIPACION", "CODESTUDIANTE",
						"NOMBREESTUDIANTE", "TIPAPORTACION", "NUMAPORTACIONES", "IMPORTE");
				System.out.printf("%20s %15s %25s %18s %15s %7s %n", "----------------", "-------------",
						"----------------", "-------------", "---------------", "-------");
				float totalImport = 0;
				int totalAport = 0;
				for (Participa par : participantes) {
					System.out.printf("%20s %15s %25s %18s %15s %7s %n", par.getCodparticipacion(),
							par.getEstudiante().getCodestudiante(), par.getEstudiante().getNombre(),
							par.getTipoparticipacion(), par.getNumaportaciones(),
							par.getNumaportaciones() * pro.getExtraaportacion());
					totalImport += par.getNumaportaciones() * pro.getExtraaportacion();
					totalAport += par.getNumaportaciones();
				}
				System.out.printf("%20s %15s %25s %18s %15s %7s %n", "----------------", "-------------",
						"----------------", "-------------", "---------------", "-------");
				System.out.printf("%20s %15s %25s %18s %15s %7s %n", "TOTALES", "", "", "", totalAport, totalImport);
				System.out.println();
			}else {
				System.out.println("El proyecto: " + codpro+" NO TIENE PARTICIPANTES.");
				System.out.println();
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println("El proyecto: " + codpro + " NO EXISTE.");
			System.out.println();
		}
	}

	private static void cargarBD(Connection conexion) throws SQLException {
		rellenarProyectos(conexion);
		rellenarEstudiantes(conexion);
		rellenarParticipa(conexion);
		llenarListParticipaProyectos(conexion);
		llenarListParticipaEstudiantes(conexion);
	}

	private static void llenarListParticipaEstudiantes(Connection conexion) throws SQLException {
		Objects<Estudiantes> objects = bd.getObjects(Estudiantes.class);
		while (objects.hasNext()) {
			Estudiantes est = objects.next();
			ArrayList<Participa> participantes = new ArrayList<Participa>();
			Statement sentencia = conexion.createStatement();
			ResultSet resul = sentencia
					.executeQuery("SELECT * FROM participa where codestudiante = '" + est.getCodestudiante() + "'");
			while (resul.next()) {
				IQuery consulta = new CriteriaQuery(Participa.class, Where.equal("codparticipacion", resul.getInt(1)));
				Participa par = (Participa) bd.getObjects(consulta).getFirst();
				participantes.add(par);
			}
			est.setParticipaen(participantes);
			bd.store(est);
			resul.close();
			sentencia.close();
		}
		bd.commit();
		System.out.println();
	}

	private static void llenarListParticipaProyectos(Connection conexion) throws SQLException {
		Objects<Proyectos> objects = bd.getObjects(Proyectos.class);
		while (objects.hasNext()) {
			Proyectos pro = objects.next();
			ArrayList<Participa> participantes = new ArrayList<Participa>();
			Statement sentencia = conexion.createStatement();
			ResultSet resul = sentencia
					.executeQuery("SELECT * FROM participa where codigoproyecto = '" + pro.getCodigoproyecto() + "'");
			while (resul.next()) {
				IQuery consulta = new CriteriaQuery(Participa.class, Where.equal("codparticipacion", resul.getInt(1)));
				Participa par = (Participa) bd.getObjects(consulta).getFirst();
				participantes.add(par);
			}
			pro.setParticipantes(participantes);
			bd.store(pro);
			resul.close();
			sentencia.close();
		}
		bd.commit();
	}

	private static void rellenarParticipa(Connection conexion) {
		try {
			Statement sentencia = (Statement) conexion.createStatement();
			ResultSet resul = sentencia.executeQuery("SELECT * FROM participa");
			while (resul.next()) {
				if (comprobarPar(resul.getInt(1)) == false) {
					IQuery consulta1 = new CriteriaQuery(Estudiantes.class,
							Where.equal("codestudiante", resul.getInt(2)));
					Estudiantes est = (Estudiantes) bd.getObjects(consulta1).getFirst();
					IQuery consulta2 = new CriteriaQuery(Proyectos.class,
							Where.equal("codigoproyecto", resul.getInt(3)));
					Proyectos pro = (Proyectos) bd.getObjects(consulta2).getFirst();
					Participa par = new Participa(resul.getInt(1), est, pro, resul.getString(4), resul.getInt(5));
					bd.store(par);
					System.out.println("Participante grabado " + resul.getInt(1));
				} else
					System.out.println("Participante: " + resul.getString(1) + ", EXISTE.");
			}
			bd.commit();
			resul.close();
			sentencia.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static boolean comprobarPar(int cod) {
		try {
			IQuery consulta = new CriteriaQuery(Participa.class, Where.equal("codparticipacion", cod));
			Participa obj = (Participa) bd.getObjects(consulta).getFirst();
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	private static void rellenarEstudiantes(Connection conexion) {
		try {
			Statement sentencia = (Statement) conexion.createStatement();
			ResultSet resul = sentencia.executeQuery("SELECT * FROM estudiantes");
			while (resul.next()) {
				if (comprobarEstudia(resul.getInt(1)) == false) {
					ArrayList<Participa> participantes = new ArrayList<Participa>();
					Estudiantes est = new Estudiantes(resul.getInt(1), resul.getString(2), resul.getString(3),
							resul.getString(4), resul.getDate(5), participantes);
					bd.store(est);
					System.out.println("Estudiante grabado " + resul.getInt(1));
				} else
					System.out.println("Estudiante: " + resul.getString(1) + ", EXISTE.");
			}
			bd.commit();
			resul.close();
			sentencia.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static boolean comprobarEstudia(int cod) {
		try {
			IQuery consulta = new CriteriaQuery(Estudiantes.class, Where.equal("codestudiante", cod));
			Estudiantes obj = (Estudiantes) bd.getObjects(consulta).getFirst();
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	private static void rellenarProyectos(Connection conexion) {
		try {
			Statement sentencia = (Statement) conexion.createStatement();
			ResultSet resul = sentencia.executeQuery("SELECT * FROM proyectos");
			while (resul.next()) {
				if (comprobarProyec(resul.getInt(1)) == false) {
					ArrayList<Participa> participantes = new ArrayList<Participa>();
					Proyectos pro = new Proyectos(resul.getInt(1), resul.getString(2), resul.getDate(3),
							resul.getDate(4), resul.getFloat(5), resul.getFloat(6), participantes);
					bd.store(pro);
					System.out.println("Proyecto grabado " + resul.getInt(1));
				} else
					System.out.println("Proyecto: " + resul.getString(1) + ", EXISTE.");
			}
			bd.commit();
			resul.close();
			sentencia.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static boolean comprobarProyec(int cod) {
		try {
			IQuery consulta = new CriteriaQuery(Proyectos.class, Where.equal("codigoproyecto", cod));
			Proyectos obj = (Proyectos) bd.getObjects(consulta).getFirst();
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	private static void menu() {
		System.out.println("\tOPERACIONES PROYECTO\n\n" + "1. Crear BD\n" + "2. Listar un proyecto.\n"
				+ "3. Insertar participación.\n" + "0. Salir");
	}
}