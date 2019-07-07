package com.yakimov.teamdocs.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

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

	private String name;
	private Date createdAt;
	private Date updatedAt;

	private String text;

	@PrePersist
	public void setDate() {
		updatedAt = new Date();
		if (createdAt == null) {
			createdAt = updatedAt;
			String key = Long.toHexString(createdAt.getTime());
			hash = DigestUtils.md5DigestAsHex(key.getBytes());
		}
	}
	
	@PreUpdate
	public void updateDate() {
		updatedAt = new Date();
		id = null;
	}
	

	public Document(String name, String text) {
		this.name = name;
		this.text = text;
	}
}
