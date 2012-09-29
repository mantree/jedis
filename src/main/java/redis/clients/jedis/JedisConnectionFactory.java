package redis.clients.jedis;

import org.apache.commons.pool.BasePoolableObjectFactory;

public class JedisConnectionFactory extends BasePoolableObjectFactory {

	private final String host;
	private final int port;
	private final int timeout;
	private final String password;
	private final int database;
	
	public JedisConnectionFactory(final String host, final int port, final int timeout,
			final String password, final int database) {
		super();
		this.host = host;
		this.port = port;
		this.timeout = timeout;
		this.password = password;
		this.database = database;
	}

	public Object makeObject() throws Exception {
		final Jedis jedis = new Jedis(this.host, this.port, this.timeout);

		jedis.connect();
		if (null != this.password) {
			jedis.auth(this.password);
		}
		if (database != 0) {
			jedis.select(database);
		}

		return jedis;
	}

	public void destroyObject(final Object obj) throws Exception {
		if (obj instanceof Jedis) {
			final Jedis jedis = (Jedis) obj;
			if (jedis.isConnected()) {
				try {
					try {
						jedis.quit();
					} catch (Exception e) {
					}
					jedis.disconnect();
				} catch (Exception e) {

				}
			}
		}
	}

	public boolean validateObject(final Object obj) {
		if (obj instanceof Jedis) {
			final Jedis jedis = (Jedis) obj;
			try {
				return jedis.isConnected() && jedis.ping().equals("PONG");
			} catch (final Exception e) {
				return false;
			}
		} else {
			return false;
		}
	}
}
