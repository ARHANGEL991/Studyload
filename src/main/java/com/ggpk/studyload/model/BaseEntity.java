package com.ggpk.studyload.model;

import com.ggpk.studyload.model.interfaces.IBaseEntity;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@ToString
public abstract class BaseEntity implements IBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

}