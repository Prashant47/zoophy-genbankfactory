package zodo;

public class Institute {

	private Integer institutionID;
	private String country;
	private String city;
	private Double latitude;
	private Double longitude;
	private Integer geonameID;
	public Integer getInstitutionID() {
		return institutionID;
	}
	public void setInstitutionID(Integer institutionID) {
		this.institutionID = institutionID;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Integer getGeonameID() {
		return geonameID;
	}
	public void setGeonameID(Integer geonameID) {
		this.geonameID = geonameID;
	}
	@Override
	public String toString() {
		return "Institute [institutionID=" + institutionID + ", country=" + country + ", city=" + city + ", latitude="
				+ latitude + ", longitude=" + longitude + "]";
	}
	
	
}
