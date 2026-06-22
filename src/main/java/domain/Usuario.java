package domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author AaCcAio
 */

@Data
@Entity
@Table(name = "usuario")

public class Usuario implements Serializable {
    
}
