package kr.co.seesoft.nemo.starnemoapp.nemoapi.po;

public class NemoVisitListPO {

	private String userId;
	private String ymd;

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

	@Override
	public String toString() {
		return "NemoVisitListPO{" +
				"userId='" + userId + '\'' +
				", ymd='" + ymd + '\'' +
				'}';
	}
}
