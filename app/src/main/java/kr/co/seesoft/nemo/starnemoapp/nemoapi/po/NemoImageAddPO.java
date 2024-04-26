package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;

public class NemoImageAddPO {

	private String userId;
	private String smplTakePlanDt;
	private String upprDeptCd;
	private String deptCd;
	private String custCd;

	private String imageBase64;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSmplTakePlanDt() {
		return smplTakePlanDt;
	}

	public void setSmplTakePlanDt(String smplTakePlanDt) {
		this.smplTakePlanDt = smplTakePlanDt;
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
				", smplTakePlanDt='" + smplTakePlanDt + '\'' +
				", upprDeptCd='" + upprDeptCd + '\'' +
				", deptCd='" + deptCd + '\'' +
				", custCd='" + custCd + '\'' +
				", imageBase64='" + imageBase64 + '\'' +
				'}';
	}
}
