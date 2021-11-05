package com.springboot;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

	//for this we have to create constructor in document class with parameter id.name and size
	@Query("SELECT new Document(d.id,d.name,d.size) FROM Document d ORDER By d.uploadTime DESC")
	List<Document> findAll();
	
}
