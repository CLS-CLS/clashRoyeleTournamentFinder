package org.lytsiware.clash.tournament;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class Tournament {
    private String tag;
    private String type;
    private String status;
    private String creatorTag;
    private String name;
    private String description;
    private int capacity;
    private int maxCapacity;
    private String preparationDuration;
    private String duration;
    @JsonFormat(pattern = "yyyyMMdd'T'HHmmss.SSS'Z'")
    private LocalDateTime createdTime;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tournament that = (Tournament) o;

        return tag != null ? tag.equals(that.tag) : that.tag == null;
    }

    @Override
    public String toString() {
        return String.format("%1$20.20s", name)
                + " "
                + String.format("%1$2s/%2$2s", capacity,maxCapacity)
                + "  " + createdTime.atZone(ZoneId.of("GMT")).withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    @Override
    public int hashCode() {
        return tag != null ? tag.hashCode() : 0;
    }
}
