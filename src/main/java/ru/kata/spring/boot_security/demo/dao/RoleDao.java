package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.EntityManager;
import java.util.List;


@Repository
public class RoleDao {

    private final EntityManager entityManager;

    @Autowired
    public RoleDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }



    public List<Role> index() {
        return entityManager.createQuery("from Role", Role.class).getResultList();
    }

    public Role show(int id) {
        return entityManager.find(Role.class, id);
    }
}
