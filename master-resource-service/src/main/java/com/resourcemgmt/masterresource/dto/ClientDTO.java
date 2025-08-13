package com.resourcemgmt.masterresource.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {

    private Long id;
    private String name;
    private String code;
    private String contactEmail;
    private String contactPhone;
    private Boolean isActive = true;
    private LocalDateTime createdAt;
}
