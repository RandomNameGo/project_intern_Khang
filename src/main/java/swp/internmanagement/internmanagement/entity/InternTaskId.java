package swp.internmanagement.internmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class InternTaskId implements Serializable {
    private static final long serialVersionUID = 9061899864845776803L;
    @NotNull
    @Column(name = "task_id", nullable = false)
    private Integer taskId;

    @NotNull
    @Column(name = "intern_id", nullable = false)
    private Integer internId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        InternTaskId entity = (InternTaskId) o;
        return Objects.equals(this.internId, entity.internId) &&
                Objects.equals(this.taskId, entity.taskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(internId, taskId);
    }

}