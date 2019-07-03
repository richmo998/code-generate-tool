package ${packageName}.dao.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 基础dao
 *
 * @param <T>  实体类型
 * @param <PK> 实体主键类型
 */
public interface BaseDao<T, PK extends Serializable> {
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
