import java.util.Hashtable;

public class ExpirableHashtable extends Hashtable {

    private final Hashtable<Object, Long> ttl;
    private final Hashtable<Object, Long> lastUpdate;
    private Long defaultTtl;

    public ExpirableHashtable() {
        super();
        lastUpdate = new Hashtable<Object, Long>();
        ttl = new Hashtable<Object, Long>();
        setDefaultTtl(500L);
    }

    @Override
    public synchronized Object get(Object key) {
        if (super.get(key) != null) {
            if ((System.currentTimeMillis() - lastUpdate.get(key)) >= ttl.get(key)) {
                super.remove(key);
                lastUpdate.remove(key);
                ttl.remove(key);
            }
            else {
                lastUpdate.put(key, System.currentTimeMillis());
            }
        }
        return super.get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized Object put(Object key, Object value) {
        return put(key,value, defaultTtl);
    }

    @SuppressWarnings("unchecked")
    public synchronized Object put(Object key, Object value, long ttl) {
        lastUpdate.put(key, System.currentTimeMillis());
        this.ttl.put(key, ttl);
        return super.put(key, value);
    }

    public String showLastUpdates() {
        return lastUpdate.toString();
    }

    public String showTtls() {
        return ttl.toString();
    }

    public Long getDefaultTtl() {
        return defaultTtl;
    }

    public void setDefaultTtl(Long ttl) {
        this.defaultTtl = ttl;
    }

}
