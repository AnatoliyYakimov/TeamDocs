package com.yakimov.teamdocs.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;

import org.springframework.util.DigestUtils;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Document {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String identifier;

	private String name;
	private Date createdAt;
	private Date updatedAt;

	private String text;

	@PrePersist
	public void setDate() {
		updatedAt = new Date();
		if (createdAt == null) {
			createdAt = updatedAt;
			String hash = Long.toHexString(createdAt.getTime());
			identifier = DigestUtils.md5DigestAsHex(hash.getBytes());
		}
	}
	

	public Document(String name, String text) {
		this.name = name;
		this.text = text;
	}
}
