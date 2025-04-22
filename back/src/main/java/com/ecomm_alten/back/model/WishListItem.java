package com.ecomm_alten.back.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "wish_list_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Product product;


}