package com.borjaglez.repository.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.borjaglez.repository.entity.UserStateEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	private String name;
	
	@Column
	private String lastname;
	
	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn(name = "address_id")
	private Address address;
	
	@Column
	@Enumerated(EnumType.STRING)
	private UserStateEnum state;
	
	@Column(name = "reg_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date regDate;
	
	@Column(name = "mod_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modDate;
	
	public User() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public UserStateEnum getState() {
		return state;
	}

	public void setState(UserStateEnum state) {
		this.state = state;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public Date getModDate() {
		return modDate;
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}
}
