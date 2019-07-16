package com.yakimov.teamdocs.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.util.DigestUtils;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Document{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String hash;
	
	@NotBlank(message = "Document`s name can`t be blank")
	private String name;
	@NotNull
	@Column(name = "document_text", columnDefinition = "TEXT NOT NULL")
	private String text;
	
	private Date createdAt;
	private Date updatedAt;
	
	private String author;
	private String modifiedBy;

	@PrePersist
	public void setDate() {
		updatedAt = new Date();
		if (createdAt == null) {
			createdAt = updatedAt;
			String key = Long.toHexString(createdAt.getTime());
			hash = DigestUtils.md5DigestAsHex(key.getBytes());
		}
		if (author == null) {
			author = modifiedBy;
		}
	}
	
	@PreUpdate
	public void updateDate() {
		updatedAt = new Date();
	}
	

	public Document(String name, String text) {
		this.name = name;
		this.text = text;
	}
}
