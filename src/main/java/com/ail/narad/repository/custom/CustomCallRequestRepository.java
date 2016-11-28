package com.ail.narad.repository.custom;

import com.ail.narad.domain.CallRequest;
import com.ail.narad.repository.CallRequestRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomCallRequestRepository extends CallRequestRepository {
	
	@Query("select cr from CallRequest cr where cr.consignmentid = :consid and cr.phoneno = :phone and cr.cancelled = :cancelled")
	List<CallRequest> findCancealStatus(@Param("consid") String consid,@Param("phone") String phone,@Param("cancelled") boolean cancelled);
	
	@Query("select cr from CallRequest cr where cr.consignmentid = :consid and cr.phoneno = :phone  and cr.requestId = :requestid" )
	CallRequest findCancealStatusWithConsAndPhone(@Param("consid") String consid,@Param("phone") String phone,@Param("requestid") String requestid);

}
