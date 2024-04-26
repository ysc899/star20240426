package kr.co.seesoft.nemo.starnemoapp.nemoapi.ro;


import java.io.Serializable;

/**
 * 방문 계획 리스트 결과
 */
public class NemoVisitListRO implements Comparable<NemoVisitListRO>, Serializable {
	/** SEQ */
	private int seq;
	/** SEQ */
	private int order;

	private String hospitalName;

	private String hospitalTel;

	private String hospitalAdr;

	private String hospitalCode;

	/** S_SABUN 담당자 사번 */
	private String managerId;

	/** S_NAME 담당자 이름 */
	private String managerName;

	/** B_CNT 해당 병원 이미지 row 개수 */
	private int imgCount;
	/** LAST_SEQUENCE 다음 이미지 카운트 번호 */
	private int nextImageNum;

	public NemoVisitListRO(int order, String hospitalName, String hospitalCode) {
		this.order = order;
		this.hospitalName = hospitalName;
		this.hospitalCode = hospitalCode;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalTel() {
		return hospitalTel;
	}

	public void setHospitalTel(String hospitalTel) {
		this.hospitalTel = hospitalTel;
	}

	public String getHospitalAdr() {
		return hospitalAdr;
	}

	public void setHospitalAdr(String hospitalAdr) {
		this.hospitalAdr = hospitalAdr;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public int getImgCount() {
		return imgCount;
	}

	public void setImgCount(int imgCount) {
		this.imgCount = imgCount;
	}

	public int getNextImageNum() {
		return nextImageNum;
	}

	public void setNextImageNum(int nextImageNum) {
		this.nextImageNum = nextImageNum;
	}

	@Override
	public int compareTo(NemoVisitListRO ro) {
		return Integer.compare(order, ro.order);
	}

	@Override
	public String toString() {
		return "NemoVisitListRO{" +
				"seq=" + seq +
				", order=" + order +
				", hospitalName='" + hospitalName + '\'' +
				", hospitalTel='" + hospitalTel + '\'' +
				", hospitalAdr='" + hospitalAdr + '\'' +
				", hospitalCode='" + hospitalCode + '\'' +
				", managerId='" + managerId + '\'' +
				", managerName='" + managerName + '\'' +
				", imgCount=" + imgCount +
				", nextImageNum=" + nextImageNum +
				'}';
	}
}
