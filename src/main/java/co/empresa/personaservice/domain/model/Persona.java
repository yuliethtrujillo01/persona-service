package co.empresa.personaservice.domain.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "No puede estar vacío")
    @Size(min = 2, max = 20, message = "El tamaño tiene que estar entre 2 y 20")
    @Column(nullable = false)
    private String nombre;

    // Nuevo campo: correo (en lugar de descripción)
    @NotEmpty(message = "El correo no puede estar vacío")
    @Email(message = "Debe ser un correo electrónico válido")
    @Column(nullable = false, unique = true)
    private String correo;

    // Nuevo campo: número de teléfono (en lugar de precio)
    @NotEmpty(message = "El número de teléfono es obligatorio")
    @Pattern(
            regexp = "^[0-9]{7,15}$",
            message = "El número de teléfono debe tener entre 7 y 15 dígitos numéricos"
    )
    @Column(nullable = false, unique = true)
    private String telefono;

    // Nuevo campo: cédula (en lugar de foto)
    @NotEmpty(message = "La cédula es obligatoria")
    @Pattern(
            regexp = "^[0-9]{6,12}$",
            message = "La cédula debe contener solo números y tener entre 6 y 12 dígitos"
    )
    @Column(nullable = false, unique = true)
    private String cedula;
}
