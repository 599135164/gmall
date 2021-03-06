package com.hui.gmall.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/8 18:12
 */
@Data
public class BaseCatalog1 implements Serializable {
    @Id
    @Column
    private String id;
    @Column
    private String name;

}
