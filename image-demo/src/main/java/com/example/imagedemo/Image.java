package com.example.imagedemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="images")
@AllArgsConstructor
@Data
@NoArgsConstructor
@ToString
public class Image {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "gallery")
    private String gallery;

    @ManyToOne
    private User user;

    @Lob
    @Column(name = "pic")
    private byte[] pic;

    //Custom Construtor
    public Image(String name, String type, byte[] pic, User user, String gallery) {
        this.name = name;
        this.type = type;
        this.pic = pic;
        this.user = user;
        this.gallery = gallery;
    }
}
