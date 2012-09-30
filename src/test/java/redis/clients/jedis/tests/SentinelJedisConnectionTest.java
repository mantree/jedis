package redis.clients.jedis.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelConnectionFactory;
import redis.clients.jedis.tests.HostAndPortUtil.HostAndPort;

public class SentinelJedisConnectionTest {
    private static HostAndPort hnp = HostAndPortUtil.getRedisServers().get(2);

	@Test
	public void testConnectionMadeToMaster() {
        JedisPool pool = new JedisPool(new JedisPoolConfig(),
        		new JedisSentinelConnectionFactory("mymaster", hnp.host, hnp.port, 2000, null, 0));
        Jedis jedis = pool.getResource();
        assertFalse("info should not contain sentinel", jedis.info().contains("sentinel"));
        pool.returnResource(jedis);
        pool.destroy();
	}

}
