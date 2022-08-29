package com.web.event.content;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 **/
@Data
public class SaleOrderContent {
    private BigDecimal quantity;
    private Integer idinventory;


    private Integer idbaseunit;

    private Integer idsubunit;

    private Integer idunit;

    private Integer idwarehouse;
}
