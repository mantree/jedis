package redis.clients.jedis;

import java.util.List;

public class JedisSentinelConnectionFactory extends JedisConnectionFactory {

	final String masterName;
	
	public JedisSentinelConnectionFactory(String masterName, String host, int port, int timeout,
			String password, int database) {
		super(host, port, timeout, password, database);
		this.masterName = masterName;
	}

	@Override
	public Object makeObject() throws Exception{
		Jedis sentinel = null;
		try {
			sentinel = (Jedis) super.makeObject();

			List<String> master = sentinel.sentinelMasterFor(masterName);

			return super.makeConnection(master.get(0),
					Integer.parseInt(master.get(1)));
		} finally {
			if(sentinel != null){
				super.destroyObject(sentinel);
			}
		}
	}
}
