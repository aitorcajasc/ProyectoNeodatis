package clases;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Estudiantes {
	private int codestudiante;
	private String nombre;
	private String direccion;
	private String tlf;
	private Date fechaalta;
	private ArrayList<Participa> participaen;
	
	public Estudiantes(int codestudiante, String nombre, String direccion, String tlf, Date fechaalta,
			ArrayList<Participa> participaen) {
		super();
		this.codestudiante = codestudiante;
		this.nombre = nombre;
		this.direccion = direccion;
		this.tlf = tlf;
		this.fechaalta = fechaalta;
		this.participaen = participaen;
	}
	public Estudiantes() {}
	public int getCodestudiante() {
		return codestudiante;
	}
	public void setCodestudiante(int codestudiante) {
		this.codestudiante = codestudiante;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getTlf() {
		return tlf;
	}
	public void setTlf(String tlf) {
		this.tlf = tlf;
	}
	public Date getFechaalta() {
		return fechaalta;
	}
	public void setFechaalta(Date fechaalta) {
		this.fechaalta = fechaalta;
	}
	public ArrayList<Participa> getParticipaen() {
		return participaen;
	}
	public void setParticipaen(ArrayList<Participa> participaen) {
		this.participaen = participaen;
	}
	@Override
	public String toString() {
		return "Estudiantes [codestudiante=" + codestudiante + ", nombre=" + nombre + ", direccion=" + direccion
				+ ", tlf=" + tlf + ", fechaalta=" + fechaalta + ", participaen=" + participaen + "]";
	}
}
