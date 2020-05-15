package it.polito.tdp.borders.model;

public class Country implements Comparable<Country>{

	private int code;
	private String abb;
	private String name;
	
	public Country(int code, String abb, String name) {
		super();
		this.code = code;
		this.abb = abb;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getAbb() {
		return abb;
	}

	public void setAbb(String abb) {
		this.abb = abb;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Country other = (Country) obj;
		if (code != other.code)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public int compareTo(Country o) {
		return this.getName().compareTo(o.getName());
	}

	
}
