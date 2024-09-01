import redis
from dotenv import dotenv_values
 
class RedisClient:
    def __init__(self):
        self.config = dotenv_values(".env")
        self.client = redis.Redis(host=self.config['REDIS_HOST'], port=self.config['REDIS_PORT'],
                                  password=self.config['REDIS_PASSPORT'], ssl=self.config['REDIS_SSL_CONFIG'])

    def set_data(self, key, value):
        self.client.set(key, value)

    def get_data(self, key):
        return self.client.get(key)