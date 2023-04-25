package org.example.controller;


import org.example.comunicaciones.Comunications;
import org.example.models.*;
import org.example.saves.Saves;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Properties;

public class Controller implements Serializable {

    //Atributos
    private ArrayList<User> usuarios;
    private ArrayList<Propietario> propietarios;
    private ArrayList<Admin> admins;

    //Constructor
    public Controller() {
        usuarios = new ArrayList<>();
        propietarios = new ArrayList<>();
        admins = new ArrayList<>();
    }

    //Gets
    public ArrayList<User> getUsuarios() {
        return usuarios;
    }

    public ArrayList<Propietario> getPropietarios() {
        return propietarios;
    }

    public ArrayList<Admin> getAdmins() {
        return admins;
    }


    //MÉTODOS

    //Método para calcular cuantas reservas hay en las viviendas de un usuario
    public int totalReservas(Propietario propietario) {
        int reservas = 0;

        if (propietario.getViviendas() == null) return 0;

        for (Vivienda v :
                propietario.getViviendas()) {
            if (v.getReservas() != null) {
                reservas += v.getReservas().size();
            }
        }
        return reservas;
    }

    //Método para calcular cuantos usuarios se han registrado actualmente
    public int numeroUsuarios() {
        return (usuarios.size() + propietarios.size() + admins.size());
    }

    //Método para calcular cuantas viviendas se han registrado actualmente
    public int numeroViviendas() {
        int resultado = 0;

        for (Propietario p :
                propietarios) {
            if (p.getViviendas() != null)
                resultado += p.getViviendas().size();
        }

        return resultado;
    }

    //Método para calcular cuantas reservas se han realizado actualmente
    public int numeroReservas() {
        int resultado = 0;

        for (Propietario p :
                propietarios) {
            if (p.getViviendas() != null)
                for (Vivienda v :
                        p.getViviendas()) {
                    if (v.getReservas() != null) resultado += v.getReservas().size();
                }
        }

        return resultado;

    }

    //Método para mostrar todas las viviendas registradas.
    public ArrayList<Vivienda> getAllViviendas() {
        ArrayList<Vivienda> resultado = new ArrayList<>();

        for (Propietario p :
                propietarios) {
            if (p.getViviendas() != null)
                resultado.addAll(p.getViviendas());
        }

        return resultado;
    }

    //Método que devuelve el total de reservas que tiene un usuario.
    public int totalReservasUsuario(User user) {
        if (user.getReservas() == null) return 0;
        for (Reserva r :
                user.getReservas()) {
            return user.getReservas().size();
        }
        return 0;
    }

    //Método que devuelve el total de viviendas que tiene un propietario.
    public int totalViviendas(Propietario propietario) {
        if (propietario.getViviendas() == null) return 0;
        for (Vivienda v :
                propietario.getViviendas()) {
            return propietario.getViviendas().size();
        }
        return 0;
    }

    //Método para comprobar si el email introducido ya está registrado en algún otro usuario de cualquier tipo.
    public boolean existeEmail(String email) {

        //Buscamos el email introducido entre todos los usuarios.
        if (!usuarios.isEmpty()) {
            for (User u :
                    usuarios) {
                if (u.getEmail().equals(email)) return true;
            }
        }

        //Buscamos el email introducido entre todos los propietarios.
        if (!propietarios.isEmpty()) {
            for (Propietario p :
                    propietarios) {
                if (p.getEmail().equals(email)) return true;
            }
        }

        //Buscamos el email introducido entre todos los administradores.
        if (!admins.isEmpty()) {
            for (Admin a :
                    admins) {
                if (a.getEmail().equals(email)) return true;
            }
        }

        return false;
    }

    //Método para agregar un nuevo usuario.
    public User addUsuario(String nombre, String passw, String email) {
        User user = new User(generaIdUsuario(), nombre, passw, email);
        usuarios.add(user);
        return user;
    }

    //Método para generar ids para un usuario entre 0 y 1000
    private int generaIdAdmin() {
        int id;
        boolean repetido;

        do {
            repetido = false;
            id = (int) (Math.random() * 1000);

            for (User u :
                    usuarios) {
                if (u.getId() == id) {
                    repetido = true;
                    break;
                }
            }

        } while (repetido);

        return id;
    }

    //Método para agregar un nuevo propietario.
    public Propietario addPropietario(String nombre, String passw, String email) {
        Propietario propietario = new Propietario(generaIdPropietario(), nombre, passw, email);
        propietarios.add(propietario);
        return propietario;
    }

    //Método para generar id única entre 1000 y 2000
    private int generaIdPropietario() {
        int id;
        boolean repetido;

        do {
            repetido = false;
            id = (int) (Math.random() * 1000) + 1000;

            for (Propietario p :
                    propietarios) {
                if (p.getId() == id) {
                    repetido = true;
                    break;
                }
            }

        } while (repetido);

        return id;
    }

    //Método para agregar un nuevo administrador.
    public Admin addAdministrador(String nombre, String passw, String email) {
        Admin admin = new Admin(generaIdAdmin(), nombre, passw, email);
        admins.add(admin);
        return admin;
    }

    //Método para generar id única entre 2000 y 3000
    private int generaIdUsuario() {
        int id;
        boolean repetido;

        do {
            repetido = false;
            id = (int) (Math.random() * 1000) + 2000;

            for (Admin a :
                    admins) {
                if (a.getId() == id) {
                    repetido = true;
                    break;
                }
            }

        } while (repetido);

        return id;
    }

    //Método para realizar el login el cual devuelve un objeto genérico comprobando el email entre todos los tipos de usuarios
    public Object login(String email, String passw) {
        //Comprobamos los usuarios
        if (!usuarios.isEmpty()) {
            for (User u :
                    usuarios) {
                if (u.getEmail().equals(email) && u.getClave().equals(passw)) return u;
            }
        }

        //Comprobamos los propietarios
        if (!propietarios.isEmpty()) {
            for (Propietario p :
                    propietarios) {
                if (p.getEmail().equals(email) && p.getClave().equals(passw)) return p;
            }
        }

        //Comprobamos los admins
        if (!admins.isEmpty()) {
            for (Admin a :
                    admins) {
                if (a.getEmail().equals(email) && a.getClave().equals(passw)) return a;
            }
        }
        return null;
    }

    //Métodos que devuelve todos los usuarios.
    public ArrayList<User> getAllUsuarios() {
        return usuarios;
    }

    //Métodos que devuelve todos los propietarios.
    public ArrayList<Propietario> getAllPropietarios() {
        return propietarios;
    }

    //Métodos que devuelve todos los administradores.
    public ArrayList<Admin> getAllAdmins() {
        return admins;
    }

    //Método que nos devuelve todas las reservas de todos los usuarios.
    public ArrayList<Reserva> getReservas() {
        ArrayList<Reserva> reservas = new ArrayList<>();

        for (User u :
                usuarios) {
            if (u.getReservas() != null) reservas.addAll(u.getReservas());
        }


        return reservas;
    }

    //Método que nos permite modificar el perfil según el tipo de usuario inyectado.
    public boolean modificaPerfil(Object temp) {

        //Comprobamos si el perfil a modificar es de tipo administrador
        if (temp instanceof Admin) {
            for (Admin a :
                    admins) {
                if (a.getId() == ((Admin) temp).getId()) {
                    admins.remove(a);
                    admins.add((Admin) temp);
                    return true;
                }
            }
        }
        //Comprobamos si el perfil a modificar es de tipo propietario
        if (temp instanceof Propietario) {
            for (Propietario p :
                    propietarios) {
                if (p.getId() == ((Propietario) temp).getId()) {

                    for (Vivienda v :
                            p.getViviendas()) {
                        ((Propietario) temp).addVivienda(v);
                    }

                    propietarios.remove(p);
                    propietarios.add((Propietario) temp);
                    return true;
                }
            }
        }
        //Comprobamos si el perfil a modificar es de tipo usuario
        if (temp instanceof User) {
            for (User u :
                    usuarios) {
                if (u.getId() == ((User) temp).getId()) {
                    for (Reserva r :
                            u.getReservas()) {
                        ((User) temp).addReserva(r);
                    }

                    usuarios.remove(u);
                    usuarios.add((User) temp);
                    return true;
                }
            }
        }

        return false;

    }

    //Método que devuelve todas las viviendas de un propietario
    public ArrayList<Vivienda> buscaViviendasByPropietario(Propietario propietario) {
        return propietario.getViviendas();
    }

    //Método que devuelve una arraylist con todas las viviendas que tengan alguna reserva de un propietario
    public ArrayList<Vivienda> getViviendasReservadasPropietario(Propietario propietario) {
        ArrayList<Vivienda> viviendas = new ArrayList<>();

        for (Vivienda v :
                propietario.getViviendas()) {
            if (!v.getReservas().isEmpty()) viviendas.add(v);
        }

        return viviendas;
    }

    //Método que genera id para una vivienda entre 5000 y 6000.
    public int generaIdVivienda() {
        int id;
        boolean repetido;

        do {
            repetido = false;
            id = (int) (Math.random() * 1000) + 5000;

            for (Propietario p :
                    propietarios) {
                for (Vivienda v :
                        p.getViviendas()) {
                    if (v.getId() == id) {
                        repetido = true;
                        break;
                    }
                }
            }

        } while (repetido);

        return id;
    }

    //Método que devuelve una vivienda según la id introducida.
    public Vivienda buscaViviendaId(int id) {

        for (Propietario p :
                propietarios) {
            for (Vivienda v :
                    p.getViviendas()) {
                if (id == v.getId()) return v;
            }
        }
        return null;
    }

    //Método que devuelve una vivienda buscada por id en un propietario concreto.
    public Vivienda buscaViviendaByIdPropietario(int id, Propietario propietario) {

        if (propietario.getViviendas().isEmpty()) return null;

        for (Vivienda v :
                propietario.getViviendas()) {
            if (v.getId() == id) return v;
        }

        return null;
    }

    //Método que devuelve las reservas de un usuario.
    public ArrayList<Reserva> getReservasByUser(User user) {
        return user.getReservas();
    }

    //Método que permite modificar una vivienda. Si no se encuentra esa vivienda devuelve false.
    public boolean modificaVivienda(Vivienda viviendaNueva) {
        for (Propietario p :
                propietarios) {
            for (Vivienda v :
                    p.getViviendas()) {
                if (p.getViviendas().isEmpty()) return false;
                if (v.getId() == viviendaNueva.getId()) {
                    if (v.getReservas().isEmpty()) {
                        p.getViviendas().remove(v);
                        p.getViviendas().add(viviendaNueva);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    //Método que devuelve todas las viviendas que contengan en su descripción
    public ArrayList<Vivienda> buscaViviendaByParametro(String parametro) {
        ArrayList<Vivienda> resultado = new ArrayList<>();
        for (Propietario p :
                propietarios) {
            for (Vivienda v :
                    p.getViviendas()) {
                if (p.getViviendas().isEmpty()) break;
                if (v.getDescripcion().toLowerCase().strip().contains(parametro.toLowerCase().strip()))
                    resultado.add(v);
            }
        }

        return resultado;

    }

    //Método que genera id para una reserva entre 6000 y 7000.
    public int generaIdReserva() {
        int id;
        boolean repetido;

        do {
            repetido = false;
            id = (int) (Math.random() * 1000) + 6000;

            for (User u :
                    usuarios) {
                for (Reserva r :
                        u.getReservas()) {
                    if (r.getId() == id) {
                        repetido = true;
                        break;
                    }
                }
            }

        } while (repetido);

        return id;
    }

    //Método para añadir una nueva reserva. Si el user es null es una reserva de periodo de no disponibilidad
    public void addReserva(Vivienda v, User user, Reserva reserva) {
        v.getReservas().add(reserva);
        if (user != null) user.addReserva(reserva);
    }

    //Método para buscar un usuario por su Id.
    private User buscaUserById(int idUsuario) {
        for (User u :
                usuarios) {
            if (u.getId() == idUsuario) return u;
        }
        return null;
    }

    //Método que devuelve el propietario de una vivienda mediante su id.
    public Propietario buscaPropietarioIdVivienda(int id) {
        for (Propietario p :
                propietarios) {
            if (propietarios.isEmpty()) return null;
            if (p.getViviendas().isEmpty()) continue;
            for (Vivienda v :
                    p.getViviendas()) {
                if (v.getId() == id) return p;
            }
        }
        return null;
    }

    public void enviaCorreo(String asunto, String contenido) {
        Comunications.enviarCorreo(asunto, contenido);
    }

    public void guardarDatos() {
        Saves.generarBackUp(this, "data");
    }

    public ArrayList<String> obtenerConfiguracion(Properties p) {
        ArrayList<String> resultado = new ArrayList<>();
        // Obtenemos todas las claves
        for (String clave : p.stringPropertyNames()) {
            // Obtener el valor correspondiente a la clave
            String valor = p.getProperty(clave);

            // Mostramos la clave y el valor
            String contenido = clave + ", "+valor;
            resultado.add(contenido);
        }
        return resultado;
    }

    public void escribirLogUsuario(String nuevaReserva, User user, int idVivienda, Reserva r) {
        Saves.escribeLogUsuario("nueva reserva", user, r.getIdVivienda());
    }

    public void escribirLogAdmin(String inicioSesion, Admin admin) {
        Saves.escribeLogAdministrador("inicio sesion", admin);
    }

    public boolean generarBackUp(Controller c, String ruta) {
        return Saves.generarBackUp(c, ruta);
    }

    public boolean existeBackUp(String ruta) {
        return Saves.existeBackUp(ruta);
    }

    public Object recuperarBackUp(String ruta) {
        return Saves.recuperaBackUp(ruta);
    }

    public void escribirLogPropietario(String inicioSesion, Propietario propietario, int i) {
        Saves.escribeLogPropietario("inicio sesion", propietario, -1);
    }

    public void enviaCorreoCSV(String asunto, ArrayList<Vivienda> viviendas) {
        Comunications.enviarCorreoCVS(asunto, viviendas);
    }
}
