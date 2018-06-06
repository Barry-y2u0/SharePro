package pers.yxb.share.helloworld.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pers.yxb.share.helloworld.entity.Customer;

/**
 * @author Yuxb.
 * @description.
 * @create 2018-4-11 9:11
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Customer findByName(String name);
}
