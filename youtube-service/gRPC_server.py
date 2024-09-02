from concurrent import futures
import grpc
from generated import youtube_caption_service_pb2, youtube_caption_service_pb2_grpc
from redis_client import RedisClient
from youtube_caption_client import YoutubeCaptionClient
from protobuf_helper import ProtobufHelper

class MyServiceServicer(youtube_caption_service_pb2_grpc.MyServiceServicer):
    def __init__(self):
        self.redis_client = RedisClient()
    def MyEndpoint(self, request, context):
        # Handle the request (e.g., store in Redis)
        print(f"Received request with videoId: {request.videoId}")
        
        key = f"my_service:response:{request.videoId}"
        
        transcript = self.redis_client.get_data(key)
        
        response = None
        if transcript is None:
            transcript = YoutubeCaptionClient.get_transcript(request.videoId)
            
            # Convert list of caption objects to Protobuf message
            response = ProtobufHelper.map_list_to_response(transcript)

            # Store the serialized response in Redis
            serialized_response = ProtobufHelper.serialize_response(response)
            
            # Save serialized Protobuf message in Redis
            self.redis_client.set_data(key, serialized_response)
            print("pushed", "key_name:[", key, "] to redis")
        else:
            print("retrieved", "key_name:[", key, "] from redis")
            # Deserialize Protobuf message
            response = ProtobufHelper.deserialize_response(transcript)

        # Return the response to the client
        return response

def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    youtube_caption_service_pb2_grpc.add_MyServiceServicer_to_server(MyServiceServicer(), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    print("gRPC server started on port 50051")
    server.wait_for_termination()

if __name__ == "__main__":
    serve()

