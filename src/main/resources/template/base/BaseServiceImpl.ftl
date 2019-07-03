package ${packageName}.service.impl.base;

import ${packageName}.dao.base.BaseDao;
import ${packageName}.service.base.BaseService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 基础业务接口实现
 *
 * @param <T>
 * @param <PK>
 */
public class BaseServiceImpl<T, PK extends Serializable> implements BaseService<T, PK> {

    protected BaseDao<T, PK> dao;

    protected void setDao(BaseDao dao) {
        this.dao = dao;
    }

    @Override
    public Integer insert(T t) {
        return dao.insert(t);
    }

    @Override
    public Integer insertByBatch(List<T> list) {
        return dao.insertByBatch(list);
    }

    @Override
    public Integer update(T t) {
        return dao.update(t);
    }

    @Override
    public Integer updateByBatch(List<T> list) {
        return dao.updateByBatch(list);
    }

    @Override
    public Integer delete(PK id) {
        return dao.delete(id);
    }

    @Override
    public Integer deleteBatch(List<PK> ids) {
        return dao.deleteBatch(ids);
    }

    @Override
    public T queryById(PK id) {
        return dao.queryById(id);
    }

    @Override
    public Integer queryAllCount(Map<String, Object> params) {
        return dao.queryAllCount(params);
    }

    @Override
    public List<T> queryAllList(Map<String, Object> params) {
        return dao.queryAllList(params);
    }
}
