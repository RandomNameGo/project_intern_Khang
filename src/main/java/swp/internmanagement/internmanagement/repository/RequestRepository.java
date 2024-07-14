package swp.internmanagement.internmanagement.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import swp.internmanagement.internmanagement.entity.Request;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    @Query("SELECT r FROM Request r ORDER BY r.id DESC")
    Page<Request> findAllRequest(Pageable pageable); 
}