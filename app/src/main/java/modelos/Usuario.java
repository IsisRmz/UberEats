package modelos;

public class Usuario {

    private int id;
    private String nickname;
    private String nombre;
    private String direccion;
    private String correo;
    private String password;

    //Constructor vacio
    public Usuario() {
    }

    //Constructor para hacer las consultas
    public Usuario(int id, String nickname, String nombre, String direccion, String correo, String password) {
        this.id = id;
        this.nickname = nickname;
        this.nombre = nombre;
        this.direccion = direccion;
        this.correo = correo;
        this.password = password;
    }

    //Constructor para poder agregar usuario no se agrega el id
    public Usuario(String nickname, String nombre, String direccion, String correo, String password) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.direccion = direccion;
        this.correo = correo;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
