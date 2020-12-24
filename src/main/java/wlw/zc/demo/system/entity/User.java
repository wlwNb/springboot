package wlw.zc.demo.system.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
	private Integer id;
	private String username;
	private Integer state;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
