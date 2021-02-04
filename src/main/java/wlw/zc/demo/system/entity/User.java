package wlw.zc.demo.system.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class User implements Serializable {
	private String id;
	private String userName;
	private String password;
	/**
	 * 用户对应的角色集合
	 */
	private Set<Role> roles;


}
