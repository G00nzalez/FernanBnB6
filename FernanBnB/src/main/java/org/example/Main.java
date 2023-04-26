package org.example;

import org.example.controller.Controller;
import org.example.models.*;
import org.example.saves.Saves;
import org.example.utils.Utils;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    public final static String RUTA_PROPERTIES = "configuracion/config.properties";
    public final static String RUTA_LASTACCESS = "configuracion/lastaccess.properties";

    public static Scanner s = new Scanner(System.in);

    public static void main(String[] args) {
        Controller c;
        Properties p = new Properties();
        try {
            p.load(new FileReader(RUTA_PROPERTIES));

        } catch (IOException e) {
            System.out.println("Error al cargar el properties.");
        }

        //Buscamos si existe algún fichero en la ruta especificada en el properties
        File f = new File((String) p.get("rutaData"));

        if (f.exists()) { //Si encuentra el fichero de copia, lo carga.
            //System.out.println("Fichero encontrado.");
            c = (Controller) Saves.recuperaBackUp((String) p.get("rutaData"));
            System.out.println("Fichero cargado");

        } else { //Si no encuentra el fichero de copia de seguridad, genera un nuevo controler con el mock que tenemos.
            c = new Controller();
            mock(c);
        }

        Object user = null;

        int op = -1;

        do {
            op = menuInicio();
            switch (op) {
                case 1 -> { //Loggear un usuario,
                    user = login(c);
                    if (user == null) System.out.println("Credenciales incorrectas");
                }

                case 2 -> { //Registrar un usuario nuevo,
                    user = registro(c);
                    if (user == null)
                        System.out.println("Error al registrar su usuario." + '\n' + "Pruebe con otro email.");
                }

                case -1 -> {
                    System.out.println("Debe introducir un número para este menú");
                }

                case 3 -> { //Cerrar programa,
                    System.out.println("Cerrando programa");
                    Utils.cerrarPrograma();
                }
                default -> System.out.println("Opción incorrecta.");
            }

            // Si el usuario ya está loggeado
            if (user != null) {
                if (user instanceof User) menuUsuario(p, c, (User) user);
                if (user instanceof Propietario) menuPropietario(c, (Propietario) user);
                if (user instanceof Admin) menuAdmin(c, (Admin) user, p);
            }

            user = null;

        } while (op != 3);

    }

    //mock
    private static void mock(Controller c) {
        c.addAdministrador("admin", "admin", "admin@fernanbnb.com");
        c.addAdministrador("admin2", "admin", "admin2@fernanbnb.com");
        c.addUsuario("user", "user", "user@fernanbnb.com");
        c.addUsuario("user2", "user", "user2@fernanbnb.com");
        c.addPropietario("propietario", "propietario", "propietario@fernanbnb.com");
        c.addPropietario("propietario2", "propietario", "propietario2@fernanbnb.com");

    }

    //Registro de un nuevo usuario.
    private static Object registro(Controller c) {
        int op = 0;
        String nombre, passw, email;

        System.out.println("===== REGISTRO =====");
        do {
            System.out.println("""
                    Seleccione su tipo de usuario
                    1. Usuario
                    2. Propietario""");
            try {
                op = Integer.parseInt(s.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opción incorrecta, debe introducir un número");
            }

        } while (op != 1 && op != 2 && op != 100);

        System.out.print("Nombre de usuario: ");
        nombre = s.nextLine();
        System.out.print("Contraseña: ");
        passw = s.nextLine();
        System.out.print("Email: ");
        email = s.nextLine();

        if (c.existeEmail(email)) return null;

        if (op == 1) return c.addUsuario(nombre, passw, email);
        if (op == 2) return c.addPropietario(nombre, passw, email);
        if (op == 100) return c.addAdministrador(nombre, passw, email);


        return null;
    }

    //Log de usuario.
    private static Object login(Controller c) {
        String passw, email;

        System.out.println("===== LOGGING =====");

        System.out.print("Introduzca su email: ");
        email = s.nextLine();
        System.out.print("Contraseña: ");
        passw = s.nextLine();

        return c.login(email, passw);

    }

    //Menu para los administradores.
    private static void menuAdmin(Controller c, Admin admin, Properties p) {
        c.escribirLogAdmin("inicio sesion", admin);

        int op = 0;

        do {
            System.out.println("^^^^ MENU ADMINISTRADOR ^^^^");
            System.out.println("Bienvenido: " + admin.getNombre());
            assert c != null;
            System.out.println("Tiene " + c.numeroUsuarios() + " usuarios. Viviendas " + c.numeroViviendas() + ". Reservas " + c.numeroReservas());
            System.out.println("""
                    Menú de operaciones:
                    1. Ver todas las viviendas en alquiler.
                    2. Ver todos los usuarios.
                    3. Ver todas las reservas.
                    4. Ver perfil.
                    5. Modificar perfil.
                    6. Mostrar configuración.
                    7. Enviar listado de reservas por correo.
                    8. Realizar copia de seguridad.
                    9. Recuperar copia de seguridad.
                    10. Salir""");
            try {
                op = Integer.parseInt(s.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Debe introducir un número en este menú.");
            }

            switch (op) {
                case 1 -> { //Ver todas las viviendas
                    ArrayList<Vivienda> mostrarViviendas = c.getAllViviendas();
                    if (mostrarViviendas.isEmpty()) System.out.println("Aún no se ha registrado ninguna vivienda");
                    else System.out.println(mostrarViviendas);
                    Utils.pulseParaContinuar();
                }
                case 2 -> { //Ver todos los usuarios
                    //Mostramos los usuarios normales.
                    ArrayList<User> usuarios = c.getAllUsuarios();
                    if (usuarios.isEmpty()) System.out.println("Aún no se ha registrado ningún usuario.");
                    else {
                        for (User u :
                                usuarios) {
                            System.out.println(u);
                        }
                    }
                    System.out.println();

                    //Mostramos los propietarios.
                    ArrayList<Propietario> propietarios = c.getAllPropietarios();
                    if (propietarios.isEmpty()) System.out.println("Aún no se ha registrado ningún propietario.");
                    else {
                        for (Propietario pr :
                                propietarios) {
                            System.out.println(pr);
                        }
                    }
                    System.out.println();
                    //Mostramos los admins
                    ArrayList<Admin> admins = c.getAllAdmins();
                    if (admins.isEmpty()) System.out.println("Aún no se ha registrado ningún administrador.");
                    else {
                        for (Admin a :
                                admins) {
                            System.out.println(a);
                        }
                    }
                    Utils.pulseParaContinuar();
                }
                case 3 -> { //Ver todas las reservas
                    ArrayList<Reserva> reservas = c.getReservas();
                    if (reservas.isEmpty()) System.out.println("No se ha realizado ninguna reserva aún.");
                    else {
                        for (Reserva r :
                                reservas) {
                            System.out.println(r);
                        }
                    }
                    Utils.pulseParaContinuar();
                }
                case 4 -> { //Ver perfil
                    System.out.println(admin);
                    Utils.pulseParaContinuar();
                }
                case 5 -> { //Modificar perfil
                    System.out.println("*** Modificación de perfil ***");
                    System.out.print("Introduce el nuevo nombre de perfil: ");
                    String nuevoNombre = s.nextLine();
                    System.out.print("Introduce la nueva contraseña: ");
                    String nuevoPassw = s.nextLine();
                    System.out.print("Introduce el nuevo email: ");
                    String nuevoEmail = s.nextLine();

                    if (c.existeEmail(nuevoEmail)) {
                        System.out.println("Email ya en uso. Pruebe con otro.");
                        break;
                    }

                    Admin temp = new Admin(admin.getId(), nuevoNombre, nuevoPassw, nuevoEmail);
                    if (c.modificaPerfil(temp)) {
                        System.out.println("""
                                Perfil modificado con éxito
                                Cierre sesión y vuelva a iniciar para ver sus cambios reflejados.""");

                    } else System.out.println("Error al modificar el perfil. Inténtelo más tarde.");

                    Utils.pulseParaContinuar();
                }
                case 6 -> { //Mostrar configuración. También muestra la última conexión de cada usuario.
                    System.out.println("*** Configuración ***");
                    ArrayList<String> configuracion = c.obtenerConfiguracion(p);

                    for (int i = 0; i < configuracion.size(); i++) {
                        System.out.println(configuracion.get(i));
                        System.out.println("-----");
                    }

                    Utils.pulseParaContinuar();
                }
                case 7 -> { //Enviar reservas al correo.
                    System.out.println("*** Envío de reservas al correo ***");
                    ArrayList<Propietario> propietarios = c.getPropietarios();
                    ArrayList<Vivienda> viviendas = new ArrayList<>();

                    //Obtenemos todas las viviendas registradas en el sistema.
                    for (Propietario propietario : propietarios
                    ) {
                        viviendas.addAll(propietario.getViviendas());
                    }

                    if (viviendas.isEmpty()) System.out.println("No existen viviendas registradas aún.");
                    else {
                        c.enviaCorreoCSV("Resumen de todas las viviendas", viviendas);
                        System.out.println("Resumen de todas las viviendas enviadas a su correo.");
                    }

                    Utils.pulseParaContinuar();
                }
                case 8 -> { //Realizar copia de seguridad.
                    System.out.println("*** Copia de Seguridad ***");

                    System.out.print("Introduzca la ruta donde desea generar la copia de seguridad: ");
                    String ruta = s.nextLine();

                    if (c.generarBackUp(c, ruta))
                        System.out.println("Copia de seguridad generada con éxito en: " + ruta + "/backup.out");
                    else System.out.println("No se pudo realizar la copia de seguridad.");

                    Utils.pulseParaContinuar();
                }
                case 9 -> { //Recuperar copia de seguridad
                    System.out.println("*** Recuperación de copia de seguridad ***");

                    System.out.print("Introduzca la ruta donde desea generar la copia de seguridad: ");
                    String ruta = s.nextLine();


                    if (c.existeBackUp(ruta)) {
                        if (c.recuperarBackUp(ruta) != null) {
                            c = (Controller) c.recuperarBackUp(ruta);
                            System.out.println("Copia recuperada con éxito.");
                        } else System.out.println("No se pudo recuperar la copia de seguridad");
                    } else System.out.println("Ruta incorrecta. Pruebe introduciendo otra.");

                    Utils.pulseParaContinuar();
                }
                case 10 -> { //Cerrar sesión
                    c.escribirLogAdmin("cerrar sesion", admin);
                    c.guardarDatos();
                    System.out.print("Cerrando sesión ");
                    Utils.cerrarPrograma();
                }

            }
        } while (op != 10);
    }

    //Menu para los propietarios
    private static void menuPropietario(Controller c, Propietario propietario) {
        c.escribirLogPropietario("inicio sesion", propietario, -1);

        int op = 0;

        do {
            System.out.println("^^^^ MENU PROPIETARIO ^^^^");
            System.out.println("Bienvenido: " + propietario.getNombre() + ". Tiene " + c.totalViviendas(propietario) + " viviendas.");
            System.out.println("Tiene " + c.totalReservas(propietario) + " reservas en sus alojamientos.");
            System.out.println("""
                    Menú de operaciones:
                    1. Ver mis viviendas en alquiler.
                    2. Editar mis viviendas.
                    3. Ver las reservas de mis viviendas.
                    4. Establecer un periodo de no disponibilidad para una vivienda.
                    5. Ver mi perfil.
                    6. Modificar mi perfil.
                    7. Salir""");
            try {
                op = Integer.parseInt(s.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Debe introducir un número en este menú.");
            }

            switch (op) {
                case 1 -> { //Ver viviendas en alquiler
                    ArrayList<Vivienda> viviendas = c.buscaViviendasByPropietario(propietario);
                    if (!viviendas.isEmpty()) {
                        for (Vivienda v :
                                viviendas) {
                            System.out.println(v);
                        }
                    } else System.out.println("No hay viviendas registradas aún");
                    Utils.pulseParaContinuar();
                }
                case 2 -> { //Editar viviendas
                    int maxOcupantes = 0, id = -1;
                    double precioNoche = 0.0;

                    System.out.println("--- Editar una vivienda ---");
                    do {
                        System.out.print("Introduzca la id de la vivienda (pulse 0 para registrar una nueva vivienda): ");
                        try {
                            id = Integer.parseInt(s.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Debe introducir un valor numérico.");
                        }
                    } while (id < 0);

                    if (id == 0) System.out.println("--- Registro de nueva vivienda ---");
                    else {
                        System.out.print("¿Desea eliminar esa vivienda? s/n: ");
                        //Eliminación de una vivienda.
                        if (s.nextLine().equals("s")) {
                            Vivienda v = c.buscaViviendaId(id);

                            //Comprobación de sí la vivienda existe y contiene reservas.
                            if (v != null && v.getReservas().isEmpty()) {
                                propietario.getViviendas().remove(v);
                                System.out.println("Se ha eliminado la vivienda con id " + id);
                                c.escribirLogPropietario("eliminar vivienda", propietario, id);

                            } else
                                System.out.println("No se puede eliminar una vivienda que tenga reservas realizadas.");
                            break;
                        }
                    }
                    System.out.print("Introduce el título: ");
                    String titulo = s.nextLine();
                    System.out.print("Introduce la descripción: ");
                    String descripcion = s.nextLine();
                    System.out.print("Introduce la localidad: ");
                    String localidad = s.nextLine();
                    System.out.print("Introduce la provincia: ");
                    String provincia = s.nextLine();
                    do {
                        System.out.print("Introduce el número máximo de ocupantes: ");
                        try {
                            maxOcupantes = Integer.parseInt(s.nextLine());

                        } catch (NumberFormatException e) {
                            System.out.println("Debe introducir un valor numérico.");
                        }
                    } while (maxOcupantes < 1);

                    do {
                        System.out.print("Introduce el precio por noche: ");
                        try {
                            precioNoche = Double.parseDouble(s.nextLine());

                        } catch (NumberFormatException e) {
                            System.out.println("Debe introducir un valor numérico.");
                        }
                    } while (precioNoche < 1);

                    //Si la id = 0 significa que es una vivienda nueva, entonces hay que generarle una id nuevo. Si es!= 0, introducimos la id que
                    Vivienda temp;
                    if (id == 0) {
                        temp = new Vivienda(c.generaIdVivienda(), titulo, descripcion, localidad, provincia, maxOcupantes, precioNoche);
                    } else {
                        temp = c.buscaViviendaByIdPropietario(id, propietario);
                    }

                    if (temp == null) System.out.println("Vivienda no encontrada. Compruebe la id de su vivienda.");
                    else {

                        if (propietario.addVivienda(temp)) {
                            if (id == 0) {
                                System.out.println("Vivienda registrada con éxito");
                                c.escribirLogPropietario("insertar vivienda", propietario, temp.getId());
                            } else System.out.println("Vivienda modificada con éxito.");
                        } else {
                            if (id == 0) System.out.println("Error al registrar su vivienda.");
                            else System.out.println("Error al modificar su vivienda. ");
                        }
                    }
                    c.guardarDatos();
                    Utils.pulseParaContinuar();
                }
                case 3 -> { //Ver reservas en viviendas
                    ArrayList<Vivienda> viviendas = c.getViviendasReservadasPropietario(propietario);
                    if (viviendas.isEmpty()) System.out.println("No han realizado ninguna reserva aún.");
                    for (Vivienda v :
                            viviendas) {
                        System.out.println(v);
                    }
                    Utils.pulseParaContinuar();
                }
                case 4 -> { //Establecer periodo de no disponibilidad
                    int id = -1;
                    System.out.println("=== PERIODO NO DISPONIBILIDAD ===");
                    do {
                        System.out.print("Introduce la id de la vivienda (0 para cancelar): ");
                        try {
                            id = Integer.parseInt(s.nextLine());

                        } catch (NumberFormatException e) {
                            System.out.println("Debe introducir un valor numérico.");
                        }
                        if (id == 0) break;

                    } while (id < 5000 || id > 6000);

                    Vivienda v = c.buscaViviendaId(id);
                    if (v != null) {
                        int day = -1, month = -1, year = -1, noches = -1;
                        System.out.print("Introduzca el día de entrada: ");
                        try {
                            day = Integer.parseInt(s.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Introduzca un valor numérico");
                        }
                        System.out.print("Introduzca el mes de entrada: ");
                        try {
                            month = Integer.parseInt(s.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Introduzca un valor numérico");
                        }
                        System.out.print("Introduzca el año de entrada: ");
                        try {
                            year = Integer.parseInt(s.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Introduzca un valor numérico");
                        }
                        System.out.print("Introduzca el número de noches: ");
                        try {
                            noches = Integer.parseInt(s.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Introduzca un valor numérico");
                        }

                        LocalDate fecha = LocalDate.of(year, month, day);

                        if (v.tieneDisponibilidad(fecha, noches)) {
                            c.addReserva(v, null, new Reserva(c.generaIdReserva(), v.getId(), propietario.getId(), fecha, noches, v.getPrecioNoche(), -1));
                            c.escribirLogPropietario("indisponibilidad", propietario, v.getId());
                            c.guardarDatos();
                        } else System.out.println("No se ha podido realizar la operación por falta de días hábiles.");
                    } else System.out.println("Vivienda no encontrada. Compruebe que ha insertado bien la id.");
                }
                case 5 -> { //Ver perfil
                    System.out.println(propietario);
                    Utils.pulseParaContinuar();
                }
                case 6 -> { //Modificar perfil
                    System.out.println("*** Modificación de perfil ***");
                    System.out.print("Introduce el nuevo nombre de perfil: ");
                    String nuevoNombre = s.nextLine();
                    System.out.print("Introduce la nueva contraseña: ");
                    String nuevoPassw = s.nextLine();
                    System.out.print("Introduce el nuevo email: ");
                    String nuevoEmail = s.nextLine();

                    if (c.existeEmail(nuevoEmail)) {
                        System.out.println("Email ya en uso. Pruebe con otro.");
                        break;
                    }

                    Propietario temp = new Propietario(propietario.getId(), nuevoNombre, nuevoPassw, nuevoEmail);
                    if (c.modificaPerfil(temp)) {
                        System.out.println("""
                                Perfil modificado con éxito
                                Cierre sesión y vuelva a iniciar para ver sus cambios reflejados.""");

                    } else System.out.println("Error al modificar el perfil. Inténtelo más tarde.");

                    c.guardarDatos();
                    Utils.pulseParaContinuar();
                }
                case 7 -> { //Cerrar
                    c.escribirLogPropietario("cerrar sesion", propietario, -1);
                    System.out.print("Cerrando sesión ");
                    c.guardarDatos();
                    Utils.cerrarPrograma();
                }
            }
        } while (op != 7);
    }

    //Menu para los usuarios.
    private static void menuUsuario(Properties p, Controller c, User user) {
        Saves.escribeLogUsuario("inicio sesion", user, -1);
        Saves.escribeUltimoAccesoUsuario(p, user);
        Saves.escribeProperties(RUTA_LASTACCESS, user.getEmail() + "LastAccess", Utils.formateaFecha(LocalDate.now()));
        int op = 0;

        do {
            System.out.println("^^^^ MENU USUARIO ^^^^");
            System.out.println("Bienvenido: " + user.getNombre() + ". Tiene " + c.totalReservasUsuario(user) + " reservas pendientes.");
            System.out.println("Último acceso: " + p.get(user.getEmail() + "LastAccess"));
            System.out.println("""
                    Menú de operaciones:
                    1. Búsqueda de alojamientos.
                    2. Ver mis reservas.
                    3. Modificar mis reservas.
                    4. Ver mi perfil.
                    5. Modificar mis datos.
                    6. Enviar reserva como PDF.
                    7. Salir""");
            try {
                op = Integer.parseInt(s.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Debe introducir un número en este menú.");
            }

            switch (op) {
                case 1 -> { //Búsqueda de alojamientos.
                    System.out.print("=== BUSQUEDA DE ALOJAMIENTOS ===");
                    System.out.print("Introduzca una descripción con la cual filtrar: ");
                    String parametro = s.nextLine();

                    ArrayList<Vivienda> viviendas = c.buscaViviendaByParametro(parametro);
                    if (viviendas.isEmpty())
                        System.out.println("No se ha encontrado ninguna vivienda con esa busqueda.");
                    else {
                        for (int i = 0; i < viviendas.size(); i++) {
                            System.out.println((i + 1) + ") " + viviendas.get(i).getTitulo());
                        }

                        int reserva = -1;

                        do {
                            System.out.print("¿Qué vivienda quiere reserva? Introduzca 0 para cancelar la reserva: ");
                            try {
                                reserva = Integer.parseInt(s.nextLine()) - 1;
                            } catch (NumberFormatException e) {
                                System.out.println("Debe introducir un valor numérico");
                            }

                            if (reserva > viviendas.size() + 1) System.out.println("Número no válido.");

                        } while (reserva == -1 && reserva < viviendas.size() + 1);

                        if (reserva == -1) System.out.println("Se ha cancelado su reserva.");
                        else {
                            int ocupantes = -1;
                            Vivienda v = viviendas.get(reserva);

                            do {
                                System.out.print("Introduzca el número de ocupantes: ");
                                try {
                                    ocupantes = Integer.parseInt(s.nextLine());
                                } catch (NumberFormatException e) {
                                    System.out.println("Introduzca un valor numérico");
                                }

                            } while (ocupantes < 0);
                            if (ocupantes > v.getMaxOcupantes()) {
                                System.out.println("Ha introducido más ocupantes de los permitidos");
                                break;
                            } else {
                                int day = -1, month = -1, year = -1, noches = -1;
                                System.out.print("Introduzca el día de entrada: ");
                                try {
                                    day = Integer.parseInt(s.nextLine());
                                } catch (NumberFormatException e) {
                                    System.out.println("Introduzca un valor numérico");
                                }
                                System.out.print("Introduzca el mes de entrada: ");
                                try {
                                    month = Integer.parseInt(s.nextLine());
                                } catch (NumberFormatException e) {
                                    System.out.println("Introduzca un valor numérico");
                                }
                                System.out.print("Introduzca el año de entrada: ");
                                try {
                                    year = Integer.parseInt(s.nextLine());
                                } catch (NumberFormatException e) {
                                    System.out.println("Introduzca un valor numérico");
                                }
                                System.out.print("Introduzca el número de noches: ");
                                try {
                                    noches = Integer.parseInt(s.nextLine());
                                } catch (NumberFormatException e) {
                                    System.out.println("Introduzca un valor numérico");
                                }

                                LocalDate fecha = LocalDate.of(year, month, day);
                                if (v.tieneDisponibilidad(fecha, noches)) {

                                    Reserva r = new Reserva(c.generaIdReserva(), v.getId(), user.getId(), fecha, noches, v.getPrecioNoche(), ocupantes);
                                    c.addReserva(v, user, r);
                                    c.escribirLogUsuario("nueva reserva", user, r.getIdVivienda(), r);
                                    System.out.print("Enviando PDF");
                                    Utils.cerrarPrograma();
                                    c.enviarReservaPDF(r);
                                } else System.out.println("Reserva no disponible.");

                            }
                        }
                    }

                    c.guardarDatos();
                    Utils.pulseParaContinuar();
                }
                case 2 -> { //Ver reservas.
                    ArrayList<Reserva> reservas = c.getReservasByUser(user);

                    if (!reservas.isEmpty()) {

                        for (Reserva r :
                                reservas) {
                            System.out.println(r);
                        }
                    } else System.out.println("No se han realizado reservas todavía.");

                    Utils.pulseParaContinuar();

                }
                case 3 -> { //Modificar reservas.
                    ArrayList<Reserva> reservas = c.getReservasByUser(user);
                    int reservaSeleccionada = -1;
                    if (!reservas.isEmpty()) {

                        for (int i = 0; i < reservas.size(); i++) {
                            System.out.println("Reserva número: " + (i + 1) + '\n'
                                    + reservas.get(i) + '\n' +
                                    "=========");
                        }

                        do {
                            System.out.print("Indica que reserva quieres modificar: ");
                            try {
                                reservaSeleccionada = Integer.parseInt(s.nextLine()) - 1;
                            } catch (NumberFormatException e) {
                                System.out.println("Debe introducir un valor numérico");
                            }
                        } while (reservaSeleccionada == -1 || reservaSeleccionada > reservas.size());

                        int id = -1;

                        System.out.println("=== MODIFICACIÓN DE RESERVA ===");

                        do {
                            System.out.print("Introduce la id de la vivienda (pulse 0 para borrar la reserva):  ");
                            try {
                                id = Integer.parseInt(s.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Introduzca un valor numérico.");
                            }
                        } while (id == -1);

                        if (id == 0) {
                            if (user.deleteReserva(user.getReservas().get(reservaSeleccionada)))
                                System.out.println("Reserva eliminada con éxito.");
                            else System.out.println("Error al eliminar la reserva.");
                            break;
                        }
                        Vivienda vivienda = c.buscaViviendaId(id);
                        if (vivienda == null) System.out.println("Vivienda no encontrada. Pruebe con otra id.");


                    } else System.out.println("No ha realizado reservas aún.");

                    c.guardarDatos();
                    Utils.pulseParaContinuar();
                }
                case 4 -> { //Ver perfil
                    System.out.println(user);
                    Utils.pulseParaContinuar();

                }
                case 5 -> { //Modificar datos
                    System.out.println("*** Modificación de perfil ***");
                    System.out.print("Introduce el nuevo nombre de perfil: ");
                    String nuevoNombre = s.nextLine();
                    System.out.print("Introduce la nueva contraseña: ");
                    String nuevoPassw = s.nextLine();
                    System.out.print("Introduce el nuevo email: ");
                    String nuevoEmail = s.nextLine();

                    if (c.existeEmail(nuevoEmail)) {
                        System.out.println("Email ya en uso. Pruebe con otro.");
                        break;
                    }

                    User temp = new User(user.getId(), nuevoNombre, nuevoPassw, nuevoEmail);
                    if (c.modificaPerfil(temp)) {
                        System.out.println("""
                                Perfil modificado con éxito
                                Cierre sesión y vuelva a iniciar para ver sus cambios reflejados.""");

                    } else System.out.println("Error al modificar el perfil. Inténtelo más tarde.");

                    c.guardarDatos();
                    Utils.pulseParaContinuar();
                }
                case 6 -> { //Enviar reserva como PDF.
                    System.out.println("*** Enviar reserva al PDF ***");

                    ArrayList<Reserva> reservas = c.getReservasByUser(user);
                    int reservaSeleccionada = -1;
                    if (!reservas.isEmpty()) {

                        for (int i = 0; i < reservas.size(); i++) {
                            System.out.println("Reserva número: " + (i + 1) + '\n'
                                    + reservas.get(i) + '\n' +
                                    "=========");
                        }

                        do {
                            System.out.print("Indica que reserva quieres enviar a tu correo (Pulse 0 para cancelar): ");
                            try {
                                reservaSeleccionada = Integer.parseInt(s.nextLine()) - 1;
                                if (reservaSeleccionada == -1) {
                                    System.out.println("Operación cancelada");
                                    Utils.pulseParaContinuar();
                                    break;
                                } else {
                                    Reserva reserva = reservas.get(reservaSeleccionada);
                                    System.out.println("Generando pdf ");
                                    Utils.cerrarPrograma();
                                    c.enviarReservaPDF(reserva);
                                    System.out.println("PDF enviado a su correo.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Debe introducir un valor numérico");
                            }
                        } while (reservaSeleccionada == -1 || reservaSeleccionada > reservas.size());

                    } else System.out.println("Usted no ha realizado ninguna reserva aún.");

                    Utils.pulseParaContinuar();
                }
                case 7 -> { //Salir
                    System.out.print("Cerrando sesión ");
                    Saves.escribeLogUsuario("cerrar sesion", user, -1);
                    c.guardarDatos();
                    Utils.cerrarPrograma();
                    System.out.println();
                }
            }

        } while (op != 7);
    }


    //Menu de inicio.
    private static int menuInicio() {
        System.out.println("""
                 _______  _______ .______      .__   __.      ___      .__   __. .______   .__   __. .______  \s
                |   ____||   ____||   _  \\     |  \\ |  |     /   \\     |  \\ |  | |   _  \\  |  \\ |  | |   _  \\ \s
                |  |__   |  |__   |  |_)  |    |   \\|  |    /  ^  \\    |   \\|  | |  |_)  | |   \\|  | |  |_)  |\s
                |   __|  |   __|  |      /     |  . `  |   /  /_\\  \\   |  . `  | |   _  <  |  . `  | |   _  < \s
                |  |     |  |____ |  |\\  \\----.|  |\\   |  /  _____  \\  |  |\\   | |  |_)  | |  |\\   | |  |_)  |\s
                |__|     |_______|| _| `._____||__| \\__| /__/     \\__\\ |__| \\__| |______/  |__| \\__| |______/ \s""");

        System.out.println("""
                                
                ======================
                1. Logging.
                2. Registrar usuario.
                3. Salir""");
        System.out.print("Introduzca una opción: ");
        try {
            return Integer.parseInt(s.nextLine());

        } catch (NumberFormatException e) {
            return -1;
        }
    }
}