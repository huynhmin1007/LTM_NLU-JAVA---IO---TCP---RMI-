package giaide_2021.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Candidate {
	
	private int id;
	private String fullName;
	private Date birthDate;
	private String address;
	private byte[] img;
	
	public Candidate(int id, String fullName, Date birthDate, String address, byte[] img) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.birthDate = birthDate;
		this.address = address;
		this.img = img;
	}
	
	public Candidate(int id, String fullName, Date birthDate, String address) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.birthDate = birthDate;
		this.address = address;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
	}

	@Override
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return "THI SINH [MSDT=" + String.format("%03d", id) + ", fullName=" + fullName + ", birthDate=" + format.format(birthDate) + ", address=" + address
				+ ", img=" + img.length + " bytes]";
	}
}
