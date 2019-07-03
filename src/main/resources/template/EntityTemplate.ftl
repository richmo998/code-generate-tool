package ${packageName}.entity;

import com.xhg.core.base.BasePojo;
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
public class ${className}Entity extends BasePojo implements Serializable {

	private static final long serialVersionUID=1L;
	
	${entityFeilds}
}

