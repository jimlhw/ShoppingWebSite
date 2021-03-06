package service.system.impl;

import common.dao.system.IUserDao;
import common.entity.system.User;
import common.enums.RegisterEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.system.IUserService;

import java.io.Serializable;
import java.util.List;

/**
 * Created by peyppicp on 2017/3/14.
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDao iUserDao;

    @Transactional
    public RegisterEnum register(User user) {
        try {
            boolean isExist = iUserDao.isExist(user);
            if (isExist) {
                return RegisterEnum.EXISTED;
            }
            iUserDao.insertEntity(user);
            return RegisterEnum.SUCCESS;
        } catch (Exception e) {
            return RegisterEnum.UNKNOWEN;
        }
    }

    @Transactional
    public boolean isExist(User user) {
        if ("".equals(user.getUser_id()) || null == user.getUser_id()) {
            return iUserDao.isExist(user);
        } else {
            User entity = iUserDao.getEntity(user.getUser_id());
            if (entity == null) {
                return false;
            }
            return true;
        }
    }

    @Transactional
    public User getEntity(User user) {
        return iUserDao.getEntity(user);
    }

    @Transactional
    public User getEntity(Serializable id) {
        return iUserDao.getEntity(id);
    }

    @Transactional
    public User loadEntity(Serializable id) {
        return iUserDao.loadEntity(id);
    }

    @Transactional
    public User updateEntity(User user) {
        return iUserDao.updateEntity(user);
    }

    @Transactional
    public User deleteEntity(User user) {
        return iUserDao.deleteEntity(user);
    }

    @Transactional
    public Serializable insertEntity(User user) {
        return iUserDao.insertEntity(user);
    }

    @Transactional
    public List<User> getEntities() {
        return iUserDao.getEntities();
    }

    @Transactional
    public List<User> getEntities(int from, int size) {
        return iUserDao.getEntities(from, size);
    }
}
