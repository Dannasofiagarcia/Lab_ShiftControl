package model;

import java.io.Serializable;

public class Turn implements Serializable, Comparable<Turn> {

	// ------------------------
	// CONSTANTES
	// ------------------------

	// Los tiempos de duración de los turnos esta en minutos
	public final static float BREAKFAST_DURATION = 2.5f;
	public final static float LUNCH_DURATION = 3.5f;
	public final static float REFRESHMENT_DURATION = 2.0f;
	// El tiempo de atencion de cada turno esta en segundos
	public final static float ATTENTION_TIME = 15.0f;

	// ------------------------
	// ATRIBUTOS
	// ------------------------

	// La idea de que el id sea un int, es castearlo a char para que salgan las
	// respectivas letras
	private int id;
	private String number;
	private boolean attended;
	private Typeturn typeturn;
	private float duration;
	private boolean called;

	// ------------------------
	// RELACIONES
	// ------------------------

	private User user;
	private Timetable creationTime;

	public enum Typeturn {
		breakfastTurn, lunchTurn, refreshmentTurn
	}

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	public Turn(int id, String number, Typeturn typeturn) {
		this.id = id;
		this.number = number;
		this.typeturn = typeturn;
		setDuration();
		called = false;
		attended = false;
	}

	// ------------------------
	// MÉTODOS
	// ------------------------

	// ------------------------
	// GETTER Y SETTER
	// ------------------------

	public int getId() {
		return id;
	}

	public Typeturn getTypeturn() {
		return typeturn;
	}

	public void setTypeturn(Typeturn typeturn) {
		this.typeturn = typeturn;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration() {
		if (typeturn == Typeturn.breakfastTurn) {
			duration = BREAKFAST_DURATION;
		} else if (typeturn == Typeturn.lunchTurn) {
			duration = LUNCH_DURATION;
		} else if (typeturn == Typeturn.refreshmentTurn) {
			duration = REFRESHMENT_DURATION;
		}
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public boolean isAttended() {
		return attended;
	}

	public void setAttended(boolean attended) {
		this.attended = attended;
	}

	public boolean isCalled() {
		return called;
	}

	public void setCalled(boolean called) {
		this.called = called;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public Timetable getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Timetable creationTime) {
		this.creationTime = creationTime;
	}

	@Override
	public int compareTo(Turn other) {
		int outCome = -2;
		if (this.duration < other.duration) {
			outCome = -1;
		} else if (this.duration > other.duration) {
			outCome = 1;
		} else if (this.duration == other.duration) {
			outCome = 0;
		}
		return outCome;
	}

}
