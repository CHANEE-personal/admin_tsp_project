package com.tsp.new_tsp_admin.api.domain.user;

import com.tsp.new_tsp_admin.api.domain.common.NewCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class AdminUserDTO extends NewCommonDTO {

	@ApiModelProperty(required = true, value = "rnum", hidden = true)
	Integer rnum;

	@ApiModelProperty(required = true, value = "user Seq", hidden = true)
	Integer idx;

	@ApiModelProperty(required = true, value = "user Id")
	String userId;

	@ApiModelProperty(required = true, value = "user Password")
	String password;

	@ApiModelProperty(required = true, value = "user Name", hidden = true)
	String name;

	@ApiModelProperty(required = true, value = "user email", hidden = true)
	String email;

	@ApiModelProperty(required = true, value = "user visible", hidden = true)
	String visible;

	@ApiModelProperty(required = false, value = "user Token", hidden = true)
	String userToken;
}
