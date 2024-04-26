package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;

public class NemoVisitAddPO {

	private String userId;
	private String ymd;
	private String visitJSON;
	private String deptCode;

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

	public String getVisitJSON() {
		return visitJSON;
	}

	public void setVisitJSON(String visitJSON) {
		this.visitJSON = visitJSON;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}


	public static class VisitInfo {
		private int order;
		private String hospitalCode;

		public VisitInfo(int order, String hospitalCode) {
			this.order = order;
			this.hospitalCode = hospitalCode;
		}

		public int getOrder() {
			return order;
		}

		public void setOrder(int order) {
			this.order = order;
		}

		public String getHospitalCode() {
			return hospitalCode;
		}

		public void setHospitalCode(String hospitalCode) {
			this.hospitalCode = hospitalCode;
		}

		@Override
		public String toString() {
			return "VisitInfo{" +
					"order=" + order +
					", hospitalCode='" + hospitalCode + '\'' +
					'}';
		}
	}

	@Override
	public String toString() {
		return "NemoVisitAddPO{" +
				"userId='" + userId + '\'' +
				", ymd='" + ymd + '\'' +
				", visitJSON='" + visitJSON + '\'' +
				", deptCode='" + deptCode + '\'' +
				'}';
	}
}
