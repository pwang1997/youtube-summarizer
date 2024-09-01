from youtube_transcript_api import YouTubeTranscriptApi
import json
from google.protobuf.json_format import Parse, ParseDict
from redis_client import RedisClient

class YoutubeCaptionClient:
    redis_hset_prefix = 'youtube_transcript_'
    
    @staticmethod
    def get_transcript(videoId, **kwargs):
        key_name = redis_hset_prefix + videoId
        r = RedisClient()
        transcript = r.get_data(key_name)
        # transcript = None

        if transcript is None:
            print("calling get_transcript on vId:", videoId)
            transcript = YouTubeTranscriptApi.get_transcript(videoId)

            transcript = json.dumps(transcript)
            
            r.set_data(key_name, transcript)
            
            print("push transcript", "key_name:[", key_name, "] to redis")
        else: 
            print("fetch transcript", "key_name:[", key_name, "] from redis")
        parsed_data = json.loads(transcript)
        return parsed_data