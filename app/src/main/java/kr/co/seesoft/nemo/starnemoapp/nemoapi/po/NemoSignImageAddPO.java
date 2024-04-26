package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;

public class NemoSignImageAddPO {

	private String userId;
	private String upprDeptCd;
	private String deptCd;
	private String custCd;
	private String jobYm;

	private String imageBase64;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUpprDeptCd() {
		return upprDeptCd;
	}

	public void setUpprDeptCd(String upprDeptCd) {
		this.upprDeptCd = upprDeptCd;
	}

	public String getDeptCd() {
		return deptCd;
	}

	public void setDeptCd(String deptCd) {
		this.deptCd = deptCd;
	}

	public String getCustCd() {
		return custCd;
	}

	public void setCustCd(String custCd) {
		this.custCd = custCd;
	}

	public String getJobYm() {
		return jobYm;
	}

	public void setJobYm(String jobYm) {
		this.jobYm = jobYm;
	}

	public String getImageBase64() {
		return imageBase64;
	}

	public void setImageBase64(String imageBase64) {
		this.imageBase64 = imageBase64;
	}

	@Override
	public String toString() {
		return "NemoSignImageAddPO{" +
				"userId='" + userId + '\'' +
				", jobYm='" + jobYm + '\'' +
				", upprDeptCd='" + upprDeptCd + '\'' +
				", deptCd='" + deptCd + '\'' +
				", custCd='" + custCd + '\'' +
				", imageBase64='" + imageBase64 + '\'' +
				'}';
	}
}
