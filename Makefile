define REDIS1_CONF
daemonize yes
port 6379
requirepass foobared
pidfile /tmp/redis1.pid
endef

define REDIS2_CONF
daemonize yes
port 6380
requirepass foobared
pidfile /tmp/redis2.pid
endef

define REDIS3_CONF
daemonize yes
port 6385
pidfile /tmp/redis3.pid
endef

define REDIS_SENTINEL_CONF
daemonize yes
port 26379
pidfile /tmp/redis_sentinel1.pid
sentinel monitor mymaster 127.0.0.1 6385 1
endef

export REDIS1_CONF
export REDIS2_CONF
export REDIS3_CONF
export REDIS_SENTINEL_CONF
test:
	echo "$$REDIS_SENTINEL_CONF" > /tmp/redis-sentinel.conf

	echo "$$REDIS1_CONF" | redis-server -
	echo "$$REDIS2_CONF" | redis-server -
	echo "$$REDIS3_CONF" | redis-server -
	echo "$$REDIS_SENTINEL_CONF" | redis-server /tmp/redis-sentinel.conf --sentinel

	mvn clean compile test

	kill `cat /tmp/redis1.pid`
	kill `cat /tmp/redis2.pid`
	kill `cat /tmp/redis3.pid`
	kill `cat /tmp/redis_sentinel1.pid`
	rm /tmp/redis-sentinel.conf

.PHONY: test
