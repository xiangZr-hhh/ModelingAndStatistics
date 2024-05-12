package com.modeling.model.vodata;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 描述：返回日志
 *
 * @author zrx
 */
@Data
@Accessors(chain = true)
public class UpdatedLogReturnVO {

	private String content;

	private String timestamp;

	private String color;

	private String size = "large";

}


