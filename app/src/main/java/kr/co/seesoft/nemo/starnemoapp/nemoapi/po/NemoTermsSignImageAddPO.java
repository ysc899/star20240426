package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;

public class NemoTermsSignImageAddPO {

	private String userId;
	private String upprDeptCd;
	private String deptCd;
	private String custCd;
	private String trmsCd;

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

	public String getTrmsCd() {
		return trmsCd;
	}

	public void setTrmsCd(String trmsCd) {
		this.trmsCd = trmsCd;
	}

	public String getImageBase64() {
		return imageBase64;
	}

	public void setImageBase64(String imageBase64) {
		this.imageBase64 = imageBase64;
	}

	@Override
	public String toString() {
		return "NemoTermsSignImageAddPO{" +
				"userId='" + userId + '\'' +
				", trmsCd='" + trmsCd + '\'' +
				", upprDeptCd='" + upprDeptCd + '\'' +
				", deptCd='" + deptCd + '\'' +
				", custCd='" + custCd + '\'' +
				", imageBase64='" + imageBase64 + '\'' +
				'}';
	}
}
