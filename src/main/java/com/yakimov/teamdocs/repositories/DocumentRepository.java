package com.yakimov.teamdocs.repositories;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.yakimov.teamdocs.entities.Document;

public interface DocumentRepository extends CrudRepository<Document, Long>{
	
	@Query(value = "select * from document where hash = ? order by updated_At desc limit 1", nativeQuery = true)
	public Optional<Document> findLastVersionOfDocumentWith(String identifier);
	
	@Query(value = "select * from document where hash = ? order by updated_At asc", nativeQuery = true)
	public Iterable<Document> findChangeHistoryOfDocumentSortAscending(Long id);
	
	public Page<Document> findAllByHash(String hash, Pageable page);
	
	@Query(value = "select * from document where hash = ? order by updated_At desc", nativeQuery = true)
	public Iterable<Document> findChangeHistoryOfDocumentSortDescending(Long id);
	
	@Query(value = "select author from document where hash = ? order by updated_At asc limit 1", nativeQuery = true)
	public String getAuthorOfDocument(String hash);
	
	@Query(value = "select created_at from document where hash = ? order by updated_At asc limit 1", nativeQuery = true)
	public Date getDocumentCreationTime(String hash);
}
