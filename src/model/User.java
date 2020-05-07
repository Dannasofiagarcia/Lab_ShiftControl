package model;

import java.io.Serializable;
import java.util.Comparator;

public class User implements Serializable, Comparable<User> {

	// ------------------------
	// ATRIBUTOS
	// ------------------------

	private int idNumber;
	private String name;
	private String lastName;
	private int phoneNumber;
	private String address;
	private TypeDocument typeDocument;
	private boolean suspended;

	// ------------------------
	// RELACIONES
	// ------------------------

	public enum TypeDocument {
		identificationCard, identityCard, civilRegistration, passport, foreignCard;
	}

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	public User(int idNumber, String name, String lastName, int phoneNumber, String address,
			TypeDocument typeDocument) {
		this.idNumber = idNumber;
		this.name = name;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.typeDocument = typeDocument;
	}

	// ------------------------
	// MÉTODOS
	// ------------------------

	// ------------------------
	// GETTER Y SETTER
	// ------------------------

	public int getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(int idNumber) {
		this.idNumber = idNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public TypeDocument getTypeDocument() {
		return typeDocument;
	}

	public void setTypeDocument(TypeDocument typeDocument) {
		this.typeDocument = typeDocument;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	@Override
	public String toString() {
		String typeDocuments = "";
		if (typeDocument == TypeDocument.identificationCard) {
			typeDocuments = "identification card";
		} else if (typeDocument == TypeDocument.civilRegistration) {
			typeDocuments = "civil registration";
		} else if (typeDocument == TypeDocument.foreignCard) {
			typeDocuments = "foreign card";
		} else if (typeDocument == TypeDocument.identityCard) {
			typeDocuments = "identity card";
		} else if (typeDocument == TypeDocument.passport) {
			typeDocuments = "pasport";
		}

		return name + " " + lastName + " with id number " + idNumber + ", " + typeDocuments;
	}

	@Override
	public int compareTo(User other) {
		int outCome = -2;
		if (this.idNumber < other.getIdNumber()) {
			outCome = -1;
		} else if (this.idNumber > other.getIdNumber()) {
			outCome = 1;
		} else if (this.idNumber == other.getIdNumber()) {
			outCome = 0;
		}
		return outCome;
	}

	public class UserComparator implements Comparator<User> {

		@Override
		public int compare(User one, User two) {
			return one.getName().compareTo(two.getName());
		}

	}

}
