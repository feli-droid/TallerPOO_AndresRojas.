package co.edu.puj.rpg.modelo;

import co.edu.puj.rpg.exception.CapacidadExcedidaException;
import co.edu.puj.rpg.exception.SinManaException;
import co.edu.puj.rpg.exception.VidaInsuficienteException;
import co.edu.puj.rpg.modelo.enums.EstadoPersonaje;
import co.edu.puj.rpg.modelo.enums.TipoPersonaje;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** Clase base abstracta: estado común + relaciones (Arma, Gremio, CorazonRPG). */
public abstract class Personaje implements ICombatiente {

    // Estado común (protected: visible para las subclases)
    protected String nombre;
    protected int vida;
    protected int ataqueBase;
    protected int puntosMana;  // maná actual (los tipos sin magia empiezan en 0)
    protected int posicionX;   // fila del tablero
    protected int posicionY;   // columna del tablero

    // Estado encapsulado (taller 1)
    private int nivel = 1;
    private double oro = 50.0;
    private int experiencia = 0;

    // ---------- ENUMS Y COLECCIONES (taller 3) ----------
    private static final int MAX_HABILIDADES = 5;                  // capacidad del libro de habilidades
    private final TipoPersonaje tipoPersonaje;                     // reemplaza al String tipo
    private EstadoPersonaje estado;                                // reemplaza a las banderas de vida/muerte
    private final Set<String> habilidades = new HashSet<>();       // sin duplicados

    // ---------- RELACIONES (taller 2.5) ----------
    private Arma armaEquipada;            // Asociación unidireccional 0..1 (puede ser null)
    private Gremio gremio;                // Asociación bidireccional   0..*  a 1 (puede ser null)
    private final CorazonRPG corazon;     // Composición 1..1 (final: nunca se reemplaza)

    public Personaje(String nombre, TipoPersonaje tipoPersonaje, int vida, int ataqueBase, int posX, int posY) {
        this(nombre, tipoPersonaje, vida, ataqueBase, posX, posY, 0);
    }

    public Personaje(String nombre, TipoPersonaje tipoPersonaje, int vida, int ataqueBase,
                     int posX, int posY, int manaBase) {
        this.nombre = nombre;
        this.tipoPersonaje = tipoPersonaje;
        this.estado = EstadoPersonaje.VIVO;                // estado inicial por defecto
        this.vida = vida;
        this.ataqueBase = ataqueBase;
        this.puntosMana = manaBase;
        this.posicionX = posX;
        this.posicionY = posY;
        this.armaEquipada = null;                        // inicia desarmado
        this.gremio = null;                              // inicia sin gremio
        this.corazon = new CorazonRPG(vida, manaBase);   // COMPOSICIÓN: se crea AQUÍ, dentro del constructor
    }

    // ---------- Métodos abstractos / ganchos ----------
    public abstract void realizarAtaqueEspecial(Personaje objetivo);

    public abstract char getSimbolo();

    public void finalizarTurno() { }

    // ---------- Asociación unidireccional: Arma ----------
    public void equiparArma(Arma nuevaArma) {
        if (nuevaArma == null) {
            System.out.println("No se puede equipar un arma nula.");
            return;
        }
        this.armaEquipada = nuevaArma;
        System.out.printf("%s equipa %s.%n", nombre, nuevaArma);
    }

    public void desequiparArma() {
        if (this.armaEquipada != null) {                 // ¡siempre validar null!
            System.out.printf("%s guarda su arma %s y queda desarmado.%n", nombre, armaEquipada.getNombre());
            this.armaEquipada = null;
        } else {
            System.out.printf("%s no tiene arma para retirar.%n", nombre);
        }
    }

    public int calcularAtaqueTotal() {
        int total = this.ataqueBase + (int) tipoPersonaje.getBonificadorAtaqueBase();   // bonificador del enum
        if (this.armaEquipada != null) {                 // evita NullPointerException
            total += this.armaEquipada.getDanioBase();
        }
        return total;
    }

    // ---------- Asociación bidireccional: Gremio ----------
    public void unirseAGremio(Gremio nuevoGremio) {
        if (nuevoGremio == null || nuevoGremio == this.gremio) {
            return;                                      // nada que hacer
        }
        if (this.gremio != null) {
            salirDeGremio();                             // primero se desvincula del gremio anterior
        }
        this.gremio = nuevoGremio;                       // actualiza su propia referencia
        nuevoGremio.agregarMiembro(this);                // sincronización cruzada
    }

    public void salirDeGremio() {
        if (this.gremio == null) {
            return;
        }
        Gremio anterior = this.gremio;
        this.gremio = null;                              // primero se limpia el lado propio...
        anterior.removerMiembro(this);                   // ...y luego el otro extremo
    }

    // ---------- Combate ----------
    public void atacar(Personaje objetivo) {
        int dano = calcularAtaqueTotal();
        boolean critico = Math.random() < 0.15;          // 15% de golpe crítico
        if (critico) {
            dano *= 2;
        }
        System.out.printf("%s ataca a %s%s%s.%n", nombre, objetivo.getNombre(),
                armaEquipada != null ? " con " + armaEquipada.getNombre() : "",
                critico ? " (GOLPE CRITICO!)" : "");
        objetivo.defender(dano);
    }

    public void atacar(Personaje objetivo, boolean modoFuria) {
        if (!modoFuria) {
            atacar(objetivo);
            return;
        }
        System.out.printf("%s entra en FURIA y golpea a %s (dano x2, pierde 5 de vida).%n",
                nombre, objetivo.getNombre());
        objetivo.defender(calcularAtaqueTotal() * 2);
        this.vida = Math.max(1, this.vida - 5);
    }

    @Override
    public void defender(int danoRecibido) {
        int mitigacion = (int) tipoPersonaje.getBonificadorDefensaBase();   // bonificador del enum
        int danoFinal = Math.max(1, danoRecibido - mitigacion);
        recibirDano(danoFinal);
        System.out.printf("  -> %s recibe %d de dano (vida: %d/%d)%n", nombre, danoFinal, vida, getVidaMaxima());
        if (!estaVivo()) {
            System.out.printf("  -> %s ha caido!%n", nombre);
        }
    }

    public boolean estaVivo() {
        return estado != EstadoPersonaje.DERROTADO;
    }

    public void recibirDano(int cantidad) {
        this.vida = Math.max(0, this.vida - cantidad);
        if (this.vida == 0) {
            this.estado = EstadoPersonaje.DERROTADO;
        }
    }

    // ---------- Reglas de negocio con excepciones ----------
    /** Lanza un hechizo. Verificada: SinManaException si costoMana > maná actual. */
    public void lanzarHechizo(int costoMana) throws SinManaException {
        if (costoMana > puntosMana) {
            throw new SinManaException(String.format(
                    "%s no tiene mana suficiente: el hechizo cuesta %d y solo tiene %d.",
                    nombre, costoMana, puntosMana));
        }
        puntosMana -= costoMana;
        System.out.printf("%s lanza un hechizo! (mana -%d, restante %d)%n", nombre, costoMana, puntosMana);
    }

    /** Descuenta vida como costo de una acción. No verificada: VidaInsuficienteException si costoVida >= vida. */
    public void consumirVida(int costoVida) {
        if (costoVida >= vida) {
            throw new VidaInsuficienteException(String.format(
                    "%s no puede pagar %d de vida: solo tiene %d.", nombre, costoVida, vida));
        }
        vida -= costoVida;
    }

    // ---------- Libro de habilidades (HashSet) ----------
    /** true si se aprendió; false si ya la conocía. Verificada: CapacidadExcedidaException si el libro está lleno. */
    public boolean aprenderHabilidad(String habilidad) throws CapacidadExcedidaException {
        if (habilidades.contains(habilidad)) {
            return false;                                  // duplicado rechazado, el conjunto no cambia
        }
        if (habilidades.size() >= MAX_HABILIDADES) {
            throw new CapacidadExcedidaException(String.format(
                    "El libro de %s esta lleno (maximo %d habilidades).", nombre, MAX_HABILIDADES));
        }
        return habilidades.add(habilidad);
    }

    public Set<String> obtenerHabilidades() {
        return Collections.unmodifiableSet(habilidades);   // vista de solo lectura
    }

    public void curar(int cantidad) {
        if (!estaVivo()) {
            System.out.printf("%s esta derrotado y no puede curarse.%n", nombre);
            return;
        }
        int antes = this.vida;
        this.vida = Math.min(getVidaMaxima(), this.vida + cantidad);
        System.out.printf("%s recupera %d de vida (vida: %d/%d)%n", nombre, vida - antes, vida, getVidaMaxima());
    }

    /** Recupera toda la vida (las subclases pueden restaurar más cosas). */
    public void descansar() {
        this.vida = getVidaMaxima();
        this.estado = EstadoPersonaje.VIVO;
    }

    public boolean ganarExperiencia(int xp) {
        this.experiencia += xp;
        int umbral = nivel * 50;
        if (experiencia >= umbral) {
            experiencia -= umbral;
            setNivel(nivel + 1);
            corazon.aumentarSaludBase(10);               // el corazón crece (solo Personaje puede)
            ataqueBase += 3;
            vida = getVidaMaxima();
            return true;
        }
        return false;
    }

    public void setPosicion(int x, int y) {
        this.posicionX = x;
        this.posicionY = y;
    }

    public String describir() {
        return String.format("%s | Nv %d | Vida %d/%d | Ataque %d | Oro %.2f",
                nombre, nivel, vida, getVidaMaxima(), calcularAtaqueTotal(), oro);
    }

    /** Reporte detallado con StringBuilder. Del gremio imprime SOLO el nombre (evita StackOverflowError). */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(nombre).append(" (").append(getClass().getSimpleName()).append(") ===\n");
        sb.append("Tipo    : ").append(tipoPersonaje).append(" | Estado: ").append(estado).append('\n');
        sb.append("Nivel   : ").append(nivel).append(" | XP: ").append(experiencia)
                .append("/").append(nivel * 50).append('\n');
        sb.append("Vida    : ").append(vida).append("/").append(getVidaMaxima()).append('\n');
        sb.append("Ataque  : ").append(calcularAtaqueTotal()).append(" (base ").append(ataqueBase).append(")\n");
        sb.append("Arma    : ").append(armaEquipada != null ? armaEquipada.toString() : "Desarmado").append('\n');
        sb.append("Gremio  : ").append(gremio != null ? gremio.getNombre() : "Sin gremio").append('\n');
        sb.append("Corazon : ").append(corazon).append('\n');
        sb.append("Habilid.: ").append(habilidades).append('\n');
        sb.append("Oro     : ").append(String.format("%.2f", oro));
        return sb.toString();
    }

    // ---------- Setters validados (taller 1) ----------
    public void setOro(double oro) {
        if (oro < 0.0) {
            System.out.println("Saldo insuficiente: operacion rechazada.");
            return;
        }
        this.oro = oro;
    }

    public void setNivel(int nivel) {
        if (nivel < 1) {
            return;
        }
        this.nivel = nivel;
    }

    // ---------- Getters ----------
    // (no existe setCorazon(): la composición prohíbe reemplazar el corazón)
    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public int getVidaMaxima() { return corazon.getSaludBase(); }
    public int getAtaqueBase() { return ataqueBase; }
    public int getPosicionX() { return posicionX; }
    public int getPosicionY() { return posicionY; }
    public int getNivel() { return nivel; }
    public double getOro() { return oro; }
    public int getExperiencia() { return experiencia; }
    public int getPuntosMana() { return puntosMana; }
    public TipoPersonaje getTipoPersonaje() { return tipoPersonaje; }
    public EstadoPersonaje getEstado() { return estado; }
    public Arma getArmaEquipada() { return armaEquipada; }
    public Gremio getGremio() { return gremio; }
    public CorazonRPG getCorazon() { return corazon; }
}