package ru.kata.spring.boot_security.demo.dao;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final EntityManager entityManager;

    @Autowired
    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<User> index() {
        return entityManager.createQuery("from User", User.class).getResultList();
    }


    public User show(int id) {
        return entityManager.find(User.class, id);
    }

    public void save(User user) {
        entityManager.persist(user);
    }

    public void update(int id, User updatedUser) {
        updatedUser.setId(id);
        entityManager.merge(updatedUser);
    }

    public void delete(int id) {
        entityManager.remove(entityManager.find(User.class, id));
    }

    public User findUserByName(String name) {
        return entityManager.createQuery("select u from User u left join fetch u.roles where u.name=:name", User.class)
                        .setParameter("name", name).getSingleResult();


    }

    public Optional<User> show(String name) {
        return entityManager.createQuery("select u from User u where u.name=:name", User.class)
                .setParameter("name", name).getResultStream().findAny();


    }

}
