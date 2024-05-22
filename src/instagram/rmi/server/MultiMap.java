package instagram.rmi.server;


import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Un MultiMap es un Mapa que permite almacenar en una misma clave varios valores, todos ellos en una lista de elementos
 *
 * @param <K> La clave que usuará el mapa
 * @param <V> Los valores que almacenará la lista de valores
 */
public class MultiMap<K, V> {

    private final ConcurrentHashMap<K, ConcurrentLinkedQueue<V>> mapaContenido; // Mapa que contiene clave - valores

    /**
     * Crea una instancia de la clase MultiMap con un tamaño inicial 0
     */
    public MultiMap() {
        mapaContenido = new ConcurrentHashMap<>(0);
    }

    /**
     * Introduce un valor en una determinada clave. No sobreescribe el valor actual sino que lo introduce en una lista.
     *
     * @param clave la clave del mapa. Si no existe se crea.
     * @param valor el valor que se quiere introducir en la lista.
     */
    public void push(K clave, V valor) {
        Queue<V> cola = mapaContenido.get(clave);
        if (cola == null) {
            ConcurrentLinkedQueue<V> nueva = new ConcurrentLinkedQueue<>();
            Queue<V> previa = mapaContenido.putIfAbsent(clave, nueva);
            cola = (previa == null) ? nueva : previa;
        }
        cola.add(valor);
    }

    /**
     * Elimina y devuelve el primer valor de la lista dada una clave
     *
     * @param key la clave de una lista determinada.
     *            Si la clave no está contenida devolverá null
     * @return el primer valor de la lista especificada por la clave {@code key}
     */
    public V pop(K key) {
        Queue<V> cola = mapaContenido.get(key);
        return (cola != null) ? cola.poll() : null;
    }

    /**
     * Elimina la clave y la lista del mapa
     *
     * @param key la clave de la lista que se quiere eliminar
     * @return true si se ha eliminado correctamente, false si la clave no estaba contenida
     */
    public boolean deleteList(K key) {
        if (mapaContenido.containsKey(key)) {
            mapaContenido.remove(key);
            return true;
        }
        return false;
    }

    public V peek(K key) {
        if (!mapaContenido.containsKey(key)) {
            return null;
        }
        return mapaContenido.get(key).peek();
    }

    public Queue<V> getQueue(K key) {
        if (!mapaContenido.containsKey(key))
            throw new IllegalArgumentException();
        return mapaContenido.get(key);
    }

    public Set<K> keySet() {
        return mapaContenido.keySet();
    }


}