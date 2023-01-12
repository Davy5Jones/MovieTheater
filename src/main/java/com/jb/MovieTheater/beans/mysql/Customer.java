package com.jb.MovieTheater.beans.mysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "customers")
@NoArgsConstructor
@SuperBuilder
public class Customer extends User{

}
