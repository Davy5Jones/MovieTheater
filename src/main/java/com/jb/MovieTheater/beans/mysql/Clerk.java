package com.jb.MovieTheater.beans.mysql;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Clerks")
@NoArgsConstructor
@SuperBuilder
public class Clerk extends User{
}
