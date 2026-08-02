package the.aditya.live_chat_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.OrderBy;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String roomId;

    @OneToMany(
            mappedBy = "room",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("timeStamp ASC")
    @JsonIgnore
    private List<Message> messages = new ArrayList<>();

}
