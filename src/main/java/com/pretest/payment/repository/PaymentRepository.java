package com.pretest.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pretest.payment.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

//	@Query(value = "SELECT * FROM PAYMENT WHERE ID = :id AND PAY_TYPE = :payType ", nativeQuery = true)
//	List<Object[]> getPaymentInfoById(@Param("id") String id, String payType);

	@Query(value = "SELECT SUM(AMOUNT) AS AMOUNT_SUM , SUM(VAT) AS VAT_SUM FROM PAYMENT WHERE REF_ID = :refId", nativeQuery = true)
	List<Object[]> getAmtVatSumByRefId(@Param("refId") String refId);

}
