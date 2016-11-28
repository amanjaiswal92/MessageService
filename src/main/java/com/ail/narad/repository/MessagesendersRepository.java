package com.ail.narad.repository;

import com.ail.narad.domain.Messagesenders;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Messagesenders entity.
 */
public interface MessagesendersRepository extends JpaRepository<Messagesenders,Long> {

}
