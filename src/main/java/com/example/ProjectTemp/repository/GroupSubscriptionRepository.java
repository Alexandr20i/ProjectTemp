package com.example.ProjectTemp.repository;

import com.example.ProjectTemp.models.Group;
import com.example.ProjectTemp.models.GroupSubscription;
import com.example.ProjectTemp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GroupSubscriptionRepository extends JpaRepository<GroupSubscription, Long> {
    boolean existsByGroupAndUser(Group group, User user);
    void deleteByGroupAndUser(Group group, User user);
    GroupSubscription findByGroupAndUser(Group group, User user);

    @Transactional
    void deleteByUser(User user);

}
