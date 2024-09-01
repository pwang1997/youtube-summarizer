from generated import youtube_caption_service_pb2

class ProtobufHelper:
    @staticmethod
    def serialize_response(response):
        return response.SerializeToString()

    @staticmethod
    def deserialize_response(data):
        response = youtube_caption_service_pb2.MyResponse()
        response.ParseFromString(data)
        return response

    @staticmethod
    def create_sample_response():
        response = youtube_caption_service_pb2.MyResponse()
        caption1 = response.data.add()
        caption1.text = "Hello, world!"
        caption1.start = 0.0
        caption1.duration = 2.5

        caption2 = response.data.add()
        caption2.text = "This is a test."
        caption2.start = 2.5
        caption2.duration = 3.0

        return response
    
    @staticmethod
    def map_list_to_response(data):
        response = youtube_caption_service_pb2.MyResponse()
        for item in data:
            caption = response.data.add()
            caption.text = item['text']
            caption.start = item['start']
            caption.duration = item['duration']
        return response
