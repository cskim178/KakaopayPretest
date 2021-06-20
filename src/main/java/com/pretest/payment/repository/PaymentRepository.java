package com.pretest.payment.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pretest.payment.entity.PaymentVO;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentVO, String> {

	@Query(value = "SELECT * FROM PAYMENTVO WHERE id = :id  ", nativeQuery = true)
	ArrayList<PaymentVO> getPaymentInfoById(@Param("id") String id);

//	@Query(value = "SELECT * FROM PAYMENTVO WHERE id = ?1  ", nativeQuery = true)
//	List<Object[]> getPaymentInfoById(String id);

}
