package xyz.ibenben.zhongdian.system.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Task implements Serializable {
    private String id;
	private String name;
	private String descript;
	private Integer state;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
