package com.ail.narad.repository.custom;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ail.narad.domain.Messagesenders;
import com.ail.narad.domain.enumeration.Message_sender_status;
import com.ail.narad.domain.enumeration.TemplateType;
import com.ail.narad.repository.MessagesendersRepository;

/**
 * Spring Data JPA repository for the Templates entity.
 */
public interface CustomMessagesendersRepository extends MessagesendersRepository {

	@Query("select ms from Messagesenders ms where ms.type = :type and ms.status = :status")
	List<Messagesenders> findMessageSendersByType(@Param("type") TemplateType type, @Param("status") Message_sender_status status);
	
}
