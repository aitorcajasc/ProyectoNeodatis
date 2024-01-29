package clases;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Proyectos {
	private int codigoproyecto;
	private String nombre;
	private Date fechainicio;
	private Date fechafin;
	private float presupuesto;
	private float extraaportacion;
	private ArrayList<Participa> participantes;
	
	public Proyectos(int codigoproyecto, String nombre, Date fechainicio, Date fechafin, float presupuesto,
			float extraaportacion, ArrayList<Participa> participantes) {
		super();
		this.codigoproyecto = codigoproyecto;
		this.nombre = nombre;
		this.fechainicio = fechainicio;
		this.fechafin = fechafin;
		this.presupuesto = presupuesto;
		this.extraaportacion = extraaportacion;
		this.participantes = participantes;
	}
	public Proyectos() {}
	
	public int getCodigoproyecto() {
		return codigoproyecto;
	}
	public void setCodigoproyecto(int codigoproyecto) {
		this.codigoproyecto = codigoproyecto;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Date getFechainicio() {
		return fechainicio;
	}
	public void setFechainicio(Date fechainicio) {
		this.fechainicio = fechainicio;
	}
	public Date getFechafin() {
		return fechafin;
	}
	public void setFechafin(Date fechafin) {
		this.fechafin = fechafin;
	}
	public float getPresupuesto() {
		return presupuesto;
	}
	public void setPresupuesto(float presupuesto) {
		this.presupuesto = presupuesto;
	}
	public float getExtraaportacion() {
		return extraaportacion;
	}
	public void setExtraaportacion(float extraaportacion) {
		this.extraaportacion = extraaportacion;
	}
	public ArrayList<Participa> getParticipantes() {
		return participantes;
	}
	public void setParticipantes(ArrayList<Participa> participantes) {
		this.participantes = participantes;
	}
	@Override
	public String toString() {
		return "Proyectos [codigoproyecto=" + codigoproyecto + ", nombre=" + nombre + ", fechainicio=" + fechainicio
				+ ", fechafin=" + fechafin + ", presupuesto=" + presupuesto + ", extraaportacion=" + extraaportacion
				+ ", participantes=" + participantes + "]";
	}
}