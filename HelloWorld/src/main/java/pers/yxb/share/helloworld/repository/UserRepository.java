package pers.yxb.share.helloworld.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pers.yxb.share.helloworld.entity.User;

/**
 * @author Yuxb.
 * @description.
 * @create 2018-4-12 17:52
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByUserName(String userName);

    User findByUserNameOrEmail(String username, String email);

}
