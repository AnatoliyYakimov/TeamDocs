package com.yakimov.teamdocs.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.yakimov.teamdocs.entities.Document;

public interface DocumentRepository extends CrudRepository<Document, Long>{
	@Query(value = "select * from document where original_Document_Id = ? order by updated_At desc limit 1", nativeQuery = true)
	public Optional<Document> findLastUpdatedVersionOfDocumentWithId(Long id);
	
	@Query(value = "select * from document where original_Document_Id = ? order by updated_At asc", nativeQuery = true)
	public Iterable<Document> findChangeHistoryOfDocumentSortAscending(Long id);
	
	@Query(value = "select * from document where original_Document_Id = ? order by updated_At desc", nativeQuery = true)
	public Iterable<Document> findChangeHistoryOfDocumentSortDescending(Long id);
}
