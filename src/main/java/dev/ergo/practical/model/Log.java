package dev.ergo.practical.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

import static jakarta.persistence.GenerationType.IDENTITY;

@Table(name = "logs")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Log {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name= "id")
    private Integer id;

    @Column(name = "message")
    private String message;

    @Column(name = "type")
    private String type;

    @Column(name = "created")
    private Timestamp created;
}
