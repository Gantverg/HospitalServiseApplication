package tel_ran.hsa.dto;

public class Info {
	int count;
	String time;

	public Info(int count, String time) {
		super();
		this.count = count;
		this.time = time;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getTime() {
		return time;
	}

}
