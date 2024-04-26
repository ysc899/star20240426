package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;

public class NemoGpsAddPO {

	private String userId;
	private String ymd;
	private String time;
	private String deptCode;
	/** x */
	private double lat;
	/** y */
	private double lng;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getYmd() {
		return ymd;
	}

	public void setYmd(String ymd) {
		this.ymd = ymd;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	@Override
	public String toString() {
		return "NemoGpsAddPO{" +
				"userId='" + userId + '\'' +
				", ymd='" + ymd + '\'' +
				", time='" + time + '\'' +
				", deptCode='" + deptCode + '\'' +
				", lat=" + lat +
				", lng=" + lng +
				'}';
	}
}
