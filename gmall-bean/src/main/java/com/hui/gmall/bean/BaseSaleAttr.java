package com.hui.gmall.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/10 20:53
 */
@Data
public class BaseSaleAttr implements Serializable {
    @Id
    @Column
    String id;

    @Column
    String name;
}
