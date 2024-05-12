package com.modeling.model.vodata;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 描述：更新日志添加VO类
 *
 * @author zrx
 */
@Data
public class UpdatedLogAddVO {

	//内容
	@NotBlank(message = "内容不能为空")
	private String content;
	//类型
	@NotBlank(message = "类型不能为空")
	private String type;
	//版本
	@NotBlank(message = "版本不能为空")
	private String version;

}


