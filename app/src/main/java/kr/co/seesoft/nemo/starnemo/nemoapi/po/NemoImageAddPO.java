package kr.co.seesoft.nemo.starnemo.nemoapi.po;

public class NemoImageAddPO {

	private String userId;
	private String ymd;
	private String hospitalCode;
	private String deptCode;

	private String imageBase64;

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

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getImageBase64() {
		return imageBase64;
	}

	public void setImageBase64(String imageBase64) {
		this.imageBase64 = imageBase64;
	}

	@Override
	public String toString() {
		return "NemoImageAddPO{" +
				"userId='" + userId + '\'' +
				", ymd='" + ymd + '\'' +
				", hospitalCode='" + hospitalCode + '\'' +
				", deptCode='" + deptCode + '\'' +
				", imageBase64='" + imageBase64 + '\'' +
				'}';
	}
}
