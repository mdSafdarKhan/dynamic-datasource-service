package com.example.dynamicdatasourceservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name="users")
public class User {

	@Id
	@Column(name="email")
	private String email;
	
	@Column(name="name")
	private String name;
}
