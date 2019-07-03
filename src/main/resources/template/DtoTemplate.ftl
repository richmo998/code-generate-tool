package ${packageName}.dto;

import com.xhg.core.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 * ${tableComment}
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "${className}")
public class ${className}DTO extends BaseDto implements Serializable{
	
	private static final long serialVersionUID=1L;
	
	${dtoFeilds}
}

