package ${packageName}.service.base;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 基础业务逻辑接口
 *
 * @param <T>
 * @param <PK>
 */
public interface BaseService<T, PK extends Serializable> {

    Integer insert(T t);

    Integer insertByBatch(List<T> list);

    Integer update(T t);

    Integer updateByBatch(List<T> list);

    Integer delete(PK id);

    Integer deleteBatch(List<PK> ids);

    T queryById(PK id);

    Integer queryAllCount(Map<String, Object> params);

    List<T> queryAllList(Map<String, Object> params);
}
